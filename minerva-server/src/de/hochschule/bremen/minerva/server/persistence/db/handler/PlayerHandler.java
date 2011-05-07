/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: PlayerHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseIOException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PlayerNotFoundException;

/**
 * Handler, which provides the functionality to select, save or deletes
 * players from the database.
 *
 * @since 1.0
 * @version $Id: PlayerHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
 *
 */
public class PlayerHandler extends AbstractDatabaseHandler implements Handler {

	private final static HashMap<String, String> sql = new HashMap<String, String>();

	static {
		sql.put("selectById", "select \"id\", \"username\", \"password\", \"last_name\", \"first_name\", \"email\", \"logged_in\" from player where \"id\" = ?");
		sql.put("selectByName", "select \"id\", \"username\", \"password\", \"last_name\", \"first_name\", \"email\", \"logged_in\" from player where \"username\" = ?");
		sql.put("selectAll", "select \"id\", \"username\", \"password\", \"last_name\", \"first_name\", \"email\", \"logged_in\" from player order by \"username\"");
		sql.put("insert", "insert into player (\"username\", \"password\", \"last_name\", \"first_name\", \"email\") values (?, ?, ?, ?, ?)");
		sql.put("update", "update player set \"username\" = ?, \"password\" = ?, \"last_name\" = ?, \"first_name\" = ?, \"email\" = ?, \"logged_in\" = ? where \"id\" = ?");
		sql.put("delete", "delete from player where \"id\" = ?");
	}


	/**
	 * Reads ONE player with the given id from the database.
	 * 
	 * @param id The unique player id.
	 * @return The found player.
	 *
	 * @throws PlayerNotFoundException
	 * @throws DataAccessExceptiona
	 * 
	 */
	@Override
	public Player read(int id) throws PlayerNotFoundException, PersistenceException {
		Player player = null;
		Object[] params = {id};

		try {
			player = this.read(sql.get("selectById"), params);
		} catch (PlayerNotFoundException e) {
			throw new PlayerNotFoundException("The player with the id '"+id+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the player (id=" + id +") "
					                       + "from the database.\nReason: "+e.getMessage());
		}

		return player;
	}

	/**
	 * Reads ONE player with the given name from the database.
	 * 
	 * @param name The username from those player we are looking for.
	 * @return The found player.
	 *
	 * @throws PlayerNotFoundException
	 * @throws PersistenceException
	 * 
	 */
	public Player read(String name) throws PlayerNotFoundException, PersistenceException {
		Player player = null;
		Object[] params = {name};

		try {
			player = this.read(sql.get("selectByName"), params);
		} catch (PlayerNotFoundException e) {
			throw new PlayerNotFoundException("The player with the username '"+name+"' wasn't found.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Error occurred while reading "
					                       + "the player (username=" + name +") "
					                       + "from the database.\nReason: "+e.getMessage());
		}
		
		return player;
	}

	/**
	 * Private method that wraps the select from the database
	 * functionality. Don't repeat yourself! :)
	 * 
	 * @param sql The raw sql statement.
	 * @param params The sql statement parameters.
	 * @return The player, which was read from the database.
	 * 
	 * @throws PlayerNotFoundException
	 * @throws DatabaseIOException
	 *
	 */
	private Player read(String sql, Object[] params) throws PlayerNotFoundException, DatabaseIOException {
		Player player = null;

		try {
			ResultSet record = this.select(sql, params);

			if (record.next()) {
				player = this.resultSetToObject(record);
				record.close();
			} else {
				throw new PlayerNotFoundException();
			}
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while reading from result set: " + e.getErrorCode());
		}

		return player;
	}
	
	/**
	 * Reads ALL player from the database.
	 * 
	 * @return A vector with found players
	 * @throws PersistenceException
	 *
	 */
	@Override
	public Vector<Player> readAll() throws PersistenceException {
		Vector<Player> players = new Vector<Player>();

		try {
			ResultSet record = this.select(sql.get("selectAll"));

			while (record.next()) {
				players.add(this.resultSetToObject(record));
			}

			record.close();
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		} catch (SQLException e) {
			throw new PersistenceException("Error occurred while receiving a player list from the database: "
											 +e.getMessage()+" - "+e.getErrorCode());
		}
		
		return players;
	}

	/**
	 * Deletes a player.
	 * 
	 * @param candidate The deletable player.
	 * @throws PersistenceException
	 * 
	 */
	@Override
	public void remove(ValueObject candidate) throws PersistenceException {
		Player deletablePlayer = (Player)candidate;
		Object[] params = {deletablePlayer.getId()};

		try {
			this.delete(sql.get("delete"), params);
		} catch (DatabaseIOException e) {
			throw new PersistenceException(e.getMessage());
		}
		
	}

	/**
	 * Save a player. 
	 * 
	 * @param player The saveable player.
	 *
	 * @throws PlayerExistsException
	 * @throws PersistenceException
	 *
	 */
	@Override
	public void save(ValueObject player) throws PlayerExistsException, PersistenceException {
		Player registrablePlayer = (Player)player;

		try {
			try {
				// We try to load the player by the given id.
				// When this is not possible (PlayerNotFoundException), we
				// will update the record else we will insert it.
				this.read(registrablePlayer.getId());
				
				Object[] params = {
					registrablePlayer.getUsername(),
					registrablePlayer.getPassword(),
					registrablePlayer.getLastName(),
					registrablePlayer.getFirstName(),
					registrablePlayer.getEmail(),
					((registrablePlayer.isLoggedIn()) ? 1 : 0),
					registrablePlayer.getId()
				};

				this.update(sql.get("update"), params);
			} catch (PlayerNotFoundException e) {
				Object[] params = {
					registrablePlayer.getUsername(),
					registrablePlayer.getPassword(),
					registrablePlayer.getLastName(),
					registrablePlayer.getFirstName(),
					registrablePlayer.getEmail(),
				};

				this.insert(sql.get("insert"), params);
			}
		} catch (DatabaseDuplicateRecordException ex) {
			throw new PlayerExistsException("Unable to serialize the "
					+"player: '"+registrablePlayer.getUsername()+"'. There is already "
					+"a similar one.");
		} catch (DatabaseIOException e) {
			throw new PersistenceException("Unable to serialize the player: '"+registrablePlayer.getUsername()+"'.\nReason: "+e.getMessage());			
		}

		// The player does not have a player id.
		// So we read the player object by the given username
		// to fulfill the referenced player value object.
		try {
			registrablePlayer.setId(this.read(registrablePlayer.getUsername()).getId());
		} catch (PlayerNotFoundException e) {
			// It is not possible, that this exception will be thrown.
			// We created the player moments before.
		}
		player = registrablePlayer;
	}

	/**
	 * Converts a database result set to an
	 * player value object.
	 * 
	 * @param convertable The convertable player result set.
	 * 
	 * @throws SQLException
	 * 
	 */
	@Override
	protected Player resultSetToObject(ResultSet current) throws SQLException {
		Player player = new Player();
		
		player.setId(current.getInt(1));
		player.setUsername(current.getString(2));
		player.setPassword(current.getString(3));
		player.setLastName(current.getString(4));
		player.setFirstName(current.getString(5));
		player.setEmail(current.getString(6));
		player.setLoggedIn((current.getShort(7)) == 1); 
		
		return player;
	}

	/**
	 * Not in use. But the interface forces me to declare this method.
	 * What a pity! Would be great to define a second interface, but, well the time :(
	 * 
	 */
	@Override
	public Vector<Player> readAll(ValueObject reference) throws PersistenceException {return null;}
}