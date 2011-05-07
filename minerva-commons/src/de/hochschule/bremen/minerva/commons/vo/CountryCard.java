/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: CountryCard.java 706 2010-07-04 18:26:52Z andre.koenig $
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
 * The CountryCard.class is the upper class of the three sub classes. 
 * 
 * @since 1.0
 * @version $Id: CountryCard.java 706 2010-07-04 18:26:52Z andre.koenig $
 *
 */
public class CountryCard extends ValueObject {

	private static final long serialVersionUID = 9007285166742175852L;

	private Country reference = new Country();
	
	/**
	 * The constructor gets a country.
	 * 
	 * @param country
	 */
	public CountryCard(Country country) {
		setReference (country);
	}
	
	/**
	 * Sets the referenced country.
	 * 
	 * @param country
	 */
	private void setReference(Country country) {
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