/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: GameInitPanel.java 806 2010-07-06 01:26:45Z cbollmann@stud.hs-bremen.de $
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

package de.hochschule.bremen.minerva.client.ui.gui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.client.ui.gui.MinervaGUI;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MButton;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.client.ui.gui.panels.subpanels.PlayerInitPanel;
import de.hochschule.bremen.minerva.client.ui.gui.panels.subpanels.GameWaitingPanel;
import de.hochschule.bremen.minerva.client.ui.gui.panels.subpanels.WorldInitPanel;
import de.hochschule.bremen.minerva.client.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.commons.exceptions.NoPlayerLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.NotEnoughPlayersLoggedInException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldNotDefinedException;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.World;

/**
 * Main panel for game initialization
 * 
 * @version $Id: GameInitPanel.java 806 2010-07-06 01:26:45Z cbollmann@stud.hs-bremen.de $
 * @since 1.0
 *
 */
public class GameInitPanel extends JLayeredPane implements TextResources, Observer {

	private Background background;
	private PlayerInitPanel playerInitPanel;
	private WorldInitPanel worldInitPanel;
	private GameWaitingPanel gameWaitingPanel;
	private MButton buttonStartGame;

	private static final long serialVersionUID = -8901679483780034723L;
	
	/**
	 * Constructor
	 */
	public GameInitPanel() {
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
		this.setOpaque(true);
		
		this.background = new Background(this.getClass());
		this.background.setBounds(0, 0, 500, 500);
		
		// player list
		this.playerInitPanel = new PlayerInitPanel();
		this.playerInitPanel.setOpaque(false);
		this.playerInitPanel.setBounds(75, 150, 350, 500);
		this.playerInitPanel.setBorder(BorderFactory.createEmptyBorder());
	
		// game init
		Vector<World> worlds = new Vector<World>();

		try {
			worlds = MinervaGUI.getEngine().getWorldList();
		} catch (DataAccessException e) {
			MMessageBox.show(e.getMessage());
			Runtime.getRuntime().exit(ERROR);
		}

		// TODO: CLIENT-SERVER
		Player gamemaster = null;
		try {
			gamemaster = MinervaGUI.getEngine().getGamePlayers().firstElement();
		} catch (DataAccessException e) {
			MMessageBox.error(e.getMessage());
		}
		this.worldInitPanel = new WorldInitPanel(gamemaster, worlds);
		this.worldInitPanel.setOpaque(false);
		this.worldInitPanel.setBounds(585, 140, 300, 350);
		
		this.gameWaitingPanel = new GameWaitingPanel();
		this.gameWaitingPanel.setOpaque(false);
		this.gameWaitingPanel.setBounds(585, 80, 300, 350);

		this.buttonStartGame = new MButton(GAME_INIT_PANEL_BUTTON_START_GAME);
		this.buttonStartGame.setBounds(720, 500, 150, 20);

		//adding panels to stage
		if (MinervaGUI.getEngine().getClientPlayer().isMaster()) {
			this.add(this.worldInitPanel, 20);
			this.add(this.buttonStartGame, 20);
		} else {
			this.add(this.gameWaitingPanel, 20);
		}
		
		this.add(this.playerInitPanel, 20);
		this.add(this.background,10);
		
		this.addListeners();
		
		MinervaGUI.getEngine().addObserver(this);
		MinervaGUI.getEngine().addObserver(this.playerInitPanel);
	}

	/**
	 * Register the event handlers.
	 * 
	 */
	private void addListeners() {
		this.buttonStartGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				JLayeredPane nextPanel;

				World selectedWorld = GameInitPanel.this.worldInitPanel.getSelectedWorld();
				
				try {
					MinervaGUI.getEngine().setGameWorld(selectedWorld);
					MinervaGUI.getEngine().startGame();
					
					// Load the selected world with all country dependencies
					// from the engine.
					selectedWorld = MinervaGUI.getEngine().getGameWorld();
					nextPanel = new GamePanel(selectedWorld);
					
					MinervaGUI.getInstance().changePanel(nextPanel);
				} catch (NotEnoughPlayersLoggedInException e) {
					// Okay, back to the login panel
					MMessageBox.error(e.getMessage());
					nextPanel = new LoginPanel();
				} catch (NoPlayerLoggedInException e) {
					// Okay, back to the login panel
					MMessageBox.error(e.getMessage());
					nextPanel = new LoginPanel();
				} catch (WorldNotDefinedException e) {
					// Okay, back to the game init panel
					MMessageBox.error(e.getMessage());
					nextPanel = new GameInitPanel();
				} catch (DataAccessException e) {
					MMessageBox.error(e.getMessage());
				}
			}
		});
	}

	/**
	 * Method, which will be called on notifyObservers()
	 *
	 */
	@Override
	public void update(Observable o, Object arg) {
		try {
			World loadedWorld = MinervaGUI.getEngine().getGameWorld();
			if (loadedWorld != null) {
				MinervaGUI.getEngine().deleteObserver(this);
				MinervaGUI.getEngine().deleteObserver(this.playerInitPanel);
				MinervaGUI.getInstance().changePanel(new GamePanel(loadedWorld));
			}
			
		} catch (DataAccessException e) {
			MMessageBox.error(e);
		}
	}
}