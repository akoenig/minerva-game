/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: GameEngine.java 839 2010-08-07 19:50:36Z andre.koenig $
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
package de.hochschule.bremen.minerva.commons.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Observer;
import java.util.Vector;

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
 * The game engine.
 * A facade interface, that represents a lightweight subsystem access.
 * 
 * @since 1.0
 * @version $Id: GameEngine.java 839 2010-08-07 19:50:36Z andre.koenig $
 * 
 */
public interface GameEngine {

	// --------------------------------------
	// -- login and registration subsystem --
	// --------------------------------------
	public void login(Player player) throws PlayerAlreadyLoggedInException, GameAlreadyStartedException, WrongPasswordException, PlayerDoesNotExistException, NoPlayerSlotAvailableException, DataAccessException;

	public void register(Player player) throws PlayerExistsException, DataAccessException;

	public Player getClientPlayer();

	// ---------------------
	// -- world subsystem --
	// ---------------------
	public Vector<World> getWorldList() throws DataAccessException;

	public Vector<World> getWorldList(boolean lite) throws DataAccessException;

	public void importWorld(File worldFile) throws WorldNotStorable, WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException, DataAccessException;

	// ----------------------------
	// -- game core logic subsystem
	// ----------------------------
	public void startGame() throws NotEnoughPlayersLoggedInException, NoPlayerLoggedInException, WorldNotDefinedException, DataAccessException;

	public void killGame(boolean createNewOne) throws DataAccessException;

	public void setGameWorld(World world) throws DataAccessException;

	public World getGameWorld() throws DataAccessException;

	public Vector<Player> getGamePlayers() throws DataAccessException;

	public Vector<Mission> getGameMissions() throws DataAccessException;

	public boolean isGameFinished() throws DataAccessException;

	public Player getGameWinner() throws DataAccessException;

	public BufferedImage getGameMapImage() throws DataAccessException;

	public BufferedImage getGameMapUnderlayImage() throws DataAccessException;

	public void setCurrentPlayerState(PlayerState state) throws DataAccessException;
	
	public void releaseCard(CountryCard card) throws DataAccessException;

	public void releaseCards(Vector<CountryCard> cards) throws DataAccessException;
	
	public int getAllocatableArmyCount() throws DataAccessException;

	public void allocateArmy(Country allocatable) throws NotEnoughArmiesException, CountryOwnerException, DataAccessException;

	public AttackResult attack(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, IsOwnCountryException, DataAccessException;
	
	public void move(Country source, Country destination, int armyCount) throws CountriesNotInRelationException, NotEnoughArmiesException, CountryOwnerException, DataAccessException;

	public void addObserver(Observer o);
	
	public void deleteObserver(Observer o);

	public void finishTurn() throws DataAccessException;
}