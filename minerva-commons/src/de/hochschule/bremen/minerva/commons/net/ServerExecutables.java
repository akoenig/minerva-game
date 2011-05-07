/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ServerExecutables.java 839 2010-08-07 19:50:36Z andre.koenig $
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
package de.hochschule.bremen.minerva.commons.net;

import java.io.IOException;
import java.util.Vector;

import de.root1.simon.SimonRemote;
import de.root1.simon.exceptions.SimonRemoteException;
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
import de.hochschule.bremen.minerva.commons.vo.AttackResult;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.CountryCard;
import de.hochschule.bremen.minerva.commons.vo.Mission;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.PlayerState;
import de.hochschule.bremen.minerva.commons.vo.World;

/**
 * The server protocol.
 * Contains methods, which are callable via minerva clients.
 * 
 * @since 1.0
 * @version $Id: ServerExecutables.java 839 2010-08-07 19:50:36Z andre.koenig $
 * 
 */
public interface ServerExecutables extends SimonRemote {

	// Login a player and add this player to the game (if it was not started)
	// define the callback interface, which will be called if the client has
	// to change something.
	public void login(Player player, ClientExecutables clientExecutables) throws SimonRemoteException, PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException;

	// Registers a new player.
	public void register(Player player) throws SimonRemoteException, PlayerExistsException, DataAccessException;

	// Imports a world from the world import file. Note, that you have to upload the
	// file via prepareWorldFileTransfer/getWorldFileBytes before.
	public void importWorld(String filename) throws SimonRemoteException, WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException, WorldNotStorable, DataAccessException;
	
	// Returns all available world (flatView = no country dependencies)
	public Vector<World> getWorlds(boolean flatView) throws SimonRemoteException, DataAccessException;

	// Start the game (the gamemaster can do this!)
	public void startGame() throws SimonRemoteException, NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException, DataAccessException;

	// Kill the running game and log all players out.
	public void killGame(boolean createNewOne) throws SimonRemoteException, DataAccessException;

	// Sets the world to play on.
	public void setGameWorld(World world) throws SimonRemoteException, DataAccessException;

	// Return the world the current game is running on.
	public World getGameWorld() throws SimonRemoteException, DataAccessException;

	// Returns the current game players (logged in and assigned to the game).-
	public Vector<Player> getGamePlayers() throws SimonRemoteException;

	// Returns the game missions.
	public Vector<Mission> getGameMissions() throws SimonRemoteException;

	// Return the game winner.
	public Player getGameWinner() throws SimonRemoteException;	
	
	// Game session finished?
	public boolean isGameFinished() throws SimonRemoteException;

	// Gets the current game map image from the server (base64 encoded).
	public String getGameMapImage() throws SimonRemoteException, IOException;

	// Gets the current game map underlay image from the server (base64 encoded).
	public String getGameMapUnderlayImage() throws SimonRemoteException, IOException;	
	
	// -- Game core subsystem --

	// Change player state
	public void setCurrentPlayerState(PlayerState state) throws SimonRemoteException;
	
	// Release a country card.
	public void releaseCard(CountryCard card) throws SimonRemoteException;

	// Release a card stack.
	public void releaseCards(Vector<CountryCard> cards) throws SimonRemoteException;

	// Get the count for allocatable armies.
	public int getAllocatableArmyCount() throws SimonRemoteException;

	// Place one army on the given country.
	public void allocateArmy(Country allocatable) throws SimonRemoteException, NotEnoughArmiesException, CountryOwnerException;

	// The attack method.
	public AttackResult attack(Country source, Country destination, int armyCount) throws SimonRemoteException, CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException;

	// Move the specified army count from one country to another.
	public void move(Country source, Country destination, int armyCount) throws SimonRemoteException, CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException;

	// Finish the current turn.
	public void finishTurn() throws SimonRemoteException;

	// -- World importer subsystem --

	// Open a raw channel pipe for transferring the world file.
	// Returns a token, with which it is possible to initialize the transfer.
	public int prepareWorldFileImport(String worldFileName) throws SimonRemoteException;
}