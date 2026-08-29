/* $Id: MiterNPC.java,v 1.19 2010/09/19 02:35:36 nhnb Exp $ */
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


import games.stendhal.server.entity.npc.shop.ShopsList;
import games.stendhal.server.entity.npc.shop.ShopType;
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
 * A young lady (original name: Miter) who heals players without charge. 
 */
public class MiterNPC implements ZoneConfigurator {
	
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Miter") {
			@Override
			public void createDialog() {
				addGreeting("Welcome, I am Miter. I sell starter gear—say #offer to view my wares. I can also restore your health if you say #heal. For general help, chat globally using #/k.");
				addJob("My magic allows me to heal wounds. I also sell healing potions and basic equipment.");
				addHelp("I can heal you for free—just say #heal. You can also view my items with #offer. Right-click items to inspect stats, and drag ground items into your inventory.");
				addEmotionReply("hugs", "hugs");
				addGoodbye();
			}

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(18, 5));
				nodes.add(new Node(18, 8));
				setPath(new FixedPath(nodes, true));
			}
		};
		ShopsList.get().configureNPC(npc, "startkasa", ShopType.ITEM_SELL, true);
		new HealerAdder().addHealer(npc, 0);
		npc.setPosition(18, 6);
		npc.setDescription("You see friendly Miter. Greet her by saying #hi");
		npc.setEntityClass("miternpc");
		zone.add(npc);
	}
}
