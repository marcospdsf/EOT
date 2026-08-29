/* $Id: GoldApple.java,v 1.58 2012/04/24 17:01:18 kymara/edited by szyg Exp $ */
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
public class GoldApple extends AbstractQuest {
	private static final int REQUIRED_IRON = 10;

	private static final int REQUIRED_GOLD_BAR = 10;

	private static final int REQUIRED_WOOD = 10;

	private static final int REQUIRED_GIANT_HEART = 1;

	private static final int REQUIRED_MINUTES = 300;

	private static final String QUEST_SLOT = "goldapple_quest";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Tajemniczy Wędrowiec");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
						raiser.say("If you slay a green dragon and bring me the necessary ingredients, I can craft a golden apple for you.");
					} else if (player.isQuestCompleted(QUEST_SLOT)) {
						raiser.say("You have already received a golden apple...");
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
						+ " bear claws, "
						+ REQUIRED_WOOD
						+ " tiger claws, "
						+ REQUIRED_GOLD_BAR
						+ " wolf claws, and "
						+ REQUIRED_GIANT_HEART
						+ " apple. Return when you have them in #order! If you forget, just ask to #remind.");
					player.setQuest(QUEST_SLOT, "start;0;0;0;0");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"Oh, you truly do not desire a golden apple?",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));

		npc.addReply(Arrays.asList("exact", "order", "dokładnie"),
			"Order is paramount to transmutation...");
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Tajemniczy Wędrowiec");

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
						if (player.isEquipped("bear claws", neededIron)) {
							player.drop("bear claws", neededIron);
							neededIron = 0;
						} else {
							final int amount = player.getNumberOfEquipped("bear claws");
							if (amount > 0) {
								player.drop("bear claws", amount);
								neededIron -= amount;
							}

							raiser.say("I cannot prepare the apple without "
								+ Grammar.quantityplnoun(
										neededIron, "bear claws", "")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededWoodLogs > 0)) {
						if (player.isEquipped("tiger claws", neededWoodLogs)) {
							player.drop("tiger claws", neededWoodLogs);
							neededWoodLogs = 0;
						} else {
							final int amount = player.getNumberOfEquipped("tiger claws");
							if (amount > 0) {
								player.drop("tiger claws", amount);
								neededWoodLogs -= amount;
							}

							raiser.say("How can you ask for the apple without "
								+ Grammar.quantityplnoun(neededWoodLogs, "tiger claws","")
								+ " for me?");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGoldBars > 0)) {
						if (player.isEquipped("wolf claws", neededGoldBars)) {
							player.drop("wolf claws", neededGoldBars);
							neededGoldBars = 0;
						} else {
							final int amount = player.getNumberOfEquipped("wolf claws");
							if (amount > 0) {
								player.drop("wolf claws", amount);
								neededGoldBars -= amount;
							}
							raiser.say("Without wolf claws you cannot claim this apple. I need "
									+ Grammar.quantityplnoun(neededGoldBars, "wolf claws","one") + " more.");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGiantHearts > 0)) {
						if (player.isEquipped("apple", neededGiantHearts)) {
							player.drop("apple", neededGiantHearts);
							neededGiantHearts = 0;
						} else {
							final int amount = player.getNumberOfEquipped("apple");
							if (amount > 0) {
								player.drop("apple", amount);
								neededGiantHearts -= amount;
							}
							raiser.say("Without an apple you cannot create a golden apple, bring me "
								+ Grammar.quantityplnoun(neededGiantHearts, "apple","one") + " more.");
							missingSomething = true;
						}
					}

					if (player.hasKilled("green dragon") && !missingSomething) {
						raiser.say("You brought everything! I shall begin transmuting the apple. Return in "
							+ REQUIRED_MINUTES
							+ " minutes, and it will be ready.");
						player.setQuest(QUEST_SLOT, "forging;" + System.currentTimeMillis());
					} else {
						if (!player.hasKilled("green dragon") && !missingSomething) {
							raiser.say("First you must slay a green dragon!");
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
						raiser.say("I have not finished transmuting the apple yet. Return in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ ".");
						return;
					}

					raiser.say("It is finished! Behold the golden apple!");
					player.addXP(5000);
					
					player.setAtkXP(1000 + player.getAtkXP());
					player.setDefXP(1000 + player.getDefXP());
					

					player.addKarma(15);
					player.incAtkXP();
					player.incDefXP();
					final Item magicSword = SingletonRepository.getEntityManager().getItem("golden apple");
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

					raiser.say("I will need " + neededIron + " #bear #claws, "
							+ neededWoodLogs + " #tiger #claws, "
							+ neededGoldBars + " #wolf #claws, and "
							+ neededGiantHearts + " #apple");
				}
			});
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Golden Apple",
				"The Mysterious Traveler can transmute a legendary golden apple.",
				false);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public String getName() {
		return "GoldApple";
	}
	
	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("I met the Mysterious Traveler in the Ados caves.");
			if (questState.equals("rejected")) {
				res.add("I did not want a golden apple.");
				return res;
			} 
			res.add("To create a golden apple, the traveler requires: " + REQUIRED_IRON
					+ " bear claws, "
					+ REQUIRED_WOOD
					+ " tiger claws, "
					+ REQUIRED_GOLD_BAR
					+ " wolf claws, and "
					+ REQUIRED_GIANT_HEART
					+ " apple, in exact order.");
			if (questState.startsWith("forging")) {
				res.add("The traveler is transmuting the golden apple.");
			} 
			if (isCompleted(player)) {
				res.add("I was rewarded with a mystical golden apple.");
			}
			return res;	
	}

 	// match to the min level of the immortal sword
	@Override
	public int getMinLevel() {
		return 20;
	}

	@Override
	public String getNPCName() {
		return "Tajemniczy Wędrowiec";
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}
}
