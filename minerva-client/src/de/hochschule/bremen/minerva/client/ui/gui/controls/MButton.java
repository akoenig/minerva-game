/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MButton.java 674 2010-07-04 16:34:12Z andre.koenig $
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

import java.awt.Font;

import javax.swing.JButton;

/**
 * Our special MButton. The "M" stands for Minerva ;)
 * 
 * @version $Id: MButton.java 674 2010-07-04 16:34:12Z andre.koenig $
 * @since 1.0
 *
 */
public class MButton extends JButton implements MControl {

	private static final long serialVersionUID = -354154441855499481L;

	/**
	 * Constructor
	 * 
	 * @param caption The button label.
	 *
	 */
	public MButton(String caption) {
		super(caption);
		this.init();
	}

	/**
	 * Button initialization (e. g. font definition, etc.)
	 * 
	 */
	private void init() {
		this.setFont(FONT.deriveFont(Font.BOLD));
	}
}