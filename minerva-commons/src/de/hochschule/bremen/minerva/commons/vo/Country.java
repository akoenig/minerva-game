/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Country.java 706 2010-07-04 18:26:52Z andre.koenig $
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

import java.awt.Color;
import java.util.Vector;

/**
 * Represents a country.
 *
 * @since 1.0
 * @version $Id: Country.java 706 2010-07-04 18:26:52Z andre.koenig $
 * 
 */
public class Country extends ValueObject {

	private static final long serialVersionUID = -170169514455685462L;

	protected int id = DEFAULT_ID;
	protected String token = null;
	protected String name = null;
	protected Color color = null;
	protected Continent continent = null;
	protected int worldId = 0;

	protected Vector<Army> armies = new Vector<Army>();

	/**
	 * Creates a new country and add one army by default.
	 * 
	 */
	public Country() {
		this.armies.add(new Army());
	}

	/**
	 * Creates a new country with the given id.
	 * 
	 * @param id
	 * 
	 */
	public Country(int id) {
		this();
		this.setId(id);
	}
	
	/**
	 * Sets the country id.
	 * 
	 * @param id
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the id.
	 * 
	 * @return The country id.
	 * 
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the country token.
	 * 
	 * @param token
	 * 
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Returns the token.
	 * 
	 * @return The country token.
	 * 
	 */
	public String getToken() {
		return this.token;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The country name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the country color.
	 * 
	 * @param color
	 * 
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Returns the color.
	 * 
	 * @return The country color.
	 * @see Color
	 * 
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Sets the country continent.
	 * 
	 * @param continent
	 * 
	 */
	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	/**
	 * Returns the continent.
	 * 
	 * @return The country continent.
	 * 
	 */
	public Continent getContinent() {
		return this.continent;
	}

	/**
	 * Sets the world id.
	 * 
	 * @param worldId
	 */
	public void setWorldId(int worldId) {
		this.worldId = worldId;
	}

	/**
	 * Returns the world id
	 * 
	 * @return
	 */
	public int getWorldId() {
		return worldId;
	}

	/**
	 * Sets army-vector.
	 * 
	 * @param armies
	 */
	public void setArmies(Vector<Army> armies) {
		this.armies = armies;
	}

	/**
	 * Gets army-vector.
	 * 
	 * @return
	 */
	public Vector<Army> getArmies() {
		return armies;
	}
	
	/**
	 * Adds one army to an existing army-vector.
	 * 
	 */
	public void addArmy() {
		getArmies().add(new Army());
	}

	/**
	 * Adds a army object to the country.
	 * 
	 * @param newArmy
	 */
	public void addArmy(Army newArmy) {
		this.armies.add(newArmy);
	}

	/**
	 * Removes one army from an existing army-vector.
	 * 
	 */
	public void removeArmy() {
		if (getArmies().size() > 0) {
			getArmies().remove(getArmies().size()-1);
		}
	}

	/**
	 * Removes the army object from the vector.
	 * 
	 * @param army
	 * 
	 */
	public void removeArmy(Army army) {
		this.armies.remove(army);
	}

	/**
	 * Returns the army count.
	 * 
	 * @return The count, which armies are on this country.
	 *
	 */
	public int getArmyCount() {
		return this.armies.size();
	}
	
	/**
	 * Returns a string with all object attributes.
	 * 
	 * @return All object attributes.
	 * 
	 */
	public String toString() {
		return getClass().getName() + "[id="+this.id +",token="+this.token+",name="+this.name+",color="+this.color+",continent="+this.continent+", world id="+this.worldId+"]";
	}
}