/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: NeighbourHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.persistence.db.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.Neighbour;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.NeighbourExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.NeighbourNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;

/**
 * Handler, which provides the functionality to select, save or deletes
 * neighbours from the database.
 *
 * @since 1.0
 * @version $Id: NeighbourHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
 *
 */
public class NeighbourHandler extends AbstractDatabaseHandler implements Handler {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectByMappingId", "select \"id\", \"neighbour_country\", \"country\" as \"neighbour\" from neighbour where \"id\" = ?");
		sql.put("selectByReferencedCountryId", "select \"id\", \"neighbour_country\", \"country\" as \"neighbour\" from neighbour where \"country\" = ?");
		sql.put("insert", "insert into neighbour (\"country\", \"neighbour_country\") values (?, ?)");
		sql.put("update", "update neighbour set \"country\" = ?, \"neighbour_country\" = ? where \"id\" = ?");
	}

	/**
	 * Reads ONE neighbour from the database by the given id.
	 * 
	 * @param id The neighbour country id.
	 * @return The neighbour value object.
	 * 
	 * @throws NeighbourNotFoundException
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public ValueObject read(int countryId) throws NeighbourNotFoundException, PersistenceException {
		Neighbour neighbour = null;
		Object[] params = {countryId};

		try {
			neighbour = this.read(sql.get("selectByMappingId"), params);
		} catch (NeighbourNotFoundException e) {
			throw new NeighbourNotFoundException("The neighbour mapping with the id '"+countryId+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the neighbour (mapping id = " + countryId +") "
					                       + "from the database. Reason: "+e.getMessage());
		}

		return neighbour;
	}

	/**
	 * Private method that wraps the select from the database
	 * functionality. Don't repeat yourself! :)
	 * 
	 * @param sql The raw sql statement.
	 * @param params The sql statement parameters.
	 * @return The neighbour, which was read from the database.
	 * 
	 * @throws NeighbourNotFoundException
	 * @throws DatabaseIOException
	 * 
	 */
	private Neighbour read(String sql, Object[] params) throws NeighbourNotFoundException, DatabaseIOException {
		Neighbour neighbour = null;

		try {
			ResultSet record = this.select(sql, params);

			if (record.next()) {
				neighbour = this.resultSetToObject(record);
				record.close();
			} else {
				throw new NeighbourNotFoundException();
			}
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while reading from result set: " + e.getErrorCode());
		}

		return neighbour;
	}

	/**
	 * Reads all neighbours by a given reference country
	 * 
	 * @param referencedCountry The country whose neighbours we are looking for.
	 * @return A vector with all neighbours.
	 *
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public Vector<Neighbour> readAll(ValueObject referencedCountry) throws PersistenceException {
		Country referenceCountry = (Country)referencedCountry;
		Vector<Neighbour> neighbours = new Vector<Neighbour>();

		try {
			Object[] params = {referenceCountry.getId()};
			ResultSet record = this.select(sql.get("selectByReferencedCountryId"), params);

			while (record.next()) {
				Neighbour neighbour = this.resultSetToObject(record);
				neighbours.add(neighbour);
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceException("Error occurred while "
											+"receiving a neighbours "
											+"from the database (reference "
											+"country = "+referenceCountry.getId()+"): "
											+e.getMessage()
											+" - "+e.getErrorCode());
		}

		return neighbours;
	}

	/**
	 * Saves a neighbour-country mapping.
	 * 
	 * @param registrable The registrable neighbour value object.
	 * 
	 * @throws NeighbourExistsException
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public void save(ValueObject registrable) throws NeighbourExistsException, PersistenceException {
		Neighbour registrableNeighbour = (Neighbour)registrable;

		try {
			try {
				// We try to load the neighbour relation by a given mapping id.
				// When this is not possible (NeighbourNotFoundException), we
				// will update the record else we will insert it.
				this.read(registrableNeighbour.getMappingId());
				
				Object[] params = {
					registrableNeighbour.getReference().getId(),
					registrableNeighbour.getId(),
					registrableNeighbour.getMappingId()
				};
				this.update(sql.get("update"), params);
			} catch (NeighbourNotFoundException e) {
				Object[] params = {
					registrableNeighbour.getReference().getId(),
					registrableNeighbour.getId()
				};
				this.insert(sql.get("insert"), params);
			}
		} catch (DatabaseDuplicateRecordException e) {
			throw new NeighbourExistsException("Unable to serialize the "
					+"neighbour relation. There is already a relation with: country id = '"
					+registrableNeighbour.getReference().getId() + " and neighbour id = '"
					+registrableNeighbour.getId());
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Unable to serialize the neighbour mapping with referenced country id = '"
					+registrableNeighbour.getReference().getId()+"' and neighbour id = '"
					+registrableNeighbour.getId()+"'. Reason: "+e.getMessage());
		}
	}
	
	/**
	 * Converts a database result set to an
	 * neighbour value object.
	 * 
	 * @param convertable The convertable neighbour result set.
	 * 
	 * @throws SQLException
	 * 
	 */
	@Override
	protected Neighbour resultSetToObject(ResultSet current) throws SQLException {
		Neighbour neighbour = new Neighbour();
		neighbour.setMappingId(current.getInt(1));
		neighbour.setId(current.getInt(2));
		
		Country country = new Country();
		country.setId(current.getInt(3));

		neighbour.setReference(country);
		return neighbour;
	}

	/**
	 * Not in use. But the interface forces me to declare this method.
	 * What a pity! Would be great to define a second interface, but, well the time :(
	 * 
	 */
	@Override
	public Vector<? extends ValueObject> readAll() throws PersistenceException {return null;}

	/**
	 * Not in use. But the interface forces me to declare this method.
	 * What a pity! Would be great to define a second interface, but, well the time :(
	 * 
	 */
	@Override
	public void remove(ValueObject candidate) throws PersistenceException {}

	/**
	 * Not in use. But the interface forces me to declare this method.
	 * What a pity! Would be great to define a second interface, but, well the time :(
	 * 
	 */
	@Override
	public ValueObject read(String name) throws PersistenceException {return null;}
}