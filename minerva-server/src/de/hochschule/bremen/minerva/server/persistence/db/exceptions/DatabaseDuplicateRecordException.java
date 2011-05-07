/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: DatabaseDuplicateRecordException.java 663 2010-07-04 16:24:05Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.persistence.db.exceptions;

/**
 * Exception will thrown if someone tried to save an already existing entry.
 * 
 * @since 1.0
 * @version $Id: DatabaseDuplicateRecordException.java 663 2010-07-04 16:24:05Z andre.koenig $
 *
 */
public class DatabaseDuplicateRecordException extends DatabaseIOException {

	private static final long serialVersionUID = 618058872724951001L;

	/**
	 * Duplicate record was found.
	 * 
	 * @param reason Technical error message.
	 * 
	 */
	public DatabaseDuplicateRecordException(String reason) {
		super("A duplicate record was found. Tried to save a entry, which is already available. Details:\n"+reason);
	}
}
