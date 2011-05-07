/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: CountryService.java 663 2010-07-04 16:24:05Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.exceptions.CountryExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.CountryNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;

/**
 * Provides methods for country I/O operations, like:
 * selecting, inserting, updating and deleting them via
 * the persistence handlers.
 *
 * This service is a wrapper for the underlying persistence handler.
 *
 * @since 1.0
 * @version $Id: CountryService.java 663 2010-07-04 16:24:05Z andre.koenig $
 * 
 * @see CountryHandler
 *
 */
public class CountryService extends PersistenceService {

	// The CountryService instance (singleton pattern)
	private static CountryService instance = null;

	// The country persistence handler
	private Handler handler = CountryService.storage.createHandler(Country.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a CountryService in the common way.
	 * So this constructor is private.
	 * 
	 */
	private CountryService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return {@link CountryService}
	 * 
	 */
	public static CountryService getInstance() {
		if (CountryService.instance == null) {
			CountryService.instance = new CountryService();
		}
		return CountryService.instance;
	}

	/**
	 * Returns a vector with all available countries.
	 * 
	 * @return Country vector.
	 *
	 * @throws PersistenceException Common persistence exception.
	 *  
	 */
	@SuppressWarnings("unchecked")
	public Vector<Country> findAll() throws PersistenceException {
		Vector<Country> countries = (Vector<Country>)handler.readAll();
		return countries;
	}

	/**
	 * Loads all countries by an given world value object.
	 * 
	 * @param byWorldVo The world reference.
	 * @return A vector with all countries regarding to the given world.
	 *
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Vector<Country> findAll(ValueObject byWorldVo) throws PersistenceException {
		Vector<Country> countries = (Vector<Country>)handler.readAll(byWorldVo);
		return countries;
	}

	/**
	 * Returns a country by id.
	 * 
	 * @param id The unique identifier.
	 * @return The country value object.
	 *
	 * @throws CountryNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	public Country find(int id) throws CountryNotFoundException, PersistenceException {
		try {
			return (Country)handler.read(id);
		} catch (EntryNotFoundException e) {
			throw new CountryNotFoundException(e.getMessage());
		}
	}

	/**
	 * Returns a country by name.
	 *  
	 * Note that if there is more than one country with
	 * the given name, the method returns the first value object,
	 * that was found.
	 * 
	 * @param name The name identifier.
	 * @return The country value object.
	 * 
	 * @throws CountryNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	public Country find(String name) throws CountryNotFoundException, PersistenceException {
		try {
			return (Country)handler.read(name);
		} catch (EntryNotFoundException e) {
			throw new CountryNotFoundException(e.getMessage());
		}
	}
	
	/**
	 * Saves a country.
	 * 
	 * @param candidate The saveable candidate.
	 * 
	 * @throws CountryExistsException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@Override
	public void save(ValueObject candidate) throws CountryExistsException, PersistenceException {
		try {
			handler.save((Country)candidate);
		} catch (EntryExistsException e) {
			throw new CountryExistsException(e.getMessage());
		}
	}

	/**
	 * Deletes a country.
	 * 
	 * @param candidate The deletable candidate.
	 * 
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@Override
	public void delete(ValueObject candidate) throws PersistenceException {
		Country deletableCountry = (Country)candidate;
		
		handler.remove(deletableCountry);
	}
}