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
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						if (player.isEquipped("mega potion")) {
							if (Rand.throwCoin() == 1) {
								raiser.say("I see you seek further blessings.");
							} else {
								raiser.say("Welcome back.");
							}
						} else {
							raiser.say("Greetings! Have you come to make a holy donation to our chapel?");
						}
					}
				});

				addReply(Arrays.asList("nothing", "nic"), "As you wish.");
				addJob("I am the vicar of Zakopane. I am raising funds to purchase a proper pipe organ for our chapel. Ask me for an #offer to see how you may be rewarded for your generosity.");
				addHelp("Support the chapel's organ fund by saying #donation #<amount> #money, or cleanse a murderer's mark by saying #absolve.");
				addQuest("The only worldly need I have is a #donation for the chapel organ. Furthermore, for a modest fee, I can #absolve you of a murderer's mark.");
				addOffer("I will grant you a powerful healing elixir in exchange for a generous #donation to the organ fund, or #absolve you of a PK skull curse for the appropriate penance.");

				addReply(Arrays.asList("donation", "datek"), null,
					new BehaviourAction(new Behaviour("money"), "charity", "offer") {
					@Override
					public void fireSentenceError(Player player, Sentence sentence, EventRaiser raiser) {
						raiser.say(sentence.getErrorString() + " Are you trying to deceive me?");
					}

					@Override
						public void fireRequestOK(final ItemParserResult res, final Player player, final Sentence sentence, final EventRaiser raiser) {
						final int amount = res.getAmount();

						if (sentence.getExpressions().size() == 1) {
							raiser.say("You do not have enough coin to support the chapel organ. Come back another time.");
						} else {
							if (amount < 4000) {
								raiser.say("A minimum donation of 4000 gold is required to support the organ fund. Come back another time.");
							} else {
								if (player.isEquipped("money", amount)) {
									player.drop("money", amount);
									raiser.say("May heaven bless your generosity! We are one step closer to our new chapel organ.");
									final Item drink = SingletonRepository.getEntityManager().getItem(
											"mega potion");
									player.equipOrPutOnGround(drink);
								} else {
									raiser.say("You do not have " + amount + " gold to support the chapel organ.");
								}
							}
						}
					}

					@Override
					public void fireRequestError(final ItemParserResult res, final Player player, final Sentence sentence, final EventRaiser raiser) {
						if (res.getChosenItemName() == null) {
							fireRequestOK(res, player, sentence, raiser);
						} else {
							raiser.say("I can only accept gold coin for the chapel organ fund.");
						}
					}
				});

				add(ConversationStates.ATTENDING, Arrays.asList("remove", "absolve", "cleanse", "zdejmij"), null,
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

							raiser.say("To cleanse the murderer's mark from your soul, you must offer "
											+ price
											+ " gold in penance. Would you like to pay?");
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
								raiser.say("The mark of the slayer has been lifted from you. Go in peace, and sin no more!");
							} else {
								raiser.say("You do not have enough coin to pay the penance!");
							}
						}
					});

				add(ConversationStates.SERVICE_OFFERED,
						ConversationPhrases.NO_MESSAGES, null,
						ConversationStates.ATTENDING,
						"As you wish.", null);

				addGoodbye("Go with peace!");
			}
		};

		npc.setDescription("You see the Vicar of Zakopane, collecting charitable donations for the chapel.");
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
