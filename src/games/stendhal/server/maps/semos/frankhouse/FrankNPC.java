/* $Id: FrankNPC.java,v 1.5 2020/12/07 15:51:04 davvids Exp $ */
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
package games.stendhal.server.maps.semos.frankhouse;

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
public class FrankNPC implements ZoneConfigurator {

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
		final SpeakerNPC barman = new SpeakerNPC("Frank") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(17, 23));
				nodes.add(new Node(17, 14));
				nodes.add(new Node(26, 14));
				nodes.add(new Node(26, 6));
	            nodes.add(new Node(9, 6));
			    nodes.add(new Node(9, 16));
			    nodes.add(new Node(4, 16));
			    nodes.add(new Node(4, 27));
			    nodes.add(new Node(7, 27));
				nodes.add(new Node(7, 15));
			    nodes.add(new Node(17, 15));
				nodes.add(new Node(17, 23));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome.");
				addJob("Na razie niczego nie potrzebuje.");
				addQuest("Na razie niczego nie potrzebuje.");
				final Map<String, Integer> offerings = new HashMap<String, Integer>();
				addGoodbye("Miłego dnia.");
			}
		};

		barman.setEntityClass("podrozniknpc");
		barman.setPosition(23, 23);
		barman.initHP(100);
		barman.setDescription("Oto Frank. Prawdopodobnie pracuje w tutejszej kopalni.");
		zone.add(barman);
	}
}
