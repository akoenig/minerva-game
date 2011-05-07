/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Game.java 683 2010-07-04 16:39:39Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.core.logic;

import java.awt.Color;
import java.util.Collections;
import java.util.Vector;

import de.hochschule.bremen.minerva.commons.exceptions.GameAlreadyStartedException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerSlotAvailableException;
import de.hochschule.bremen.minerva.commons.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.commons.vo.CanonCard;
import de.hochschule.bremen.minerva.commons.vo.CavalerieCard;
import de.hochschule.bremen.minerva.commons.vo.Continent;
import de.hochschule.bremen.minerva.commons.vo.ContinentConquerMission;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.CountryCard;
import de.hochschule.bremen.minerva.commons.vo.CountryConquerMission;
import de.hochschule.bremen.minerva.commons.vo.DefeatPlayerMission;
import de.hochschule.bremen.minerva.commons.vo.Mission;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.PlayerState;
import de.hochschule.bremen.minerva.commons.vo.SoldierCard;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.vo.CardSeriesCounter;

/**
 * The core game class.
 * 
 * @since 1.0
 * @version $Id: Game.java 683 2010-07-04 16:39:39Z andre.koenig $
 *
 */
public class Game {

	private World world = null;
	private Vector<Player> players = new Vector<Player>();
	private Vector<Turn> turns = new Vector<Turn>();
	private Vector<Mission> missions = new Vector<Mission>();
	private Vector<CountryCard> countryCards = new Vector<CountryCard>();
	private Vector<CountryCard> usedCountryCards = new Vector<CountryCard>();
	private CardSeriesCounter seriesCounter = new CardSeriesCounter();
	private boolean started = false;
	private boolean finished = false;
	private Player winner = null;

	private Vector<Color> availablePlayerColors = new Vector<Color>();

	/**
	 * Creates a "empty" game object.
	 * Please note that it is not possible to start the game
	 * if you don't have defined an world or no players logged in.
	 * 
	 * @see Game#start()
	 * 
	 */
	public Game() {
		this.availablePlayerColors.add(Color.BLUE);
		this.availablePlayerColors.add(Color.GREEN);
		this.availablePlayerColors.add(Color.ORANGE);
		this.availablePlayerColors.add(Color.RED);

		// TODO: Release 1.1
		// Should be possible to play with more than 4 players
		// The "available colors" defines the player slots.
		// 4 Colors = 4 possible players
		// this.availablePlayerColors.add(Color.MAGENTA);
		// this.availablePlayerColors.add(Color.GRAY);
		// this.availablePlayerColors.add(Color.YELLOW);*/
	}
	
	/**
	 * Creates a new game, which already has all necessary
	 * data for starting the game.
	 * 
	 * @param world The world to play on.
	 * @param players The players.
	 *
	 * @throws NoPlayerLoggedInException 
	 * @throws NotEnoughPlayersLoggedInException
	 * @throws WorldNotDefinedException 
	 * @throws NoPlayerSlotAvailableException If the given player vector is bigger than the available player slots.
	 * @throws GameAlreadyStartedException 
	 *  
	 */
	public Game(World world, Vector<Player> players) throws NoPlayerLoggedInException, NotEnoughPlayersLoggedInException, WorldNotDefinedException, GameAlreadyStartedException, NoPlayerSlotAvailableException {
		this();
		this.setWorld(world);
		
		for (Player player : players) {
			this.addPlayer(player);
		}
	}

	/**
	 * Starts the initialization process (generate player countries,
	 * allocate missions and so on). And ... starts the first turn.
	 *
	 * @throws NotEnoughPlayersLoggedInException
	 * @throws NoPlayerLoggedInException
	 * @throws WorldNotDefinedException
	 *
	 */
	public void start() throws NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException {
		if (this.players.size() == 0) {
			throw new NoPlayerLoggedInException();
		} else if (this.players.size() == 1) {
			throw new NotEnoughPlayersLoggedInException(players);
		} else if (this.world == null) {
			throw new WorldNotDefinedException();
		}

		this.allocateCountries();
		this.allocateMissions();
		this.generateCountryCards();
		
		this.setStarted(true);

		this.nextTurn();
	}
	
	/**
	 * Sets the next player and creates a new
	 * Turn. A turn provides the core game logic.
     *
	 * @return Turn
	 * 
	 */
	public Turn nextTurn() {
		if (!this.turns.isEmpty()) {
			this.getCurrentTurn().getCurrentPlayer().setState(PlayerState.IDLE);
		}
		Player nextPlayer = this.nextPlayer();
		nextPlayer.setState(PlayerState.RELEASE_CARDS);
		this.turns.add(new Turn(nextPlayer, this.world, this.players, this.countryCards, this.usedCountryCards, this.seriesCounter));
		return this.getCurrentTurn();
	}

	/**
	 * The world on which this game is running.
	 * 
	 * @return The games world.
	 *
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Sets the world to play within this game session.
	 * 
	 * @param world The world on which the game should be played.
	 *
	 */
	public void setWorld(World world) {
		this.world = world;
	}
	
	/**
	 * Gets the player assigned to this game.
	 *
	 * @return A vector with all players assigned to this game.
	 *
	 */
	public Vector<Player> getPlayers() {
		return players;
	}

	/**
	 * Returns the owner of a country
	 * 
	 * @param country country
	 * @return owner
	 * 
	 */
	public Player getPlayer(Country byCountry) {
		for (Player player : this.players) {
			if (player.hasCountry(byCountry)) {
				return player;
			}
		}
		return null;
	}

	/**
	 * Wrapper method for returning the player count.
	 *
	 * @return A count, that represents the players assigned to this game.
	 * 
	 */
	public int getPlayerCount() {
		return this.players.size();
	}
	
	/**
	 * Add a new player to a game that is not running.
	 * 
	 * @param player The player to add.
	 * @throws GameAlreadyStartedException Not able to add a new player to a already running game session.
	 * @throws NoPlayerSlotAvailableException The game is full.
	 * 
	 */
	public void addPlayer(Player player) throws GameAlreadyStartedException, NoPlayerSlotAvailableException {
		if (!this.isRunning()) {
			if (!this.isPlayerInGame(player)) {
				if (this.isPlayerColorAvailable()) {
					player.setColor(this.generatePlayerColor());
					this.players.add(player);
				} else {
					throw new NoPlayerSlotAvailableException(player);
				}
			}
		} else {
			throw new GameAlreadyStartedException(player);
		}
	}

	/**
	 * Check if an given player is already in this game.
	 * 
	 * @param checkablePlayer The player to check for.
	 *
	 * @return boolean
	 *
	 */
	private boolean isPlayerInGame(Player checkablePlayer) {
		for (Player player : this.players) {
			if (player.getUsername().equals(checkablePlayer.getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the player, which represents the gamemaster.
	 * 
	 * @return The game master player.
	 * 
	 */
	public Player getMaster() {
		for (Player player : this.players) {
			if (player.isMaster()) {
				return player;
			}
		}
		
		return null;
	}

	/**
	 * Returns the vector with all previous turns.
	 * 
	 * @return The vector with turns.
	 * 
	 * @deprecated Use a different method. Turns should not be visible!
	 *
	 */
	public Vector<Turn> getTurns() {
		return this.turns;
	}

	/**
	 * Returns the turn count.
	 * 
	 * @return int The turn count.
	 *
	 */
	public int getTurnCount() {
		return this.turns.size();
	}

	/**
	 * Returns the current turn object.
	 * 
	 * @return The current turn to play with.
	 * 
	 */
	public Turn getCurrentTurn() {
		return this.turns.lastElement();
	}

	/**
	 * Is the game finished?
	 *
	 * @return boolean
	 *
	 */
	public boolean isFinished() {
		if (turns.lastElement().getCurrentPlayer().getCountryCount() == world.getCountryCount()) {
			this.setWinner(turns.lastElement().getCurrentPlayer());
			finished = true;
		} else {
			Vector<Player> winners = new Vector<Player>();
			for(Mission mission : this.missions) {
				if (mission.isFulfilled()) {
					winners.add(mission.getOwner());
				}
			}
			if (!winners.isEmpty()) {
				if (winners.contains(this.getCurrentTurn().getCurrentPlayer())) {
					this.setWinner(this.getCurrentTurn().getCurrentPlayer());
				} else {
					this.setWinner(winners.firstElement());
				}
				finished = true;
			}
		}
		
		//sets all players idle when game is finished.
		if (finished) {
			for (Player player : this.players) {
				player.setState(PlayerState.IDLE);
			}
		}

		return finished;
	}

	/**
	 * Is the game running?
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isRunning() {
		return this.started;
	}

	/**
	 * Sets the "game started flag".
	 * 
	 * @param started
	 * 
	 */
	private void setStarted(boolean started) {
		this.started = started;
	}

	/**
	 * Returns the generated mission vector.
	 * 
	 * @return Vector<Mission> The missions
	 * 
	 */
	public Vector<Mission> getMissions() {
		return this.missions;
	}

	/**
	 * The match winner.
	 *
	 * @return The winner player.
	 *
	 */
	public Player getWinner() {
		return this.winner;
	}

	/**
	 * Determines the next player object.
	 * 
	 * @return The next player.
	 * 
	 */
	private Player nextPlayer() {
		if (!turns.isEmpty()) {
			int currentIndex = players.indexOf(turns.lastElement().getCurrentPlayer());
			turns.lastElement().getCurrentPlayer().setCurrentPlayer(false);
			int nextIndex ;
			do {
				nextIndex = (++currentIndex)%players.size();
			} while (!players.get(nextIndex).hasCountries());
			Player nextPlayer = players.get(nextIndex);
			nextPlayer.setCurrentPlayer(true);
			return nextPlayer;
		} else {
			Player nextPlayer = players.firstElement();
			nextPlayer.setCurrentPlayer(true);
			return nextPlayer;
		}
	}

	/**
	 * Allocates the countries. Every player gets an
	 * country in a randomized kind of way.
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void allocateCountries() {
		Vector<Country> allocatableCountries = new Vector<Country>();
		allocatableCountries = (Vector<Country>) this.world.getCountries().clone();
		
		for (int i = 0; i < ((this.world.getCountryCount() / this.players.size()) + 1); i++) {
			for (Player player : this.players) {
				if (allocatableCountries.size() > 0) {
					int index = (int) (Math.random() * allocatableCountries.size());
					player.addCountry(allocatableCountries.get(index));
					allocatableCountries.remove(index);
				}
			}
		}
	}
	
	/**
	 * Generates missions randomly for all players in the game.
	 * 
	 */
	private void allocateMissions() {
		Vector<Player> defeatablePlayers = new Vector<Player>(this.getPlayers());
		Vector<Continent> conquerableContinents = new Vector<Continent>(this.world.getContinents());
		
		int missionType = 0;

		for (Player player : this.players) {
			missionType = (int)(Math.random()*3);
			
			switch (missionType) {
				
				// Conquer country mission
				case 0:
					short conquerableCountryCount = (short)((this.world.getCountryCount()*4)/7);
					this.missions.add(new CountryConquerMission(conquerableCountryCount, player));
				break;

				// Conquer continent mission
				case 1:
					Collections.shuffle(conquerableContinents);
					Vector<Country> firstContinent = this.world.getCountries(conquerableContinents.get(0));
					Vector<Country> secondContinent = this.world.getCountries(conquerableContinents.get(1));
					this.missions.add(new ContinentConquerMission(firstContinent, secondContinent, player));
				break;

				// Defeat player mission
				default:
					// Creates a copy of the defeatable players vector and removes the mission owner.
					// So that the mission owner has no possibility to be the enemy.
					Vector<Player> shuffableDefeatablePlayer = new Vector<Player>(defeatablePlayers);
					shuffableDefeatablePlayer.remove(player);

					Collections.shuffle(shuffableDefeatablePlayer);
					Player enemy = shuffableDefeatablePlayer.firstElement();
					this.missions.add(new DefeatPlayerMission(enemy, player));
					
					// Remove the enemy from the vector with defeatable player. So that the enemy can
					// no be the enemy in another defeat player mission.
					defeatablePlayers.remove(enemy);
				break;
			}
		}
	}

	/**
	 * Generates full stack of country cards
	 * according to the number country list with a random symbol.
	 * 
	 */
	private void generateCountryCards() {
		//TODO:	maybe shuffle country vector without creating temp
		Vector<Country> temp = new Vector<Country>(this.world.getCountries());
		Collections.shuffle(temp);
		
		for (int countryNumber = 0; countryNumber < this.world.getCountryCount(); countryNumber++) {
			CountryCard card;
			if ((countryNumber % 3) == 0) {
				card = new SoldierCard(temp.elementAt(countryNumber));
			} else if ((countryNumber % 3) == 1) {
				card = new CanonCard(temp.elementAt(countryNumber));
			} else {
				card = new CavalerieCard(temp.elementAt(countryNumber));
			}
			this.countryCards.add(card);
		}
		Collections.shuffle(this.countryCards);
	}

	/**
	 * Sets the champ.
	 * 
	 * @param champ Player The winner
	 * 
	 */
	private void setWinner(Player champ) {
		this.winner = champ;
	}

	/**
	 * Gets a randomized color from the "available colors vector".
	 * 
	 * @return The determined color.
	 * 
	 */
	private Color generatePlayerColor() {
		Collections.shuffle(this.availablePlayerColors);
		Color playerColor = new Color(this.availablePlayerColors.get(0).getRGB());
		this.availablePlayerColors.remove(0);

		return playerColor;		
	}

	/**
	 * Are there any player colors available?
	 * If not, the game is full and it is not possible
	 * add a new player.
	 *  
	 * @return boolean
	 *
	 */
	private boolean isPlayerColorAvailable() {
		return !this.availablePlayerColors.isEmpty();
	}
}