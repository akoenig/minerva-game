/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Die.java 805 2010-07-06 01:16:23Z andre.koenig $
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

import java.io.Serializable;
import java.util.Vector;

/**
 * A game die.
 *  
 * @since 1.0
 * @version $Id: Die.java 805 2010-07-06 01:16:23Z andre.koenig $
 *
 */
public class Die implements Serializable {

	private static final long serialVersionUID = -1400070982828639319L;
	
	private int number = 0;

	/**
	 * Rolls the dice.
	 * 
	 */
	public void dice() {
		this.number = (int)((Math.random() * 6) + 1);
	}

	/**
	 * 
	 * Returns the roll result.
	 * 
	 * @return A random value in the range: 1 - 6
	 */
	public int getRollResult() {
		return this.number;
	}

	/**
	 * Helper method, that compares a vector with dice
	 * and returns the die with the biggest roll result.
	 * 
	 * @param dice The vector with dice.
	 * @return The die with the biggest roll result.
	 * 
	 */
	public static Die getLargest(Vector<Die> dice) {
		Die output = new Die();
		int largest = 0;
		for (Die die : dice) {
			if (die.getRollResult() > largest) {
				largest = die.getRollResult();
				output = die;
			}
		}
		return output;
	}	
}