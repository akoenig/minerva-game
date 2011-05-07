/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MapPanel.java 758 2010-07-05 15:45:26Z cbollmann@stud.hs-bremen.de $
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

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Plain map picture output panel with getter for its image.
 * Meant for map overlay and underlay
 * 
 * @version $Id: MapPanel.java 758 2010-07-05 15:45:26Z cbollmann@stud.hs-bremen.de $
 * @since 1.0
 * 
 */
public class MapPanel extends JPanel {

	private BufferedImage mapImage;

	private static final long serialVersionUID = 8073669333884391243L;
	
	/**
	 * Creates new MapPanel with designated map image
	 *
	 * @param filepath path of map image file (overlay or underlay)
	 *
	 */
	public MapPanel(BufferedImage mapImage) {
		this.mapImage = mapImage;
		this.repaint();
	}

	/**
	 * Typical panel paint method
	 *
	 */
	public void paint(Graphics g) {
		g.drawImage(this.mapImage, 0, 0, this.mapImage.getWidth(this), this.mapImage.getHeight(this), this);
		this.setSize(this.mapImage.getWidth(this), this.mapImage.getHeight(this));
	}
	
	/**
	 * Gets the map image.
	 *
	 * @return buffered image of map
	 *
	 */
	public BufferedImage getMapImage() {
		return this.mapImage;
	}
}