/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: StartPanel.java 674 2010-07-04 16:34:12Z andre.koenig $
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

import java.awt.BorderLayout;

import javax.swing.JLayeredPane;

import de.hochschule.bremen.minerva.client.ui.gui.MinervaGUI;

/**
 * First Panel shown in the client.
 * Contains the title screen.
 * 
 * @version $Id: StartPanel.java 674 2010-07-04 16:34:12Z andre.koenig $
 * @since 1.0
 *
 */
public class StartPanel extends JLayeredPane {

	private Background background;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8815190687201347357L;

	/**
	 * Initialization
	 */
	public StartPanel() {
		super();
		this.setLayout(new BorderLayout());
		this.background = new Background(this.getClass());
		this.add(this.background,BorderLayout.NORTH,DEFAULT_LAYER);
		this.setPreferredSize(MinervaGUI.WINDOW_SIZE);
	}
}
