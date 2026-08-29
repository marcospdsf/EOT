/* $Id: DendryNPC.java,v 1.24 2012/08/23 20:05:43 yoriy Exp $ */
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
package games.stendhal.server.maps.fado.city;


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
public class DendryNPC implements ZoneConfigurator {
	

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
		final SpeakerNPC xinBlanca = new SpeakerNPC("Dendry") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Nie mam dla ciebie żadnego zadania.");
				addHelp("Aktualnie nie potrzebuje pomocy.");
				ShopsList.get().configureNPC(this, "giantitems", ShopType.ITEM_BUY, false);
				addOffer("Spójrz na tablicę koło mnie, aby zobaczyć co kupuję.");
				addQuest("Nie mam dla ciebie żadnego zadania.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("shopnpc1");
		xinBlanca.setDescription("Widzisz Dendrego. Podobno siedzi pod tym drzewem, bo kiedyś zakopał tutaj skarb i nie może go wydobyć..");
		xinBlanca.setPosition(115, 18);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
