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

import java.util.Arrays;
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
				addGreeting("Greetings, brave and noble warrior.");
				addHelp("Please help me! Slay that dreadful dragon, and the #'golden blade' shall be yours!");
				addReply(Arrays.asList("golden blade", "blade", "sword"), "That vile dragon ambushed me and swallowed it whole! If you vanquish the beast, you may keep the blade as your reward.");
				addOffer("I have a #quest for you.");
				addQuest("I was gathering rare herbs for a magical remedy when a vicious two-headed dragon blocked my path. I beg of you, slay the monster guarding the passage. If you will undertake this task, say #'I will kill it'.");
				addReply(Arrays.asList("i will kill it", "zabiję", "zabije"), "Splendid! Go forth and strike it down. The magnificent #weapon you recover from its belly will be yours to keep.");
				addReply(Arrays.asList("weapon", "broń"), "The dragon swallowed my golden blade whole when it ambushed me!");
				addGoodbye("May fortune and strength go with you, my savior.");
			}
		};

		jolanda.setEntityClass("elegantladynpc");
		jolanda.setPosition(127, 120);
		jolanda.initHP(100); 
		jolanda.setDescription("You see Jolanda. While traveling through these caverns, she was attacked by a monstrous dragon.");
		zone.add(jolanda);
	}
}
