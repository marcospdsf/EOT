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
package games.stendhal.server.maps.pol.zakopane.townhall;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GazdaWojtekNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildZakopaneRatuszArea(zone);
	}

	private void buildZakopaneRatuszArea(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Gazda Wojtek") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(12, 4));
				nodes.add(new Node(20, 4));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings.");
				addJob("I am Gazda Wojtek. I look after the town of Zakopane!");
				addHelp("Our town needs many resources and helping hands. If you need the bank, you will find it south of the town hall; for healing, visit Gaździna Jadźka to the east.");
				addOffer("I no longer sell scrolls myself—Juhas handles all scrolls now. Perhaps you would like to earn renown by taking on a #quest? You will be well rewarded.");
				addGoodbye("Farewell and good fortune!");
			}
		};

		npc.setEntityClass("npcgazda");
		npc.setPosition(12, 4);
		npc.initHP(100);
		zone.add(npc);
	}
}
