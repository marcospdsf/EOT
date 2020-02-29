/* $Id: MarketerNPC.java,v 1.24 2012/08/23 20:05:43 yoriy Exp $ */
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
package games.stendhal.server.maps.semos.market;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ShopList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.BuyerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;

/*
 */
public class MarketerNPC implements ZoneConfigurator {
	private final ShopList shops = SingletonRepository.getShopList();

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildXinBlanca(zone);
	}

	private void buildXinBlanca(final StendhalRPZone zone) {
		final SpeakerNPC xinBlanca = new SpeakerNPC("Kubola") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Jestem pracownikiem marketu Semos. Skupujemy świeże warzywa, owoce, wypieki oraz ryby.");
				addHelp("Nasz lokal wygląda na mały ponieważ mamy bardzo duży magazyn który zajmuje przestrzeń. Możesz nam pomóc i sprzedać trochę jedzenia abyśmy mogli się stale rozwijać.");
				new BuyerAdder().addBuyer(this, new BuyerBehaviour(shops.get("semosmarket")), false);
				addOffer("Spójrz na tablicę na ścianie, aby zobaczyć naszą #ofertę.");
				addQuest("Jedyne co możesz dla mnie zrobić to sprzedać trochę swojego jedzenia.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("cloakedwomannpc");
		xinBlanca.setDescription("Kubola wygląda na przyjazną osobę. Możesz sprzedać jej swoje jedzenie.");
		xinBlanca.setPosition(7, 2);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
