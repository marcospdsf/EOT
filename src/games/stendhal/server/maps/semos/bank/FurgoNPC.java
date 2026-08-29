/* $Id: FurgoNPC.java,v 1.19 2012/08/23 20:05:44 yoriy Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.semos.bank;


import games.stendhal.server.entity.npc.shop.ShopsList;
import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds a Bar Maid NPC to buy food from players.
 *
 * @author kymara
 */
public class FurgoNPC implements ZoneConfigurator {

	


	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Furgo") {

			@Override
			protected void createPath() {
				// NPC doesn't move
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witam!");
				addGreeting("Welcome! I can purchase angel figurines from you if you possess any. Say: #sell #angel #figurine.");
				addJob("I buy angel figurines. You can obtain them on the starter map. If you wish to sell, say: #sell #angel #figurine.");
				addHelp("I buy angel figurines. If you have any to sell, say: #sell #angel #figurine.");
 				addGoodbye("Dowidzenia, dowidzenia!");
 				ShopsList.get().configureNPC(this, "furgo1", ShopType.ITEM_BUY, true);
			}
		};
		npc.setDescription("You see friendly Furgo. He looks like someone you can ask for assistance.");
		npc.setEntityClass("furgo_npc");
		npc.setPosition(43, 2);
		npc.initHP(100);
		zone.add(npc);
	}
}
