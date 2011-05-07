/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: MapTool.java 839 2010-08-07 19:50:36Z andre.koenig $
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
package de.hochschule.bremen.minerva.commons.util;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.mina.util.Base64;

import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.ValueObject;
import de.hochschule.bremen.minerva.commons.vo.World;

/**
 * Provides some helper methods for map processing.
 *
 * @since 1.0
 * @version $Id: MapTool.java 839 2010-08-07 19:50:36Z andre.koenig $
 * 
 */
public class MapTool {

	// The color on a map, that identifies the center point,
	// where to place some controls (e. g. "army count").
	private static final int COUNTRY_ANCHOR_IDENTIFIER = 0xFF000000;
	
	/**
	 * Returns a hash map, which contains the country anchor points.
	 * A country anchor means the center point where controls, the
	 * army count for example, can be placed.
	 * 
	 * Checking each map pixel if the color equals the COUNTRY_ANCHOR_IDENTIFIER
	 * color. If so, a new anchor was found and will be placed in the hash map
	 * identified by the country.
	 * 
	 * @return HashMap Contains the country as key and the anchor point as value.
	 * 
	 */
	public static HashMap<Country, Point> getCountryAnchors(BufferedImage map, BufferedImage mapUnderlay, World world) {
		HashMap<Country, Point> countryAnchors = new HashMap<Country, Point>();

		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if (map.getRGB(x, y) == COUNTRY_ANCHOR_IDENTIFIER) {
					Point point = new Point(x,y);
					Country country = world.getCountry(new Color(mapUnderlay.getRGB(x, y)));

					if (country.getId() > ValueObject.getDefaultId()) {
						countryAnchors.put(country, point);
					}
				}
			}
		}
		
		return countryAnchors;
	}
	
	/**
	 * Creates a map image out of a 2-dimensional integer array equivalent in aRGB
	 * @param integerArray map as integer array
	 * @return map as buffered image
	 * 
	 * @deprecated
	 * 
	 */
	public static BufferedImage createMapImageFromArray(int[][] integerArray) {
		BufferedImage image = new BufferedImage(integerArray.length, integerArray[0].length, BufferedImage.TYPE_INT_ARGB_PRE);
		for(int x = 0; x < integerArray.length; x++) {
			for(int y = 0; y < integerArray[0].length; y++) {
				image.setRGB(x, y, integerArray[x][y]);
			}
		}
		return image;
	}
	
	/**
	 * Creates a 2-dimensional integer array consisting of aRGB color values from
	 * a buffered image map.
	 * @param mapImage buffered image of map
	 * @return integer array of image
	 * 
	 * @deprecated
	 * 
	 */
	public static int[][] createArrayFromMapImage(BufferedImage mapImage) {
		int[][] map = new int[mapImage.getWidth()][mapImage.getHeight()];
		for (int x = 0; x < mapImage.getWidth(); x++) {
			for (int y = 0; y < mapImage.getHeight(); y++) {
				map[x][y] = mapImage.getRGB(x, y);
			}
		}
		return map;
	}

	/**
	 * Converts a buffered image to an base64 encoded string.
	 *
	 * @param map The map, which should be converted.
	 * @return The base64 string.
	 *
	 * @throws IOException
	 *
	 */
	public static String toBase64(BufferedImage map) throws IOException {
		
		ByteArrayOutputStream baStream = new ByteArrayOutputStream();
		
		ImageIO.write(map, "PNG", baStream);
		baStream.flush();

		String base64Map = new String(Base64.encodeBase64(baStream.toByteArray()));
		
		baStream.close();

		return base64Map;
	}
	
	/**
	 * Creates a buffered image from an base64 string.
	 * 
	 * @param base64 The map as base64 encoded string.
	 * @return The map as an buffered image.
	 *
	 * @throws IOException
	 *
	 */
	public static BufferedImage fromBase64(String base64) throws IOException {
		byte[] mapData = Base64.decodeBase64(base64.getBytes());

		return ImageIO.read(new ByteArrayInputStream(mapData));
	}
}