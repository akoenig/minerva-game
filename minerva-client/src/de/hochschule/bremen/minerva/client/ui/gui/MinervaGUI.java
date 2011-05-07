/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MinervaGUI.java 728 2010-07-05 06:41:27Z andre.koenig $
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

package de.hochschule.bremen.minerva.client.ui.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.*;

import de.hochschule.bremen.minerva.client.ui.UserInterface;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.client.ui.gui.listener.MWindowListener;
import de.hochschule.bremen.minerva.client.ui.gui.panels.LoginPanel;
import de.hochschule.bremen.minerva.client.ui.gui.panels.StartPanel;
import de.hochschule.bremen.minerva.commons.core.GameEngine;
import de.hochschule.bremen.minerva.commons.exceptions.AppConfigurationNotFoundException;
import de.hochschule.bremen.minerva.commons.exceptions.AppConfigurationNotReadableException;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.client.core.GameEngineNetwork;
import de.hochschule.bremen.minerva.client.manager.ApplicationConfigurationManager;

/**
 * Main frame of the game filled with JLayeredPanes which represents the all
 * crucial screens of the game.
 * 
 * @version $Id: MinervaGUI.java 728 2010-07-05 06:41:27Z andre.koenig $
 * @since 1.0
 *
 */
public class MinervaGUI extends JFrame implements UserInterface {

	private static final long serialVersionUID = 8038646974358166493L;

	private static String GAME_SESSION_ID = null;

	public static final Dimension WINDOW_SIZE = new Dimension(1000, 700);

	private final int INTRO_DELAY = 1000;  
	
	private JLayeredPane currentPanel;

	private static MinervaGUI instance = null;	
	
	private static GameEngine GAME_ENGINE = null;

	
	/**
	 * Sets the GUI instance
	 * 
	 */
	public MinervaGUI() { 
		MinervaGUI.instance = this;
	}
	
	/**
	 * Gets an instance if the GUI already initialized
	 * 
	 * @return GUI instance
	 * 
	 */
	public static MinervaGUI getInstance() {
		return MinervaGUI.instance;
	}

	/**
	 * Starts the UI and initializes the frame.
	 */
	public void run() {
		try {
			// Application configuration manager setup.
			ApplicationConfigurationManager.setup();

			// Init the game engine.
			MinervaGUI.GAME_ENGINE = GameEngineNetwork.getEngine();

			//initialization
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setBackground(Color.black);
			this.setResizable(false);
			this.setTitle(ApplicationConfigurationManager.get().getAppName());
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ApplicationConfigurationManager.get().getAppIconPath()));
			
			//start screen
			this.currentPanel = new StartPanel();
			this.setContentPane(this.currentPanel);
			this.currentPanel.updateUI();
			
			//login screen after 5 seconds
			Timer timer = new Timer(this.INTRO_DELAY, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() instanceof Timer) {
						MinervaGUI.this.changePanel(new LoginPanel());
						((Timer) e.getSource()).stop();
					}
				}
			});
			
			timer.start();
			this.validate();
			
			//packs frame to the size of its panel
			this.pack();
			
			//centers frame on the screen
			this.centerFrame();
			
			//lets show the frame .. yeeeaaaahhh =D
			this.setVisible(true);
			
			this.addListeners();
		} catch (DataAccessException e) {
			MMessageBox.show(e.getMessage());
		} catch (AppConfigurationNotFoundException e) {
			MMessageBox.show(e.getMessage());
		} catch (AppConfigurationNotReadableException e) {
			MMessageBox.show(e.getMessage());
		}
	}
	
	/**
	 * Puts main frame in the center of the screen
	 */
	private void centerFrame() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (this.getWidth() / 2); // Center horizontally.
		int Y = (screen.height / 2) - (this.getHeight() / 2); // Center vertically.
		this.setBounds(X,Y , this.getWidth(),getHeight());
	}

	/**
	 * Changes main panel to another one
	 * 
	 * @param newPanel Panel to change to
	 */
	public void changePanel(JLayeredPane newPanel) {
		this.currentPanel = newPanel;
		this.setContentPane(this.currentPanel);
		this.pack();
	}

	/**
	 * Returns the game session id for this gui instance.
	 * 
	 * @return String The unique game session instance.
	 * 
	 */
	public static String getSessionId() {
		return MinervaGUI.GAME_SESSION_ID;
	}

	/**
	 * Returns the game session id.
	 * 
	 * @param sessionID The unique session id.
	 * 
	 */
	public static void setSessionId(String sessionID) {
		MinervaGUI.GAME_SESSION_ID = sessionID;
	}

	/**
	 * Returns the game engine.
	 * 
	 * @return game engine
	 */
	public static GameEngine getEngine() {
		return MinervaGUI.GAME_ENGINE;
	}

	/**
	 * Add event listeners.
	 *
	 */
	private void addListeners() {
		// Kill the game while closing the window.
		this.addWindowListener(new MWindowListener() {
			public void windowClosing(WindowEvent arg0) {
				try {
					MinervaGUI.getEngine().killGame(false);
				} catch (DataAccessException e) {
					MMessageBox.error(e.getMessage());
				}
			}
		});
	}
}