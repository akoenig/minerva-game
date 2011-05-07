/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MArmyCountIcon.java 674 2010-07-04 16:34:12Z andre.koenig $
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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JPanel;

import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.Player;

/**
 * Represents a filled circle with an army count which is placed on each country. It
 * also has the color of the player owning the country.
 * It can be marked with circle in a designated color.
 * 
 * @version $Id: MArmyCountIcon.java 674 2010-07-04 16:34:12Z andre.koenig $
 * @since 1.0
 * 
 */
public class MArmyCountIcon extends JPanel implements MControl {
	
	private Color color;
	private int armyCount = 0;
	private boolean marked = false;
	private Color markColor;

	private static final long serialVersionUID = 1865891788129661164L;
	
	/**
	 * Constructs the icon.
	 *
	 * @param color inner color of the icon
	 * @param p point on the map where it shall be put at.
	 *
	 */
	public MArmyCountIcon(Color color, Point p) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.color = color;
		this.setSize(30,30);
		
		this.setBounds(p.x - 15, p.y - 15, 24, 24);
		
	}
	
	/**
	 * Sets the owner + the country itself where this icon is at.
	 *
	 * @param country The country on which the label should be placed.
	 * @param player The country owner
	 *
	 *
	 */
	public void setPlayer(Country country, Player player) {
		this.armyCount = country.getArmyCount();
		this.color = player.getColor();
		this.repaint();
	}
	
	/**
	 * Marks the icon with a circle
	 *
	 * @param color color of the circle
	 *
	 */
	public void mark(Color color) {
		this.marked = true;
		this.markColor = color;
		this.repaint();
	}
	
	/**
	 * Removes the mark.
	 *
	 */
	public void unmark() {
		marked = false;
		this.repaint();
	}
	
	/**
	 * AWT/Swing paint method ... never use this manually
	 * 
	 * @param g The graphics context.
	 * 
	 */
	public void paint(Graphics g) {
		int height = 20;
		int width = 20;

		// inner color
		g.setColor(this.color);
		g.fillArc(1, 1, width, height, 0, 360);
		
		//border
		g.setColor(new Color(51, 53, 55));
		g.drawArc(1, 1, width, height, 0, 360);
	
		g.setFont(new Font(FONT.getFamily(), Font.ROMAN_BASELINE, 10));
		
		String armyCountText = String.valueOf(this.armyCount);

		FontMetrics fMetrics = g.getFontMetrics();
		int textWidth = (fMetrics.stringWidth(armyCountText) / 2);
		int textHeight = (fMetrics.getHeight() / 2);
		int descent = fMetrics.getDescent();

		if (this.isDarkColor()) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.BLACK);
		}

		//army count
		int x = 0;
		if (armyCountText.length() == 1) {
			x = (int) ((width / 2) - (textWidth / 2)* 1);
		} else if (armyCountText.length() == 2) {
			x = (int) ((width / 2) - (textWidth / 2)* 1.75);
		} else {
			x = (int) ((width / 2) - (textWidth / 2)* 2);
		}
		int y = ((height / 2) + (textHeight - (descent / 2)));

		g.drawString(armyCountText, x, y);

		//marking circle
		if (marked) {
			g.setColor(this.markColor);
			g.drawArc(-1, -1, 24, 24, 0, 360);
			g.drawArc(0, 0, 22, 22, 0, 360);
		}
	}

	/**
	 * Is the background a dark color?
	 * 
	 * @return boolean
	 * 
	 */
	private boolean isDarkColor() {
		Vector<Color> darkColors = new Vector<Color>();
		darkColors.add(Color.BLUE);
		darkColors.add(Color.RED);
		darkColors.add(Color.GRAY);

		if (darkColors.contains(this.color)) {
			return true;
		}

		return false;
	}
}