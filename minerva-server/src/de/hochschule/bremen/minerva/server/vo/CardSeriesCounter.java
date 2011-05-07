/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: CardSeriesCounter.java 683 2010-07-04 16:39:39Z andre.koenig $
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

package de.hochschule.bremen.minerva.server.vo;

/**
 * Counter for released card series.
 */
public class CardSeriesCounter {

	private int counter;
	
	/**
	 * Constructor
	 */
	public CardSeriesCounter() {
		counter = 0;
	}
	
	/**
	 * Returns the counter.
	 * @return counter
	 */
	public int getCounter() {
		return this.counter;
	}
	
	/**
	 * Increases the counter by 1.
	 */
	public void increaseCounter() {
		this.counter++;
	}
	
	/**
	 * Increases the counter by given number.
	 * @param increasement counter increasement
	 */
	public void increaseCounter(int increasement) {
		this.counter += increasement;
	}
}
