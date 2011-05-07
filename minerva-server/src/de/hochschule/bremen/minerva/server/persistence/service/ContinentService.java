/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ContinentService.java 663 2010-07-04 16:24:05Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.vo.Continent;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.exceptions.ContinentExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.ContinentNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;

/**
 * Provides methods for continent I/O operations, like:
 * selecting, inserting, updating and deleting them via
 * the persistence handlers.
 * 
 * This service is a wrapper for the underlying persistence handler. 
 * 
 * @since 1.0
 * @version $Id: ContinentService.java 663 2010-07-04 16:24:05Z andre.koenig $
 * 
 * @see ContinentHandler
 * 
 */
public class ContinentService extends PersistenceService {

	// The ContinentService instance (singleton pattern)
	private static ContinentService instance = null;

	// The continent persistence handler
	private Handler handler = CountryService.storage.createHandler(Continent.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a ContinentService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private ContinentService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return {@link ContinentService}
	 * 
	 */
	public static ContinentService getInstance() {
		if (ContinentService.instance == null) {
			ContinentService.instance = new ContinentService();
		}
		return ContinentService.instance;
	}

	/**
	 * Returns all continents.
	 * 
	 * @return All available continent value objects.
	 *
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Vector<Continent> findAll() throws PersistenceException {
		Vector<Continent> continents = (Vector<Continent>)handler.readAll();
		return continents;
	}

	/**
	 * Find a continent by an given id.
	 * 
	 * @param id The unique identifier.
	 * @return The continent (if found).
	 * 
	 * @throws ContinentNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@Override
	public Continent find(int id) throws ContinentNotFoundException, PersistenceException {
		try {
			return (Continent)handler.read(id);
		} catch (EntryNotFoundException e) {
			throw new ContinentNotFoundException(e.getMessage());
		}
	}

	/**
	 * Find a continent by an given name.
	 * 
	 * @param name The unique name.
	 * @return The continent (if found)
	 * 
	 * @throws ContinentNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	public Continent find(String name) throws ContinentNotFoundException, PersistenceException {
		try {
			return (Continent)handler.read(name);
		} catch (EntryNotFoundException e) {
			throw new ContinentNotFoundException(e.getMessage());
		}
	}
	
	
	/**
	 * Saves a continent.
	 * 
	 * @param candidate The saveable continent value object.
	 * 
	 * @throws ContinentExistsException
	 * @throws PersistenceException Common persistence exception.
	 *
	 */
	@Override
	public void save(ValueObject candidate) throws ContinentExistsException, PersistenceException {
		try {
			handler.save((Continent)candidate);
		} catch (EntryExistsException e) {
			throw new ContinentExistsException(e.getMessage());
		}
	}

	/**
	 * Deletes a continent.
	 * 
	 * @param candidate The removable continent value object.
	 * 
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@Override
	public void delete(ValueObject candidate) throws PersistenceException {
		Continent continent = (Continent)candidate;
		handler.remove(continent);
	}
}