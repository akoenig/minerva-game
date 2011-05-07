/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ApplicationConfiguration.java 781 2010-07-05 20:36:06Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.vo;

import de.hochschule.bremen.minerva.commons.vo.ValueObject;

/**
 * A value object that encapsulate the configuration from the
 * "application configuration file".<br /><br />
 * 
 * This object will be instantiated by the ApplicationConfigurationManager.
 *
 * @see ApplicationConfigurationManager
 * @version $Id: ApplicationConfiguration.java 781 2010-07-05 20:36:06Z andre.koenig $
 * @since 1.0
 * 
 */
public class ApplicationConfiguration extends ValueObject {

	private static final long serialVersionUID = -4632671967532487127L;

	// #########################################################
	// #
	// # DON'T FORGET TO MODIFY THE MANAGER
	// # IF YOU ADD NEW ATTRIBUTES AND GETTER-/SETTER-METHODS
	// # TO THIS CLASS. OTHERWISE THEY WILL NOT READ-/STORABLE!
	// #
	// # A nice but time-consuming solution is described in the
	// # AppConfigurationManager (see: NICE SOLUTION).
	// #
	// #########################################################
	private String appName = null;
	private String appVersion = null;

	private String serverName = null;
	private int serverPort = 0;
	private String serverWelcomeMessage = null;

	private String assetsWorldDirectory = null;
	private String importerWorkspaceDirectory = null;

	/**
	 * Sets the minerva application name.
	 * 
	 * @param appName The minerva application name.
	 * 
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * Returns the minerva application name.
	 * 
	 * @return The minerva application name.
	 */
	public String getAppName() {
		return this.appName;
	}

	/**
	 * Sets the minerva application version.
	 * 
	 * @param appVersion The minerva version.
	 * 
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * Returns the minerva application version.
	 * 
	 * @return The minerva version.
	 * 
	 */
	public String getAppVersion() {
		return this.appVersion;
	}

	/**
	 * Sets the server name.
	 *
	 * @param serverName The server name.
	 *
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * Gets the server name.
	 *
	 * @return The server name.
	 *
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Sets the socket server port.
	 * 
	 * @param serverPort The socket port
	 * 
	 */
	public void setServerPort(String serverPort) {
		this.serverPort = Integer.parseInt(serverPort);
	}

	/**
	 * Returns the socket port.
	 * 
	 * @return
	 */
	public String getServerPort() {
		return (String.valueOf(this.serverPort));
	}

	/**
	 * Sets the server welcome message.
	 *
	 * @param serverWelcomeMessage
	 *
	 */
	public void setServerWelcomeMessage(String serverWelcomeMessage) {
		this.serverWelcomeMessage = serverWelcomeMessage;
	}

	/**
	 * Gets the server welcome message.
	 * 
	 * @return The server welcome message.
	 *
	 */
	public String getServerWelcomeMessage() {
		return serverWelcomeMessage;
	}

	/**
	 * Sets the worlds assets directory (contains maps, underlay maps, thumbnails).
	 *
	 * @param assetsWorldDirectory
	 *
	 */
	public void setAssetsWorldDirectory(String assetsWorldDirectory) {
		this.assetsWorldDirectory = assetsWorldDirectory;
	}

	/**
	 * Returns the worlds assets directory (contains maps, underlay maps, thumbnails).
	 *
	 * @return The worlds assets directory.
	 *
	 */
	public String getAssetsWorldDirectory() {
		return assetsWorldDirectory;
	}

	/**
	 * Sets the importer workspace directory.
	 * Is the directory in which the world file importer will
	 * place some temp files.
	 *
	 * @param importerWorkspaceDirectory The importer temp directory.
	 *
	 */
	public void setImporterWorkspaceDirectory(String importerWorkspaceDirectory) {
		this.importerWorkspaceDirectory = importerWorkspaceDirectory;
	}

	/**
	 * Returns the importer workspace directory.
	 *
	 * @return The importer workspace directory.
	 *
	 */
	public String getImporterWorkspaceDirectory() {
		return importerWorkspaceDirectory;
	}
}