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
package games.stendhal.server.maps.pol.krakow.ship;

import games.stendhal.common.Direction;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.pol.krakow.ship.KrakowFerry.Status;

import java.util.Arrays;
import java.util.Map;

/** Factory for cargo worker on Athor Ferry. */

public class CoastConveyerNPC implements ZoneConfigurator  {

	@Override
	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
		buildNPC(zone);
	}
	private static StendhalRPZone islandDocksZone;
	private static StendhalRPZone mainlandDocksZone;

	private StendhalRPZone getIslandDockZone() {
		if (islandDocksZone == null) {
			islandDocksZone = SingletonRepository.getRPWorld().getZone("0_krakow_wisla_e");
		}

		return islandDocksZone;
	}

	private Status ferryState;

	private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Maryna") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			public void createDialog() {
				addGoodbye("Goodbye!");
				addGreeting("Ahoy my friend! How can I help you?");
				addHelp("Yes, you can go down by saying #disembark, but only when we dock at the marina. Ask me for #status if you have no idea where we are .");
				addJob("I am taking passengers who want to go ashore.");

				add(ConversationStates.ATTENDING, "status",
						null,
						ConversationStates.ATTENDING,
						null,
						new ChatAction() {
							@Override
							public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
								npc.say(ferryState.toString());
							}
						});

				add(ConversationStates.ATTENDING,
						Arrays.asList("disembark", "leave", "zejdź", "opuść"),
						null,
						ConversationStates.ATTENDING,
						null,
						new ChatAction() {
							@Override
							public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
								switch (ferryState) {
									case ANCHORED_AT_WARSZAWA:
										npc.say("Do you want me to take you to Warsaw?");
										npc.setCurrentState(ConversationStates.SERVICE_OFFERED);
										break;
									case ANCHORED_AT_KRAKOW:
										npc.say("Do you want me to take you to Krakow?");
										npc.setCurrentState(ConversationStates.SERVICE_OFFERED);
										break;

									default:
										npc.say(ferryState.toString()
											+ " You can go ashore while we are moored at the marina.");
								}
							}
						});

				add(ConversationStates.SERVICE_OFFERED,
						ConversationPhrases.YES_MESSAGES,
						null,
						ConversationStates.ATTENDING, null,
						new ChatAction() {
							@Override
							public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
								switch (ferryState) {
									case ANCHORED_AT_WARSZAWA:
										player.teleport(getMainlandDocksZone(), 3, 63, Direction.LEFT, null);
										npc.setCurrentState(ConversationStates.IDLE);
										break;
									case ANCHORED_AT_KRAKOW:
										player.teleport(getIslandDockZone(), 12, 94, Direction.LEFT, null);
										npc.setCurrentState(ConversationStates.IDLE);
										break;
									default:
										npc.say("Not good! The ship has already left.");
								}
							}
						});

				add(ConversationStates.SERVICE_OFFERED,
						ConversationPhrases.NO_MESSAGES,
						null,
						ConversationStates.ATTENDING,
						"Aye my friend!", null);
			}
		};

		new KrakowFerry.FerryListener() {

			@Override
			public void onNewFerryState(final Status status) {
				ferryState = status;
				switch (status) {
					case ANCHORED_AT_WARSZAWA:
						npc.say("NOTE: The ferry reached Warsaw! You can go down now by saying #disembark or #leave.");
						break;
					case ANCHORED_AT_KRAKOW:
						npc.say("NOTE: The ferry reached Krakow! You can go down now by saying #disembark or #leave.");
						break;
					default:
						npc.say("NOTE: The ferry has departed.");
						break;
				}
			}
		};

		npc.setPosition(28, 34);
		npc.setEntityClass("pirate_sailor2npc");
		npc.setDirection(Direction.LEFT);
		zone.add(npc);
	}

	private static StendhalRPZone getMainlandDocksZone() {
		if (mainlandDocksZone == null) {
			mainlandDocksZone = SingletonRepository.getRPWorld().getZone("0_warszawa_ne");
		}

		return mainlandDocksZone;
	}
}
