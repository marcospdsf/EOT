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

import games.stendhal.common.MathHelper;
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
public class rubmedal extends AbstractQuest {
	private static final int REQUIRED_IRON = 1;

	private static final int REQUIRED_GOLD_BAR = 40;

	private static final int REQUIRED_WOOD = 1;

	private static final int REQUIRED_GIANT_HEART = 200;

	private static final int REQUIRED_MINUTES = 1440;

	private static final String QUEST_SLOT = "rubemedal_quest";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Trener Magów");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
						raiser.say("Nasze wojsko potrzebuję zapasów. Chcesz nam pomóc?");
					} else if (player.isQuestCompleted(QUEST_SLOT)) {
						raiser.say("Już przyniosłeś nam to co potrzebowaliśmy.");
						raiser.setCurrentState(ConversationStates.ATTENDING);
					} else {
						raiser.say("Dlaczego zawracasz mi głowę?");
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
					raiser.say("Będę potrzebował kilku rzeczy: "
						+ REQUIRED_IRON
						+ " pazur niebieskiego smoka "
						+ REQUIRED_WOOD
						+ " obsidianowego medalu "
						+ REQUIRED_GOLD_BAR
						+ " sztabek mithrilu"
						+ REQUIRED_GIANT_HEART
						+ " serc olbrzyma. Wróć, gdy będziesz je miał w tej kolejności! Jeżeli zapomnisz to powiedz #przypomnij");
					player.setQuest(QUEST_SLOT, "start;0;0;0;0");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"Och, naprawdę nie chcesz nam pomóc?",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Trener Magów");

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
						if (player.isEquipped("pazur niebieskiego smoka", neededIron)) {
							player.drop("pazur niebieskiego smoka", neededIron);
							neededIron = 0;
						} else {
							final int amount = player.getNumberOfEquipped("pazur niebieskiego smoka");
							if (amount > 0) {
								player.drop("pazur niebieskiego smoka", amount);
								neededIron -= amount;
							}

							raiser.say("Potrzebujemy "
								+ Grammar.quantityplnoun(
										neededIron, "pazur niebieskiego smoka", "")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededWoodLogs > 0)) {
						if (player.isEquipped("obsidianowy medal", neededWoodLogs)) {
							player.drop("obsidianowy medal", neededWoodLogs);
							neededWoodLogs = 0;
						} else {
							final int amount = player.getNumberOfEquipped("obsidianowy medal");
							if (amount > 0) {
								player.drop("obsidianowy medal", amount);
								neededWoodLogs -= amount;
							}

							raiser.say("Nie mam jak cię awansować bez "
								+ Grammar.quantityplnoun(neededWoodLogs, "obsidianowy medal","")
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
							raiser.say("Potrzebujemy materiały do zbroji. Przynieś "
									+ Grammar.quantityplnoun(neededGoldBars, "mithril bar","one") + " więcej.");
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
							raiser.say("Potrzebujemy serc olbrzyma do wytwarzania mikstur. Nadal potrzebujemy  "
								+ Grammar.quantityplnoun(neededGiantHearts, "giant heart","one") + " wciąż.");
							missingSomething = true;
						}
					}

					if (player.hasKilled("złota śmierć") && !missingSomething) {
						raiser.say("Przyniosłeś wszystko. Za niedługo zostaniesz odznaczony. Wróć za  "
							+ REQUIRED_MINUTES
							+ " minutę" + ", a będzie gotowy.");
						player.setQuest(QUEST_SLOT, "forging;" + System.currentTimeMillis());
					} else {
						if (!player.hasKilled("złota śmierć") && !missingSomething) {
							raiser.say("Zanim dostanisz medal zabij złotą śmierć");
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
					
					final long delay = REQUIRED_MINUTES * MathHelper.MILLISECONDS_IN_ONE_MINUTE; 
					final long timeRemaining = (Long.parseLong(tokens[1]) + delay)
							- System.currentTimeMillis();

					if (timeRemaining > 0L) {
						raiser.say("Musisz jeszcze poczekać. Wróć za "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ ".");
						return;
					}

					raiser.say("Proszę bardzo, oto Twoje odznaczenie.");
					player.addXP(5000000);
					player.setAtkXP(50000 + player.getAtkXP());
					player.setDefXP(60000 + player.getDefXP());
					
					player.incAtkXP();
					player.incDefXP();
					player.addKarma(300);
					final Item magicSword = SingletonRepository.getEntityManager().getItem("sapphireowy medal");
					magicSword.setBoundTo(player.getName());
					player.equipOrPutOnGround(magicSword);
					player.notifyWorldAboutChanges();
					player.setQuest(QUEST_SLOT, "done");
				}
			});

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("odznaczenie", "missing", "medal", "brakuje", "lista", "przypomnij"), 
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

					raiser.say("Będę potrzebował " + neededIron + " pazura niebieskiego smoka, "
							+ neededWoodLogs + " obsidianowego medalu "
							+ neededGoldBars + " sztabek mithrilu i "
							+ neededGiantHearts + " serc giganta.");
				}
			});

		npc.add(
			ConversationStates.ANY,
			"pazur niebieskiego smoka",
			null,
			ConversationStates.ATTENDING,
			"Wyrwij niebieskiemu smoku pazur z jego stopy.",
			null);

		npc.add(ConversationStates.ANY, "obsidianowy medal", null,
				ConversationStates.ATTENDING,
				"Musisz udowodnić, że pomogłeś Trenerowi Łuczników.", null);
		npc.add(ConversationStates.ANY, "mithril", null,
				ConversationStates.ATTENDING,
				"Kowal w Ados może dla Ciebie odlać bryłki mithrilu w sztabki mithrilu.",
				null);
		npc.add(
			ConversationStates.ANY,
			Arrays.asList("giant","olbrzyma", "olbrzym"),
			null,
			ConversationStates.ATTENDING,
			"Są starodawne legendy o olbrzymach żyjących w górach na północ od Semos i Ados.",
			null);
	}

	@Override
	public void addToWorld() {
		//super.addToWorld();
		fillQuestInfo(
				"Rzeczy dla żołnierzy Ados",
				"Trener Magów potrzebuje zapasów.",
				false);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public String getName() {
		return "rubmedal";
	}
	
	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("Spotkałem Trenera Magów w twierdzy Ados.");
			if (questState.equals("rejected")) {
				res.add("Nie pomogę Trenerowi Magów.");
				return res;
			} 
			res.add("Trener Magów poprosił mnie abym przyniósł mu: " + REQUIRED_IRON
					+ " pazur niebieskiego smoka, "
					+ REQUIRED_WOOD
					+ " obsidianowy medal, "
					+ REQUIRED_GOLD_BAR
					+ " sztabki mithrilu i "
					+ REQUIRED_GIANT_HEART
					+ " serc olbrzyma, dokładnie w tej kolejności.");
			// yes, yes. this is the most horrible quest code and so you get a horrible quest history. 
			if(questState.startsWith("start") && !"start;15;26;12;6".equals(questState)){
				res.add("Nie dostarczyłem wszystkiego. Trener Magów powie mi czego jeszcze potrzebuje.");
			} else if ("start;15;26;12;6".equals(questState) || !questState.startsWith("start")) {
				res.add("Dostarczyłem wszystko co potrzebne dla Trenera Magów.");
			}
			if("start;15;26;12;6".equals(questState) && !player.hasKilled("złota śmierć")){
				res.add("Aby zasłużyć na odznakę muszę zabić złotą śmierć.");
			} 
			if (questState.startsWith("forging")) {
				res.add("Muszę poczekać na swoją odznakę.");
			} 
			if (isCompleted(player)) {
				res.add("Pomogłem Trenerowi Magów i dostałem sapphireową odznakę.");
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
		return "Trener Magów";
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}
}
