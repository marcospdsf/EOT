/* $Id: UrzednikFadoNPC.java,v 1.9 2029/11/27 14:38:15 davvids Exp $ */
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
package games.stendhal.server.maps.fado.tavern;


import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.impl.QuestCompletedSellerBehaviour;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class UrzednikFadoNPC implements ZoneConfigurator {

	public static final int BUYING_PRICE = 400;
 	private static final String QUEST_SLOT = "fado_badge";

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildUrzędnik(zone);
	}

	private void buildUrzędnik(final StendhalRPZone zone) {
		final SpeakerNPC Urzędnik = new SpeakerNPC("Urzędnik Fado") {

			@Override
			protected void createPath() {
				// NPC doesn't move
				setPath(null);
			}

			@Override
			protected void createDialog() {

				final Map<String, Integer> items = new HashMap<String, Integer>();
				items.put("fado city scroll", BUYING_PRICE);

				new SellerAdder().addSeller(this, new QuestCompletedSellerBehaviour(QUEST_SLOT, "I only sell city teleport scrolls to recognized citizens of Fado.", items));
				// for quest see games.stendhal.server.maps.quest.HelpMrsYeti
				addGreeting("Dzień dobry. Witaj w mieście Fado.");
				addJob("We currently have no official administrative tasks. Citizens of Fado often need assistance, you should ask around for a #quest.");
				addHelp("Miasto Fado znajduje się na południowym-wschodzie Faumonii zaraz za rzeką.");				
				addGoodbye();
			}
		};

		Urzędnik.setEntityClass("urzednikfadonpc");
		Urzędnik.setDescription("Oto Urzędnik Fado. Powinieneś z nim porozmawiać i zapytać o zadanie.");
		Urzędnik.setPosition(12, 24);
		Urzędnik.initHP(100);
		zone.add(Urzędnik);
	}
}
