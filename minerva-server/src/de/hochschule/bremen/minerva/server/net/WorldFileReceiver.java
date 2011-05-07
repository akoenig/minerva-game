/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: WorldFileReceiver.java 789 2010-07-05 23:24:48Z andre.koenig $
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import de.root1.simon.RawChannelDataListener;

/**
 * Receives the world import file from the client.
 * 
 * @since 1.0
 * @version $Id: WorldFileReceiver.java 789 2010-07-05 23:24:48Z andre.koenig $
 * 
 */
public class WorldFileReceiver implements RawChannelDataListener {

	private FileChannel fileChannel;

	/**
	 * Initialize the stream for writing the
	 * world import file.
	 *
	 * @param filename
	 *
	 */
	public WorldFileReceiver(String filename) {
		try {
	        this.fileChannel = new FileOutputStream(new File(filename)).getChannel();
        } catch (FileNotFoundException ex) {
        	// This exception can not occur. We create the file here ;)
        }
	}

	/**
	 * Writes the data through the file channel.
	 * 
	 * @param Writes the data buffer through the file channel.
	 *
	 */
	@Override
	public void write(ByteBuffer data) {
        try {
        	this.fileChannel.write(data);
        } catch (IOException e) {
        	e.printStackTrace();
        }
	}

	/**
	 * Closes the file channel.
	 *
	 */
	@Override
	public void close() {
		try {
			this.fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}