/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: CountryGraph.java 735 2010-07-05 07:25:28Z andre.koenig $
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

package de.hochschule.bremen.minerva.commons.util;

import de.hochschule.bremen.minerva.commons.vo.Country;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

/**
 * Represents a graph adt, which we use for country
 * relationship visualization.
 *
 * @since 1.0
 * @version $Id: CountryGraph.java 735 2010-07-05 07:25:28Z andre.koenig $
 * 
 */
public class CountryGraph implements Serializable {

	private static final long serialVersionUID = 800121527454876742L;

	// HashMap for neighbour relations.
	private HashMap<Integer, Vector<Integer>> neighbours = new HashMap<Integer, Vector<Integer>>();

	/**
	 * Connects country one with country two.
	 * 
	 * @param one The country source value object.
	 * @param two The country to connect with the first one.
	 * 
	 */
	public void connect(Country one, Country two) {
		Vector<Integer> connections;

		if (neighbours.containsKey(one.getId())) {
			 connections = neighbours.get(one.getId());
		} else {
			connections = new Vector<Integer>();
		}

		connections.add(two.getId());
		neighbours.put(one.getId(), connections);
	}
	
	/**
	 * Checks if two countries are neighbours or not.
	 * In technical words: Is country one connected with
	 * country two.
	 * 
	 * @param one
	 * @param two 
	 * 
	 * @return boolean
	 * 
	 */
	public boolean neighbours(Country one, Country two) {
		if (neighbours.get(one.getId()).contains(two.getId())) {
			return true;
		}
		return false;
	}

	/**
	 * Returns a integer vector with the
	 * neighbour id's by a given country.
	 * 
	 * @param byCountryId
	 * @return A vector containing all neighbour id's from the given source country.
	 * 
	 */
	public Vector<Integer> getNeighbours(int byCountryId) {
		return this.neighbours.get(byCountryId);
	}

	/**
	 * Has a specific country neighbours?
	 * 
	 * @param countryId The id from the checkable country
	 * @return boolean
	 * 
	 */
	public boolean hasNeighbours(int countryId) {
		if (this.neighbours.get(countryId) == null) {
			return false;
		} else {
			return !this.neighbours.get(countryId).isEmpty();
		}
	}
}