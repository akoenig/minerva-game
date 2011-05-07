/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ConsoleLogger.java 763 2010-07-05 17:23:39Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.util;

/**
 * A simple logger that prints messages on System.out.
 *
 * @since 1.0
 * @version $Id: ConsoleLogger.java 763 2010-07-05 17:23:39Z andre.koenig $
 * 
 */
public class ConsoleLogger {

	private static ConsoleLogger instance = null;

	/**
	 * Singleton pattern.
	 *
	 */
	private ConsoleLogger() {}
	
	/**
	 * Returns a logger instance.
	 *
	 * @return The console logger instance.
	 *
	 */
	public static ConsoleLogger getLogger() {
		if (ConsoleLogger.instance == null) {
			ConsoleLogger.instance = new ConsoleLogger();
		}
		return ConsoleLogger.instance;
	}

	/**
	 * Prints a log message.
	 * 
	 * @param message The log message.
	 *
	 */
	public void log(String message) {
		System.out.println("[LOG] "+message);
	}

	/**
	 * Prints error messages.
	 *
	 * @param message The error message.
	 */
	public void error(String message) {
		System.err.println("[ERROR] "+message);
	}
}