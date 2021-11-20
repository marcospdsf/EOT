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
// Base on ../games/stendhal/server/maps/ados/barracks/BuyerNPC.java
package games.stendhal.server.maps.pol.zakopane.hostel;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.mapstuff.sign.Sign;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.BuyerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to buy previously un bought armor.
 *
 * @author kymara
 */
public class BronekNPC implements ZoneConfigurator {
	private final ShopList shops = SingletonRepository.getShopList();

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Bronek") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello, adventurer.");
				addJob("I buy dragon claws.");
				addHelp("I will gladly buy dragon claws from you, because I heard that somewhere beyond the seven forests it can be made a talisman! If you have something, #offer it to me .");
				addOffer("I have written on the blackboard what items I buy .");
				addQuest("Oh thank you, but I don't need anything anymore.");
				new BuyerAdder().addBuyer(this, new BuyerBehaviour(shops.get("buydragonitems")), false);
				addGoodbye("Goodbye buddy.");
			}
		};

		npc.setDescription("Here is Bronek, he looks like a decent highlander.");
		npc.setEntityClass("npcbronek");
		npc.setPosition(16, 18);
		npc.initHP(100);
		zone.add(npc);

		final Sign tablica = new Sign();
		tablica.setPosition(16, 15);
		tablica.setText(" -- Możesz sprzedać te rzeczy Bronkowi -- \n wolf claws\t 5\n pazury niedźwiedzie\t 8\n pazury tygrysie\t 100\n green dragon claw\t 5000\n"+
						" pazur niebieskiego smoka\t 5000\n red dragon claw\t 5000\n black dragon's claw\t 10000\n gold dragon claw\t 15000");
		tablica.setEntityClass("blackboard");
		tablica.setResistance(10);
		zone.add(tablica);

	}
}
