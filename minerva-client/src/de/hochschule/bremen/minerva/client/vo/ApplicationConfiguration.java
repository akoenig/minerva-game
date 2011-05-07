/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ApplicationConfiguration.java 711 2010-07-04 19:25:11Z andre.koenig $
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
package de.hochschule.bremen.minerva.client.vo;

import de.hochschule.bremen.minerva.commons.vo.ValueObject;

/**
 * A value object that encapsulate the configuration from the
 * "application configuration file".<br /><br />
 * 
 * This object will be instantiated by the ApplicationConfigurationManager.
 *
 * @see ApplicationConfigurationManager
 * @version $Id: ApplicationConfiguration.java 711 2010-07-04 19:25:11Z andre.koenig $
 * @since 1.0
 * 
 */
public class ApplicationConfiguration extends ValueObject {

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

	private static final long serialVersionUID = -6277211242792174861L;

	private String appName = null;
	private String appVersion = null;
	private String appIconPath = null;
	private String uiAssetsDirectory = null;
	private String uiAssetsFileExtension = null;

	private String serverName = null;
	private String serverHost = null;
	private String serverPort = null;

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
	 * Sets the app icon path.
	 * 
	 * @param appIconPath The icon path.
	 * 
	 */
	public void setAppIconPath(String appIconPath) {
		this.appIconPath = appIconPath;
	}

	/**
	 * Returns the app icon path.
	 * 
	 * @return The icon path.
	 * 
	 */
	public String getAppIconPath() {
		return this.appIconPath;
	}

	/**
	 * Sets the user interface assets directory path.
	 * This directory holds all user interface related resources.
	 * 
	 * @param path The string with the full path to the ui assets.
	 * 
	 */
	public void setUIAssetsDirectory(String path) {
		this.uiAssetsDirectory = path;
	}

	/**
	 * Returns the ui assets directory path.
	 * 
	 * @return The path to the ui assets.
	 * 
	 */
	public String getUIAssetsDirectory() {
		return uiAssetsDirectory;
	}

	/**
	 * Sets the user interface assets file extension.
	 * 
	 * @param fileExtension The ui assets file extension (e.g. ".jpg").
	 * 
	 */
	public void setUIAssetsFileExtension(String fileExtension) {
		this.uiAssetsFileExtension = fileExtension;
	}

	/**
	 * Returns the user interface assets file extension.
	 * 
	 * @return The ui assets file extension.
	 * 
	 */
	public String getUIAssetsFileExtension() {
		return this.uiAssetsFileExtension;
	}

	/**
	 * Sets the server name.
	 *
	 * @param serverName
	 *
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * Returns the server name.
	 *
	 * @return The server name (rmi registry name)
	 *
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * Sets the server name.
	 *
	 * @param serverHost The server name (rmi registry name)
	 *
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * Returns the server host.
	 *
	 * @return ip address or hostname
	 *
	 */
	public String getServerHost() {
		return serverHost;
	}

	/**
	 * Sets the server port
	 *
	 * @param serverPort The server port.
	 *
	 */
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Returns the server port.
	 *
	 * @return The server port.
	 *
	 */
	public String getServerPort() {
		return serverPort;
	}

	/**
	 * Returns the application configuration in a single string.
	 * 
	 * @return The application configuration in a single string.
	 * 
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + ": [appName="+this.getAppName() + ", appVersion="+this.getAppVersion() + ", appIconPath=" + this.getAppIconPath() +"]";
	}
}