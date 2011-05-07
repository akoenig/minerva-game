/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MinervaServerEngine.java 839 2010-08-07 19:50:36Z andre.koenig $
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Contact:
 *     Christian Bollmann: cbollmann@stud.hs-bremen.de
 *     Carina Strempel: cstrempel@stud.hs-bremen.de
 *     André König: akoenig@stud.hs-bremen.de
 * 
 * Web:
 *     http://minerva.idira.de
 * 
 */
package de.hochschule.bremen.minerva.server.net;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

import javax.imageio.ImageIO;

import de.hochschule.bremen.minerva.commons.exceptions.CountriesNotInRelationException;
import de.hochschule.bremen.minerva.commons.exceptions.CountryOwnerException;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.exceptions.GameAlreadyStartedException;
import de.hochschule.bremen.minerva.commons.exceptions.IsOwnCountryException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerSlotAvailableException;
import de.hochschule.bremen.minerva.commons.exceptions.NotEnoughArmiesException;
import de.hochschule.bremen.minerva.commons.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotStorable;
import de.hochschule.bremen.minerva.commons.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.commons.net.ClientExecutables;
import de.hochschule.bremen.minerva.commons.net.ServerExecutables;
import de.hochschule.bremen.minerva.commons.util.MapTool;
import de.hochschule.bremen.minerva.commons.vo.AttackResult;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.CountryCard;
import de.hochschule.bremen.minerva.commons.vo.Mission;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.PlayerState;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.core.logic.Game;
import de.hochschule.bremen.minerva.server.core.logic.Turn;
import de.hochschule.bremen.minerva.server.manager.AccountManager;
import de.hochschule.bremen.minerva.server.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.server.manager.WorldManager;
import de.hochschule.bremen.minerva.server.util.ConsoleLogger;
import de.hochschule.bremen.minerva.server.util.WorldFile;
import de.hochschule.bremen.minerva.server.vo.ApplicationConfiguration;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;
import de.root1.simon.exceptions.SimonRemoteException;

/**
 * The server engine, which implements the server protocol.
 * In other words: Contains methods, which can be invoked from any client.
 * It's like a facade for accessing the minerva subsystems.
 * 
 * This facade was the 'GameEngineLocal' in the "hotseat" version ;)
 *
 * @since 1.0
 * @version $Id: MinervaServerEngine.java 839 2010-08-07 19:50:36Z andre.koenig $
 * 
 */
public class MinervaServerEngine implements ServerExecutables {

	private static final long serialVersionUID = 1911446743019185828L;

	private static final ConsoleLogger LOGGER = ConsoleLogger.getLogger(); 

	private Game game = new Game();

	// The clients with whose client executables. The client executable interface
	// provides the methods, which the server is able to execute on the client.
	private HashMap<Player, ClientExecutables> clients = new HashMap<Player, ClientExecutables>();

	/**
	 * Create the minerva server engine.
	 * Uses the entries, which are configured in
	 * the application configuration file.
	 * 
	 * @throws UnknownHostException
	 * @throws NameBindingException
	 * @throws IOException
	 * @throws DataAccessException
	 *
	 */
	public MinervaServerEngine(String name, int port) throws UnknownHostException, IOException, NameBindingException, DataAccessException {
		Registry registry = Simon.createRegistry(port);
		registry.bind(name, this);
		
		// Log out all players (previous game sessions)
		AccountManager.getInstance().logout();
	}

    /**
     * Login a player into the current game
     *
     * @param player The player value object.
     * @param clientExecutables The methods, which the server is able to execute.
     *
     * @throws SimonRemoteException
     * @throws GameAlreadyStartedException It is not possible to login a player if the game is already running. 
     * @throws WrongPasswordException
     * @throws PlayerDoesNotExistException
     * @throws NoPlayerSlotAvailableException
     * @throws DataAccessException
     * 
     */
	@Override
	public void login(Player player, ClientExecutables clientExecutables) throws SimonRemoteException, PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException {
		LOGGER.log("login(): login this player: "+player.toString());
		AccountManager.getInstance().login(player);

		// If the player count is 0
		// We create a new game.
		if (this.game.getPlayerCount() == 0) {
			this.game = null;
			player.setMaster(true);
		}

		player.setState(PlayerState.GAME_INIT);

		if (this.game == null) {
			this.game = new Game();
		}

		this.game.addPlayer(player);

		// Put the new client in our central client map.
		this.clients.put(player, clientExecutables);

		this.notifyClients();
	}

    /**
     * Registers a new player.
     *
     * @param player The player, which should be registered.
     *
     * @throws SimonRemoteException
     * @throws PlayerExistsException
     * @throws DataAccessException
     *
     */
	public void register(Player player) throws SimonRemoteException, PlayerExistsException, DataAccessException {
		LOGGER.log("register(): this player: "+player.toString());
		AccountManager.getInstance().createPlayer(player);
	}

	/**
	 * Imports the given world file via the importer class.
	 * Note that you have to upload the file before you can import it.
	 * Use {@link ServerExecutables#prepareWorldFileImport(String)} and
	 * write the file via FileChannel.
	 * 
	 * @see WorldFile
	 *
	 */
	public void importWorld(String filename) throws SimonRemoteException, WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException, WorldNotStorable, DataAccessException {
		LOGGER.log("importWorld(): Importing the world: "+filename);

		File worldFile = new File(filename);

		WorldManager.getInstance().store(worldFile);

		// Was the import successful -> delete the import file.
		worldFile.delete();

		this.notifyClients();
	}

    /**
     * Returns a list with all available worlds in a flat view.
     * Flat view means, that each world doesn't hold the complete
     * country dependencies (cool, for requesting an world overview).
     *
     * @param flatView Flat view = worlds without country dependencies (only meta data: name, description, etc.)
     * 
     * @return A vector with the available worlds.
     *
     * @throws SimonRemoteException
     * @throws DataAccessException 
     *
     */
	@Override
	public Vector<World> getWorlds(boolean flatView) throws SimonRemoteException, DataAccessException {
		LOGGER.log("getWorlds(): flatView:"+flatView);
		return WorldManager.getInstance().getList(flatView);
	}

    /**
     * Starts the game session. Please verify, that
     * you have already defined all necessary data for starting a game.
     *
     * @throws SimonRemoteException
     * @throws WorldNotDefinedException 
     * @throws NoPlayerLoggedInException 
     * @throws NotEnoughPlayersLoggedInException 
     * 
     */
	@Override
	public void startGame() throws SimonRemoteException, NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException, DataAccessException {
		LOGGER.log("startGame(): THE GAME WAS STARTED!");
		for (Player player :this.game.getPlayers()) {
			player.setState(PlayerState.IDLE);
		}
		this.game.start();
		
		this.notifyClients();
	}

    /**
     * Stops a game and logs out all players.
     * After executing this method it is possible to login new
     * players and start a new game session.
     *
     * @param createNewOne Create a new game object after killing the old one?
     *
     * @throws SimonRemoteException
     * @throws DataAccessException
     * 
     */
	@Override
	public void killGame(boolean createNewOne) throws SimonRemoteException, DataAccessException {
		// If there are players ...
		if (this.game.getPlayerCount() > 0) {
			AccountManager.getInstance().logout();
		
			if (createNewOne) {
				this.game = new Game();
			}
			LOGGER.log("killGame(): THE GAME WAS KILLED! :o");
			this.notifyClients();
		}
	}

    /**
     * Sets the world on which the game should be played.
     * Will only be defined if the game is not running.
     *
     * @throws SimonRemoteException
     * @throws DataAccessException
     *
     */
	@Override
	public void setGameWorld(World world) throws SimonRemoteException, DataAccessException {
		LOGGER.log("setGameWorld(): Gamemaster defined the following world to play on: '"+world.getName()+"'");
		
		ApplicationConfiguration appConfig = ApplicationConfigurationManager.get();
		String filepath = appConfig.getAssetsWorldDirectory();

		world.setMapImage(this.convertMapImage(filepath + world.getMap()));
		world.setMapUnderlayImage(this.convertMapImage(filepath + world.getMapUnderlay()));

		this.game.setWorld(world);
	}

    /**
     * Returns the world value object from
     * the current game.
     * 
     * @return The world value object.
     *
     * @throws SimonRemoteException
     * @throws DataAccessException
     *
     */
	@Override
	public World getGameWorld() throws SimonRemoteException {
		LOGGER.log("getGameWorld(): The world was requested.");
		return this.game.getWorld();
	}

    /**
     * Returns the logged in players
     * 
     * @return A vector with all logged in players.
     *
     * @throws SimonRemoteException
     *
     */
	@Override
	public Vector<Player> getGamePlayers() throws SimonRemoteException {
		LOGGER.log("getGamePlayers(): Requested logged in players.");
		return this.game.getPlayers();
	}

	/**
	 * Returns all available missions.
	 *
	 * @return A vector with all available missions.
	 *
     * @throws SimonRemoteException
     *
	 */
	@Override
	public Vector<Mission> getGameMissions() throws SimonRemoteException {
		LOGGER.log("getGameMissions()");
		return this.game.getMissions();
	}
	
    /**
     * Is the current game session over?
     *
     * @return boolean
     *
     * @throws SimonRemoteException
     *
     */
	@Override
	public boolean isGameFinished() throws SimonRemoteException {
		// Turn count 0 means, that a new game object was created
		// in the killGame().
		if (this.game.getTurnCount() == 0) {
			LOGGER.log("isGameFinished(): Game finished? -> Yes :(");
			return true;
		} else {
			LOGGER.log("isGameFinished(): Game finished? -> " + ((this.game.isFinished()) ? "Yes :(" : "No :)"));			
			return this.game.isFinished();
		}
	}

	/**
	 * Returns the map from the current game world.
	 *
	 * @return A byte array with the map image.
	 * @throws IOException 
     *
	 */
	@Override
	public String getGameMapImage() throws IOException {
		LOGGER.log("getGameMapImage(): Load the map image (world = '" + this.game.getWorld().getName() + "').");
		
		BufferedImage mapImage = this.game.getWorld().getMapImage();
		return MapTool.toBase64(mapImage);
	}

	/**
	 * Returns the map underlay from the current game world.
	 *
	 * @return A byte array with the map underlay image.
	 * @throws IOException 
	 *
     * @throws SimonRemoteException
     *
	 */
	@Override
	public String getGameMapUnderlayImage() throws IOException {
		LOGGER.log("getGameMapImage(): Load the map image underlay (world = '" + this.game.getWorld().getName() + "').");

		BufferedImage mapImage = this.game.getWorld().getMapUnderlayImage();
		return MapTool.toBase64(mapImage);
	}

    /**
     * Returns the game winner if the game is terminated.
     *
     * @return The game winner. null if the game is still running.
     *
     * @throws SimonRemoteException
     *
     */
	@Override
	public Player getGameWinner() throws SimonRemoteException {
		LOGGER.log("getGameWinner(): AND THE WINNER IS: " + ((this.game.isFinished()) ? this.game.getWinner().getUsername() : "nobody - Game is still running."));
		return this.game.getWinner();
	}

	/**
	 * Sets state of the current player
	 * @param state player state
	 */
	public void setCurrentPlayerState(PlayerState state) throws SimonRemoteException {
		this.game.getCurrentTurn().getCurrentPlayer().setState(state);
		this.notifyClients();
	}
	
    /**
     * Release a country card.
     *
     * @param The releasable country card.
     *
     * @throws SimonRemoteException
     *
     */
	@Override
	public void releaseCard(CountryCard card) throws SimonRemoteException {
		LOGGER.log("releaseCard(): Release the card from country: '"+card.getReference().getName()+"'");
		Turn turn = this.game.getCurrentTurn();
		turn.releaseCard(card);

		this.notifyClients();
	}

    /**
     * Releases a country card series.
     * 
     * @param cards A vector with releasable country cards.
     *
     * @throws SimonRemoteException
     *
     */
	@Override
	public void releaseCards(Vector<CountryCard> cards) throws SimonRemoteException {
		LOGGER.log("releaseCards(): Release a card stack.");
		Turn turn = this.game.getCurrentTurn();
		turn.releaseCardSeries(cards);

		this.notifyClients();
	}

    /**
     * Returns the current allocatable army count
     * 
     * @return army count
     *
     * @throws SimonRemoteException
     *
     */
	@Override
	public int getAllocatableArmyCount() throws SimonRemoteException {
		int armyCount = this.game.getCurrentTurn().getAllocatableArmyCount();
		LOGGER.log("getAllocatableArmyCount(): Allocatable armies in this turn: "+armyCount);
		return armyCount;
	}

    /**
     * Adds one army to the specified country.
     *
     * @param The country on which the army should be placed.
     *
     * @throws SimonRemoteException
     * @throws NotEnoughArmiesException 
     * @throws CountryOwnerException 
     * 
     */
	@Override
	public void allocateArmy(Country allocatable) throws SimonRemoteException, NotEnoughArmiesException, CountryOwnerException {
		LOGGER.log("allocateArmy(): Allocate one army on country '"+ allocatable.getName() +"'");
        Turn turn = this.game.getCurrentTurn();
        turn.allocateArmy(allocatable);

        this.notifyClients();
	}

    /**
     * Attacks a country with an specified army count.
     * 
     * @param source The country from which the attack will be started.
     * @param destination The country which should be attacked.
     * @param armyCount The attacking army units.
     *
     * @throws SimonRemoteException
     * @throws CountriesNotInRelationException
     * @throws NotEnoughArmiesException
     * @throws IsOwnCountryException
     * 
     */
	@Override
	public AttackResult attack(Country source, Country destination, int armyCount) throws SimonRemoteException, CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException {
		Turn turn = this.game.getCurrentTurn();
		AttackResult result = turn.attack(source, destination, armyCount);
		LOGGER.log("attack(): Attacking '"+destination.getName()+"' from '"+source.getName()+"' with " + armyCount + " army units. Result: "+result.toString());

        return result;
	}

    /**
     * Moves armies from one country to another country.
     * 
     * @param from The country FROM which we will move the armies.
     * @param to The country TO which we will move the armies.
     * @param armyCount How many army units should be moved?
     *
     * @throws SimonRemoteException
     * @throws CountriesNotInRelationException 
     * @throws NotEnoughArmiesException 
     * @throws CountryOwnerException 
     * 
     */
	@Override
	public void move(Country source, Country destination, int armyCount) throws SimonRemoteException, CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException {
		LOGGER.log("move(): Move "+armyCount+" army units from '"+source.getName()+"' to '"+destination.getName()+"'.");

		Turn turn = this.game.getCurrentTurn();
        turn.moveArmies(source, destination, armyCount);

        this.notifyClients();
	}

    /**
     * Starts the next turn.
     *
     * @throws SimonRemoteException
     *
     */
	@Override
	public void finishTurn() throws SimonRemoteException {
        this.game.nextTurn();

        this.notifyClients();
	}
	
	/**
	 * Notifies all registered clients by invoking the
	 * "refreshPlayer" method on the client side.
	 * The client knows then, that something has changed.
	 *
	 * @throws SimonRemoteException
	 *
	 */
	private void notifyClients() throws SimonRemoteException {
		LOGGER.log("notifyClients");
		Iterator<Entry<Player, ClientExecutables>> iter = this.clients.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Player, ClientExecutables> entry = iter.next();
			ClientExecutables client = entry.getValue();
			
			client.refreshPlayer(entry.getKey());
		}
	}

	/**
	 * Prepares a world file upload.
	 * This is a necessary process for import new worlds.
	 * 
	 * @param filename The file name, which will be used for saving the uploaded world import file.
	 *
	 * @throws SimonRemoteException
	 *
	 */
	@Override
	public int prepareWorldFileImport(String worldFileName) throws SimonRemoteException {
		LOGGER.log("prepareWorldFileTransfer(): Preparing a world file transfer.");
		
		return Simon.prepareRawChannel(new WorldFileReceiver(worldFileName), this);
	}

	/**
	 * Converts the map image to an {@link BufferedImage}.
	 *
	 * @param filepath
	 * 
	 * @return The {@link BufferedImage} image object.
	 *
	 */
	private BufferedImage convertMapImage(String filepath) {
		BufferedImage map = null; 
		try {
			map = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			LOGGER.error("Loading map image failed. Path: '"+filepath+"'. Reason: " + e.getMessage());
		}
		return map;
	}
}