/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: DataAccessException.java 839 2010-08-07 19:50:36Z andre.koenig $
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
package de.hochschule.bremen.minerva.commons.exceptions;

import java.io.IOException;
import java.io.Serializable;

import de.root1.simon.exceptions.EstablishConnectionFailed;
import de.root1.simon.exceptions.LookupFailedException;
import de.root1.simon.exceptions.SimonRemoteException;

/**
 * This exception is a heavy one. If this exception will be thrown
 * it is an indication that something with the storage subsystem
 * or the client-server communication went wrong.
 *
 * The best way is to quit minerva and inform the admin ;)
 *
 * @since 1.0
 * @version $Id: DataAccessException.java 839 2010-08-07 19:50:36Z andre.koenig $
 * 
 */
public class DataAccessException extends Exception implements Serializable {

	private static final long serialVersionUID = -5068821212470257120L;

	/**
	 * A common data access error occurred.
	 * 
	 * @param message The reason.
	 * 
	 */
	public DataAccessException(String message) {
		super("Beim Zugriff auf das Speichersystem des Servers ist ein schwerwiegender Fehler aufgetreten:\n\n"+message);
	}

	public DataAccessException(SimonRemoteException e) {
		super("Bei der Kommunikation mit dem Server ist ein schwerwiegender Fehler aufgetreten:\n"+e.getMessage());
	}

	public DataAccessException(EstablishConnectionFailed e) {
		super("Es konnte keine Verbindung zu dem Server hergestellt werden.\nBitte die Angaben in der 'application.configuration' prüfen.\n\nTechnische Fehlermeldung:\n"+e.getMessage());
	}

	public DataAccessException(LookupFailedException e, String serverName) {
		super("Unter '" + serverName + "' ist kein Server registriert.\nBitte 'server.name' in der 'application.configuration' prüfen.\n\nTechnische Fehlermeldung:\n"+e.getMessage());
	}

	public DataAccessException(IOException e, boolean simon) {
		super("Der Server hat ein technisches Problem festgestellt. \n Bitte den Administrator informieren. \n Meldung: "+e.getMessage());
	}
}
