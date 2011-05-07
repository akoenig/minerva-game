/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Background.java 811 2010-07-06 11:06:48Z andre.koenig $
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
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import de.hochschule.bremen.minerva.client.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.client.ui.gui.controls.MMessageBox;
import de.hochschule.bremen.minerva.client.vo.ApplicationConfiguration;

/**
 * Represents the background image in all main panels.
 * Usually will be set in a lower panel of a LayeredPane.
 * This panel stays empty and just has a background image.
 * 
 * @version $Id: Background.java 811 2010-07-06 11:06:48Z andre.koenig $
 * @since 1.0
 *
 */
public class Background extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2926270274740692246L;

	private Image image;
	
	/**
	 * Constructs the background panel out of the class name of a main panel.
	 * @param concernedPanel class name of a main panel
	 */
	public Background(Class<? extends JLayeredPane> concernedPanel) {
		super();
		
		//getting the image file to the class name
		ApplicationConfiguration configuration = ApplicationConfigurationManager.get();
		File file = new File(configuration.getUIAssetsDirectory() + concernedPanel.getSimpleName() + configuration.getUIAssetsFileExtension());
		
		try {
			this.image = ImageIO.read(file);
		} catch (IOException e) {
			MMessageBox.error(e);
		}
		
		//drawing the background
		this.repaint();
	}
	
	/**
	 * Paints the background image and sets the size of the panel.
	 */
	public void paint(Graphics g) {
		g.drawImage(this.image, 0, 0, this.image.getWidth(this), this.image.getHeight(this), this);
		this.setSize(this.image.getWidth(this), this.image.getHeight(this));
	}
}
