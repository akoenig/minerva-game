/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ApplicationConfiguration.java 699 2010-07-04 17:07:33Z andre.koenig $
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
package de.hochschule.bremen.minerva.client.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import de.hochschule.bremen.minerva.client.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.client.vo.ApplicationConfiguration;
import de.hochschule.bremen.minerva.commons.core.GameEngine;
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

import de.root1.simon.RawChannel;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;
import de.root1.simon.exceptions.SimonRemoteException;

/**
 * The network game engine.
 *
 * A facade, which provides access to the minerva subsystems. These subsystems are
 * placed on the server side and will be called via the {@link ServerExecutables} (serverEngine) interface.
 * Note, that the engine implements the {@link ClientExecutables} interface, too. This interface
 * provides methods, which are invokable by the server side.
 *
 * @see ApplicationConfigurationManager
 * @version $Id: ApplicationConfiguration.java 699 2010-07-04 17:07:33Z andre.koenig $
 * @since 1.0
 * 
 */
public class GameEngineNetwork extends Observable implements GameEngine, ClientExecutables {

	private static final long serialVersionUID = 7809319868203138075L;

	private static GameEngineNetwork instance = null;
	
	private ServerExecutables serverEngine = null;
	
	private Player clientPlayer = null;
	
	/**
	 * Creates the engine.
	 * Establishes the server connection.
	 *
	 * @throws LookupFailedException 
	 * @throws IOException 
	 * @throws SimonRemoteException 
	 * @throws EstablishConnectionFailed 
	 * 
	 */
	private GameEngineNetwork() throws EstablishConnectionFailed, SimonRemoteException, IOException, LookupFailedException {
		ApplicationConfiguration appConfg = ApplicationConfigurationManager.get();

		String serverHost = appConfg.getServerHost();
		String serverName = appConfg.getServerName();
		int serverPort = Integer.parseInt(appConfg.getServerPort());

		this.serverEngine = (ServerExecutables)Simon.lookup(serverHost, serverPort, serverName);		
	}

	/**
	 * Returns the engine instance.
	 * 
	 * @return The game engine.
	 *
	 * @throws DataAccessException 
	 *
	 */
	public static GameEngineNetwork getEngine() throws DataAccessException {
		if (GameEngineNetwork.instance == null) {
			try {
				GameEngineNetwork.instance = new GameEngineNetwork();
			} catch (EstablishConnectionFailed e) {
				throw new DataAccessException(e);
			} catch (SimonRemoteException e) {
				throw new DataAccessException(e);
			} catch (IOException e) {
				throw new DataAccessException(e, true);
			} catch (LookupFailedException e) {
				throw new DataAccessException(e, ApplicationConfigurationManager.get().getServerName());
			}
		}

		return GameEngineNetwork.instance;
	}

    /**
     * Login a given player on the server and pass the {@link ClientExecutables} implementation.
     * So that, the server is able to invoke methods on the client.
     *
     * @param player The player value object.
     *
     * @throws GameAlreadyStartedException It is not possible to login a player if the game is already running. 
     * @throws WrongPasswordException
     * @throws PlayerDoesNotExistException
     * @throws NoPlayerSlotAvailableException
     * @throws DataAccessException
     * 
     */
	@Override
	public void login(Player player) throws PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException {
		try {
			this.serverEngine.login(player, GameEngineNetwork.getEngine());
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Registers a new player on the server.
     *
     * @param player The player, which should be registered.
     *
     * @throws PlayerExistsException
     * @throws DataAccessException
     *
     */
	@Override
	public void register(Player player) throws PlayerExistsException, DataAccessException{
		try {
			this.serverEngine.register(player);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Request a world list from the server.
	 *
	 * @return The vector with all available worlds.
	 *
	 * @throws DataAccessException
	 *
	 */
	@Override
	public Vector<World> getWorldList() throws DataAccessException {
		try {
			return this.serverEngine.getWorlds(false);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	/**
	 * Request a world list from the server with the possibility
	 * to decide to load a lite world list (worlds without country dependencies).
	 *
	 * @param lite Lite version with only world meta data?
	 * @return A vector with all available worlds.
	 *
	 * @throws DataAccessException
	 *
	 */
	@Override
	public Vector<World> getWorldList(boolean lite) throws DataAccessException {
		try {
			return this.serverEngine.getWorlds(lite);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e.getMessage());
		}
	}

	/**
	 * Uploads a given world import file to the server and adds the new world to
	 * the minerva system, so that everyone can play on this world.
	 *
	 * @param worldFile The local world file object.
	 *
	 * @throws WorldNotStorable
	 * @throws WorldFileNotFoundException
	 * @throws WorldFileExtensionException
	 * @throws WorldFileParseException
	 * @throws DataAccessException
	 *
	 */
	@Override
	public void importWorld(File worldFile) throws WorldNotStorable, WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException, DataAccessException {
		try {
			// Uploading the file to the server.
			int pipeToken = this.serverEngine.prepareWorldFileImport(worldFile.getName());

			RawChannel rawChannel = Simon.openRawChannel(pipeToken, this.serverEngine);
			FileChannel fileChannel = new FileInputStream(worldFile).getChannel();

			ByteBuffer data = ByteBuffer.allocate(512);
			while (fileChannel.read(data) != -1) {
				rawChannel.write(data);
				data.clear();
			}

			fileChannel.close();
			rawChannel.close();

			// Registering the new world.
			this.serverEngine.importWorld(worldFile.getName());
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		} catch (FileNotFoundException e) {
			MMessageBox.error(e.getMessage());
		} catch (IllegalStateException e) {
			MMessageBox.error(e.getMessage());
		} catch (IOException e) {
			MMessageBox.error(e);
		}
	}

    /**
     * Starts the game session.
     * The gamemaster is the one and only who can start
     * a game session.
     *
     * @throws WorldNotDefinedException 
     * @throws NoPlayerLoggedInException 
     * @throws NotEnoughPlayersLoggedInException
     * @throws DataAccessException
     * 
     */
	@Override
	public void startGame() throws NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException, DataAccessException {
		try {
			this.serverEngine.startGame();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Stops a game and logs all players out.
     * After executing this method it is possible to login new
     * players and start a new game session.
     *
     * @throws DataAccessException If player logout fails.
     * 
     */
	@Override
	public void killGame(boolean createNewOne) throws DataAccessException {
		try {
			this.serverEngine.killGame(createNewOne);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Returns the players, which are logged in
     * and assigned to the currently running game session.
     *
     * @return A vector with all logged in players, which are assigned to the current game.
     *
     * @throws DataAccessException
     *
     */
	@Override
	public Vector<Player> getGamePlayers() throws DataAccessException {
		try {
			return this.serverEngine.getGamePlayers();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Sets the world on which the game should be played.
     * Will only be defined if the game is not running.
     * 
     * @param The world to play on.
     *
     * @throws DataAccessException
     *
     */
	@Override
	public void setGameWorld(World world) throws DataAccessException {
		try {
			this.serverEngine.setGameWorld(world);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Returns the world value object from
     * the current game.
     * 
     * @return The world value object.
     *
     * @throws DataAccessException
     *
     */
	@Override
	public World getGameWorld() throws DataAccessException {
		try {
			return this.serverEngine.getGameWorld();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Returns all player missions.
     * 
     * @return A vector with all player missions.
     *
     * @throws DataAccessException
     *
     */
	@Override
	public Vector<Mission> getGameMissions() throws DataAccessException {
		try {
			return this.serverEngine.getGameMissions();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Is the current game session terminated?
     *
     * @return boolean
     *
     * @throws DataAccessException
     *
     */
	@Override
	public boolean isGameFinished() throws DataAccessException {
		try {
			return this.serverEngine.isGameFinished();
		} catch (SimonRemoteException e) {
			 throw new DataAccessException(e);
		}
	}

    /**
     * Returns the game winner if the game is terminated.
     *
     * @return The game winner. null if the game is still running.
     *
     * @throws DataAccessException
     *
     */
	@Override
	public Player getGameWinner() throws DataAccessException {
		try {
			return this.serverEngine.getGameWinner();
		} catch (SimonRemoteException e) {
			 throw new DataAccessException(e);
		}
	}

    /**
     * Release a country card.
     *
     * @param The releasable country card.
     *
     * @throws DataAccessException;
     *
     */
	@Override
	public void releaseCard(CountryCard card) throws DataAccessException {
		try {
			this.serverEngine.releaseCard(card);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Releases a country card series.
     * 
     * @param cards A vector with releasable country cards.
     *
     * @throws DataAccessException
     *
     */
	@Override
	public void releaseCards(Vector<CountryCard> cards) throws DataAccessException {
		try {
			this.serverEngine.releaseCards(cards);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Returns the current allocatable army count from the server.
     * 
     * @return army count
     *
     * @throws DataAccessException
     *
     */
	@Override
	public int getAllocatableArmyCount() throws DataAccessException {
		try {
			return this.serverEngine.getAllocatableArmyCount();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Adds one army to the specified country.
     *
     * @param The country on which the army should be placed.
     *
     * @throws CountryOwnerException 
     * @throws NotEnoughArmiesException
     * @throws DataAccessException 
     * 
     */
	@Override
	public void allocateArmy(Country allocatable) throws NotEnoughArmiesException, CountryOwnerException, DataAccessException {
		try {
			this.serverEngine.allocateArmy(allocatable);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Attacks a country with an specified army count.
     * 
     * @param source The country from which the attack will be started.
     * @param destination The country which should be attacked.
     * @param armyCount The attacking army units.
     * 
     * @throws CountriesNotInRelationException
     * @throws NotEnoughArmiesException
     * @throws IsOwnCountryException
     * @throws DataAccessException
     * 
     */
	@Override
	public AttackResult attack(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException, DataAccessException {
		try {
			return this.serverEngine.attack(source, destination, armyCount);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Moves armies from one country to another country.
     * 
     * @param from The country FROM which we will move the armies.
     * @param to The country TO which we will move the armies.
     * @param armyCount How many army units should be moved?
     *
     * @throws CountryOwnerException 
     * @throws NotEnoughArmiesException 
     * @throws CountriesNotInRelationException
     * @throws DataAccessException
     * 
     */
	@Override
	public void move(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException, DataAccessException {
		try {
			this.serverEngine.move(source, destination, armyCount);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

    /**
     * Starts the next turn.
     * 
     */
	@Override
	public void finishTurn() throws DataAccessException {
		try {
			this.serverEngine.finishTurn();
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Sets the clients player state.
	 *
	 * @param state The new player state.
	 *
	 * @throws DataAccessException
	 *
	 */
	@Override
	public void setCurrentPlayerState(PlayerState state) throws DataAccessException {
		try {
			this.serverEngine.setCurrentPlayerState(state);
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Returns the player value object assigned to this client.
	 * 
	 * @return The clients player value object.
	 *
	 */
	public Player getClientPlayer() {
		return this.clientPlayer;
	}

	/**
	 * Add an observer.
	 * The game engine is observable.
	 * So an observer object can, well, observe the
	 * engine and will be informed by some methods,
	 * that something has changed.
	 * 
	 * @param The observer.
	 *
	 */
	public void addObserver(Observer o) {
		super.addObserver(o);
	}
	
	/**
	 * Remove an observer.
	 *
	 * @param The remove candidate.
	 *
	 */
	public void deleteObserver(Observer o) {
		super.deleteObserver(o);
	}

	/**
	 * Method, which can be invoked from the
	 * server side. The server calls this method
	 * and pushes the updated client player object.
	 * 
	 * The engine will then inform all observers that
	 * an "change" occurred.
	 *
	 * @param player The 'fresh' player object.
	 *
	 */
	@Override
	public void refreshPlayer(Player player) {
		this.clientPlayer = player;

		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * Requests the current games "world map image" from the server.
	 * 
	 * @return A array, which holds the rgb values from every pixel.
	 *
	 */
	@Override
	public BufferedImage getGameMapImage() throws DataAccessException {
		try {
			return MapTool.fromBase64(this.serverEngine.getGameMapImage());
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		} catch (IOException e) {
			throw new DataAccessException(e, false);
		}
	}

	/**
	 * Requests the current games "world map underlay image" from the server.
	 * 
	 * @return A array, which holds the rgb values from every pixel.
	 *
	 */
	@Override
	public BufferedImage getGameMapUnderlayImage() throws DataAccessException {
		try {
			return MapTool.fromBase64(this.serverEngine.getGameMapUnderlayImage());
		} catch (SimonRemoteException e) {
			throw new DataAccessException(e);
		} catch (IOException e) {
			throw new DataAccessException(e, false);
		}
	}
}