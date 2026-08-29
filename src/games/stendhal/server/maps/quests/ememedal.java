/* $Id: ElementalDagger1.java,v 1.58 2012/04/24 17:01:18 kymara/ edited by szyg Exp $ */
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
public class ememedal extends AbstractQuest {
	private static final int REQUIRED_IRON = 1;

	private static final int REQUIRED_GOLD_BAR = 10;

	private static final int REQUIRED_WOOD = 15;

	private static final int REQUIRED_GIANT_HEART = 20;

	private static final int REQUIRED_MINUTES = 120;

	private static final String QUEST_SLOT = "emeraldmedal_quest";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Trener rycerzy");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
						raiser.say("Our garrison is in need of military supplies. Are you willing to assist us?");
					} else if (player.isQuestCompleted(QUEST_SLOT)) {
						raiser.say("You have already delivered what our forces needed.");
						raiser.setCurrentState(ConversationStates.ATTENDING);
					} else {
						raiser.say("Why are you bothering me then?");
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
					raiser.say("I will require several supplies: "
						+ REQUIRED_IRON
						+ " green dragon claw, "
						+ REQUIRED_WOOD
						+ " gold bars, "
						+ REQUIRED_GOLD_BAR
						+ " iron, and "
						+ REQUIRED_GIANT_HEART
						+ " giant hearts. Return once you have gathered them! Say #remind if you forget.");
					player.setQuest(QUEST_SLOT, "start;0;0;0;0");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"Oh, you truly do not wish to help our knights?",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Trener Rycerzy");

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
						if (player.isEquipped("green dragon claw", neededIron)) {
							player.drop("green dragon claw", neededIron);
							neededIron = 0;
						} else {
							final int amount = player.getNumberOfEquipped("green dragon claw");
							if (amount > 0) {
								player.drop("green dragon claw", amount);
								neededIron -= amount;
							}

							raiser.say("We still need "
								+ Grammar.quantityplnoun(
										neededIron, "green dragon claw", "")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededWoodLogs > 0)) {
						if (player.isEquipped("gold bar", neededWoodLogs)) {
							player.drop("gold bar", neededWoodLogs);
							neededWoodLogs = 0;
						} else {
							final int amount = player.getNumberOfEquipped("gold bar");
							if (amount > 0) {
								player.drop("gold bar", amount);
								neededWoodLogs -= amount;
							}

							raiser.say("We also require "
								+ Grammar.quantityplnoun(neededWoodLogs, "gold bar","")
								+ " for soldiers' weapons.");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGoldBars > 0)) {
						if (player.isEquipped("iron", neededGoldBars)) {
							player.drop("iron", neededGoldBars);
							neededGoldBars = 0;
						} else {
							final int amount = player.getNumberOfEquipped("iron");
							if (amount > 0) {
								player.drop("iron", amount);
								neededGoldBars -= amount;
							}
							raiser.say("We need armor materials. Bring "
									+ Grammar.quantityplnoun(neededGoldBars, "iron","one") + " more.");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGiantHearts > 0)) {
						if (player.isEquipped("giant heart", neededGiantHearts)) {
							player.drop("giant heart", neededGiantHearts);
							neededGiantHearts = 0;
						} else {
							final int amount = player.getNumberOfEquipped("giant heart");
							if (amount > 0) {
								player.drop("giant heart", amount);
								neededGiantHearts -= amount;
							}
							raiser.say("We need giant hearts to brew potent combat draughts. We still need "
								+ Grammar.quantityplnoun(neededGiantHearts, "giant heart","one") + ".");
							missingSomething = true;
						}
					}

					if (player.hasKilled("green dragon") && !missingSomething) {
						raiser.say("You have brought all required supplies! Your medal is being forged. Return in "
							+ REQUIRED_MINUTES
							+ " minutes and it will be ready.");
						player.setQuest(QUEST_SLOT, "forging;" + System.currentTimeMillis());
					} else {
						if (!player.hasKilled("green dragon") && !missingSomething) {
							raiser.say("Before you receive your decoration, you must slay the green dragon to prove the claw is authentic!");
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
						raiser.say("Your medal is still being forged. Please return in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ ".");
						return;
					}

					raiser.say("Here is your distinguished medal, noble champion!");
					player.addXP(1000000);
					player.setAtkXP(15000 + player.getAtkXP());
					player.setDefXP(20000 + player.getDefXP());
					
					player.incAtkXP();
					player.incDefXP();
					player.addKarma(100);
					final Item magicSword = SingletonRepository.getEntityManager().getItem("emerald medal");
					magicSword.setBoundTo(player.getName());
					player.equipOrPutOnGround(magicSword);
					player.notifyWorldAboutChanges();
					player.setQuest(QUEST_SLOT, "done");
				}
			});

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("decoration", "missing", "medal", "remind", "list", "przypomnij"), 
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

					raiser.say("I still require " + neededIron + " green dragon claw, "
							+ neededWoodLogs + " gold bars, "
							+ neededGoldBars + " iron, and "
							+ neededGiantHearts + " giant hearts.");
				}
			});

		npc.add(
			ConversationStates.ANY,
			"iron",
			null,
			ConversationStates.ATTENDING,
			"Mine iron ore from mineral-rich underground veins.",
			null);

		npc.add(ConversationStates.ANY, "green dragon claw", null,
				ConversationStates.ATTENDING,
				"Tear it from the ferocious foot of the green dragon.", null);
		npc.add(ConversationStates.ANY, "gold", null,
				ConversationStates.ATTENDING,
				"The blacksmith in Ados can smelt gold nuggets into bars of pure gold.",
				null);
		npc.add(
			ConversationStates.ANY,
			Arrays.asList("giant","olbrzyma", "olbrzym"),
			null,
			ConversationStates.ATTENDING,
			"Ancient legends speak of mighty giants dwelling in the mountains north of Semos and Ados.",
			null);
	}

	@Override
	public void addToWorld() {
		//super.addToWorld();
		fillQuestInfo(
				"Supplies for the Knight Trainer",
				"The Knight Trainer in Ados keep wants you to prove your valor.",
				false);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public String getName() {
		return "ememedal";
	}
	
	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("I met the Knight Trainer in Ados keep.");
			if (questState.equals("rejected")) {
				res.add("I declined to assist the Knight Trainer.");
				return res;
			} 
			res.add("I must bring the Knight Trainer: " + REQUIRED_IRON
					+ " green dragon claw, "
					+ REQUIRED_WOOD
					+ " gold bars, "
					+ REQUIRED_GOLD_BAR
					+ " iron, and "
					+ REQUIRED_GIANT_HEART
					+ " giant hearts.");
			// yes, yes. this is the most horrible quest code and so you get a horrible quest history. 
			if(questState.startsWith("start") && !"start;15;26;12;6".equals(questState)){
				res.add("I have not delivered everything yet. The Knight Trainer can tell me what is still needed.");
			} else if ("start;15;26;12;6".equals(questState) || !questState.startsWith("start")) {
				res.add("I delivered all required supplies for the Knight Trainer.");
			}
			if("start;15;26;12;6".equals(questState) && !player.hasKilled("green dragon")){
				res.add("To earn the medal I must slay the green dragon.");
			} 
			if (questState.startsWith("forging")) {
				res.add("I must wait for my medal to be finished.");
			} 
			if (isCompleted(player)) {
				res.add("I helped the Knight Trainer and received my emerald medal.");
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
		return "Trener Rycerzy";
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}
}
