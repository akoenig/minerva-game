/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ContinentHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.vo.Continent;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.ContinentExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.ContinentNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;

/**
 * Handler, which provides the functionality to select, save or deletes
 * continents from the database.
 *
 * @since 1.0
 * @version $Id: ContinentHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
 *
 */
public class ContinentHandler extends AbstractDatabaseHandler implements Handler {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select \"id\", \"name\" from continent where \"id\" = ?");
		sql.put("selectByName", "select \"id\", \"name\" from continent where \"name\" = ?");
		sql.put("selectAll", "select \"id\", \"name\" from continent order by name");
		sql.put("insert", "insert into continent (\"name\") values (?)");
		sql.put("update", "update continent set \"name\" = ? where \"id\" = ?");
		sql.put("delete", "delete from continent where \"id\" = ?");
	}

	/**
	 * Reads ONE continent from the database by the given id.
	 * 
	 * @param id The continent id.
	 * @return The continent value object.
	 * 
	 * @throws ContinentNotFoundException
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public Continent read(int id) throws ContinentNotFoundException, PersistenceException {
		Continent continent = null;
		Object[] params = {id};

		try {
			continent = this.read(sql.get("selectById"), params);
		} catch (ContinentNotFoundException e) {
			throw new ContinentNotFoundException("The continent with the id '"+id+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the continent (id=" + id +") "
					                       + "from the database. Reason: "+e.getMessage());
		}

		return continent;
	}

	/**
	 * Reads ONE continent from the database by the given name.
	 * 
	 * @param name The continent name.
	 * @return The continent value object
	 *
	 * @throws ContinentNotFoundException
	 * @throws PersistenceException Common persistence io exception
	 * 
	 */
	public Continent read(String name) throws ContinentNotFoundException, PersistenceException {
		Continent continent = null;
		Object[] params = {name};

		try {
			continent = this.read(sql.get("selectByName"), params);
		} catch (ContinentNotFoundException e) {
			throw new ContinentNotFoundException("The continent '"+name+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the continent (name=" + name +") "
					                       + "from the database. Reason: "+e.getMessage());
		}

		return continent;
	}

	/**
	 * Private method that wraps the select from the database
	 * functionality. Don't repeat yourself! :)
	 * 
	 * @param sql The raw sql statement.
	 * @param params The sql statement parameters.
	 * @return The continent, which was read from the database.
	 * 
	 * @throws ContinentNotFoundException
	 * @throws DatabaseIOException
	 * 
	 */
	private Continent read(String sql, Object[] params) throws ContinentNotFoundException, DatabaseIOException {
		Continent continent = null;

		try {
			ResultSet record = this.select(sql, params);

			if (record.next()) {
				continent = this.resultSetToObject(record);
				record.close();
			} else {
				throw new ContinentNotFoundException();
			}
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while reading from result set: " + e.getErrorCode());
		}

		return continent;
	}
	
	/**
	 * Reads all continents from the database.
	 * 
	 * @return A vector with continent value objects.
	 * 
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public Vector<Continent> readAll() throws PersistenceException {
		Vector<Continent> continents = new Vector<Continent>();

		try {
			ResultSet record = this.select(sql.get("selectAll"));

			while (record.next()) {
				continents.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceException("Error occurred while receiving "
											+"a continent list from the database: "
											 +e.getMessage()+" - "
											 +e.getErrorCode());
		}

		return continents;
	}

	/**
	 * Persists a continent in the database.
	 * 
	 * @param registrable The registrable continent.
	 * 
	 * @throws ContinentExistsException 
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public void save(ValueObject registrable) throws ContinentExistsException, PersistenceException {
		Continent registrableContinent = (Continent)registrable;

		try {
			try {
				// We try to load the continent by the given id.
				// When this is not possible (ContinentNotFoundException), we
				// will update the record else we will insert it.
				this.read(registrableContinent.getId());
	
				Object[] params = {
					registrableContinent.getName(),
					registrableContinent.getId()
				};
				this.update(sql.get("update"), params);
			} catch (ContinentNotFoundException e) {
				Object[] params = {registrableContinent.getName()};
				
				this.insert(sql.get("insert"), params);
			}
		} catch (DatabaseDuplicateRecordException ex) {
			throw new ContinentExistsException("Unable to serialize the continent: '"
					+registrableContinent.getName()+"'. There is already a similar one.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Unable to serialize the continent object: '"+registrableContinent.getName()+"'. Reason: "+e.getMessage());
		}

		// The continent does not have a continent id.
		// So we read the continent object by the given name
		// to fulfill the referenced continent value object.
		try {
			registrableContinent.setId(this.read(registrableContinent.getName()).getId());
		} catch (ContinentNotFoundException e) {
			// It is not possible, that this exception will be thrown. We created the
			// continent moments before.
		}
		registrable = registrableContinent;
	}

	/**
	 * Deletes a specific continent.
	 * 
	 * @param candidate The saveable continent
	 * 
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public void remove(ValueObject candidate) throws PersistenceException {
		Continent deletableContinent = (Continent)candidate;
		
		Object[] params = {deletableContinent.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	/**
	 * Converts a database result set to an continent
	 * value object.
	 * 
	 * @param convertable The convertable continent result set.
	 * 
	 * @throws SQLException
	 * 
	 */
	@Override
	protected Continent resultSetToObject(ResultSet convertable) throws SQLException {
		Continent continent = new Continent();
		
		continent.setId(convertable.getInt(1));
		continent.setName(convertable.getString(2));

		return continent;
	}

	/**
	 * TODO: This method is not necessary. Please check the interface
	 * design to avoid such unused methods.
	 * There is no way to declare optional method (makes no sense) or something.
	 *  
	 */
	@Override
	public Vector<? extends ValueObject> readAll(ValueObject referencedCountry) throws PersistenceException {		
		// TODO Auto-generated method stub
		return null;
	}	
}