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
 //Zrobiony na podstawie GateKeeperNPC z Sedah city 
 
package games.stendhal.server.maps.pol.zakopane.church;

import games.stendhal.common.Rand;
import games.stendhal.common.grammar.ItemParserResult;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.BehaviourAction;
import games.stendhal.server.entity.npc.behaviour.impl.Behaviour;
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds a gatekeeper NPC Bribe him with at least 300 money to get the key for
 * the Sedah city walls. He stands in the doorway of the gatehouse till the
 * interior is made.
 *
 * @author kymara
 */
public class WikaryNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param zone
	 *            The zone to be configured.
	 * @param attributes
	 *            Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Wikary") {

			@Override
			protected void createPath() {
				List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(5, 12));
				nodes.add(new Node(18, 12));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting(null, new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						if (player.isEquipped("mega potion")) {
							// give some money to get
							// giant potion
							if (Rand.throwCoin() == 1) {
								raiser.say("I can see you need more.");
							} else {
								raiser.say("Welcome Back.");
							}
						} else {
							raiser.say("Welcome! Have you come here to make a donation?");
						}
					}
				});

				addReply("nic", "Dobrze.");
				addJob("I am a priest in Zakopane. I am collecting money to buy organs for the chapel, ask me for an #offer to find out how I can reward you for this gesture. ");
				addHelp("Support the organ purchase for the chapel by saying #donate #<quantity> #money or lift the curse on yourself by saying #remove ");
				addQuest("The only thing I need is a #donation for the organ for the chapel. Besides, for a fee, I can #remove the stigma of a killer off you.");
				addOffer("I will give you a potion to heal you in exchange for a small donation to buy the organ or I will remove the skull curse off you for an appropriate price .");

				addReply("donation", null,
					new BehaviourAction(new Behaviour("money"), "charity", "offer") {
					@Override
					public void fireSentenceError(Player player, Sentence sentence, EventRaiser raiser) {
						raiser.say(sentence.getErrorString() + " Are you trying to cheat me?");
					}

					@Override
						public void fireRequestOK(final ItemParserResult res, final Player player, final Sentence sentence, final EventRaiser raiser) {
						final int amount = res.getAmount();

						if (sentence.getExpressions().size() == 1) {
							// player only said 'datek'
							raiser.say("You don't have enough money to make a donation. Come back another time.");
						} else {
							if (amount < 4000) {
								// Less than 4000 is not money for him
								raiser.say("You don't have enough money to make a donation. Come back another time.");
							} else {
								if (player.isEquipped("money", amount)) {
									player.drop("money", amount);
									raiser.say("God bless you. We are getting closer to buying new organs!");
									final Item drink = SingletonRepository.getEntityManager().getItem(
											"mega potion");
									player.equipOrPutOnGround(drink);
								} else {
									// player gave enough but doesn't have
									// the cash
									raiser.say("You do not have " + amount + " money to help support organ purchases. Come back another time.");
								}
							}
						}
					}

					@Override
					public void fireRequestError(final ItemParserResult res, final Player player, final Sentence sentence, final EventRaiser raiser) {
						if (res.getChosenItemName() == null) {
							fireRequestOK(res, player, sentence, raiser);
						} else {
							// This bit is just in case the player says 'datek X potatoes', not money
							raiser.say("I need money to buy organs.");
						}
					}
				});

				add(ConversationStates.ATTENDING, Arrays.asList("remove", "zdejmij"), null,
					ConversationStates.ATTENDING, null, new ChatAction() {
						public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
							int price = 1000;
							if (player.getLevel() < 10) {
								price = 1000;
							} else if (player.getLevel() >= 10 && player.getLevel() < 250) {
								price = 10000;
							} else if (player.getLevel() >= 250 && player.getLevel() < 500) {
								price = 250000;
							} else {
								price = 500000;
							}

							raiser.say("If you want to remove the stigma of a killer, you have to pay."
											+ price
											+ " money. Do you wish to pay? ");
							raiser.setCurrentState(ConversationStates.SERVICE_OFFERED);
						}
					});

				add(ConversationStates.SERVICE_OFFERED,
					ConversationPhrases.YES_MESSAGES, null,
					ConversationStates.ATTENDING, null, new ChatAction() {
						public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
							int price = 1000;
							if (player.getLevel() < 10) {
								price = 1000;
							} else if (player.getLevel() >= 10 && player.getLevel() < 250) {
								price = 10000;
							} else if (player.getLevel() >= 250 && player.getLevel() < 500) {
								price = 250000;
							} else {
								price = 500000;
							}

							if (player.drop("money", price)) {
								player.rehabilitate();
								raiser.say("I took the stigma of a killer off you. Look after yourself!");
							} else {
								raiser.say("You don't have enough money to remove the stigma of a killer!");
							}
						}
					});

				add(ConversationStates.SERVICE_OFFERED,
						ConversationPhrases.NO_MESSAGES, null,
						ConversationStates.ATTENDING,
						"Nie wiesz co tracisz.", null);

				addGoodbye("Bywaj!");
			}
		};

		npc.setDescription("Here is the Priest. He collects money to buy organs for the chapel.");
		/*
		 * We don't seem to be using the recruiter images that lenocas made for
		 * the Fado Raid area so I'm going to put him to use here. If the raid
		 * part ever gets done, this image can change.
		 */
		npc.setEntityClass("npcwikary");
		npc.setPosition(5, 12);
		npc.initHP(100);
		zone.add(npc);
	}
}
