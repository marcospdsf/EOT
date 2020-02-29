/* $Id: HighClericNPC.java,v 1.25 2013/04/26 21:37:11 kiheru Exp $ */
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
package games.stendhal.server.maps.polhale;

import games.stendhal.common.Direction;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;

import java.util.Map;

public class HighClericNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param zone
	 *            The zone to be configured.
	 * @param attributes
	 *            Configuration attributes.
	 */
	@Override
	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
	buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Tahaniea") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting(null, new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						String reply = "Pilnuje tego magicznego miejsca przed potworami!";

						if (player.getLevel() < 50) {
							reply += " Nie masz czego tu szukac! Uciekaj!";
						} else {
							reply += " Widze ze jestes wystarczajaco silny! Pomoz mi oczyscic ten swiat ze zla!";
						}
						raiser.say(reply);
					}
				});

				addReply("Szygolek",
						"programista serwera i ?wietny go?? :D");
				addGoodbye();
			}
			
			@Override
			protected void onGoodbye(RPEntity player) {
				setDirection(Direction.LEFT);
			}
		};


		npc.addInitChatMessage(null, new ChatAction() {
			@Override
			public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
				if (!player.hasQuest("TahanieaReward")
						&& (player.getLevel() >= 50)) {
					player.setQuest("TahanieaReward", "done");

					player.setAtkXP(45000 + player.getAtkXP());
					player.setDefXP(80000 + player.getDefXP());
					player.addXP(150000);

					player.incAtkXP();
					player.incDefXP();
				}

				if (!player.hasQuest("TahanieaFirstChat")) {
					player.setQuest("TahanieaFirstChat", "done");
					((SpeakerNPC) raiser.getEntity()).listenTo(player, "hi");
				}
				
			}
			
		});

		npc.setEntityClass("paperboynpc");
		npc.setDescription("Oto Tahaniea. Jest Wysokim Klerykiem, który próbuje chronić Faiumoni swoimi umiejętnościami magicznymi przed mrocznymi potworami.");
		npc.setPosition(57, 37);
		npc.setDirection(Direction.LEFT);
		npc.initHP(85);
		zone.add(npc);
	}
}
