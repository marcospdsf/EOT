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
package games.stendhal.server.maps.pol.zakopane.shop;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.BuyerAdder;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;
import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Inside Zakopane Tavern - Level 0 (ground floor)
 */
public class TraderNPC implements ZoneConfigurator {
	private final ShopList shops = SingletonRepository.getShopList();

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildMichal(zone);
	}

	private void buildMichal(final StendhalRPZone zone) {
		final SpeakerNPC michal = new SpeakerNPC("Michał") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(11, 8));
				nodes.add(new Node(11, 17));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Sprzedaję rzeczy początkującym łowcom wrażeń.");
				addHelp("Kupuję i sprzedaję różne przedmioty. Zapytaj mnie o moja #ofertę.");
				new SellerAdder().addSeller(this, new SellerBehaviour(shops.get("sellstuff")), false);
				new BuyerAdder().addBuyer(this, new BuyerBehaviour(shops.get("buystuff")), false);
				addOffer("Spójrz na tablicę na ścianie, aby zapoznać się z moją ofertą.");
				addQuest("Nie mam dla ciebie zadania. Porozmawiaj z ludźmi może oni potrzebują pomocy.");
				addGoodbye();
			}
		};

		michal.setEntityClass("weaponsellernpc");
		michal.setDescription("Oto Michał. Jest znany wśród początkujących podróżników głównie ze względu na asortyment...");
		michal.setPosition(11, 8);
		michal.initHP(100);
		zone.add(michal);
	}
}
