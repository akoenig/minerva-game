/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: AppConfigurationNotWritableException.java 742 2010-07-05 14:02:28Z andre.koenig $
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

import java.io.Serializable;

/**
 * If the application configuration was not writable.
 * 
 * @version $Id: AppConfigurationNotWritableException.java 742 2010-07-05 14:02:28Z andre.koenig $
 * @since 1.0
 *
 */
public class AppConfigurationNotWritableException extends Exception implements Serializable {

	private static final long serialVersionUID = 2924757645981690790L;

	/**
	 * Was not able to write the given app configuration file.
	 * 
	 * @param filename The app configuration file.
	 * @param reason The technical reason why it was not possible to write the file.
	 *
	 */
	public AppConfigurationNotWritableException(String filename, String reason) {
		super("Die Minerva Konfigurationsdatei ("+filename
				   +") kann nicht geschrieben werden. Grund: "+reason);
	}
}
