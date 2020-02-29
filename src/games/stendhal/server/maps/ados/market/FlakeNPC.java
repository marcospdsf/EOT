/* $Id: FlakeNPC.java,v 1.24 2012/08/23 20:05:43 yoriy Exp $ */
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
package games.stendhal.server.maps.ados.market;

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
public class FlakeNPC implements ZoneConfigurator {
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
		final SpeakerNPC xinBlanca = new SpeakerNPC("Flake") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Jestem starym mędrcem. Potrzebuje dla swoich wnuków trochę ekwipunku do treningu.");
				addHelp("Aktualnie nie potrzebuje pomocy.");
				new BuyerAdder().addBuyer(this, new BuyerBehaviour(shops.get("orcitems")), false);
				addOffer("Spójrz na tablicę koło mnie, aby zobaczyć czego dokładnie potrzebuje.");
				addQuest("Nie mam dla ciebie żadnego zadania.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("flakenpc");
		xinBlanca.setDescription("Flake wygląda na doświadczonego życiem wojownika.");
		xinBlanca.setPosition(48, 17);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
