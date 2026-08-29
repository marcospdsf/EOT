/* $Id$ */
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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

/**
 * Teleporter to Zakopane or Semos.
 */
public class TeleporterNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildDeathmatchRecruiter(zone);
	}

	private void buildDeathmatchRecruiter(final StendhalRPZone zone) {

		final SpeakerNPC npc = new SpeakerNPC("Universe") {

			@Override
			protected void createPath() {
				final List<Node> path = new LinkedList<Node>();
				setPath(new FixedPath(path, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Oh... Hello... I am always glad to meet new players...");
				addJob("My only job is to watch every player grow where they wish to #start.");
				addHelp("Well, I am sorry, I cannot reveal the universe secrets to you, but I can #teleport you.");
				addQuest("The quest of your life will be where you will be #teleported.");
				addOffer("I'll tell you about the #teleport.");
				add(ConversationStates.ATTENDING, Arrays.asList("teleport", "teleported", "history", "adventure","start","where","places"), 
						null, ConversationStates.ATTENDING,
				        "I am able to teleport anybody to the two places in the world, one is #Semos, the other is #Zakopane.", null);
				
				//The rest is handled in quest "TheChoice"...
				
				
				addGoodbye("Well, bye... Soon you shall return to me...");
			}
		};

		npc.setEntityClass("transparentnpc");
		npc.setPosition(51, 51);
		npc.initHP(100);
		npc.setDescription("You see something that you can not say how it looks like...");
		zone.add(npc);
	}
}
