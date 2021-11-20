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
package games.stendhal.server.maps.pol.zakopane.tavern;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.mapstuff.sign.Sign;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Food and drink seller,  Inside Semos Tavern - Level 0 (ground floor)
 * Sells the flask required for Tad's quest IntroducePlayers
 */
public class JagnaNPC implements ZoneConfigurator {
	private final ShopList shops = SingletonRepository.getShopList();

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildJagna(zone);
	}

	private void buildJagna(final StendhalRPZone zone) {
		final SpeakerNPC jagna = new SpeakerNPC("Jagna") {

			@Override
			protected void createPath() {
				// NPC does not move
				setPath(null);
			}
			@Override
			protected void createDialog() {
				addGreeting("Hello, nice guy. Can I get something?");
				addJob("I am a waitress at this inn. We sell imported and local liquors and good food. There will also be something for dessert.");
				addHelp("This inn is known throughout Zakopane. You can relax here and eat well. If you want to know our #offer, tell me about it .");
				new SellerAdder().addSeller(this, new SellerBehaviour(shops.get("urodziny")));
				addGoodbye("Enjoy your meal and have fun until the morning.");
			}
		};

		jagna.setEntityClass("hotelreceptionistnpc");
		jagna.setPosition(25, 3);
		jagna.setDirection(Direction.DOWN);
		jagna.initHP(100);
		zone.add(jagna);

		final Sign book = new Sign();
		book.setPosition(28, 1);
		book.setText(" -- Jagna sprzedaje -- \n hops juice\t 8\n grape drink\t 10\n napój z oliwką\t 50\n shake waniliowy\t 100\n shake czekoladowy\t 100\n meat\t\t 20\n ham\t\t 30\n hotdog\t\t 120\n"+
					" hotdog z serem\t 140\n sandwich z tuńczykiem\t 110\n sandwich\t\t 110\n tabliczka czekolady\t 80\n lukrecja\t\t 80");
		book.setEntityClass("blackboard");
		book.setResistance(10);
		zone.add(book);
	}
}
