/* $Id: ElementalDagger2.java,v 1.58 2012/04/24 17:01:18 kymara Exp $ */
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
package games.stendhal.server.maps.quests;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.QuestStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * QUEST: The immortal sword forging.
 * 
 * PARTICIPANTS:
 * <ul>
 * <li> Vulcanus, son of Zeus itself, will forge for you the god's sword.
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Vulcanus tells you about the sword.
 * <li> He offers to forge a immortal sword for you if you bring him what it
 * needs.
 * <li> You give him all what he ask you.
 * <li> He tells you you must have killed a giant to get the shield
 * <li> Vulcanus forges the immortal sword for you
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li> immortal sword
 * <li>15000 XP
 * </ul>
 * 
 * 
 * REPETITIONS:
 * <ul>
 * <li> None.
 * </ul>
 */
public class ElementalDagger2 extends AbstractQuest {
	private static final int REQUIRED_IRON = 50;

	private static final int REQUIRED_GOLD_BAR = 1;

	private static final int REQUIRED_WOOD = 30;

	private static final int REQUIRED_GIANT_HEART = 1;

	private static final int REQUIRED_MINUTES = 180;

	private static final String QUEST_SLOT = "upgelementalsword_quest";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Wodniczka");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
						raiser.say("I specialize in upgrading elemental daggers. Are you interested?");
					} else if (player.isQuestCompleted(QUEST_SLOT)) {
						raiser.say("You have already upgraded your dagger...");
						raiser.setCurrentState(ConversationStates.ATTENDING);
					} else {
						raiser.say("Why do you disturb me before completing the task?");
						raiser.setCurrentState(ConversationStates.ATTENDING);
					}
				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES, null,
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("I will require several items: "
						+ REQUIRED_IRON
						+ " iron, "
						+ REQUIRED_WOOD
						+ " wood, "
						+ REQUIRED_GOLD_BAR
						+ " elemental dagger, and "
						+ REQUIRED_GIANT_HEART
						+ " #emerald. Return when you have them in #order! If you forget, just ask to #remind.");
					player.setQuest(QUEST_SLOT, "start;0;0;0;0");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"Oh, you truly do not desire an upgraded weapon of the elements?",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));

		npc.addReply(Arrays.asList("exact", "order", "dokładnie"),
			"The water spirits require perfect order...");
		npc.addReply(Arrays.asList("emerald", "emeralds", "emeraldów", "emeraldy"),
			"Emeralds are necessary to channel pure energy into the blade.");
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Wodniczka");

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestStateStartsWithCondition(QUEST_SLOT, "start")),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");

					int neededIron = REQUIRED_IRON
							- Integer.parseInt(tokens[1]);
					int neededWoodLogs = REQUIRED_WOOD
							- Integer.parseInt(tokens[2]);
					int neededGoldBars = REQUIRED_GOLD_BAR
							- Integer.parseInt(tokens[3]);
					int neededGiantHearts = REQUIRED_GIANT_HEART
							- Integer.parseInt(tokens[4]);
					boolean missingSomething = false;

					if (!missingSomething && (neededIron > 0)) {
						if (player.isEquipped("iron", neededIron)) {
							player.drop("iron", neededIron);
							neededIron = 0;
						} else {
							final int amount = player.getNumberOfEquipped("iron");
							if (amount > 0) {
								player.drop("iron", amount);
								neededIron -= amount;
							}

							raiser.say("I cannot forge without "
								+ Grammar.quantityplnoun(
										neededIron, "iron", "")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededWoodLogs > 0)) {
						if (player.isEquipped("wood", neededWoodLogs)) {
							player.drop("wood", neededWoodLogs);
							neededWoodLogs = 0;
						} else {
							final int amount = player.getNumberOfEquipped("wood");
							if (amount > 0) {
								player.drop("wood", amount);
								neededWoodLogs -= amount;
							}

							raiser.say("How can I forge without "
								+ Grammar.quantityplnoun(neededWoodLogs, "wood","")
								+ " for the fire?");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGoldBars > 0)) {
						if (player.isEquipped("elemental dagger", neededGoldBars)) {
							player.drop("elemental dagger", neededGoldBars);
							neededGoldBars = 0;
						} else {
							final int amount = player.getNumberOfEquipped("elemental dagger");
							if (amount > 0) {
								player.drop("elemental dagger", amount);
								neededGoldBars -= amount;
							}
							raiser.say("I need the base "
									+ Grammar.quantityplnoun(neededGoldBars, "elemental dagger","one") + " first.");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGiantHearts > 0)) {
						if (player.isEquipped("emerald", neededGiantHearts)) {
							player.drop("emerald", neededGiantHearts);
							neededGiantHearts = 0;
						} else {
							final int amount = player.getNumberOfEquipped("emerald");
							if (amount > 0) {
								player.drop("emerald", amount);
								neededGiantHearts -= amount;
							}
							raiser.say("I need an emerald to infuse power into the dagger. "
								+ Grammar.quantityplnoun(neededGiantHearts, "emerald","one") + " still needed.");
							missingSomething = true;
						}
					}

					if (player.hasKilled("green dragon") && !missingSomething) {
						raiser.say("You brought everything! I shall begin upgrading your dagger. Return in "
							+ REQUIRED_MINUTES
							+ " minutes, and it will be ready.");
						player.setQuest(QUEST_SLOT, "forging;" + System.currentTimeMillis());
					} else {
						if (!player.hasKilled("green dragon") && !missingSomething) {
							raiser.say("You must slay a green dragon before I upgrade your dagger!");
						}

						player.setQuest(QUEST_SLOT,
							"start;"
							+ (REQUIRED_IRON - neededIron)
							+ ";"
							+ (REQUIRED_WOOD - neededWoodLogs)
							+ ";"
							+ (REQUIRED_GOLD_BAR - neededGoldBars)
							+ ";"
							+ (REQUIRED_GIANT_HEART - neededGiantHearts));
					}
				}
			});

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestStateStartsWithCondition(QUEST_SLOT, "forging;")),
			ConversationStates.IDLE, null, new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {

					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");
					
					final long delay = REQUIRED_MINUTES * TimeUtil.MILLISECONDS_IN_MINUTE; 
					final long timeRemaining = (Long.parseLong(tokens[1]) + delay)
							- System.currentTimeMillis();

					if (timeRemaining > 0L) {
						raiser.say("I have not finished upgrading the dagger yet. Return in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ ".");
						return;
					}

					raiser.say("I have finished upgrading your weapon. Behold!");
					player.addXP(10000);
					player.addKarma(20);
					final Item magicSword = SingletonRepository.getEntityManager().getItem("upgraded elemental dagger");
					magicSword.setBoundTo(player.getName());
					player.equipOrPutOnGround(magicSword);
					player.notifyWorldAboutChanges();
					player.setQuest(QUEST_SLOT, "done");
				}
			});

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("remind", "missing", "list", "wykuj", "brakuje", "lista", "przypomnij"), 
			new QuestStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");

					final int neededIron = REQUIRED_IRON
							- Integer.parseInt(tokens[1]);
					final int neededWoodLogs = REQUIRED_WOOD
							- Integer.parseInt(tokens[2]);
					final int neededGoldBars = REQUIRED_GOLD_BAR
							- Integer.parseInt(tokens[3]);
					final int neededGiantHearts = REQUIRED_GIANT_HEART
							- Integer.parseInt(tokens[4]);

					raiser.say("I will need " + neededIron + " #iron, "
							+ neededWoodLogs + " #wood, "
							+ neededGoldBars + " #elemental #dagger and "
							+ neededGiantHearts + " #emerald");
				}
			});
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Upgraded Elemental Dagger",
				"Wodniczka can upgrade your elemental dagger.",
				false);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public String getName() {
		return "ElementalDagger2";
	}
	
	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("I met Wodniczka in Ados.");
			if (questState.equals("rejected")) {
				res.add("I did not want an upgraded elemental dagger.");
				return res;
			} 
			res.add("To upgrade the elemental dagger, Wodniczka requires: " + REQUIRED_IRON
					+ " iron, "
					+ REQUIRED_WOOD
					+ " wood, "
					+ REQUIRED_GOLD_BAR
					+ " elemental dagger, and "
					+ REQUIRED_GIANT_HEART
					+ " emerald, in exact order.");
			if (questState.startsWith("forging")) {
				res.add("Wodniczka is upgrading my elemental dagger.");
			} 
			if (isCompleted(player)) {
				res.add("I was rewarded with an upgraded elemental dagger.");
			}
			return res;	
	}

 	// match to the min level of the immortal sword
	@Override
	public int getMinLevel() {
		return 50;
	}

	@Override
	public String getNPCName() {
		return "Wodniczka";
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}
}
