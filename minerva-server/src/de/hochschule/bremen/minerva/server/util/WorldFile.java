/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: WorldFile.java 786 2010-07-05 22:59:22Z andre.koenig $
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.hochschule.bremen.minerva.commons.exceptions.WorldFileExtensionException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileNotFoundException;
import de.hochschule.bremen.minerva.commons.exceptions.WorldFileParseException;
import de.hochschule.bremen.minerva.commons.util.ColorTool;
import de.hochschule.bremen.minerva.commons.vo.Continent;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.manager.ApplicationConfigurationManager;

/**
 * Represents a physical file that contains the data structure to describe
 * a world with it's countries and country dependencies. It is possible to
 * parse this file and store the information in the world value object attributes.<br /><br />
 * 
 * @version $Id: WorldFile.java 786 2010-07-05 22:59:22Z andre.koenig $
 *
 */
public class WorldFile extends World {

	private static final long serialVersionUID = 2897911590755137328L;

	private static final String WORLD_FILE_EXTENSION = ".world";
	private static final String WORLD_FILE_XML = "data.xml";
	private static final File TEMP_DIR = new File(ApplicationConfigurationManager.get().getImporterWorkspaceDirectory());

	// The "Importable" represents at the very beginning the "*.world file", but while the import process is running
	// and all the data was extracted into the workspace directory, this object represents the xml import file (data.xml)
	// which is encapsulated in the "*.world file".
	private File importable = null;

	// The workspace directory for this world import file. While the extraction process is running, all the
	// stuff will be copied into this directory. After the import process has finished the directory will
	// be deleted.
	private File workspace = new File(TEMP_DIR.getAbsolutePath() + File.separator + (System.currentTimeMillis()/10) + File.separator);

	// The assets directory is the place where all the stuff from the world import file will be placed after
	// the import process has finished. Stuff means all the images and non-textual data (which will stored
	// via the persistence layer.
	private File assetsDirectory = null;

	// Unzipped the world file?
	private boolean extracted = false;
	
	// We define an internal HashMap which contains the continent objects from the
	// world import file. It is necessary to save it temporally because of the different
	// id mapping structure in the world import file. It differs from the auto generated
	// ids in the persistence layer. This map is only for internal purposes and will not
	// stored via the persistence layer.
	private HashMap<Integer, Continent> extractedContinents = new HashMap<Integer, Continent>();

	// This temporally map contains the countries from the world import file.
	private HashMap<Integer, Country> extractedCountries = new HashMap<Integer, Country>();

	// The neighbor mapping. Because there is a different mapping in the worlds import file.
	private HashMap<Integer, Vector<Integer>> neighbourMapping = new HashMap<Integer, Vector<Integer>>();
	
	/**
	 * Registers the world import file object.
	 * 
	 * @param worldFile The world import file object (*.world)
	 * @param assetsDirectory The assets directory where to place the maps and so on.
	 * 
	 */
	public WorldFile(File worldFile, String assetsDirectory) {
		this.importable = worldFile;
		this.assetsDirectory = new File(assetsDirectory + File.separator);
	}

	/**
	 * Parses the world import file and pushs the data into the
	 * world value object attributes.
	 * 
	 * @throws WorldFileNotFoundException If the given "*.world" file was not found.
	 * @throws WorldFileParseException If the world import file is not "well-formed".
	 * @throws WorldFileExtensionException If the file extension is wrong.
	 * @see World 
	 * 
	 */
	public void parse() throws WorldFileExtensionException, WorldFileNotFoundException, WorldFileParseException {
		try {
			// Initial file validation.
			// Is this the correct file type? ...
			this.validate();

			this.extract();
			
			Element dataSource = this.openDataXml();

			// Data source validation.
			// Is the world import file valid?
			this.validate(dataSource);

			this.extractMeta(dataSource);
			this.extractContinents(dataSource);
			this.extractCountries(dataSource);
		} catch (FileNotFoundException e) {
			throw new WorldFileNotFoundException(this.importable);
		} catch (IOException e) {
			throw new WorldFileNotFoundException(this.importable);
		} catch (SAXException e) {
			throw new WorldFileParseException(e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new WorldFileParseException(e.getMessage());
		}
	}

	/**
	 * Uses the world import file internal mapping structure
	 * and ports this to the real world value objects.<br />
	 * <br />
	 * <b>IMPORTANT:</b> Use this method after you've persisted the world
	 * before. After you've persisted the countries for example will
	 * have the generated ids from the persistence layer. Otherwise it
	 * is not possible to create the mapping.<br />
	 * <br />
	 * <b>NOTE:</b> You have to store the world object after creating the country
	 * dependencies to instruct the persistence layer to store this country
	 * relations.
	 * 
	 * <br />
	 * <b>NOTE2</b>: If the world import file wasn' extracted before, this method
	 * will call the parse method.
	 * 
	 * @throws WorldFileParseException If the parse method fails. 
	 * @throws WorldFileExtensionException If the parse method fails. 
	 * @throws WorldFileNotFoundException If the parse method fails.
	 * @see WorldFile#parse()
	 * 
	 */
	public void createCountryDependencies() throws WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException {
		if (!this.isExtracted()) {
			this.parse();
		}

		for (Entry<Integer, Vector<Integer>> entry : this.neighbourMapping.entrySet()) {
			Country country = this.extractedCountries.get(entry.getKey());

			for (int neighbourId : entry.getValue()) {
				Country neighbour = this.extractedCountries.get(neighbourId);
				
				this.connectCountries(country, neighbour);
			}
		}
	}

	/**
	 * Move the world assets to the defined directory.<br /><br />
	 * 
	 * If the world import file wasn't extracted before, this method
	 * will call the parse method.
	 * 
	 * @throws WorldFileParseException If the parse method fails. 
	 * @throws WorldFileExtensionException If the parse method fails. 
	 * @throws WorldFileNotFoundException If the parse method fails.
	 * @see WorldFile#parse()
	 * 
	 */
	public void moveAssets() throws WorldFileNotFoundException, WorldFileExtensionException, WorldFileParseException {
		if (!this.isExtracted()) {
			this.parse();
		}
		
		String workspacePath = this.getWorkspace() + File.separator;

		File asset = new File(workspacePath + this.getThumbnail());
		File moveTo = new File(this.getAssetsDirectory() + File.separator + asset.getName());
		asset.renameTo(moveTo);

		asset = new File(workspacePath + this.getMap());
		moveTo = new File(this.getAssetsDirectory() + File.separator + asset.getName());
		asset.renameTo(moveTo);

		asset = new File(workspacePath + this.getMapUnderlay());
		moveTo = new File(this.getAssetsDirectory() + File.separator + asset.getName());
		asset.renameTo(moveTo);
	}

	/**
	 * Opens the world import file and returns the document root.
	 * 
	 * @return The world import file root element.
	 * @throws ParserConfigurationException
	 * @throws SAXException Parser engine exception.
	 * @throws IOException File not found.
	 */
	private Element openDataXml() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(this.getImportable().toURI().toString());
		return doc.getDocumentElement();
	}

	/**
	 * Extracts the encapsulated data from the "Importable" into
	 * the workspace directory.
	 * 
	 * @throws WorldFileParseException 
	 * @throws FileNotFoundException 
	 * @throws FileNotFoundException 
	 * 
	 */
	private void extract() throws WorldFileParseException, FileNotFoundException {
		this.getWorkspace().mkdirs();

		final int BUFFER = 2048;

		BufferedOutputStream extractor = null;
		ZipInputStream zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(this.getImportable())));

		int spointer = 0; // spointer = stream pointer ;)

		try {
			ZipEntry zipEntry;
			while ((zipEntry = zip.getNextEntry()) != null) {
				spointer = 0;
				byte data[] = new byte[BUFFER];
				extractor = new BufferedOutputStream(new FileOutputStream(this.getWorkspace().getAbsolutePath() + File.separator + zipEntry.getName()));

				while ((spointer = zip.read(data, 0, BUFFER)) != -1) {
					extractor.write(data, 0, spointer);
				}

				extractor.flush();
				extractor.close();
			}
			zip.close();
		} catch (IOException e) {
			throw new WorldFileParseException(e.getMessage());
		}
		
		this.setImportable(new File(this.getWorkspace().getAbsolutePath() + File.separator + WORLD_FILE_XML));
		
		this.setExtracted(true);
	}

	/**
	 * Cleans the workspace directory and removes all
	 * temporary created files.
	 * 
	 */
	public void clean() {
		for (File file : this.getWorkspace().listFiles()) {
			file.delete();
		}
		this.getWorkspace().delete();
	}
	
	/**
	 * Extracts the meta data from the world import file
	 * and pushs the data into the object attributes.
	 * 
	 * @param dataSource The document root @see WorldFile#open()
	 * @throws WorldFileParseException If the document is not well-formed.
	 * 
	 */
	private void extractMeta(Element dataSource) throws WorldFileParseException {
		// The worlds token
		String node = "token";
		this.setToken(this.extractText(dataSource, node));
		if (this.getToken().isEmpty()) {
			throw new WorldFileParseException(this.importable, node);
		}

		// The worlds name
		node = "name";
		this.setName(this.extractText(dataSource, node));
		if (this.getName().isEmpty()) {
			throw new WorldFileParseException(this.importable, node);
		}

		// The worlds description
		node = "description";
		this.setDescription(this.extractText(dataSource, node));
		if (this.getDescription().isEmpty()) {
			throw new WorldFileParseException(this.importable, node);
		}
		
		// The author
		node = "author";
		this.setAuthor(this.extractText(dataSource, node));
		if (this.getAuthor().isEmpty()) {
			throw new WorldFileParseException(this.importable, node);
		}

		// The version
		node = "version";
		this.setVersion(this.extractText(dataSource, node));
		if (this.getVersion().isEmpty()) {
			throw new WorldFileParseException(this.importable, node);
		}

		// The map
		node = "map";
		this.setMap(this.extractText(dataSource, node));
		if (this.getMap().isEmpty()) {
			throw new WorldFileParseException(this.importable, node);
		}

		// The map underlay
		node = "map-underlay";
		this.setMapUnderlay(this.extractText(dataSource, node));
		if (this.getMapUnderlay().isEmpty()) {
			throw new WorldFileParseException(this.importable, node);
		}
		
		// The thumbnail
		node = "thumbnail";
		this.setThumbnail(this.extractText(dataSource, node));
		if (this.getThumbnail().isEmpty()) {
			throw new WorldFileParseException(this.importable, node);
		}
	}
	
	/**
	 * Extracts the continent data from the world import file
	 * and saves the data in an temporally hash map.
	 * 
	 * @param dataSource The document root @see WorldFile#open()
	 * @throws WorldFileParseException If the document is not well-formed.
	 * 
	 */
	private void extractContinents(Element dataSource) throws WorldFileParseException {
		String continentNode = "continent";
		NodeList continents = dataSource.getElementsByTagName(continentNode);
		
		for (int i = 0; i < continents.getLength(); i++) {
			NamedNodeMap node = continents.item(i).getAttributes();
			Continent continent = new Continent();
			continent.setName(node.getNamedItem("name").getNodeValue());

			int id = Integer.parseInt(node.getNamedItem("id").getNodeValue());
			this.extractedContinents.put(id, continent);
		}

		if (this.extractedContinents.isEmpty()) {
			throw new WorldFileParseException(this.importable, continentNode);
		}
	}

	/**
	 * Extracts the countries from the world import file.
	 * The countries will be pushed into the countries vector
	 * World#getCountries()
	 * 
	 * The world import file has an own "country-country" relation
	 * mapping (the id's from the persistence layer differs from the
	 * id's in the world import file).
	 * 
	 * Further the method will extract the country dependencies
	 * and add the correct continent to the country value object.
	 * 
	 * @param dataSource The worlds import file root element
	 * @throws WorldFileParseException If the world import file has the wrong data structure.
	 * 
	 */
	private void extractCountries(Element dataSource) throws WorldFileParseException {
		String countryNode = "country";
		NodeList dataSourceCountries = dataSource.getElementsByTagName(countryNode);
		
		for (int i = 0; i < dataSourceCountries.getLength(); i++) {
			NamedNodeMap dataSourceCountry = dataSourceCountries.item(i).getAttributes();

			Country country = new Country();
			country.setToken(dataSourceCountry.getNamedItem("token").getNodeValue());
			country.setName(dataSourceCountry.getNamedItem("name").getNodeValue());

			// TODO: Get the color from the file
			country.setColor(ColorTool.fromHexCode(dataSourceCountry.getNamedItem("color").getNodeValue()));

			int continentId = Integer.parseInt(dataSourceCountry.getNamedItem("continent").getNodeValue());
			country.setContinent(this.extractedContinents.get(continentId));

			int id = Integer.parseInt(dataSourceCountry.getNamedItem("id").getNodeValue());
			this.extractedCountries.put(id, country);

			// Extract the neighbours
			String neighbourValue = dataSourceCountry.getNamedItem("neighbours").getNodeValue();
			String[] neighbours = neighbourValue.split(",");
			Vector<Integer> neighbourIds = new Vector<Integer>();

			for (String neighbourId : neighbours) {
				neighbourIds.add(Integer.parseInt(neighbourId.trim()));
			}
			this.neighbourMapping.put(id, neighbourIds);

			this.addCountry(country);
		}

		if (this.extractedCountries.isEmpty()) {
			throw new WorldFileParseException(this.importable, countryNode);
		}
	}
	
	/**
	 * Common file validation.
	 * 
	 * TODO: Use in the next release dtd or xml schema for xml validation.
	 * 
	 * @throws WorldFileExtensionException
	 */
	private void validate() throws WorldFileExtensionException {
		if (!this.importable.getName().endsWith(WORLD_FILE_EXTENSION)) {
			throw new WorldFileExtensionException(this.importable, WORLD_FILE_EXTENSION);
		}
	}
	
	/**
	 * Checks if the data sources given data structure
	 * is valid. If it is not valid the method will raise
	 * the WorldFileParseException.
	 *
	 * @throws WorldFileParseException The world import file is not well-formed.
	 *
	 */
	private void validate(Element dataSource) throws WorldFileParseException {
		String nodeName = "meta";
		Node node = dataSource.getElementsByTagName(nodeName).item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(this.importable, nodeName);
		}
		
		nodeName = "continents";
		node = dataSource.getElementsByTagName(nodeName).item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(this.importable, nodeName);
		}

		nodeName = "countries";
		node = dataSource.getElementsByTagName(nodeName).item(0);
		if (node == null || node.getChildNodes().getLength() <= 0) {
			throw new WorldFileParseException(this.importable, nodeName);
		}
	}

	/**
	 * Extracts the text from an given xml node.
	 * For example <tag>text</tag>.
	 * 
	 * @param tag The tag name from which this method will extract the text.
	 * @return The tag content.
	 * 
	 */
	private String extractText(Element root, String tag) {
		NodeList nodes = root.getElementsByTagName(tag);
		return (nodes.getLength() > 0) ? nodes.item(0).getTextContent() : "";
	}

	/**
	 * Sets the importable file.
	 * 
	 * @param importable The world import file.
	 * 
	 */
	private void setImportable(File importable) {
		this.importable = importable;
	}

	/**
	 * The world import file object.
	 * 
	 * @return The world import file.
	 * 
	 */
	private File getImportable() {
		return this.importable;
	}
	
	/**
	 * Returns the assets directory where the world images will be placed.
	 * 
	 * @return A File represents the assets directory.
	 * 
	 */
	private File getAssetsDirectory() {
		return this.assetsDirectory;
	}

	/**
	 * Returns the workspace directory.
	 * 
	 * @return A File object, which represents the workspace directory.
	 * 
	 */
	private File getWorkspace() {
		return this.workspace;
	}

	/**
	 * Notify that the file was extracted.
	 * 
	 * @param extracted boolean
	 * 
	 */
	private void setExtracted(boolean extracted) {
		this.extracted = extracted;
	}

	/**
	 * Was the world file extracted?
	 * 
	 * @return boolean
	 * 
	 */
	public boolean isExtracted() {
		return extracted;
	}
}