/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: AccountManager.java 663 2010-07-04 16:24:05Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.manager;

import java.util.Vector;

import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerAlreadyLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerDoesNotExistException;
import de.hochschule.bremen.minerva.commons.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.commons.exceptions.WrongPasswordException;
import de.hochschule.bremen.minerva.commons.util.HashTool;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.service.PlayerService;

/**
 * The login and registration subsystem.
 *
 * <br />
 * Usage:
 * 
 * <pre>
 * AccountManager.getInstance().<theMethod>
 * </pre>
 * 
 * @since 1.0
 * @version $Id: AccountManager.java 663 2010-07-04 16:24:05Z andre.koenig $
 * 
 */
public class AccountManager {

	private static AccountManager instance = null;	
	
	private PlayerService service = PlayerService.getInstance();

	/**
	 * Singleton pattern. It is not possible
	 * to create a CountryService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private AccountManager() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return A account manager instance.
	 *
	 */
	public static AccountManager getInstance() {
		if (AccountManager.instance == null) {
			AccountManager.instance = new AccountManager();
		}
		return AccountManager.instance;
	}
	
	/**
	 * Adds the desired player into the database.
	 * 
	 * @param player Player object to create an entry in the database with.
	 *
	 * @throws PlayerExistsException Thrown if the player or email already exists.
	 * @throws DataAccessException Common data access exception.
	 * 
	 */
	public void createPlayer(Player player) throws PlayerExistsException, DataAccessException {
		player.setPassword(HashTool.md5(player.getPassword()));

		try {
			service.save(player);
		} catch (de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerExistsException e) {
			throw new PlayerExistsException(player);
		} catch (PersistenceException e) {
			throw new DataAccessException(e.getMessage());
		} 
	}
	
	/**
	 * Gets all players in database
	 * 
	 * @return Vector of all registered players in the database.
	 *
	 * @throws DataAccessException Common data access exception.
	 *
	 */
	public Vector<Player> getPlayerList() throws DataAccessException {
		Vector<Player> players = new Vector<Player>();
		try {
			players = (Vector<Player>)service.findAll();
		} catch (PersistenceException e) {
			throw new DataAccessException(e.getMessage());
		}
		return players;
	}
	
	/**
	 * Gets all players in database and can return all logged in players.
	 * 
	 * @param loggedInPlayers True if you want to get all logged in players.
	 * @return Vector of all registered players in the database. If loggedInPlayers == true you will only get the logged in players.
	 *
	 * @throws DataAccessException Common data access exception.
	 *
	 */
	public Vector<Player> getPlayerList(boolean loggedInPlayers) throws DataAccessException {
		Vector<Player> players;
		try {
			players = (Vector<Player>)service.findAll();
		} catch (PersistenceException e) {
			throw new DataAccessException(e.getMessage());
		}
		if (loggedInPlayers && (players != null)) {
			Vector<Player> temp = new Vector<Player>(players);
			for (Player player : players) {
				if (!player.isLoggedIn()) {
					temp.remove(player);
				}
			}
			players = temp;
		}
		return players;
	}
	
	/**
	 * Gets a player by username out of the database.
	 * 
	 * @param username Username of the player you want to get.
	 * @return Player object of the desired player.
	 *
	 * @throws PlayerDoesNotExistException
	 * @throws DataAccessException Common data access exception.
	 *
	 */
	public Player getPlayer(String username) throws PlayerDoesNotExistException, DataAccessException {
		try {
			return service.find(username);
		} catch (PlayerNotFoundException e) {
			throw new PlayerDoesNotExistException(username);
		} catch (PersistenceException e) {
			throw new DataAccessException(e.getMessage());
		}
	}
	
	/**
	 * Gets a player by id out of the database.
	 * 
	 * @param id Id of the player you want to get.
	 * @return Player object of the desired player.
	 *
	 * @throws PlayerDoesNotExistException
	 * @throws DataAccessException Common data access exception.
	 *
	 */
	public Player getPlayer(int id) throws PlayerDoesNotExistException, DataAccessException {
		try {
			return service.find(id);
		} catch (PlayerNotFoundException e) {
			throw new PlayerDoesNotExistException(id);
		} catch (PersistenceException e) {
			throw new DataAccessException(e.getMessage());
		}
	}
	
	/**
	 * Gets a player by given username or id (if username not given).
	 * 
	 * @param player Player object you want to fully get out of database.
	 * @return Player object of the desired player.
	 *
	 * @throws DataAccessException Common data access exception.
	 *
	 */
	public Player getPlayer(Player player) throws PlayerDoesNotExistException, DataAccessException {
		
		if (player.getUsername() != null) {
			player = this.getPlayer(player.getUsername());
		} else if (player.getId() > 0) {
			try {
				player = this.getPlayer(player.getId());
			} catch (PlayerDoesNotExistException e) {
				throw new PlayerDoesNotExistException("Username not given and a player with" +
														" that id does not exist.");
			}
		} else {
			throw new PlayerDoesNotExistException("The player doesn't contain an id or a" +
													" username.");
		}
		
		return player;
	}
	
	
	/**
	 * The desired player will be logged in.
	 * 
	 * @param player Player you want to login.
	 *
	 * @throws WrongPasswordException Password typed in didn't match.
	 * @throws PlayerDoesNotExistException Player you want to login doesn't exist.
	 * @throws PersistenceException
	 * @throws DataAccessException Common data access exception.
	 *
	 */
	public void login(Player player) throws PlayerAlreadyLoggedInException, WrongPasswordException, PlayerDoesNotExistException, DataAccessException {
		Player temp = this.getPlayer(player);

		String hashedPassword = HashTool.md5(player.getPassword());

		if (hashedPassword.equals(temp.getPassword())) {
			// TODO: Refactor and move this to Player#copy(Player player)
			player.setId(temp.getId());
			player.setUsername(temp.getUsername());
			player.setFirstName(temp.getFirstName());
			player.setLastName(temp.getLastName());
			player.setEmail(temp.getEmail());
			player.setLoggedIn(temp.isLoggedIn());

			if (temp.isLoggedIn()) {
				throw new PlayerAlreadyLoggedInException(temp);
			}

			temp.setLoggedIn(true);
			
			try {
				service.save(temp);
			} catch (de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerExistsException e) {
				// It is not possible, that the player does not exist.
				// We loaded it a few code lines before. So it must exist.
				// So the catch area will never be thrown.				
			} catch (PersistenceException e) {
				throw new DataAccessException(e.getMessage());
			}

			player.setLoggedIn(temp.isLoggedIn());
		} else {
			throw new WrongPasswordException(player);
		}
	}
	
	/**
	 * This will logout the desired player, regardless if he is logged in or not.
	 * 
	 * @throws PlayerDoesNotExistException Player you want to logout doesn't exist.
	 * @throws DataAccessException Common data access exception.
	 * 
	 */
	public void logout(Player player) throws PlayerDoesNotExistException, DataAccessException {
		try {
			player = this.getPlayer(player);
		} catch (PlayerDoesNotExistException e) {
			throw new PlayerDoesNotExistException("You can't logout a player that does not exist.");
		}
		player.setLoggedIn(false);

		try {
			service.save(player);
		} catch (de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerExistsException e) {
			// It is not possible, that the player does not exist.
			// We loaded it a few code lines before. So it must exist.
			// So the catch area will never be reached.
		} catch (PersistenceException e) {
			throw new DataAccessException(e.getMessage());
		}
	}
	
	/**
	 * All logged in players will be logged out.
	 *
	 * @throws DataAccessException Common data access exception.
	 *
	 */
	public void logout() throws DataAccessException {
		Vector<Player> loggedInPlayers = this.getPlayerList(true);
		for (Player player : loggedInPlayers) {
			player.setLoggedIn(false);

			try {
				service.save(player);
			} catch (de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerExistsException e) {				
				// It is not possible, that the player does not exist.
				// We loaded it a few code lines before. So it must exist.
				// So the catch area will never be thrown.
			} catch (PersistenceException e) {
				throw new DataAccessException(e.getMessage());
			}
		}
	}
}