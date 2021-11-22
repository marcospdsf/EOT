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
// Based on ../games/stendhal/server/maps/ados/entwives/EntwifeNPC.java
package games.stendhal.server.maps.pol.dragon.cave;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

/**
 * entwife located in 0_ados_mountain_n2_w2.
 */
public class JolandaNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildjolanda(zone);
	}

	private void buildjolanda(final StendhalRPZone zone) {
		final SpeakerNPC jolanda = new SpeakerNPC("Jolanda") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello, brave and handsome warrior.");
				addHelp("Help me. Kill that nasty dragon and the #'golden blade' will be yours!");
				addReply("golden blade", "This nasty dragon attacked me and swallowed it. If you defeat him, you will be able to keep it as a reward.");
				addOffer("I have #task for you.");
				addQuest("I was looking for a rare herb for the potion, but the dragon blocked my way to it. I have a great request for you to kill the dragon that blocked my way. If you do it, say #'I will kill'.");
				addReply("I will kill", "Excellent! Go and deal with him. You can keep this great #weapon as a reward.");
				addReply("weapon", "He attacked me and swallowed it");
				addGoodbye("May the strength be with you my savior.");
			}
		};

		jolanda.setEntityClass("elegantladynpc");
		jolanda.setPosition(127, 120);
		jolanda.initHP(100); 
		jolanda.setDescription("Here is Jolanda. During the trip, she was attacked by a dragon.");
		zone.add(jolanda);
	}
}
