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
package games.stendhal.server.maps.pol.krakow.sukiennice;

import games.stendhal.common.Direction;
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
public class ChengNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Cheng") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello natives.");
				addJob("I am looking for rare stones, nice jewelry. You can #offer something. ");
				addHelp("I buy rare stones and jewelery, if you have something, #offer it to me. ");
				addOffer("Look at the book, there you will find my offer.");
				addQuest("Hmm ... tell me about your habits.");
				new BuyerAdder().addBuyer(this, new BuyerBehaviour(shops.get("buycheng")), false);
				addGoodbye("Goodbye.");
			}
		};

		npc.setDescription("Here is Cheng a merchant from the Orient.");
		npc.setEntityClass("npcwikary");
		npc.setPosition(26, 30);
		npc.setDirection(Direction.LEFT);
		npc.initHP(100);
		zone.add(npc);
		
		final Sign book = new Sign();
		book.setPosition(24, 32);
		book.setText(" -- Skup -- \n bursztyn\t\t 20\n wielka perła\t 1500\n bryłka mithrilu\t 10\n chaplet\t\t 10000");
		book.setEntityClass("book_red");
		book.setResistance(10);
		zone.add(book);
	}
}
