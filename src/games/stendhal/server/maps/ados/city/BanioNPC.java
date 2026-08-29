/* $Id: BanioNPC.java,v 1.5 2020/12/07 14:26:04 davvids Exp $ */
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
package games.stendhal.server.maps.ados.city;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.common.Direction;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Ados Tavern (Inside / Level 0).
 *
 * @author kymara
 */
public class BanioNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildTavern(zone, attributes);
	}

	private void buildTavern(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC barman = new SpeakerNPC("Banio") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(1, 82));
				nodes.add(new Node(10, 82));
				nodes.add(new Node(10, 46));
				nodes.add(new Node(29, 46));
	            nodes.add(new Node(29, 65));
			    nodes.add(new Node(56, 65));
			    nodes.add(new Node(56, 54));
			    nodes.add(new Node(53, 54));
			    nodes.add(new Node(53, 45));
				nodes.add(new Node(11, 45));
			    nodes.add(new Node(11, 82));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj.");
				addJob("Na razie niczego nie potrzebuje.");
				addQuest("Na razie niczego nie potrzebuje.");
				final Map<String, Integer> offerings = new HashMap<String, Integer>();
				addGoodbye("Miłego dnia.");
			}
		};

		barman.setEntityClass("xpphunternpc");
		barman.setPosition(44, 6);
		barman.initHP(100);
		barman.setDescription("You see Banio. He appears to enjoy walking around.");
		zone.add(barman);
	}
}
