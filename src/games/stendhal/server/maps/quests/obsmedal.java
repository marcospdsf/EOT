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
public class obsmedal extends AbstractQuest {
	private static final int REQUIRED_IRON = 1;

	private static final int REQUIRED_GOLD_BAR = 100;

	private static final int REQUIRED_WOOD = 1;

	private static final int REQUIRED_GIANT_HEART = 500;

	private static final int REQUIRED_MINUTES = 1440;

	private static final String QUEST_SLOT = "obsemedal_quest";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Major Klykh");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
						raiser.say("Our legion needs rare supplies. Are you willing to assist Major Klykh?");
					} else if (player.isQuestCompleted(QUEST_SLOT)) {
						raiser.say("You have already delivered what we required.");
						raiser.setCurrentState(ConversationStates.ATTENDING);
					} else {
						raiser.say("Why are you wasting my time?");
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
						+ " arctic dragon claw, "
						+ REQUIRED_WOOD
						+ " sapphire medal, "
						+ REQUIRED_GOLD_BAR
						+ " mithril bars, and "
						+ REQUIRED_GIANT_HEART
						+ " giant hearts. Return once you have them! Say #remind if you forget.");
					player.setQuest(QUEST_SLOT, "start;0;0;0;0");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"You refuse to assist the legion?",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Major Klykh");

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
						if (player.isEquipped("arctic dragon claw", neededIron) || player.isEquipped("arctic dragon claw", neededIron)) {
							player.drop("arctic dragon claw", neededIron);
							neededIron = 0;
						} else {
							final int amount = player.getNumberOfEquipped("arctic dragon claw") + player.getNumberOfEquipped("arctic dragon claw");
							if (amount > 0) {
								player.drop("arctic dragon claw", amount);
								neededIron -= amount;
							}

							raiser.say("We still need "
								+ Grammar.quantityplnoun(neededIron, "arctic dragon claw", "")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededWoodLogs > 0)) {
						if (player.isEquipped("sapphire medal", neededWoodLogs) || player.isEquipped("sapphire medal", neededWoodLogs)) {
							player.drop("sapphire medal", neededWoodLogs);
							neededWoodLogs = 0;
						} else {
							final int amount = player.getNumberOfEquipped("sapphire medal") + player.getNumberOfEquipped("sapphire medal");
							if (amount > 0) {
								player.drop("sapphire medal", amount);
								neededWoodLogs -= amount;
							}

							raiser.say("I cannot promote you without "
								+ Grammar.quantityplnoun(neededWoodLogs, "sapphire medal","")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGoldBars > 0)) {
						if (player.isEquipped("mithril bar", neededGoldBars)) {
							player.drop("mithril bar", neededGoldBars);
							neededGoldBars = 0;
						} else {
							final int amount = player.getNumberOfEquipped("mithril bar");
							if (amount > 0) {
								player.drop("mithril bar", amount);
								neededGoldBars -= amount;
							}
							raiser.say("We need armor materials. Bring "
									+ Grammar.quantityplnoun(neededGoldBars, "mithril bar","one") + " more.");
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
							raiser.say("We need giant hearts for alchemical infusions. Bring "
								+ Grammar.quantityplnoun(neededGiantHearts, "giant heart","one") + " more.");
							missingSomething = true;
						}
					}

					if (player.hasKilled("Wawel Dragon") || player.hasKilled("Wawel Dragon") && !missingSomething) {
						raiser.say("You brought all required materials! Your diamond medal is being struck. Return in "
							+ REQUIRED_MINUTES
							+ " minutes and it will be ready.");
						player.setQuest(QUEST_SLOT, "forging;" + System.currentTimeMillis());
					} else {
						if (!player.hasKilled("Wawel Dragon") || player.hasKilled("Wawel Dragon") && !missingSomething) {
							raiser.say("Before you receive your medal, defeat the legendary Wawel Dragon!");
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
						raiser.say("Your diamond medal is not yet ready. Please return in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ ".");
						return;
					}

					raiser.say("Here is your diamond medal, legend of the realm!");
					player.addXP(10000000);
					player.setAtkXP(100000 + player.getAtkXP());
					player.setDefXP(150000 + player.getDefXP());
					
					player.incAtkXP();
					player.incDefXP();
					player.addKarma(500);
					final Item magicSword = SingletonRepository.getEntityManager().getItem("diamond medal");
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

					raiser.say("I will require " + neededIron + " arctic dragon claw, "
							+ neededWoodLogs + " sapphire medal, "
							+ neededGoldBars + " mithril bars, and "
							+ neededGiantHearts + " giant hearts.");
				}
			});

		npc.add(
			ConversationStates.ANY,
			"arctic dragon claw",
			null,
			ConversationStates.ATTENDING,
			"Bring an arctic dragon claw to prove your great strength.",
			null);

		npc.add(ConversationStates.ANY, "sapphire medal", null,
				ConversationStates.ATTENDING,
				"Bring a sapphire medal to prove you have assisted the Mage Trainer.", null);
		npc.add(ConversationStates.ANY, "mithril", null,
				ConversationStates.ATTENDING,
				"The blacksmith in Ados can smelt mithril nuggets into mithril bars.",
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
				"Supplies for Major Klykh",
				"Major Klykh stationed in Ados fortress needs rare supplies.",
				false);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public String getName() {
		return "obsmedal";
	}
	
	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("I met Major Klykh in Ados fortress.");
			if (questState.equals("rejected")) {
				res.add("I declined to help Major Klykh.");
				return res;
			} 
			res.add("Major Klykh asked me to bring: " + REQUIRED_IRON
					+ " arctic dragon claw, "
					+ REQUIRED_WOOD
					+ " sapphire medal, "
					+ REQUIRED_GOLD_BAR
					+ " mithril bars, and "
					+ REQUIRED_GIANT_HEART
					+ " giant hearts.");
			// yes, yes. this is the most horrible quest code and so you get a horrible quest history. 
			if(questState.startsWith("start") && !"start;15;26;12;6".equals(questState)){
				res.add("I have not delivered everything yet. Major Klykh can tell me what is still missing.");
			} else if ("start;15;26;12;6".equals(questState) || !questState.startsWith("start")) {
				res.add("I delivered all required materials to Major Klykh.");
			}
			if("start;15;26;12;6".equals(questState) && !player.hasKilled("Wawel Dragon") || player.hasKilled("Wawel Dragon")){
				res.add("To demonstrate my power I must defeat the Wawel Dragon!");
			} 
			if (questState.startsWith("forging")) {
				res.add("Major Klykh told me to wait for my medal to be struck.");
			} 
			if (isCompleted(player)) {
				res.add("I aided Major Klykh and the trainers in Ados fortress and received the diamond medal.");
			}
			return res;	
	}

 	// match to the min level of the immortal sword
	@Override
	public int getMinLevel() {
		return 150;
	}

	@Override
	public String getNPCName() {
		return "Major Klykh";
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}
}
