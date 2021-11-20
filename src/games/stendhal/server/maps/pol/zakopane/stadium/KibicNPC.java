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
package games.stendhal.server.maps.pol.zakopane.stadium;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to buy previously un bought armor.
 *
 * @author kymara
 */
public class KibicNPC implements ZoneConfigurator {
	private final ShopList shops = SingletonRepository.getShopList();

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildKibic(zone);
	}

	private void buildKibic(final StendhalRPZone zone) {
		final SpeakerNPC kibic = new SpeakerNPC("Kibic") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(26, 5));
				nodes.add(new Node(37, 5));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to the Zakopane stadium !");
				addJob("I am a supporter. I support the best Zakopane Team.");
				addHelp("In the future, it will be possible to buy something to eat or drink at the stadium. For now you have to bring your own provisions.");
				new SellerAdder().addSeller(this, new SellerBehaviour(shops.get("mecz")));
				addGoodbye("Goodbye, and come to matches often. Our team needs real fans, not hooligans.");
			}
		};

		kibic.setDescription("Here is a fan who is a faithful fan of Zakopane.");
		kibic.setEntityClass("npckibic");
		kibic.setPosition(26, 5);
		kibic.initHP(1000);
		kibic.setSounds(Arrays.asList("pol-boisko-tlo", "pol-chcemy-gola"));

		zone.add(kibic);
	}
}
