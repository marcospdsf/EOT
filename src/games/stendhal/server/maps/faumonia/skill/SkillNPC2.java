/* $Id: SkillNPC2.java,v 1.25 2013/04/26 21:37:11 kiheru Exp $ */
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
package games.stendhal.server.maps.faumonia.skill ;

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

public class SkillNPC2 implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Kelogszy") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting(null, new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						String reply = "Pomagam wojownikom Faumonii w walce z siłami ciemności, wzmacniając ich umiejętności.";

						if (player.getLevel() < 70) {
							reply += " Jesteś za słaby!";
						} else {
							reply += " Widzę że jesteś wystarczająco silny! Pomóż mi oczyścić ten świat ze zła!";
						}
						raiser.say(reply);
					}
				});

				addReply("pomidorowa",
						"Też całkiem dobra zupa");
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
				if (!player.hasQuest("KelogszyReward")
						&& (player.getLevel() >= 70)) {
					player.setQuest("KelogszyReward", "done");

					player.setAtkXP(25000 + player.getAtkXP());
					player.setDefXP(30000 + player.getDefXP());
					player.addXP(100000);

					player.incAtkXP();
					player.incDefXP();
				}

				if (!player.hasQuest("KelogszyFirstChat")) {
					player.setQuest("KelogszyFirstChat", "done");
					((SpeakerNPC) raiser.getEntity()).listenTo(player, "hi");
				}
				
			}
			
		});

		npc.setEntityClass("manwithhatnpc");
		npc.setDescription("Oto Kelogszy. Jest Wysokim Kapłanem, który pomaga wojownikom chronić Faiumonię przy pomocy swoich magicznych zdolności.");
		npc.setPosition(117, 109);
		npc.setDirection(Direction.LEFT);
		npc.initHP(85);
		zone.add(npc);
	}
}
