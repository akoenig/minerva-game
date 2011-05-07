/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Mission.java 706 2010-07-04 18:26:52Z andre.koenig $
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
 * The Mission.class is the upper class of the three sub classes and 
 * contains the method isFulfilled. 
 * 
 * @since 1.0
 * @version $Id: Mission.java 706 2010-07-04 18:26:52Z andre.koenig $
 *
 */
public class Mission extends ValueObject {

	private static final long serialVersionUID = -3477343098235969938L;

	private Player missionOwner = null;

	protected String title = null;

	protected String description = null;
	
	/**
	 * The constructor gets the Player missionOwner.
	 * 
	 * @param missionOwner
	 */
	public Mission(Player missionOwner) {
		this.missionOwner = missionOwner;
	}
	
	/**
	 * Returns the missionOwner.
	 * 
	 * @return missionOwner
	 */
	public Player getOwner() {
		return missionOwner;
	}

	/**
	 * Checks if a mission is fulfilled.
	 * 
	 * @return false
	 */
	public boolean isFulfilled() {
		return false;
	}

	/**
	 * Returns the title.
	 * 
	 * @return String The mission title.
	 * 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the mission title.
	 * 
	 * @param title String The mission title.
	 * 
	 */
	protected void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the mission description.
	 * 
	 * @return String the mission description.
	 * 
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the mission description.
	 * 
	 * @param description String The mission description.
	 * 
	 */
	protected void setDescription(String description) {
		this.description = description;
	}
}