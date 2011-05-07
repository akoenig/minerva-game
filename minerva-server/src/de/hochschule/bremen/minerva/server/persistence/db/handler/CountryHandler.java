/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: CountryHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.util.ColorTool;
import de.hochschule.bremen.minerva.commons.vo.Continent;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.CountryExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.CountryNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;

/**
 * Handler, which provides the functionality to select, save or deletes
 * countries from the database.
 *
 * @since 1.0
 * @version $Id: CountryHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
 *
 */
public class CountryHandler extends AbstractDatabaseHandler implements Handler {

	//private static Logger LOGGER = Logger.getLogger(CountryHandler.class.getName());
	
	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select \"id\", \"token\", \"name\", \"color\", \"continent\", \"world\" from country where \"id\" = ?");
		sql.put("selectByName", "select \"id\", \"token\", \"name\", \"color\", \"continent\", \"world\" from country where \"name\" = ?");
		sql.put("selectAllByWorldId", "select \"id\", \"token\", \"name\", \"color\", \"continent\", \"world\" from country where \"world\" = ?");
		sql.put("selectAllByContinentId", "select \"id\", \"token\", \"name\", \"color\", \"continent\", \"world\" from country where \"continent\" = ?");
		sql.put("insert", "insert into country (\"token\", \"name\", \"color\", \"continent\", \"world\") values (?, ?, ?, ?, ?)");
		sql.put("update", "update country set \"token\" = ?, \"name\" = ?, \"color\" = ?, \"continent\" = ?, \"world\" = ? where \"id\" = ?");
		sql.put("delete", "delete from country where \"id\" = ?");
	}

	/**
	 * Reads ONE country from the database by the given id.
	 * 
	 * @param id The country id.
	 * @return The country value object.
	 * 
	 * @throws CountryNotFoundException
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public Country read(int id) throws CountryNotFoundException, PersistenceException {
		Country country = null;
		Object[] params = {id};

		try {
			country = this.read(sql.get("selectById"), params);
		} catch (CountryNotFoundException e) {
			throw new CountryNotFoundException("The country with the id '"+id+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the country (id=" + id +") "
					                       + "from the database. Reason: "+e.getMessage());
		}

		return country;
	}

	/**
	 * Reads ONE country from the database by the given name.
	 * 
	 * @param name The country name.
	 * @return The country value object
	 *
	 * @throws CountryNotFoundException
	 * @throws PersistenceException Common persistence io exception
	 * 
	 */
	public Country read(String name) throws CountryNotFoundException, PersistenceException {
		Country country = null;
		Object[] params = {name};

		try {
			country = this.read(sql.get("selectByName"), params);
		} catch (CountryNotFoundException e) {
			throw new CountryNotFoundException("The country '"+name+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the country '" + name + "'"
					                       + "from the database. Reason: "+e.getMessage());
		}
		
		return country;
	}

	/**
	 * Private method that wraps the select from the database
	 * functionality. Don't repeat yourself! :)
	 * 
	 * @param sql The raw sql statement.
	 * @param params The sql statement parameters.
	 * @return The country, which was read from the database.
	 * 
	 * @throws CountryNotFoundException
	 * @throws DatabaseIOException
	 * 
	 */
	private Country read(String sql, Object[] params) throws CountryNotFoundException, DatabaseIOException {
		Country country = null;

		try {
			ResultSet record = this.select(sql, params);

			if (record.next()) {
				country = this.resultSetToObject(record);
				record.close();
			} else {
				throw new CountryNotFoundException();
			}
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while reading from result set: " + e.getErrorCode());
		}

		return country;
	}

	/**
	 * Reads all countries from the database.
	 * We also read the first world from the database and use
	 * this as reference for selecting the countries. Well, the
	 * first world is our default world ;)
	 * 
	 * This is a wrapper class for the private method:
	 * readAll(int byWorldId).
	 * 
	 * @return A collection with the selected countries.
	 *
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public Vector<Country> readAll() throws PersistenceException {
		WorldHandler handler = new WorldHandler();
		Vector<World> worlds = handler.readAll();

		return this.readAll(worlds.firstElement());
	}

	/**
	 * Reads all countries by an given world value object
	 * 
	 * This is a wrapper class for the private method:
	 * readAll(int byWorldId).
	 *
	 * @param byWorld The world value object.
	 * @return A collection with the selected countries.
	 *
	 * @throws PersistenceException
	 *  
	 */
	public Vector<Country> readAll(ValueObject byWorld) throws PersistenceException {
		Vector<Country> countries = null;
		
		if (byWorld instanceof World) {
			countries = this.readAll((World)byWorld);
		} else if (byWorld instanceof Continent) {
			countries = this.readAll((Continent)byWorld);
		} else {
			throw new PersistenceException("There is no method implementation for the given value object: "+byWorld.getClass());
		}
		
		return countries;
	}

	/**
	 * Reads all countries from the database which are linked
	 * to the given continent. 
	 * 
	 * @param byContinent The continent, which all found countries are connected to.
	 * @return A collection with the selected countries.
	 *
	 * @throws PersistenceException
	 *
	 */
	private Vector<Country> readAll(Continent byContinent) throws PersistenceException {
		try {
			Object[] params = {byContinent.getId()};
			return this.readAll(sql.get("selectAllByContinentId"), params);
		} catch (PersistenceException e) {
			throw new PersistenceException("Error while reading all countries "
					+"from the database by the given continent id: "+byContinent.getId());
		}
	}
	
	/**
	 * Reads all countries from the database which are linked
	 * to the given world.
	 * 
	 * @param byWorld A world, whose countries should be returned.
	 * @return A collection with the selected countries.
	 *
	 * @throws PersistenceException
	 * 
	 */
	private Vector<Country> readAll(World byWorld) throws PersistenceException {
		try {
			Object[] params = {byWorld.getId()};
			return this.readAll(sql.get("selectAllByWorldId"), params);
		} catch (PersistenceException e) {
			throw new PersistenceException("Error while reading all countries "
					+"from the database by the given world id: "+byWorld.getId());
		}
	}

	/**
	 * Reads all country by an given raw sql statement.
	 * 
	 * @param sql The raw sql statement.
	 * @param params The parameters for the sql statement.
	 * 
	 * @return A vector with found countries.
	 * @throws PersistenceException
	 *
	 */
	private Vector<Country> readAll(String sql, Object[] params) throws PersistenceException {
		Vector<Country> countries = new Vector<Country>();

		try {
			ResultSet record = this.select(sql, params);

			while (record.next()) {
				countries.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceException("SQL error code: "+e.getErrorCode());
		}

		return countries;		
	}
	
	/**
	 * Deletes a specific country from the database.
	 * 
	 * @param candidate The removable country.
	 * 
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public void remove(ValueObject candidate) throws PersistenceException {
		Country deletableCountry = (Country)candidate;
		Object[] params = {deletableCountry.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	/**
	 * Saves a specific country.
	 * 
	 * @param candidate The saveable country.
	 * 
	 * @throws PersistenceException
	 * @throws CountryExistsException 
	 *
	 */
	@Override
	public void save(ValueObject candidate) throws CountryExistsException, PersistenceException {
		Country registrableCountry = (Country)candidate;

		try {
			try {
				// We try to load the country by the given id.
				// When this is not possible (CountryNotFoundException), we
				// will update the record else we will insert it.
				this.read(registrableCountry.getId());

				Object[] params = {
					registrableCountry.getToken(),
					registrableCountry.getName(),
					ColorTool.toHexCode(registrableCountry.getColor()),
					registrableCountry.getContinent().getId(),
					registrableCountry.getWorldId(),
					registrableCountry.getId()
				};

				this.update(sql.get("update"), params);
			} catch (CountryNotFoundException e) {
				Object[] params = {
					registrableCountry.getToken(),
					registrableCountry.getName(),
					ColorTool.toHexCode(registrableCountry.getColor()),
					registrableCountry.getContinent().getId(),
					registrableCountry.getWorldId()
				};

				this.insert(sql.get("insert"), params);
			}
		} catch (DatabaseDuplicateRecordException ex) {
			throw new CountryExistsException("Unable to serialize the "
						+"country: '"+registrableCountry.getName()+"'. There is already "
						+"a similar one.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Unable to serialize the country: '"+registrableCountry.getName()+"'. Reason: "+e.getMessage());
		}

		// The country does not have a player id.
		// So we read the country object by the given name
		// to fulfill the referenced country value object.
		try {
			registrableCountry.setId(this.read(registrableCountry.getName()).getId());
		} catch (CountryNotFoundException e) {
			// It is not possible, that this exception will be thrown.
			// We created the country moments before.
		}
		candidate = registrableCountry;
	}

	/**
	 * Converts a database result set to an
	 * country value object.
	 * 
	 * @param convertable The convertable country result set.
	 * 
	 * @throws SQLException
	 * 
	 */
	@Override
	protected Country resultSetToObject(ResultSet current) throws SQLException {
		Country country = new Country();
		
		country.setId(current.getInt(1));
		country.setToken(current.getString(2));
		country.setName(current.getString(3));
		country.setColor(ColorTool.fromHexCode(current.getString(4)));
		
		// Note that we only save the continent id.
		// Please verify that you will load the continent
		// data from the database in a next step.
		Continent continent = new Continent();
		continent.setId(current.getInt(5));
		country.setContinent(continent);

		country.setWorldId(current.getInt(6));

		return country;
	}
}