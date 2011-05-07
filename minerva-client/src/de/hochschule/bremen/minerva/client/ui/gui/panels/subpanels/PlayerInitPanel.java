/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: PlayerInitPanel.java 774 2010-07-05 19:40:23Z cbollmann@stud.hs-bremen.de $
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

package de.hochschule.bremen.minerva.client.ui.gui.panels.subpanels;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import de.hochschule.bremen.minerva.client.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MPlayerIcon;
import de.hochschule.bremen.minerva.client.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.vo.Player;

/**
 * This sub panel shows all logged in players.
 * 
 * @version $Id: PlayerInitPanel.java 774 2010-07-05 19:40:23Z cbollmann@stud.hs-bremen.de $
 * @since 1.0
 *
 */
public class PlayerInitPanel extends JPanel implements TextResources, Observer {

	private static final long serialVersionUID = -344922633298919424L;

	private Vector<Player> currentPlayers;
	
	/**
	 * Initializing all player icons
	 * 
	 */
	public PlayerInitPanel() {
		try {
			this.currentPlayers = MinervaGUI.getEngine().getGamePlayers();
		} catch (DataAccessException e) {
			MMessageBox.error(e.getMessage());
		}
		this.setOpaque(false);
		this.setLayout(new MigLayout());

		for (Player player : this.currentPlayers) {
			this.add(new MPlayerIcon(player), "wrap");
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		Vector<Player> players = new Vector<Player>();
		try {
			players = MinervaGUI.getEngine().getGamePlayers();
		} catch (DataAccessException e) {
			MMessageBox.error(e.getMessage());
		}
		
		//idea 1:
		for (int i = this.currentPlayers.size(); i < players.size(); i++) {
			this.add(new MPlayerIcon(players.get(i)), "wrap");
		}
		this.currentPlayers = players;
		
//		//idea 2:
//		players.removeAll(this.currentPlayers);
//		for (Player player : players) {
//			this.add(new MPlayerIcon(player), "wrap");
//		}
//		this.currentPlayers.addAll(players);
		
		
	}
}