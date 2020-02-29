/* $Id: ElectroNPC.java,v 1.24 2012/08/23 20:05:43 yoriy Exp $ */
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
public class ElectroNPC implements ZoneConfigurator {
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
		final SpeakerNPC xinBlanca = new SpeakerNPC("Electro") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Nie mam dla ciebie żadnego zadania.");
				addHelp("Aktualnie nie potrzebuje pomocy.");
				new BuyerAdder().addBuyer(this, new BuyerBehaviour(shops.get("whitegolditems")), false);
				addOffer("Spójrz na tablicę koło mnie, aby zobaczyć co kupuję.");
				addQuest("Nie mam dla ciebie żadnego zadania.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("electronpc");
		xinBlanca.setDescription("Electro wydaje się być dziwnym młodzieńcem.");
		xinBlanca.setPosition(16, 39);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
