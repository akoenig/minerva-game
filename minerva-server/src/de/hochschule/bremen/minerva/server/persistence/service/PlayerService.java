/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: PlayerService.java 663 2010-07-04 16:24:05Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.persistence.service;

import java.util.Vector;

import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerNotFoundException;

/**
 * Provides methods for player I/O operations, like:
 * selecting, inserting, updating and removing a player.
 * 
 * This service is a wrapper for the underlying persistence handler.
 * 
 * @since 1.0
 * @version $Id: PlayerService.java 663 2010-07-04 16:24:05Z andre.koenig $
 * 
 * @see PlayerHandler
 *
 */
public class PlayerService extends PersistenceService {

	// The PlayerService instance (singleton pattern)
	private static PlayerService instance = null;

	// The player persistence handler
	private Handler handler = PlayerService.storage.createHandler(Player.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a PlayerService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private PlayerService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return {@link PlayerService}
	 * 
	 */
	public static PlayerService getInstance() {
		if (PlayerService.instance == null) {
			PlayerService.instance = new PlayerService();
		}
		return PlayerService.instance;
	}

	/**
	 * Finds all player value objects.
	 * 
	 * @return A vector with all player value objects.
	 *
	 * @throws PersistenceException Common persistence exception.
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Vector<Player> findAll() throws PersistenceException {
		return (Vector<Player>)handler.readAll();
	}

	/**
	 * Find a specific player by id.
	 * 
	 * @param id The unique player id.
	 * @return The player value object with the given id.
	 * 
	 * @throws PlayerNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@Override
	public Player find(int id) throws PlayerNotFoundException, PersistenceException {
		try {
			return (Player)handler.read(id);
		} catch (EntryNotFoundException e) {
			throw new PlayerNotFoundException(e.getMessage());
		}
	}
	
	/**
	 * Finds a specific player by username.
	 * 
	 * @param username The unique username.
	 * @return The player value object with the given username.
	 * 
	 * @throws PlayerNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	public Player find(String username) throws PlayerNotFoundException, PersistenceException {
		try {
			return (Player)handler.read(username);
		} catch (EntryNotFoundException e) {
			throw new PlayerNotFoundException(e.getMessage());
		}
	}

	/**
	 * Saves a player.
	 * Updates the given player if it exists and creates it if it doesn't.
	 * 
	 * @throws PlayerExistsException If the player was not updatable (e. g. username not available).
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@Override
	public void save(ValueObject candidate) throws PlayerExistsException, PersistenceException {
		try {
			handler.save((Player)candidate);
		} catch (EntryExistsException e) {
			throw new PlayerExistsException(e.getMessage());
		}
	}

	/**
	 * Deletes a specific player.
	 * 
	 * @param candidate The deletable player.
	 *
	 * @throws PersistenceException Common persistence exception.
	 *
	 */
	@Override
	public void delete(ValueObject candidate) throws PersistenceException {
		handler.remove((Player)candidate);
	}
}