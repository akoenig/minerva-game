/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: WorldFileExtensionException.java 742 2010-07-05 14:02:28Z andre.koenig $
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

import java.io.File;
import java.io.Serializable;

/**
 * Tried to import an world file that does not have the correct file extension.
 *
 * @since 1.0
 * @version $Id: WorldFileExtensionException.java 742 2010-07-05 14:02:28Z andre.koenig $
 * 
 */
public class WorldFileExtensionException extends Exception implements Serializable {

	private static final long serialVersionUID = 6507674073849573747L;

	public WorldFileExtensionException(File worldFile, String worldFileExtension) {
		super("Die Datei '"+worldFile.getAbsolutePath() +
		      "' ist nicht vom richtigen Typ. " +
		      "Bitte verifizieren Sie, dass die Datei, die richtige Endung besitzt (*"+worldFileExtension+").");
	}
}
