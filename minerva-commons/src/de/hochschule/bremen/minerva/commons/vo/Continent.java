/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Continent.java 706 2010-07-04 18:26:52Z andre.koenig $
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
 * Represents a country value object.
 *
 * @since 1.0
 * @version $Id: Continent.java 706 2010-07-04 18:26:52Z andre.koenig $
 *
 */
public class Continent extends ValueObject {

	private static final long serialVersionUID = -4858293118667350977L;

	private int id = DEFAULT_ID;
	private String name = "";

	/**
	 * Sets the continent id.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the continent id.
	 * 
	 * @return The continent id.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the continent name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The continent name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns a string with all object attributes.
	 * 
	 * @return All object attributes.
	 * 
	 */
	public String toString() {
		return getClass().getName() + "[id=" + id + ",name=" + name + "]";
	}
}
