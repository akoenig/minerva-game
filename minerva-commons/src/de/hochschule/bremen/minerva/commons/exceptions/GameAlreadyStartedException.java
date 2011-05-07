/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: GameAlreadyStartedException.java 718 2010-07-04 20:36:02Z andre.koenig $
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
 * Exception that will thrown if someone tried to do something
 * that is not possible while the game is running.
 * 
 * @since 1.0
 * @version $Id: GameAlreadyStartedException.java 718 2010-07-04 20:36:02Z andre.koenig $
 *
 */
public class GameAlreadyStartedException extends Exception implements Serializable {

	private static final long serialVersionUID = -4647868292826391391L;

	/**
	 * Exception if we try to add a new player to a already running game.
	 * 
	 * @param player Tried to add this player.
	 * 
	 */
	public GameAlreadyStartedException(Player player) {
		super("Die Spiel-Session wurde bereits gestartet. Der Spieler '" + player.getUsername() + "' kann somit nicht mehr hinzugefügt werden.");
	}
}
