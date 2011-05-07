/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MPlayerIcon.java 835 2010-07-14 14:26:17Z cbollmann@stud.hs-bremen.de $
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
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import de.hochschule.bremen.minerva.client.ui.gui.resources.TextResources;
import de.hochschule.bremen.minerva.client.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.commons.vo.Player;

/**
 * The MPlayerIcon represents the detailed player view for the game
 * initialization view.
 * 
 * @version $Id: MPlayerIcon.java 835 2010-07-14 14:26:17Z cbollmann@stud.hs-bremen.de $
 * @since 1.0
 *
 */
public class MPlayerIcon extends JPanel implements MControl, TextResources {

	private static final long serialVersionUID = -7238829407887665957L;
	
	private static final Color FONT_COLOR_DEFAULT = new Color(139, 140, 142);
	private static final Color FONT_COLOR_GAMEMASTER = new Color(2, 175, 252);
	
	private Player player = null;

	// UI elements
	private JLabel name;
	private JLabel gamemaster;
	private JLabel username;
	private JLabel email;
	
	private PlayerImage iconArea;
	private JPanel dataArea;
	
	/**
	 * Construct the layout.
	 * 
	 * @param player The player which represents the model for the icon.
	 * 
	 */
	public MPlayerIcon(Player player) {
		this.player = player;

		// Loading the player
		this.setLayout(new MigLayout("", "[]20[]"));
		this.setOpaque(false);

		// Loading the player icon
		this.iconArea = new PlayerImage();

		this.iconArea.setOpaque(false);
		this.add(this.iconArea, "width 62, height 76");

		this.dataArea = new JPanel();
		this.dataArea.setLayout(new MigLayout());
		this.dataArea.setOpaque(false);

		this.name = new JLabel();
		this.name.setFont(new Font(FONT.getName(), Font.BOLD, FONT.getSize()));
		this.name.setForeground(Color.WHITE);
		this.dataArea.add(this.name, "wrap");
		
		this.gamemaster = new JLabel();
		this.gamemaster.setFont(FONT);
		this.gamemaster.setForeground(FONT_COLOR_GAMEMASTER);
		this.dataArea.add(this.gamemaster, "wrap");

		this.username = new JLabel();
		this.username.setFont(FONT);
		this.username.setForeground(FONT_COLOR_DEFAULT);
		this.dataArea.add(this.username, "wrap");

		this.email = new JLabel();
		this.email.setFont(FONT);
		this.email.setForeground(FONT_COLOR_DEFAULT);
		this.dataArea.add(this.email, "wrap");
		
		this.gamemaster.setVisible(false);

		this.add(this.dataArea);

		this.refresh();
	}

	
	/**
	 * Pushs the model data to the ui elements.
	 * 
	 */
	private void refresh() {
		this.name.setText(this.player.getFirstName() + " " + this.player.getLastName());
		
		if (this.player.isMaster()) {
			this.gamemaster.setText(MPLAYERICON_GAMEMASTER);
			this.gamemaster.setVisible(true);
		} else {
			this.gamemaster.setVisible(false);
		}
		
		this.username.setText(MPLAYERICON_USERNAME + " " + this.player.getUsername());
		this.email.setText(MPLAYERICON_EMAIL + " " + this.player.getEmail());
	}

	/**
	 * Returns the player object, which represents
	 * the MPlayerIcon model.
	 *
	 * @return Player The player model.
	 *
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * Determines the color string from the color,
	 * that the player uses.
	 * 
	 * @param player The player, from which we determine the color string.
	 * @return The color string.
	 * 
	 */
	private String determinePlayerColor(Player player) {		
		if (player.getColor() == null) {
			return "default";
		} else if (player.getColor().equals(Color.BLUE)) {
			return "blue";
		} else if (player.getColor().equals(Color.GRAY)) {
			return "gray";
		} else if (player.getColor().equals(Color.GREEN)) {
			return "green";
		} else if (player.getColor().equals(Color.MAGENTA)) {
			return "magenta";
		} else if (player.getColor().equals(Color.ORANGE)) {
			return "orange";
		} else if (player.getColor().equals(Color.RED)) {
			return "red";
		} else if (player.getColor().equals(Color.YELLOW)) {
			return "yellow";
		} else {
			return "default";
		}
	}
	
	/**
	 * Updates the whole icon to a "new" player.
	 * @param player
	 */
	public void updatePlayerIcon(Player player) {
		this.player = player;
		this.iconArea.updatePlayerImage();
		this.refresh();
	}
	
	/**
	 * Internal class representing the player image inside the player icon.
	 *
	 */
	class PlayerImage extends JPanel {
		
			private static final long serialVersionUID = -3484770437177556562L;
			
			private BufferedImage playerImage;
			
			/**
			 * constructing the image
			 */
			public PlayerImage() {
				this.updatePlayerImage();
			}
			
			/**
			 * Updates the image to the player of the player icon.
			 */
			public void updatePlayerImage() {
				String color = "_" + MPlayerIcon.this.determinePlayerColor(MPlayerIcon.this.player);
				String iconPath = ApplicationConfigurationManager.get().getUIAssetsDirectory() + MPlayerIcon.class.getSimpleName() + color + ApplicationConfigurationManager.get().getUIAssetsFileExtension();
				try {
					this.playerImage = ImageIO.read(new File(iconPath));
				} catch (IOException e) {
					// If a error occurred while loading the player icon, well, do nothing.;
				}
			}
			
			/**
			 * casual paint method
			 */
			public void paint(java.awt.Graphics g) {
					g.drawImage(this.playerImage, 0, 0, this);
			}
	}
}