/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: NotEnoughArmiesException.java 742 2010-07-05 14:02:28Z andre.koenig $
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

import de.hochschule.bremen.minerva.commons.vo.Country;

/**
 * Tried to attack a country with no enough armies.
 *
 * @since 1.0
 * @version $Id: NotEnoughArmiesException.java 742 2010-07-05 14:02:28Z andre.koenig $
 * 
 */
public class NotEnoughArmiesException extends Exception implements Serializable {

	private static final long serialVersionUID = 1508846470029928606L;

	public NotEnoughArmiesException() {
		super();
	}
	
	public NotEnoughArmiesException(Country attackerCountry, Country defenderCountry) {
		super("Das Land '"+attackerCountry.getName()+"' besitzt nur noch "+attackerCountry.getArmyCount()+" Einheiten und kann das Land '"+defenderCountry.getName()+"' somit nicht angreifen.");
	}

	public NotEnoughArmiesException(Country from, int armyCount, boolean move) {
		super(armyCount+" Einheite(n) sind nicht auf dem Land '"+from.getName()+"' zum Verschieben verfügbar.");
	}
}