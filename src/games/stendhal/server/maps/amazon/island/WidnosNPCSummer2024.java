/* $Id: WidnosNPCSummer2024.java,v 1.20 2024/06/25 22:17:58 davvids Exp $ */
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
package games.stendhal.server.maps.amazon.island;

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
 * Builds ados widnos NPC.
 * He may give an items quest later
 * Now he sells ados scrolls
 * @author kymara
 */
public class WidnosNPCSummer2024 implements ZoneConfigurator {
	

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildwidnos(zone);
	}

	private void buildwidnos(final StendhalRPZone zone) {
		final SpeakerNPC widnos = new SpeakerNPC("Widnos") {
			
			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(55,52));
				nodes.add(new Node(55,60));
				nodes.add(new Node(47,60));
				nodes.add(new Node(47,66));
				nodes.add(new Node(43,66));
				nodes.add(new Node(43,75));
				nodes.add(new Node(18,75));
				nodes.add(new Node(18,77));
				nodes.add(new Node(11,77));
				nodes.add(new Node(11,75));
				nodes.add(new Node(10,75));
				nodes.add(new Node(10,64));
				nodes.add(new Node(19,64));
				nodes.add(new Node(19,59));
				nodes.add(new Node(27,59));
				nodes.add(new Node(27,62));
				nodes.add(new Node(31,62));
				nodes.add(new Node(31,51));
				nodes.add(new Node(40,51));
				nodes.add(new Node(40,54));
				nodes.add(new Node(49,54));
				nodes.add(new Node(49,50));
				nodes.add(new Node(48,50));
				nodes.add(new Node(48,52));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, wojowniku!");
				addJob("Szukam kogoś kto chciałby trochę powalczyć");
				addHelp("Hm... możemy porozmawiać o jednym #zadaniu");
				addGoodbye("Żegnaj i uważaj na potwory!");
			}
		};

		widnos.setDescription("You see Widnos. He looks weary from his journey.");
		widnos.setEntityClass("oldkingnpc");
		widnos.setPosition(48, 52);
		widnos.initHP(100);
		zone.add(widnos);
	}
}
