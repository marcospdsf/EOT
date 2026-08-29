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
// Based on ../games/stendhal/server/maps/ados/barracks/BuyerNPC.java
package games.stendhal.server.maps.pol.koscielisko.zrc;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to buy previously un bought armor.
 *
 * @author kymara
 */
public class EdgardNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Edgard") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(2, 42));
				nodes.add(new Node(14, 42));
				nodes.add(new Node(14, 44));
				nodes.add(new Node(30, 44));
				nodes.add(new Node(30, 58));
				nodes.add(new Node(26, 58));
				nodes.add(new Node(26, 62));
				nodes.add(new Node(8, 62));
				nodes.add(new Node(8, 69));
				nodes.add(new Node(37, 69));
				nodes.add(new Node(37, 66));
				nodes.add(new Node(51, 66));
				nodes.add(new Node(51, 42));
				nodes.add(new Node(2, 42));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("See how fast I fly? I bet you wish you had wings of your own!");
				addJob("I carry dispatches across far battlefields and convey orders directly from the Grand Master of the Shadow Order.");
				addHelp("I fly here and there—I require no assistance from anyone.");
				addGoodbye("Off I fly, cheers!");
			}
		};

		npc.setDescription("You see Edgard, swift as the mountain wind.");
		npc.setEntityClass("npcedgard");
		npc.setPosition(2, 42);
		npc.initHP(1000);
		zone.add(npc);
	}
}
