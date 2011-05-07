/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MMessageBox.java 747 2010-07-05 14:26:31Z andre.koenig $
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

import javax.swing.JOptionPane;

import de.hochschule.bremen.minerva.client.ui.gui.resources.TextResources;

/**
 * Own minerva message box. The "M" stands for Minerva ;)
 * 
 * @version $Id: MMessageBox.java 747 2010-07-05 14:26:31Z andre.koenig $
 * @since 1.0
 *
 */
public class MMessageBox implements TextResources {

	private static final long serialVersionUID = -5590613142565702637L;

	/**
	 * Show a information box with the given message.
	 * 
	 * @param message The message string.
	 */
	public static void show(String message) {
		JOptionPane.showMessageDialog(null, message, MMESSAGE_INFO_TITLE, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Show a error box.
	 * 
	 * @param message The error message.
	 *
	 */
	public static void error(String message) {
		JOptionPane.showMessageDialog(null, message, MMESSAGE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Wrapper for handling exceptions.
	 *
	 * @param e The exception.
	 *
	 */
	public static void error(Exception e) {
		MMessageBox.error(e.getMessage());
	}
}