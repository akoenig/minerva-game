/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Army.java 706 2010-07-04 18:26:52Z andre.koenig $
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
package de.hochschule.bremen.minerva.commons.vo;

/**
 * The army value object
 * 
 * @since 1.0
 * @version $Id: Army.java 706 2010-07-04 18:26:52Z andre.koenig $
 *
 */
public class Army extends ValueObject {

	private static final long serialVersionUID = -6473599005331475995L;

	private boolean moved = false;

	/**
	 * Flag this army as "moved". Use this method,
	 * if the army was moved from one country to another country.
	 * 
	 * @param wasMoved
	 * 
	 */
	public void moved(boolean wasMoved) {
		this.moved = wasMoved;
	}

	/**
	 * Was this army moved from one country to another country?
	 *
	 * @return true / false
	 *
	 */
	public boolean wasMoved() {
		return this.moved;
	}
}
