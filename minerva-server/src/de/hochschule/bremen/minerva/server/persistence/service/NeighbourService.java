/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: NeighbourService.java 663 2010-07-04 16:24:05Z andre.koenig $
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
import de.hochschule.bremen.minerva.commons.vo.Neighbour;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.NeighbourExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.NeighbourNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;

/**
 * Service for requesting country neighbours and so on.
 * 
 * This service is a wrapper for the underlying persistence handler.
 *  
 * @since 1.0
 * @version $Id: NeighbourService.java 663 2010-07-04 16:24:05Z andre.koenig $
 * 
 * @see NeighbourHandler
 * 
 */
public class NeighbourService extends PersistenceService {

	// The NeighbourService instance (singleton pattern)
	private static NeighbourService instance = null;

	// The neighbour persistence handler
	private Handler handler = NeighbourService.storage.createHandler(Neighbour.class);

	/**
	 * Singleton pattern. It is not possible
	 * to create a NeighbourService in the common way.
	 * So this constructor is private.
	 */
	private NeighbourService() {}
	
	/**
	 * Singleton pattern.
	 * Static method that controls the object creation.
	 * 
	 * @return {@link NeighbourService}
	 * 
	 */
	public static NeighbourService getInstance() {
		if (NeighbourService.instance == null) {
			NeighbourService.instance = new NeighbourService();
		}
		return NeighbourService.instance;
	}

	/**
	 * Loads all neighbours, which regards to the given country.
	 * 
	 * @param byCountry The country, which neighbours should determined.
	 * @return A vector with the country's neighbours.
	 * 
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Vector<Neighbour> loadAll(Country byCountry) throws PersistenceException {
		return (Vector<Neighbour>)handler.readAll(byCountry);
	}

	/**
	 * Finds a neighbour by an given id.
	 * 
	 * @param id The unique neighbour id.
	 *
	 * @throws NeighbourNotFoundException
	 * @throws PersistenceException Common persistence exception.
	 * 
	 */
	@Override
	public Neighbour find(int id) throws NeighbourNotFoundException, PersistenceException {
		try {
			return (Neighbour)handler.read(id);
		} catch (EntryNotFoundException e) {
			throw new NeighbourNotFoundException(e.getMessage());
		}
	}

	/**
	 * Saves a neighbour.
	 * 
	 * @param candidate The saveable neighbour.
	 *
	 * @throws NeighbourExistsException 
	 * @throws PersistenceException Common persistence exception. 
	 * 
	 */
	@Override
	public void save(ValueObject candidate) throws NeighbourExistsException, PersistenceException {
		try {
			handler.save((Neighbour)candidate);
		} catch (EntryExistsException e) {
			throw new NeighbourExistsException(e.getMessage());
		}
	}

	/**
	 * Not implemented yet (not necessary at the moment).
	 * This method should remove the country/neighbour-mapping.
	 *
	 */
	@Override
	public void delete(ValueObject candidate) throws PersistenceException {}

	/**
	 * Not implemented yet (not necessary at the moment).
	 * Use {@link CountryService#findAll()} instead (Neighbour is a Country subclass).
	 * 
	 */
	@Override
	public Vector<?> findAll() throws PersistenceException {return null;}

	/**
	 * Not implemented yet (not necessary at the moment).
	 * Use {@link CountryService#find(String)} instead (Neighbour is a Country subclass).
	 * 
	 */
	@Override
	public ValueObject find(String name) throws EntryNotFoundException, PersistenceException {return null;}
}