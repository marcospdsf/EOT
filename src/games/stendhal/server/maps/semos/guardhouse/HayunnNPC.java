/* $Id: HayunnNPC.java,v 1.19 2021/01/13 17:26:36 nhnb Exp $ */
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
package games.stendhal.server.maps.semos.guardhouse;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.HealerAdder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A young lady (original name: Hayunn) who heals players without charge. 
 */
public class HayunnNPC implements ZoneConfigurator {
	
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Hayunn") {
			@Override
			public void createDialog() {
				addGreeting("Welcome, I am Hayunn. Before you venture out, you should learn the basics of the game. Say #quest to begin.");
				addEmotionReply("hugs", "hugs");
				addGoodbye();
			}

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(4, 9));
				nodes.add(new Node(6, 9));
				nodes.add(new Node(6, 14));
				nodes.add(new Node(6, 9));
				nodes.add(new Node(11, 9));
				setPath(new FixedPath(nodes, true));
			}
		};

		npc.setPosition(4, 9);
		npc.setDescription("You see friendly Hayunn. He looks like a seasoned mentor. Greet him by saying #hi");
		npc.setEntityClass("oldheronpc");
		zone.add(npc);
	}
}
