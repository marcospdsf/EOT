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
import games.stendhal.server.maps.pol.krakow.ship.KrakowFerry;
import games.stendhal.server.maps.pol.krakow.ship.KrakowFerry.Status;

import java.util.Arrays;
import java.util.Map;

/**
 * Factory for an NPC who brings players from the docks to Krakow Ferry in a
 * rowing boat.
 */
public class FerryConveyerNPC implements ZoneConfigurator  {

	@Override
	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
		buildNPC(zone);
	}

	protected Status ferrystate;
	private static StendhalRPZone shipZone;

	public static StendhalRPZone getShipZone() {
		if (shipZone == null) {
			shipZone = SingletonRepository.getRPWorld().getZone("int_polish_ship");
		}
		return shipZone;
	}

	private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Janek") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			public void createDialog() {
				addGoodbye("Farewell!");
				addGreeting("Welcome to the Kraków #ferry service! How can I #help you?");
				addHelp("You can #board the ferry for "
						+ KrakowFerry.PRICE
						+ " gold, but only when it is docked at the pier. Ask me for #status if you want to know where the ferry is.");
				addJob("When passengers wish to #board the #ferry to Warszawa, I ferry them to the ship.");
				addReply(
						Arrays.asList("ferry", "prom"),
						"The ferry sails regularly between Kraków and Warszawa. You may only #board when the ship is anchored here. Ask me for #status to see where it currently is.");
				add(ConversationStates.ATTENDING, "status", null,
						ConversationStates.ATTENDING, null, new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						npc.say(ferrystate.toString());
					}
				});

				add(ConversationStates.ATTENDING, Arrays.asList("board", "wejdź", "wejść"), null,
						ConversationStates.ATTENDING, null, new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						if (ferrystate == Status.ANCHORED_AT_KRAKOW) {
							npc.say("To board the ferry, you must pay "
									+ KrakowFerry.PRICE
									+ " gold. Would you like to pay?");
							npc.setCurrentState(ConversationStates.SERVICE_OFFERED);
						} else {
							npc.say(ferrystate.toString()
									+ " You can only board when the ferry is docked in Kraków.");
						}
					}
				});

				add(ConversationStates.SERVICE_OFFERED,
					ConversationPhrases.YES_MESSAGES, null,
					ConversationStates.ATTENDING, null, new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						if (player.drop("money", KrakowFerry.PRICE)) {
							player.teleport(getShipZone(), 26, 34, Direction.LEFT, null);

						} else {
							npc.say("Hey! You don't have enough money for the fare!");
						}
					}
				});

				add(ConversationStates.SERVICE_OFFERED,
					ConversationPhrases.NO_MESSAGES, null,
					ConversationStates.ATTENDING,
					"You don't know what you're missing, landlubber!", null);

			}
		};

		new KrakowFerry.FerryListener() {

			@Override
			public void onNewFerryState(final Status status) {
				ferrystate = status;
				switch (status) {
					case ANCHORED_AT_KRAKOW:
						npc.say("Attention: The ferry has arrived at the dock! You may now #board the ship.");
						break;
					case DRIVING_TO_WARSZAWA:
						npc.say("Attention: The ferry has departed. Boarding is now closed.");
						break;
					default:
						break;
				}
			}
		};

		npc.setPosition(15, 93);
		npc.setEntityClass("npc_flisak2");
		npc.setDirection(Direction.LEFT);
		zone.add(npc);
	}
}
