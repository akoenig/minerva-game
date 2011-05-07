/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: App.java 711 2010-07-04 19:25:11Z andre.koenig $
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
package de.hochschule.bremen.minerva.client;

import de.hochschule.bremen.minerva.client.ui.UserInterface;
import de.hochschule.bremen.minerva.client.ui.UserInterfaceFactory;

/**
 * The client starter class with the main method.
 *
 * @since 1.0
 * @version $Id: App.java 711 2010-07-04 19:25:11Z andre.koenig $
 * 
 */
public class App {

	/**
	 * Starts the user interface by param.
	 * 
	 * @param args [0] "cui" or "gui"
	 *
	 */
	public static void main(String[] args) {
		UserInterface ui = UserInterfaceFactory.create(args);
		ui.run();
	}
}