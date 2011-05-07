/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Handler.java 663 2010-07-04 16:24:05Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.persistence;

import java.util.Vector;

import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryExistsException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.EntryNotFoundException;
import de.hochschule.bremen.minerva.server.persistence.exceptions.PersistenceException;

/**
 * Each persistence handler needs the basic "CRUD" operations.
 * CRUD is:
 * 
 * "In computer programming, Create, Read, Update and Delete (CRUD)
 * are the four basic functions of persistent storage. Sometimes
 * CRUD is expanded with the words retrieve instead of read or destroy
 * instead of delete. It is also sometimes used to describe user interface
 * conventions that facilitate viewing, searching, and changing information;
 * often using computer-based forms and reports."
 * 
 * from: http://en.wikipedia.org/wiki/Create,_read,_update_and_delete
 * 
 * @since 1.0
 * @version $Id: Handler.java 663 2010-07-04 16:24:05Z andre.koenig $
 *
 */
public interface Handler {

	/**
	 * Read a object by id.
	 * 
	 * @param id The identifier.
	 * @return The value object
	 * @throws PersistenceException Common persistence exception
	 * 
	 */
	public ValueObject read(int id) throws EntryNotFoundException, PersistenceException;

	/**
	 * Read a object by name
	 * 
	 * @param name The name identifier.
	 * @return
	 * @throws PersistenceException Common persistence exception
	 * 
	 */
	public ValueObject read(String name) throws EntryNotFoundException, PersistenceException;

	/**
	 * Reads all objects.
	 * 
	 * @return A vector with value objects.
	 * @throws PersistenceException Common persistence exception
	 * 
	 */
	public Vector<? extends ValueObject> readAll() throws PersistenceException;

	/**
	 * Reads all by an given reference value object.
	 * 
	 * Note: The reference parameter is optional. We use it if
	 * we have to read data by a given referenced value object
	 * (for example: read all countries by the referenced world).
	 * This is because Java does not support optional parameters. In some
	 * cases, like this one, this restriction is not very dressy.
	 * 
	 * @param reference The referenced value object.
	 * @return A vector with value objects.
	 * @throws PersistenceException Common persistence exception
	 * 
	 */
	public Vector<? extends ValueObject> readAll(ValueObject reference) throws PersistenceException;

	/**
	 * Deletes the given value object.
	 * 
	 * @param candidate The deletable value object.
	 * @throws PersistenceException Common persistence exception
	 * 
	 */
	public void remove(ValueObject candidate) throws PersistenceException;

	/**
	 * Save combines the "CRUD" operations: create and update
	 * 
	 * @param registrable The saveable value object.
	 * @throws PersistenceException Common persistence exception
	 * 
	 */
	public void save(ValueObject registrable) throws EntryExistsException, PersistenceException;
}
