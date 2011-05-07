/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: PersistenceException.java 685 2010-07-04 16:40:42Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.persistence.exceptions;

/**
 * A common persistence exception. Will be thrown if some technical problem
 * occurred. For example: file not writable (filebased persistence), database
 * not reachable (database persistence).
 * 
 * @version $Id: PersistenceException.java 685 2010-07-04 16:40:42Z andre.koenig $
 * @since 1.0
 *
 */
public class PersistenceException extends Exception {

	private static final long serialVersionUID = 8365171814386246346L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(String message) {
		super(message);
	}
}