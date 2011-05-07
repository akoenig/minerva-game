/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: DefeatPlayerMission.java 706 2010-07-04 18:26:52Z andre.koenig $
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
 * The DefeatPlayerMission is a sub class of Mission.
 * It will be check if a player get all countries of an other 
 * player who is after that out of the game.
 * Then the mission is fulfilled. 
 * 
 * @since 1.0
 * @version $Id: DefeatPlayerMission.java 706 2010-07-04 18:26:52Z andre.koenig $
 * 
 */
public class DefeatPlayerMission extends Mission {

	private static final long serialVersionUID = -9029869714888269591L;

	private Player enemy;
	
	/**
	 * The constructor gets the missionOwner and an enemy Player.
	 * 
	 * @param enemy
	 * @param missionOwner
	 */
	public DefeatPlayerMission(Player enemy, Player missionOwner) {
		super(missionOwner);
		this.setEnemy(enemy);

		this.setTitle("Besiege den Spieler "+this.enemy.getUsername()+"!");
		this.setDescription("Nehme alle Länder des Spielers "+this.enemy.getUsername()+" ein!");
	}

	/**
	 * Returns the enemy.
	 * 
	 * @return enemy
	 */
	public Player getEnemy() {
		return this.enemy;
	}

	/**
	 * Sets the enemy to defeat.
	 * 
	 * @param enemy The defeatable enemy.
	 * 
	 */
	private void setEnemy(Player enemy) {
		this.enemy = enemy;
	}
	
	/**
	 * The player wins if he gets all countries of the enemy-Player, who is after that dead.
	 */
	public boolean isFulfilled() {
		return (!enemy.hasCountries());
	}
}
