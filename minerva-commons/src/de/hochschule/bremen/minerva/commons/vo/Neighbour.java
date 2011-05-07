/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Neighbour.java 706 2010-07-04 18:26:52Z andre.koenig $
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
 * The Neighbour is a marker class, which extends the
 * Country class. It represents the mapping between a country
 * and ONE neighbour (1:1 relation).
 * 
 */
public class Neighbour extends Country {

	private static final long serialVersionUID = -7663264459771331672L;

	private int mappingID = DEFAULT_ID;

	private Country reference = new Country();

	/**
	 * Sets the mapping id. It identifies
	 * the neighbour/country relationship.
	 * 
	 * @param mappingId
	 */
	public void setMappingId(int mappingId) {
		this.mappingID = mappingId;
	}

	/**
	 * Returns the mapping id.
	 * 
	 * @return
	 */
	public int getMappingId() {
		return this.mappingID;
	}

	/**
	 * Sets the referenced country. The neighbour/country
	 * mapping represents a 1:1 relation. So the neighbour
	 * and ONE nearby country.
	 * 
	 * @param country
	 */
	public void setReference(Country country) {
		this.reference = country;
	}

	/**
	 * Returns the referenced country.
	 * 
	 * @return
	 */
	public Country getReference() {
		return this.reference;
	}
}
