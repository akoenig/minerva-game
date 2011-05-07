/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: WorldService.java 663 2010-07-04 16:24:05Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.WorldExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.WorldNotFoundException;

/**
 * Provides methods for world I/O operations, like:
 * selecting, inserting, updating and removing a world.
 *
 * This service is a wrapper for the underlying persistence handler.
 * 
 * @since 1.0
 * @version $Id: WorldService.java 663 2010-07-04 16:24:05Z andre.koenig $
 * 
 * @see WorldHandler
 *
 */
public class WorldService extends PersistenceService {

	// The WorldService instance (singleton pattern)
	private static WorldService instance = null;

	// The world persistence handler
	private Handler handler = WorldService.storage.createHandler(World.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a WorldService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private WorldService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return {@link WorldService}
	 */
	public static WorldService getInstance() {
		if (WorldService.instance == null) {
			WorldService.instance = new WorldService();
		}
		return WorldService.instance;
	}

	/**
	 * Loads all worlds from the persistence layer.
	 * 
	 * @return A vector with all available worlds.
	 * 
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Vector<World> findAll() throws PersistenceException {
		return (Vector<World>)handler.readAll();
	}

	/**
	 * Find a world by an specific id.
	 * 
	 * @param id The worlds id.
	 * @return The world value object.
	 *
	 * @throws WorldNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 *
	 */
	public World find(int id) throws WorldNotFoundException, PersistenceException {
		try {
			return (World)handler.read(id);
		} catch (EntryNotFoundException e) {
			throw new WorldNotFoundException(e.getMessage());
		}
	}

	/**
	 * Find a world by name.
	 * 
	 * @param name The worlds name.
	 * @return The world value object.
	 * 
	 * @throws WorldNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	public World find(String name) throws WorldNotFoundException, PersistenceException {
		try {
			return (World)handler.read(name);
		} catch (EntryNotFoundException e) {
			throw new WorldNotFoundException(e.getMessage());
		}
	}
	
	/**
	 * Saves the given world.
	 * 
	 * @param candidate The saveable world value object.
	 * 
	 * @throws WorldExistsException
	 * @throws PersistenceException Common persistence exception.
	 *
	 */
	@Override
	public void save(ValueObject candidate) throws WorldExistsException, PersistenceException {
		try {
			handler.save((World)candidate);
		} catch (EntryExistsException e) {
			throw new WorldExistsException(e.getMessage());
		}
	}

	/**
	 * Deletes the given world.
	 *
	 * @param candidate The deletable world value object.
	 *
	 * @throws PersistenceException Common persistence exception.
	 *
	 */
	@Override
	public void delete(ValueObject candidate) throws PersistenceException {
		World deletableWorld = (World)candidate;
		
		handler.remove(deletableWorld);
	}
}