/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: CountryConquerMission.java 706 2010-07-04 18:26:52Z andre.koenig $
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
 * The CountryConquerMission is a sub class of Mission.
 * It will be check if a player get all countries.
 * Then the mission is fulfilled. 
 * 
 * @since 1.0
 * @version $Id: CountryConquerMission.java 706 2010-07-04 18:26:52Z andre.koenig $
 * 
 */
public class CountryConquerMission extends Mission  {

	private static final long serialVersionUID = -2210702472421754330L;

	private short countOfCountriesToConquer;
	
	/**
	 * The constructor gets the missionOwner and a count of the countries.
	 * 
	 * @param countOfCountriesToConquer
	 * @param missionOwner
	 */
	public CountryConquerMission(short countOfCountriesToConquer, Player missionOwner) {
		super(missionOwner);
		this.setCountOfCountriesToConquer(countOfCountriesToConquer);
		
		this.setTitle("Nehme schnell "+this.getCountOfCountriesToConquer()+" Länder ein!");
		this.setDescription("Der Spieler '"+this.getOwner().getUsername()+"' hat das Ziel "+this.getCountOfCountriesToConquer()+" Länder einzunehmen.");
	}

	/**
	 * Returns the Countries which should be conquer for this mission.
	 * 
	 * @return countOfCountriesToConquer
	 */
	public short getCountOfCountriesToConquer() {
		return this.countOfCountriesToConquer;
	}

	/**
	 * Sets the count of countries the mission owner
	 * has to conquer.
	 * 
	 * @param countOfCountriesToConquer int The country count to conque.
	 * 
	 */
	private void setCountOfCountriesToConquer(short countOfCountriesToConquer) {
		this.countOfCountriesToConquer = countOfCountriesToConquer;
	}
	
	/**
	 * The player wins if he gets all countries he has to conquer to fulfill the mission.
	 */
	public boolean isFulfilled() {
		return (this.countOfCountriesToConquer <= this.getOwner().getCountryCount());
	}	
}
