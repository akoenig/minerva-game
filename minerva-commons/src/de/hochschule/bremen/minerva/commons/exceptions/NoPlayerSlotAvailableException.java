/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: NoPlayerSlotAvailableException.java 718 2010-07-04 20:36:02Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.vo.Player;

/**
 * Exception, that will thrown if no player slot is available
 * in a existing game (full).
 *
 * @version $Id: NoPlayerSlotAvailableException.java 718 2010-07-04 20:36:02Z andre.koenig $
 * @since 1.0
 *
 */
public class NoPlayerSlotAvailableException extends Exception implements Serializable {

	private static final long serialVersionUID = -8232340455822078275L;

	public NoPlayerSlotAvailableException(Player player) {
		super("Der Spieler '"+player.getUsername()+"' kann dem Spiel nicht mehr hinzugefügt werden. Kein freier Platz verfügbar.");
	}
}