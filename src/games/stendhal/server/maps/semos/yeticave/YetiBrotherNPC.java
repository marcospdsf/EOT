/* $Id: YetiBrotherNPC.java,v 1.24 2012/08/23 20:05:43 yoriy Exp $ */
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
package games.stendhal.server.maps.semos.yeticave;


import games.stendhal.server.entity.npc.shop.ShopsList;
import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 */
public class YetiBrotherNPC implements ZoneConfigurator {
	

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
		final SpeakerNPC xinBlanca = new SpeakerNPC("Yeti Brother") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("I have no task for you.");
				addHelp("Aktualnie nie potrzebuje pomocy.");
				ShopsList.get().configureNPC(this, "yeticaveshop", ShopType.ITEM_BUY, false);
				addOffer("Check the notice board next to me to see what items I buy.");
				addQuest("I have no quests available right now.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("shopnpc2");
		xinBlanca.setDescription("Widzisz brata Mr. Yeti");
		xinBlanca.setPosition(27, 52);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
