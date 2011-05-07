/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: HashTool.java 656 2010-07-04 16:11:39Z andre.koenig $
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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Tool, that creates hashes from strings (md5 for example).
 *
 * @since 1.0
 * @version $Id: HashTool.java 656 2010-07-04 16:11:39Z andre.koenig $
 * 
 */
public class HashTool {

	private static String MD5_HASH_ALGORITHM = "MD5";
	
	/**
	 * Converts a string to an md5 hash
	 * 
	 * @param hashable The hashable string.
	 * @return The hashed string.
	 * 
	 */
	public static String md5(String hashable) {
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance(MD5_HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {}
		
		
		m.update(hashable.getBytes(), 0, hashable.length() );
		
		return new BigInteger(1, m.digest()).toString(16);
	}	
}
