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

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * QUEST: The złota ciupaga upgrading.
 * 
 * PARTICIPANTS:
 * <ul>
 * <li> Józek will upgrade for you the złota ciupaga.
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Józek tells you about the złota ciupaga z wąsem.
 * <li> He offers to upgrade a złota ciupaga for you if you bring him what it
 * needs.
 * <li> You give him all what he ask you.
 * <li> He tells you you must have killed a złota śmierć
 * <li> Józek upgrade the złota ciupaga to złota ciupaga z wąsem for you
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li>złota ciupaga z wąsem
 * <li>20000 XP
 * <li>100 Karma
 * </ul>
 * 
 * 
 * REPETITIONS:
 * <ul>
 * <li> None.
 * </ul>
 */
public class ZlotaCiupagaWas extends AbstractQuest {
	private static final int REQUIRED_WAIT_DAYS = 5;

	private static final int REQUIRED_MINUTES = 480;

	private static final String QUEST_SLOT = "zlota_ciupaga_was";

	private static final String KILL_DARK_ELVES_QUEST_SLOT = "kill_dark_elves";

	private static final String NAGRODA_WIELKOLUDA_QUEST_SLOT = "nagroda_wielkoluda";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Józek");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isQuestCompleted(NAGRODA_WIELKOLUDA_QUEST_SLOT)) {
						if(player.getLevel() >= 220) {
							if(player.getKarma()>=150) {
								if(player.hasKilledSolo("złota śmierć")) {
									if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
										raiser.say("Are you interested in upgrading your golden highlander axe? I can enhance it for you. Interested?");
									} else if (player.getQuest(QUEST_SLOT, 0).equals("start")) {
										raiser.say("I already asked if you wanted to upgrade your golden axe!");
									} else if (player.isQuestCompleted(QUEST_SLOT)) {
										final String[] waittokens = player.getQuest(QUEST_SLOT).split(";");
										final long waitdelay = REQUIRED_WAIT_DAYS * TimeUtil.MILLISECONDS_IN_DAY;
										final long waittimeRemaining = (Long.parseLong(waittokens[1]) + waitdelay) - System.currentTimeMillis();
										if (waittimeRemaining > 0L) {
											raiser.say("I am exhausted. Return in " + TimeUtil.approxTimeUntil((int) (waittimeRemaining / 1000L)) + ".");
										} else {
											raiser.say("Have you come to upgrade your golden highlander axe?");
											raiser.setCurrentState(ConversationStates.QUEST_OFFERED);
										}
									} else if (player.getQuest(QUEST_SLOT).startsWith("przedmioty")) {
										raiser.say("You have nothing to seek from me until you aid the Giant!");
										raiser.setCurrentState(ConversationStates.ATTENDING);
									}
								} else {
									npc.say("I only speak with those who proved their might by slaying the golden death alone.");
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else {
								npc.say("You are not yet worthy to wield such a weapon. Your karma must be at least 150.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("Your standing is too low for this task. Return when you reach level 220.");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}
					} else {
						npc.say("I see you have not helped the Giant. Go help him first.");
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
					raiser.say("I will require several #items to upgrade the axe.");
					player.setQuest(QUEST_SLOT, "start");
					player.addKarma(10);

				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES, null,
			ConversationStates.IDLE,
			"Your loss.",
			new SetQuestAction(QUEST_SLOT, "rejected"));
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Józek");

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("upgrade", "items", "ulepszyć", "ulepszyc", "ulepszenia"), 
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, "start"),
				new QuestCompletedCondition(NAGRODA_WIELKOLUDA_QUEST_SLOT)),
			ConversationStates.ATTENDING, "Various #items are needed for the upgrade.",
			new SetQuestAction(QUEST_SLOT, "przedmioty" ));

		final List<ChatAction> ciupagaactions = new LinkedList<ChatAction>();
		ciupagaactions.add(new DropItemAction("golden highlander axe",1));
		ciupagaactions.add(new DropItemAction("gold bar",70));
		ciupagaactions.add(new DropItemAction("golden horn",1));
		ciupagaactions.add(new DropItemAction("wood",4));
		ciupagaactions.add(new DropItemAction("money",120000));
		ciupagaactions.add(new DropItemAction("seraph feather",1));
		ciupagaactions.add(new SetQuestAction(QUEST_SLOT, "forging;" + System.currentTimeMillis()));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("seraph feather", "serafin", "seraph", "done", "przedmioty"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "przedmioty"),
								 new PlayerHasItemWithHimCondition("golden highlander axe",1),
								 new PlayerHasItemWithHimCondition("gold bar",70),
								 new PlayerHasItemWithHimCondition("golden horn",1),
								 new PlayerHasItemWithHimCondition("wood",4),
								 new PlayerHasItemWithHimCondition("money",120000),
								 new PlayerHasItemWithHimCondition("seraph feather",1)),
				ConversationStates.ATTENDING, "I see you have brought everything I requested. Return in 8 hours and your notched golden axe will be ready. Say #/reward/.",
				new MultipleActions(ciupagaactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("items", "remind", "przedmioty", "przypomnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "przedmioty"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("golden highlander axe",1),
												  new PlayerHasItemWithHimCondition("gold bar",70),
												  new PlayerHasItemWithHimCondition("golden horn",1),
												  new PlayerHasItemWithHimCondition("wood",4),
												  new PlayerHasItemWithHimCondition("money",120000),
												  new PlayerHasItemWithHimCondition("seraph feather",1)))),
				ConversationStates.ATTENDING, "I need:\n"
									+"#'1 golden highlander axe'\n"
									+"#'70 gold bars'\n"
									+"#'1 golden horn'\n"
									+"#'4 wood'\n"
									+"#'120000 money'\n"
									+"#'1 seraph feather'\n"
									+"Please bring everything at once. Keyword is #'/seraph feather/'. Thank you!", null);

	}

	private void step_4() { 
		final SpeakerNPC npc = npcs.get("Józek");

		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("reward", "nagroda"),
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
						raiser.say("I am still working on your weapon. Return in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ " to receive your axe.");
						return;
					}

					raiser.say("It was worth the wait! Here is your golden highlander axe with notch. Farewell!");
					player.addXP(20000);
					player.addKarma(100);
					final Item zlotyRog = SingletonRepository.getEntityManager().getItem("golden highlander axe with notch");
					zlotyRog.setBoundTo(player.getName());
					player.equipOrPutOnGround(zlotyRog);
					player.notifyWorldAboutChanges();
					player.setQuest(NAGRODA_WIELKOLUDA_QUEST_SLOT, "rejected");
					player.setQuest(QUEST_SLOT, "done" + ";" + System.currentTimeMillis());
				}
			});
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Złota Ciupaga z jednym Wąsem",
				"Józek wzmocni Twoją Złotą Ciupagę.",
				true);
		step_1();
		step_2();
		step_3();
		step_4();
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		final String questState = player.getQuest(QUEST_SLOT);
		res.add("Spotkałem się z Józkiem.");
		res.add("Zaproponował mi ulepszenie złotej ciupagi.");
		if ("rejected".equals(questState)) {
			res.add("Józek wymaga zbyt wiele za ulepszenie złotej ciupagi. Nie zgodziłem się na jego propozycje.");
			return res;
		}
		if ("start".equals(questState)) {
			return res;
		}
		res.add("Józek kazał mi przynnieść kilka przedmiotów, które są potrzebne. Gdybym zapomniał co mam przynieść mam mu powiedzieć: przypomnij.");
		if ("przedmioty".equals(questState)) {
			return res;
		}
		res.add("Dostarczyłem potrzebne przedmioty! Józek zabrał się za ulepszenie mojej ciupagi.");
		if (questState.startsWith("forging")) {
			if (new TimePassedCondition(QUEST_SLOT,1,REQUIRED_MINUTES).fire(player, null, null)) {
				res.add("Mogę iść do Józka i sprawdzić czy już ulepszył moją ciupagę. Hasło: ciupaga.");
			} else {
				res.add("Po ciupagę mam zgłosić się za 8 godzin. Hasło: ciupaga.");
			}
			return res;
		} 
		res.add("Warto było czekać na ciupagę z jednym wąsem. Ta piękna broń należy teraz do mnie.");
		if (isCompleted(player)) {
			return res;
		} 
		// if things have gone wrong and the quest state didn't match any of the above, debug a bit:
		final List<String> debug = new ArrayList<String>();
		debug.add("Stan zadania to: " + questState);
		return debug;
	}

	@Override
	public String getName() {
		return "ZlotaCiupagaWas";
	}

	@Override
	public String getNPCName() {
		return "Józek";
	}
}