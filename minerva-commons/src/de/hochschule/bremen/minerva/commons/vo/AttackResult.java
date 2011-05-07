/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: AttackResult.java 706 2010-07-04 18:26:52Z andre.koenig $
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

import java.io.Serializable;
import java.util.Vector;

import de.hochschule.bremen.minerva.commons.util.Die;

/**
 * Result of one attack saving the attacker,
 * defender, armies lost and if the attack was a success.
 * 
 * @since 1.0
 * @version $Id: AttackResult.java 706 2010-07-04 18:26:52Z andre.koenig $
 *
 */
public class AttackResult implements Serializable {

	private static final long serialVersionUID = 4368417989195227318L;

	private Player attacker = null;
	private Player defender = null;
	private int lostAttackerArmies = 0;
	private int lostDefenderArmies = 0;
	
	private Vector<Die> attackerDice = null;
	private Vector<Die> defenderDice = null;
	
	private boolean win = false;
	

	/**
	 * Constructor of the AttackResult.
	 *
	 * @param attacker Attacking player.
	 * @param defender Defending player.
	 * @param lostArmies Armies lost by the attacker.
	 * @param defeatedArmies Armies lost by the defender.
	 * @param win Was the attack a success?
	 *
	 */
	public AttackResult(Player attacker, Player defender, int lostArmies, int defeatedArmies, Vector<Die> attackerDice, Vector<Die> defenderDice, boolean win) {
		this.setAttacker(attacker);
		this.setDefender(defender);
		this.setLostAttackerArmies(lostArmies);
		this.setLostDefenderArmies(defeatedArmies);
		this.setWin(win);
		
		this.setAttackerDice(attackerDice);
		this.setDefenderDice(defenderDice);
	}
	
	/**
	 * Sets lost armies of the attacker.
	 * 
	 * @param lostAttackerArmies Number of lost armies.
	 *
	 */
	private void setLostAttackerArmies(int lostAttackerArmies) {
		this.lostAttackerArmies = lostAttackerArmies;
	}
	
	/**
	 * Gets lost armies of the attacker.
	 * 
	 * @return Number of lost armies.
	 *
	 */
	public int getLostAttackerArmies() {
		return lostAttackerArmies;
	}
	
	/**
	 * Sets lost armies of the defender.
	 * 
	 * @param lostDefenderArmies Number of lost armies.
	 *
	 */
	private void setLostDefenderArmies(int lostDefenderArmies) {
		this.lostDefenderArmies = lostDefenderArmies;
	}
	
	/**
	 * Gets lost armies of the defender.
	 * 
	 * @return Number of lost armies.
	 *
	 */
	public int getLostDefenderArmies() {
		return lostDefenderArmies;
	}
	
	/**
	 * Sets success of the attack.
	 * 
	 * @param win Success yes/no?
	 *
	 */
	private void setWin(boolean win) {
		this.win = win;
	}
	
	/**
	 * Returns success of the attack.
	 * 
	 * @return Success yes/no?
	 *
	 */
	public boolean isWin() {
		return win;
	}

	/**
	 * Sets attacking player.
	 *  
	 * @param attacker Attacking player.
	 *
	 */
	private void setAttacker(Player attacker) {
		this.attacker = attacker;
	}

	/**
	 * Gets attacking player.
	 * 
	 * @return Attacking player.
	 *
	 */
	public Player getAttacker() {
		return attacker;
	}

	/**
	 * Sets defending player.
	 * 
	 * @param defender Defending player.
	 *
	 */
	private void setDefender(Player defender) {
		this.defender = defender;
	}

	/**
	 * Gets defending player.
	 * 
	 * @return Defending player.
	 *
	 */
	public Player getDefender() {
		return defender;
	}

	/**
	 * Sets the attacker die.
	 *
	 * @param attackerDie The attacker die.
	 * 
	 */
	private void setAttackerDice(Vector<Die> attackerDice) {
		this.attackerDice = attackerDice;
	}

	/**
	 * Returns the attacker die.
	 * 
	 * @return A vector with all attacker dice.
	 * 
	 */
	public Vector<Die> getAttackerDice() {
		return this.attackerDice;
	}

	/**
	 * Sets the defender die.
	 * 
	 * @param defenderDie The defender die.
	 *
	 */
	private void setDefenderDice(Vector<Die> defenderDice) {
		this.defenderDice = defenderDice;
	}

	/**
	 * Returns the defender die
	 *
	 * @return Die The defender die.
	 *
	 */
	public Vector<Die> getDefenderDice() {
		return this.defenderDice;
	}

	/**
	 * Gets the whole AttackResult as a string.
	 *
	 * @return The attack result as string.
	 *
	 */
	public String toString() {
		return (""+this.attacker.getUsername()+" attacked "+this.defender.getUsername()+", lost "+this.lostAttackerArmies+ ((this.lostAttackerArmies > 1)? " armies" : " army")+" and defeated " + this.lostDefenderArmies +((this.lostDefenderArmies > 1)? " armies. " : " army. ")+this.attacker.getUsername()+ ((this.win) ? " won" : " did not win")+ " the country.");
	}
}