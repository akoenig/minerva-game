/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ContinentConquerMission.java 706 2010-07-04 18:26:52Z andre.koenig $
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

import java.util.Vector;

/**
 * The ContinentConquerMission is a sub class of Mission.
 * It will be check if a player get all countries of a continent.
 * Then the mission is fulfilled. 
 * 
 * @since 1.0
 * @version $Id: ContinentConquerMission.java 706 2010-07-04 18:26:52Z andre.koenig $
 *
 */
public class ContinentConquerMission extends Mission {

	private static final long serialVersionUID = 3274869455432429506L;

	Vector<Country> countriesOfContinentOne = new Vector<Country>();
	Vector<Country> countriesOfContinentTwo = new Vector<Country>();
	
	/**
	 * The constructor gets the missionOwner and all countries of a continent.
	 * 
	 * @param countriesOfContinent
	 * @param missionOwner
	 */
	public ContinentConquerMission(Vector<Country> countriesOfContinentOne, Vector<Country> countriesOfContinentTwo, Player missionOwner ) {
		super(missionOwner);
		
		this.setCountriesOfContinentOne(countriesOfContinentOne);
		this.setCountriesOfContinentTwo(countriesOfContinentTwo);

		Continent one = this.getCountriesOfContinentOne().get(0).getContinent();
		Continent two = this.getCountriesOfContinentTwo().get(0).getContinent();
		
		this.setTitle("Nehme die Kontinente '"+one.getName()+"' und '"+two.getName()+"' ein.");
		this.setDescription("Setze deine Einheiten auf alle Länder der beiden Kontinente.");
	}
	
	/**
	 * Returns the countries of the first continent.
	 * 
	 * @return countriesOfContinent
	 */
	public Vector<Country> getCountriesOfContinentOne() {
		return this.countriesOfContinentOne;
	}

	/**
	 * Sets the countries of the first continent, which the
	 * player has to conquer.
	 * 
	 * @param Vector<Country> All "continent one" countries.
	 * 
	 */
	private void setCountriesOfContinentOne(Vector<Country> countriesOfContinentOne) {
		this.countriesOfContinentOne = countriesOfContinentOne;
	}
	
	/**
	 * Returns the countries of the second continent.
	 * 
	 * @return countriesOfContinent
	 */
	public Vector<Country> getCountriesOfContinentTwo() {
		return this.countriesOfContinentTwo;
	}

	/**
	 * Sets the countries of the second continent, which the
	 * player has to conquer.
	 * 
	 * @param Vector<Country> All "continent two" countries.
	 * 
	 */
	private void setCountriesOfContinentTwo(Vector<Country> countriesOfContinentTwo) {
		this.countriesOfContinentTwo = countriesOfContinentTwo;
	}
	
	/**
	 * The mission is fulfilled if a player get all countries of a continent.
	 *
	 * @return check
	 */
	public boolean isFulfilled() {
		Boolean check = true;
		for (Country country : countriesOfContinentOne) {
			if (check) {
				check = this.getOwner().hasCountry(country);
			}
		}
		for (Country country : countriesOfContinentTwo) {
			if (check) {
				check = this.getOwner().hasCountry(country);
			}
		}
		return check;
	}	
}
