/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: ApplicationConfigurationManager.java 711 2010-07-04 19:25:11Z andre.koenig $
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
package de.hochschule.bremen.minerva.client.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import de.hochschule.bremen.minerva.commons.exceptions.AppConfigurationNotFoundException;
import de.hochschule.bremen.minerva.commons.exceptions.AppConfigurationNotReadableException;
import de.hochschule.bremen.minerva.commons.exceptions.AppConfigurationNotWritableException;
import de.hochschule.bremen.minerva.client.vo.ApplicationConfiguration;

/**
 * The application configuration manager.
 * Provides methods to store the fundamental configurations to a file in the application
 * root. This data is necessary for the application to run. So further game relevant data
 * will be stored via the persistence layer of course.
 * 
 * @version $Id: ApplicationConfigurationManager.java 711 2010-07-04 19:25:11Z andre.koenig $
 * @since 1.0
 *
 */
public class ApplicationConfigurationManager {

	// The expected file in which the application configuration will be stored.
	private static final String APPCONFIGURATION_FILENAME = "application.configuration";

	// Value that identifies a "comment line"
	private static final String COMMENT_IDENTIFIER = "--";

	// The key/value delimiter
	private static final String SPLIT_IDENTIFIER = "=";

	// Method invocation mapping. This hash map tells if a specific key was found, which method on the
	// AppConfiguration object should be invoked (AppConfigurationManager#fill).
	// The value (notation: key=value) will be passed to this invoked method.
	//
	// NICE SOLUTION:
	// In the next releases it would be nice to remove this mapping and read all getter and
	// setter methods from the AppConfiguration value object and use the method names as keys
	// (convention over configuration). Yeah I (akoenig) know: In the next releases :)
	//
	private static final HashMap<String, String> voReadMethodInvocationMapping = new HashMap<String, String>();

	private static final HashMap<String, String> voStoreMethodInvocationMapping = new HashMap<String, String>();

	// Method/Key initialization.
	static {
		voReadMethodInvocationMapping.put("app.name", "setAppName");
		voStoreMethodInvocationMapping.put("app.name", "getAppName");

		voReadMethodInvocationMapping.put("app.version", "setAppVersion");
		voStoreMethodInvocationMapping.put("app.version", "getAppVersion");

		voReadMethodInvocationMapping.put("app.icon.path", "setAppIconPath");
		voStoreMethodInvocationMapping.put("app.icon.path", "getAppIconPath");

		voReadMethodInvocationMapping.put("directory.assets.userinterface", "setUIAssetsDirectory");
		voStoreMethodInvocationMapping.put("directory.assets.userinterface", "getUIAssetsDirectory");

		voReadMethodInvocationMapping.put("directory.assets.userinterface.fileextension", "setUIAssetsFileExtension");
		voStoreMethodInvocationMapping.put("directory.assets.userinterface.fileextension", "getUIAssetsFileExtension");

		voReadMethodInvocationMapping.put("server.name", "setServerName");
		voStoreMethodInvocationMapping.put("server.name", "getServerName");

		voReadMethodInvocationMapping.put("server.host", "setServerHost");
		voStoreMethodInvocationMapping.put("server.host", "getServerHost");

		voReadMethodInvocationMapping.put("server.port", "setServerPort");
		voStoreMethodInvocationMapping.put("server.port", "getServerPort");
	}

	// The manager instance
	private static ApplicationConfigurationManager manager = null;

	// The cached application configuration. We do not need to parse application
	// configuration file a second time if it was done before.
	private static ApplicationConfiguration cachedConfiguration = new ApplicationConfiguration();

	/**
	 * Singleton pattern. It is not possible
	 * to create a ApplicationConfigurationManager in the common way.
	 * So this constructor is private.
	 * 
	 */
	private ApplicationConfigurationManager() {}

	/**
	 * Setups the application configuration singleton.<br />
	 * <b>NOTE</b>: Please make sure that you have called this method in the application´
	 * initialization stage. Otherwise the application configuration will be empty.
	 * 
	 * @throws AppConfigurationNotFoundException If the application configuration file wasn't found.
	 * @throws AppConfigurationNotReadableException If the application configuration file is not readable for some reasons.
	 * 
	 */
	public static void setup() throws AppConfigurationNotFoundException, AppConfigurationNotReadableException {
		if (ApplicationConfigurationManager.manager == null || ApplicationConfigurationManager.cachedConfiguration == null) {
			ApplicationConfigurationManager.manager = new ApplicationConfigurationManager();
			ApplicationConfigurationManager.manager.parse();
		}
	}
	
	/**
	 * Return the application configuration
	 *  
	 * @see ApplicationConfiguration
	 * 
	 */
	// TODO: Rename the method "e.g. get().getAppName()" is not so nice.
	public static ApplicationConfiguration get() {
		return ApplicationConfigurationManager.cachedConfiguration;
	}

	/**
	 * Stores a application configuration into a file in the app root.
	 * Please note that a existing file will be overwritten.
	 * 
	 * @param storableConfiguration The configuration which should be stored.
	 * @throws AppConfigurationNotWritableException If the app configuration is not writable
	 * 
	 */
	public static void store(ApplicationConfiguration storableConfiguration) throws AppConfigurationNotWritableException {
		Set<Entry<String, String>> invokableEntries = voStoreMethodInvocationMapping.entrySet();
		
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(ApplicationConfigurationManager.APPCONFIGURATION_FILENAME)));

			for (Entry<String, String> entry : invokableEntries) {
				String method = entry.getValue(); // The method which should invoked on the storableConfiguration object.
				
				try {
					String key = entry.getKey(); // The writable key.
					String value = (String)cachedConfiguration.getClass().getDeclaredMethod(method).invoke(storableConfiguration);


					writer.println(key+SPLIT_IDENTIFIER+value);

				} catch (IllegalArgumentException e) {
					// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
					e.printStackTrace();
				} catch (SecurityException e) {
					// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TRASH! If a line was configured and no attributes exists in the application configuration object.
				}
			}

			writer.close();
		} catch (IOException ex) {
			throw new AppConfigurationNotWritableException(APPCONFIGURATION_FILENAME, ex.getMessage());
		}
		
		cachedConfiguration = storableConfiguration;
	}
	
	/**
	 * Parses the application configuration file and pushs the data into the
	 * defined configuration object. Uses the @see {@link ApplicationConfigurationManager#fill(String, String)}
	 * for method invokation on the configuration object.
	 * 
	 * @throws AppConfigurationNotFoundException The application configuration was not found in the application root. 
	 * @throws AppConfigurationNotReadableException If the application configuration file is not readable (e. g. another process reads from the file, etc.).
	 * 
	 */
	private void parse() throws AppConfigurationNotFoundException, AppConfigurationNotReadableException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(ApplicationConfigurationManager.APPCONFIGURATION_FILENAME));
			String line = "";
			while ((line = reader.readLine()) != null) {
				
				// Ignore comments and "whitespace" lines.
				if (!this.isCommentLine(line)) {
					if (!line.isEmpty()) {

						// A single configuration line has the format "key<SPLIT_IDENTIFIER>value"
						// Here we use the String#split(<SPLIT_IDENTIFIER>) method to generate a
						// array with the key (array[0]) and the value (array[1]). So the method
						// splits at the position where the <SPLIT_IDENTIFIER> is defined.
						String[] keyValue = line.split(SPLIT_IDENTIFIER);
						this.fill(keyValue[0], keyValue[1]);
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new AppConfigurationNotFoundException(ApplicationConfigurationManager.APPCONFIGURATION_FILENAME);
		} catch (IOException e) {
			throw new AppConfigurationNotReadableException(ApplicationConfigurationManager.APPCONFIGURATION_FILENAME, e.getMessage());
		}
	}

	/**
	 * Pushs the given value to the configuration object. <br /><br />
	 * 
	 * Note that you have to define the "key-method-mapping".
	 * 
	 * @param key The key from the app configuration file.
	 * @param value The value for the key from the app configuration file.
	 * 
	 */
	private void fill(String key, String value) {
		String methodName = voReadMethodInvocationMapping.get(key);

		try {
			Method method = cachedConfiguration.getClass().getDeclaredMethod(methodName, String.class);
			method.invoke(cachedConfiguration, value);
		} catch (SecurityException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (NoSuchMethodException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (IllegalArgumentException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (IllegalAccessException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (InvocationTargetException e) {
			// TRASH! If this problem occurs, the developer missed to fill the "methods" hash map correctly.
		} catch (NullPointerException e) {
			// TRASH! If a line was configured and no attributes exists in the application configuration object.
		}
	}

	/**
	 * Check if the given line is a comment line or not
	 * 
	 * @param line The line which should be analyzed.
	 * @return true/false
	 * 
	 */
	private boolean isCommentLine(String line) {
		return line.startsWith(COMMENT_IDENTIFIER);
	}
}
