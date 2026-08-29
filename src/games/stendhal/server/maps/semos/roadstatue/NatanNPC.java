/* $Id: NatanNPC.java,v 1.9 2023/11/25 15:51:34 davvids Exp $ */
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
package games.stendhal.server.maps.semos.roadstatue;

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
 * Builds the post office elf NPC.
 * She may be used later for something else like a newspaper. 
 * Now she sells nalwor scrolls
 * @author kymara
 */
public class NatanNPC implements ZoneConfigurator {
	

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
		final SpeakerNPC npc = new SpeakerNPC("Natan") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(20, 8));
				nodes.add(new Node(20, 4));
				nodes.add(new Node(26, 4));
				nodes.add(new Node(26, 12));
				nodes.add(new Node(23, 12));
				nodes.add(new Node(23, 17));
				nodes.add(new Node(16, 17));
				nodes.add(new Node(16, 13));
				nodes.add(new Node(10, 13));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello. How can I #help you?");
				addJob("I am searching for someone to help deal with the dreamscape creatures. Could you #help me?");
				addHelp("The creatures from the dreamscape haunt my slumber... I might have a #quest for you.");
				addGoodbye("Farewell. It was nice meeting you!");
			}
		};

		npc.setDescription("You see Natan, looking troubled and focused on nightmares haunted by the dreamscape.");
		npc.setEntityClass("man_003_npc");
		npc.setPosition(10, 8);
		npc.initHP(100);
		zone.add(npc);
	}
}
