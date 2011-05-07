/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MSlidePanel.java 798 2010-07-06 00:55:41Z cbollmann@stud.hs-bremen.de $
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

package de.hochschule.bremen.minerva.client.ui.gui.controls;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import de.hochschule.bremen.minerva.client.ui.gui.panels.subpanels.GamePanelControlbar;

/**
 * A slide panel, which can, well, slide up and slide down.
 *
 * @version $Id: MSlidePanel.java 798 2010-07-06 00:55:41Z cbollmann@stud.hs-bremen.de $
 * @since 1.0
 *
 */
public class MSlidePanel extends JPanel {

	private GamePanelControlbar controlBar;
	private boolean minimized = true;
	
	private static final long serialVersionUID = 9178493186977704484L;
	

	/**
	 * The constructor.
	 * 
	 * @param controlBar The bar, which should be placed in this slider.
	 *
	 */
	public MSlidePanel(GamePanelControlbar controlBar) {
		this.controlBar = controlBar;
		this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
		this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(19, 20, 22)));

		controlBar.setSlidePanel(this);
		this.add(controlBar);
		
		this.setOpaque(false);
		this.updateUI();
	}

	/**
	 * Slide the panel up :)
	 *
	 */
	public void slideUp() {
		if (this.isMinimized()) {
			this.minimized = false;
			Rectangle r = this.getBounds();
			this.setBounds(0, r.y - this.controlBar.getRelativeHeight(), this.getPreferredSize().width, this.getPreferredSize().height);
		}
	}

	/**
	 * Slide the panel down :)
	 *
	 */
	public void slideDown() {
		if (!this.isMinimized()) {
			this.minimized = true;
			Rectangle r = this.getBounds();
			this.setBounds(0, r.y + this.controlBar.getRelativeHeight(), this.getPreferredSize().width, this.getPreferredSize().height);
		}
	}

	/**
	 * Is the panel minimized?
	 *
	 * @return boolean
	 *
	 */
	public boolean isMinimized() {
		return this.minimized;
	}
	
	/**
	 * Returns embedded ControlBarPanel.
	 *
	 * @return control bar panel
	 *
	 */
	public GamePanelControlbar getControlBar() {
		return controlBar;
	}

	/**
	 * Returns the relative height.
	 *
	 * @return The height in px.
	 *
	 */
	public int getRelativeHeight() {
		return (this.controlBar.getPreferredSize().height - this.controlBar.getRelativeHeight());
	}
}