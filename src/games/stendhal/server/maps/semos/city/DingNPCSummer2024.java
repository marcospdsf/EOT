/* $Id: DingNPCSummer2024.java,v 1.20 2024/06/29 16:17:58 davvids Exp $ */
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
package games.stendhal.server.maps.semos.city;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DingNPCSummer2024 implements ZoneConfigurator {
	

	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildding(zone);
	}

	private void buildding(final StendhalRPZone zone) {
		final SpeakerNPC ding = new SpeakerNPC("Ding") {
			
			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(14,22));
				nodes.add(new Node(10,22));
				nodes.add(new Node(10,20));
				nodes.add(new Node(7,20));
				nodes.add(new Node(7,15));
				nodes.add(new Node(5,15));
				nodes.add(new Node(5,5));
				nodes.add(new Node(3,5));
				nodes.add(new Node(3,11));
				nodes.add(new Node(4,11));
				nodes.add(new Node(4,21));
				nodes.add(new Node(10,21));
				nodes.add(new Node(10,22));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings, the weather is scorching today! How are you holding up?");
				addJob("I might have a #quest for you, if you are up for the challenge in this heat...");
				addHelp("I need your help obtaining frost giant hearts. In return, I will reward you with an ice dagger that will keep you cool on the hottest days.");
				addGoodbye("Farewell, watch out for the blazing hot sand...");
			}
		};

		ding.setDescription("You see Ding. Determination shines in his eyes—he seems in need of an adventurer's aid.");
		ding.setEntityClass("swimmer5npc");
		ding.setPosition(14, 22);
		ding.initHP(100);
		zone.add(ding);
	}
}
