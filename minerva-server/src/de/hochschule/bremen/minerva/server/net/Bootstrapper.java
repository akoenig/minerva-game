/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: Bootstrapper.java 761 2010-07-05 16:24:50Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.net;

import java.io.IOException;
import java.net.UnknownHostException;

import de.hochschule.bremen.minerva.commons.exceptions.AppConfigurationNotFoundException;
import de.hochschule.bremen.minerva.commons.exceptions.AppConfigurationNotReadableException;
import de.hochschule.bremen.minerva.commons.exceptions.DataAccessException;
import de.hochschule.bremen.minerva.server.manager.ApplicationConfigurationManager;
import de.hochschule.bremen.minerva.server.vo.ApplicationConfiguration;
import de.root1.simon.exceptions.NameBindingException;

/**
 * The server bootstrap process (configuration initialization, etc.)
 *
 * @since 1.0
 * @version $Id: Bootstrapper.java 761 2010-07-05 16:24:50Z andre.koenig $
 * 
 */
public class Bootstrapper {

	/**
	 * Do some initialization stuff, before starting the server engine.
	 * Note, that all required configurations are placed in the
	 * 'application.configuration' file.
	 *
	 */
	public static void boot() {
		try {
			// 1. Step: Init the application configuration.
			ApplicationConfigurationManager.setup();
			ApplicationConfiguration appConfig = ApplicationConfigurationManager.get();
			
			int serverPort = Integer.parseInt(appConfig.getServerPort());
			
			// 2. Init the server.
			new MinervaServerEngine(appConfig.getServerName(), serverPort);

			// 3. Show the server welcome message.
			Bootstrapper.showWelcomeMessage();

		} catch (AppConfigurationNotFoundException e) {
			Bootstrapper.error(e.getMessage());
		} catch (AppConfigurationNotReadableException e) {
			Bootstrapper.error(e.getMessage());
		} catch (UnknownHostException e) {
			Bootstrapper.error(e.getMessage());
		} catch (IOException e) {
			Bootstrapper.error(e.getMessage());
		} catch (NameBindingException e) {
			Bootstrapper.error(e.getMessage());
		} catch (DataAccessException e) {
			Bootstrapper.error(e.getMessage());
		}
	}

	/**
	 * Print a error message.
	 *
	 * @param message
	 *
	 */
	private static void error(String message) {
		System.err.println("[FEHLER] "+message);
	}

	/**
	 * Print the server welcome message on the command line.
	 * You have the possibility to modify the message (see application.configuration).
	 * 
	 */
	private static void showWelcomeMessage() {
		ApplicationConfiguration appConfig = ApplicationConfigurationManager.get();
		System.out.println(appConfig.getAppName() + " [ver."+ appConfig.getAppVersion() +"]");
		System.out.println();
		System.out.println(appConfig.getServerWelcomeMessage());
		System.out.println();
	}
}