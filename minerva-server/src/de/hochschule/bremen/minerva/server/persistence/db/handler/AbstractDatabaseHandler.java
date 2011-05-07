/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: AbstractDatabaseHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;

import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseConnectionException;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseDuplicateRecordException;
import de.hochschule.bremen.minerva.server.persistence.db.exceptions.DatabaseIOException;

/**
 * The abstract database handler, which every database handler should inherit from.
 * Provides database connection handling, sending sql statements and so on.
 * 
 * @since 1.0
 * @version $Id: AbstractDatabaseHandler.java 663 2010-07-04 16:24:05Z andre.koenig $
 *
 */
public abstract class AbstractDatabaseHandler {
	
	private static Connection connection = null;

	private static String databaseName = "database";
	private static String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";

	private static String ERROR_DB_DRIVER = "The database driver wasn't found. What about the driver jar? Is it available?";
	private static String ERROR_DB_LOCKED = "Unable to connect to the specified database. Is it locked? Is the 'sqlexplorer' currently running? ;)";
	private static String ERROR_DB_DISCONNECT_FAILED = "Unable to disconnect from the database.";

	/**
	 * Creates an database connection.
	 * 
	 * @throws DatabaseConnectionException
	 *
	 */
	private void connect() throws DatabaseConnectionException {
		try {
			if (AbstractDatabaseHandler.connection == null) {
				Class.forName(AbstractDatabaseHandler.databaseDriver).newInstance();
				AbstractDatabaseHandler.connection = DriverManager.getConnection("jdbc:derby:"+AbstractDatabaseHandler.databaseName+";create=true");
			}
		} catch (InstantiationException e) {
			throw new DatabaseConnectionException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new DatabaseConnectionException(e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new DatabaseConnectionException(ERROR_DB_DRIVER);
		} catch (SQLException e) {
			throw new DatabaseConnectionException(ERROR_DB_LOCKED);
		}
	}

	/**
	 * Disconnects an active database connection.
	 * 
	 * @throws DatabaseConnectionException
	 *
	 */
	public void disconnect() throws DatabaseConnectionException {
		try {
			if (AbstractDatabaseHandler.connection != null) {
				AbstractDatabaseHandler.connection.close();
			}
		} catch (SQLException e) {
			throw new DatabaseConnectionException(ERROR_DB_DISCONNECT_FAILED);
		}
	}

	/**
	 * Creates a prepared statements.
	 * 
	 * @param sql Raw sql statement.
	 * @param params Parameters, which are insertable into the sql statement.
	 * 
	 * @throws SQLException
	 * 
	 */
	private PreparedStatement createPreparedStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement statement = AbstractDatabaseHandler.connection.prepareStatement(sql);
		statement.clearParameters();

		for (int i = 0; i < params.length; i++) {
			Object param = params[i];
			Class<? extends Object> paramType = param.getClass();

			if (paramType == java.lang.String.class) {
				statement.setString(i+1, (String)param);
			} else if (paramType == java.lang.Integer.class) {
				statement.setInt(i+1, (Integer)param);
			} else if (paramType == java.lang.Short.class) {
				statement.setShort(i+1, (Short)param);
			}
		}

		return statement;
	}

	/**
	 * Executes an sql select statement.
	 * 
	 * @param sql The executable sql statement.
	 * @return The result set
	 * 
	 * @throws DatabaseIOException
	 * 
	 */
	protected ResultSet select(String sql) throws DatabaseIOException {
		this.connect();

		try {
			Statement statement = AbstractDatabaseHandler.connection.createStatement();
			statement.execute(sql);
			return statement.getResultSet();
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while selecting data from the database: "+e.getMessage() + " - "+e.getErrorCode());
		}
	}
	
	/**
	 * Executes an prepared sql select statement.
	 * 
	 * @param sql The executable raw sql statement.
	 * @param params The parameters for the sql statement.
	 * @return The result set.
	 * 
	 * @throws DatabaseIOException
	 * 
	 */
	protected ResultSet select(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.execute();
			return statement.getResultSet();

		} catch (SQLException e) {
			throw new DatabaseIOException("Error while selecting data (with params) from the database: "+e.getMessage() + " - "+e.getErrorCode());
		}
	}

	/**
	 * Executes an prepared sql insert statement.
	 * 
	 * @param sql The executable raw sql statement.
	 * @param params The parameters for the sql statement.
	 * 
	 * @throws DatabaseIOException
	 * 
	 */
	protected void insert(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			switch (e.getErrorCode()) {
				case 30000:
					throw new DatabaseDuplicateRecordException("Error while inserting a new record. The record exists already.");

				case 20000:
					throw new DatabaseDuplicateRecordException("Error while inserting a new record. The record exists already.");
				
				default:
					throw new DatabaseIOException("Error while inserting a new record into the database: "+e.getMessage());
			}
		}
	}

	/**
	 * Executes an prepared sql update statement.
	 * 
	 * @param sql The executable raw sql statement.
	 * @param params The parameters for the sql statement.
	 * 
	 * @throws DatabaseIOException
	 * 
	 */
	protected void update(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.executeUpdate();

			statement.close();
		} catch (SQLException e) {
			switch (e.getErrorCode()) {
				case 30000:	
					throw new DatabaseDuplicateRecordException("Error while updating a record. There is already a similar entry in the database.");

				case 20000:
					throw new DatabaseDuplicateRecordException("Error while updating a record. There is already a similar entry in the database.");

				default:
					throw new DatabaseIOException("Error while updating a record: "+e.getMessage());
			}
		}		
	}

	/**
	 * Executes an prepared sql delete statement.
	 * 
	 * @param sql The executable raw sql statement.
	 * @param params The parameters for the sql statement.
	 * 
	 * @throws DatabaseIOException
	 * 
	 */
	protected void delete(String sql, Object[] params) throws DatabaseIOException {
		this.connect();

		try {
			PreparedStatement statement = this.createPreparedStatement(sql, params);
			statement.execute();
		} catch (SQLException e) {
			throw new DatabaseIOException("Error while deleting a record from the database: "+e.getMessage() + "("+e.getErrorCode()+")");
		}
	}
	
	/**
	 * Converter method, which every handler should implement.
	 * Converts an given result set into an value object.
	 * 
	 * @param convertable The convertable result set.
	 * @return The converted value object.
	 *
	 * @throws SQLException
	 *
	 */
	protected abstract ValueObject resultSetToObject(ResultSet convertable) throws SQLException;
}