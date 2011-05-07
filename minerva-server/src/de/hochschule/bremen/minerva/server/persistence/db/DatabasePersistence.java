/*
 * Minerva - Game, Copyright 2010 Christian Bollmann, Carina Strempel, André König
 * Hochschule Bremen - University of Applied Sciences - All Rights Reserved.
 *
 * $Id: DatabasePersistence.java 663 2010-07-04 16:24:05Z andre.koenig $
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
package de.hochschule.bremen.minerva.server.persistence.db;


import de.hochschule.bremen.minerva.commons.vo.Continent;
import de.hochschule.bremen.minerva.commons.vo.Country;
import de.hochschule.bremen.minerva.commons.vo.Neighbour;
import de.hochschule.bremen.minerva.commons.vo.Player;
import de.hochschule.bremen.minerva.commons.vo.World;
import de.hochschule.bremen.minerva.server.persistence.Handler;
import de.hochschule.bremen.minerva.server.persistence.Persistence;
import de.hochschule.bremen.minerva.server.persistence.db.handler.ContinentHandler;
import de.hochschule.bremen.minerva.server.persistence.db.handler.CountryHandler;
import de.hochschule.bremen.minerva.server.persistence.db.handler.NeighbourHandler;
import de.hochschule.bremen.minerva.server.persistence.db.handler.PlayerHandler;
import de.hochschule.bremen.minerva.server.persistence.db.handler.WorldHandler;

/**
 * Database persistence handler creator.
 * 
 * @since 1.0
 * @version $Id: DatabasePersistence.java 663 2010-07-04 16:24:05Z andre.koenig $
 * 
 */
public class DatabasePersistence implements Persistence {

	/**
	 * Factory method, that creates the needed database
	 * handler by an given value object class name.
	 * 
	 * @param type The value object class name.
	 * @return The database persistence handler ("null" if no handler was found).
	 * 
	 */
	@Override
	public Handler createHandler(Class<?> type) {
		if (type == World.class) {
			return new WorldHandler();
		} else if (type == Country.class) {
			return new CountryHandler();
		} else if (type == Continent.class) {
			return new ContinentHandler();
		} else if (type == Neighbour.class) {
			return new NeighbourHandler();
		} else if (type == Player.class) {
			return new PlayerHandler();
		}
		return null;
	}
}