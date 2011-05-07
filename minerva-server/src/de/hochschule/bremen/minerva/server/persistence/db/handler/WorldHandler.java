/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: WorldHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.WorldExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.WorldNotFoundException;

/**
 * Handler, which provides the functionality to select, save or deletes
 * worlds from the database.
 *
 * @since 1.0
 * @version $Id: WorldHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
 *
 */
public class WorldHandler extends AbstractDatabaseHandler implements Handler {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select \"id\", \"token\", \"name\", \"description\", \"author\", \"version\", \"map\", \"map_underlay\", \"thumbnail\" from world where \"id\" = ?");
		sql.put("selectByName", "select \"id\", \"token\", \"name\", \"description\", \"author\", \"version\", \"map\", \"map_underlay\", \"thumbnail\" from world where \"name\" = ?");
		sql.put("selectAll", "select \"id\", \"token\", \"name\", \"description\", \"author\", \"version\", \"map\", \"map_underlay\", \"thumbnail\" from world order by \"name\"");
		sql.put("insert", "insert into world (\"token\", \"name\", \"description\", \"author\", \"version\", \"map\", \"map_underlay\", \"thumbnail\") values (?, ?, ?, ?, ?, ?, ?, ?)");
		sql.put("update", "update world set \"token\" = ?, \"name\" = ?, \"description\" = ?, \"author\" = ?, \"version\" = ?, \"map\" = ?, \"map_underlay\" = ?, \"thumbnail\" = ? where \"id\" = ?");
		sql.put("delete", "delete from world where \"id\" = ?");
	}

	/**
	 * Reads ONE world with the given id from the database.
	 * 
	 * @param id The unique world id.
	 * @return The found world.
	 *
	 * @throws WorldNotFoundException
	 * @throws DataAccessExceptiona
	 * 
	 */
	public World read(int id) throws WorldNotFoundException, PersistenceException {
		World world = new World();
		Object[] params = {id};
		
		try {
			world = this.read(sql.get("selectById"), params);
		} catch (WorldNotFoundException e) {
			throw new WorldNotFoundException("The world with the id '"+id+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
                    + "the world (id=" + id +") "
                    + "from the database. Reason: "+e.getMessage());
		}
		
		return world;
	}

	/**
	 * Reads ONE player with the given name from the database.
	 * 
	 * @param name The name from those world we are looking for.
	 * @return The found world.
	 *
	 * @throws WorldNotFoundException
	 * @throws PersistenceException
	 * 
	 */
	public World read(String name) throws WorldNotFoundException, PersistenceException {
		World world = new World();
		Object[] params = {name};

		try {
			world = this.read(sql.get("selectByName"), params);
		} catch (WorldNotFoundException e) {
			throw new WorldNotFoundException("The world '"+name+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the world '" + name +"' "
					                       + "from the database. Reason: "+e.getMessage());
		}
		
		return world;
	}

	/**
	 * Private method that wraps the select from the database
	 * functionality. Don't repeat yourself! :)
	 * 
	 * @param sql The raw sql statement.
	 * @param params The sql statement parameters.
	 * @return The world, which was read from the database.
	 * 
	 * @throws WorldNotFoundException
	 * @throws DatabaseIOException
	 *
	 */
	private World read(String sql, Object[] params) throws WorldNotFoundException, DatabaseIOException {
		World world = new World();

		try {
			ResultSet record = this.select(sql, params);

			if (record.next()) {
				world = this.resultSetToObject(record);
				record.close();
			} else {
				throw new WorldNotFoundException();
			}
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while reading from result set: " + e.getErrorCode());
		}

		return world;
	}
	
	/**
	 * Loads all worlds from the database and return a
	 * list with world value objects.
	 *
	 * @return A vector with all available worlds.
	 *
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public Vector<World> readAll() throws PersistenceException {
		Vector<World> worlds = new Vector<World>();

		try {
			ResultSet record = this.select(sql.get("selectAll"));

			while (record.next()) {
				worlds.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceException("Error occurred while receiving a world list from the database: "
											 +e.getMessage()+" - "+e.getErrorCode());
		}

		return worlds;
	}

	/**
	 * Saves a world.
	 * 
	 * @param registrable The registrable world value object.
	 *
	 * @throws WorldExistsException
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public void save(ValueObject registrable) throws WorldExistsException, PersistenceException {
		World registrableWorld = (World)registrable;

		try {
			try {
				// We try to load the world by the given id.
				// When this is not possible (WorldNotFoundException), we
				// will update the record else we will insert it.
				this.read(registrableWorld.getId());

				Object[] params = {
					registrableWorld.getToken(),
					registrableWorld.getName(),
					registrableWorld.getDescription(),
					registrableWorld.getAuthor(),
					registrableWorld.getVersion(),
					registrableWorld.getMap(),
					registrableWorld.getMapUnderlay(),
					registrableWorld.getThumbnail(),
					registrableWorld.getId()
				};
				this.update(sql.get("update"), params);
			} catch (WorldNotFoundException e) {
				Object[] params = {
					registrableWorld.getToken(),
					registrableWorld.getName(),
					registrableWorld.getDescription(),
					registrableWorld.getAuthor(),
					registrableWorld.getVersion(),
					registrableWorld.getMap(),
					registrableWorld.getMapUnderlay(),
					registrableWorld.getThumbnail()
				};

				this.insert(sql.get("insert"), params);
			}
		} catch (DatabaseDuplicateRecordException ex) {
			throw new WorldExistsException("Unable to serialize the "
					+"world '"+registrableWorld.getName()+"'. There is already "
					+"a similar one.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Unable to serialize the world '"+registrableWorld.getName()+"'. Reason: "+e.getMessage());
		}

		// The player does not have a player id.
		// So we read the player object by the given username
		// to fulfill the referenced player value object.
		try {
			registrableWorld.setId(this.read(registrableWorld.getName()).getId());
		} catch (WorldNotFoundException e) {
			// It is not possible, that this exception will be thrown.
			// We created the world moments before.
		}
		registrable = registrableWorld;
	}

	/**
	 * Deletes a world.
	 * 
	 * @param candidate The deletable world.
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public void remove(ValueObject candidate) throws PersistenceException {
		World deletableWorld = (World)candidate;
		Object[] params = {deletableWorld.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		}
	}

	/**
	 * Converts a database result set to an
	 * world value object.
	 * 
	 * @param convertable The convertable world result set.
	 * 
	 * @throws SQLException
	 * 
	 */
	protected World resultSetToObject(ResultSet current) throws SQLException {
		World world = new World();

		world.setId(current.getInt(1));
		world.setToken(current.getString(2));
		world.setName(current.getString(3));
		world.setDescription(current.getString(4));
		world.setAuthor(current.getString(5));
		world.setVersion(current.getString(6));
		world.setMap(current.getString(7));
		world.setMapUnderlay(current.getString(8));
		world.setThumbnail(current.getString(9));

		return world;
	}

	/**
	 * Not in use. But the interface forces me to declare this method.
	 * What a pity! Would be great to define a second interface, but, well the time :(
	 * 
	 */
	@Override
	public Vector<? extends ValueObject> readAll(ValueObject referencedCountry) throws PersistenceException {return null;}
}