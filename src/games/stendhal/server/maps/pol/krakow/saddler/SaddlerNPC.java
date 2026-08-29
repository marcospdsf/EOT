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
package games.stendhal.server.maps.pol.krakow.saddler;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.SpeakerNPCFactory;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The miller (original name: Jenny). She mills flour for players who bring
 * grain. 
 */
public class SaddlerNPC extends SpeakerNPCFactory {

	@Override
	public void createDialog(final SpeakerNPC npc) {
		npc.addJob("I craft fine leather goods. Bring me an animal pelt and I will sew an empty waterskin for you.");
		npc.addReply("pelt",
					"Hunting wild beasts across the lands is the best way to obtain pelts.");
		npc.addReply("animal pelt",
					"Hunting wild beasts across the lands is the best way to obtain pelts.");
		npc.addHelp("I tan hides and sew sturdy waterskins. Just say #'sew empty waterskin'.");
		npc.addGoodbye("Farewell.");

		final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
		requiredResources.put("pelt", 1);
		requiredResources.put("money", 20);

		final ProducerBehaviour behaviour = new ProducerBehaviour("rymarz_make_buklak",
				"sew", "empty waterskin", requiredResources, 1 * 5);

		new ProducerAdder().addProducer(npc, behaviour,
				"Greetings! If you bring me an #animal pelt, I can sew a waterskin for you. Just say #'sew empty waterskin'.");
	}
}
