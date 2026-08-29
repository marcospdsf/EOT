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
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ZlotaCiupagaDwaWasy extends AbstractQuest {

	private static final int REQUIRED_MINUTES = 30;

	private static final String QUEST_SLOT = "ciupaga_dwa_wasy";

	private static final String KRASNOLUD_QUEST_SLOT = "krasnolud";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Dragon Lord");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isQuestCompleted(KRASNOLUD_QUEST_SLOT)) {
						if(player.getLevel() >= 350) {
							if(player.getKarma() >= 1000) {
								if(player.hasKilled("seraph") || player.hasKilled("serafin")) {
									if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
										raiser.say("You must be a valiant warrior to have reached this sanctuary. I have a task for you. Are you ready?");
									} else if (player.getQuest(QUEST_SLOT).startsWith("done;")) {
										if (player.isQuestCompleted(QUEST_SLOT)) {
											raiser.say("I am very grateful for your assistance. The dragons obey once more.");
											raiser.setCurrentState(ConversationStates.ATTENDING);
										} else {
											raiser.say("Why do you disturb me before completing the task?");
											raiser.setCurrentState(ConversationStates.ATTENDING);
										}
									} else if (player.getQuest(QUEST_SLOT).startsWith("zbroja;")) {
										raiser.say("Why do you disturb me before finishing the Dwarf Blacksmith's task?");
										raiser.setCurrentState(ConversationStates.ATTENDING);
									}
								} else {
									npc.say("I only speak with those who proved their valor by slaying a seraph.");
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else {
								npc.say("Your karma is too low for this task. Strive for 1000 or higher.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("Your standing is too low for this task. Return when you reach level 350.");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}
					} else {
						npc.say("Before I give you this task, prove your mettle by helping the Dwarf Blacksmith.");
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

					raiser.say("Many dragons have rebelled against me. Slay any that cross your path, and collect their claws. My old friend the Dwarf Blacksmith collects them and can forge a magnificent weapon. Tell him my name! Happy hunting.");
					player.setQuest(QUEST_SLOT, "start");
					player.addKarma(10);

				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES, null,
			ConversationStates.IDLE,
			"May you encounter the Wawel Dragon on your travels.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -50.0));

	}

	private void step_2() {
		final SpeakerNPC npc = npcs.get("Dwarf Blacksmith");
		final List<ChatAction> ciupagaactions = new LinkedList<ChatAction>();
		ciupagaactions.add(new DropItemAction("blue dragon claw",1));
		ciupagaactions.add(new DropItemAction("red dragon claw",1));
		ciupagaactions.add(new DropItemAction("black dragon claw",1));
		ciupagaactions.add(new DropItemAction("golden highlander axe with notch",1));
		ciupagaactions.add(new DropItemAction("gold bar",150));
		ciupagaactions.add(new DropItemAction("money",1200000));
		ciupagaactions.add(new DropItemAction("wood",10));
		ciupagaactions.add(new DropItemAction("seraph feather",2));
		ciupagaactions.add(new SetQuestAction(QUEST_SLOT, "forging;" + System.currentTimeMillis()));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("items", "remind", "axe", "przedmioty", "przypomnij", "ciupaga"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "start"),
								 new PlayerHasItemWithHimCondition("blue dragon claw",1),
								 new PlayerHasItemWithHimCondition("red dragon claw",1),
								 new PlayerHasItemWithHimCondition("black dragon claw",1),
								 new PlayerHasItemWithHimCondition("golden highlander axe with notch",1),
								 new PlayerHasItemWithHimCondition("gold bar",150),
								 new PlayerHasItemWithHimCondition("money",1200000),
								 new PlayerHasItemWithHimCondition("wood",10),
								 new PlayerHasItemWithHimCondition("seraph feather",2)),
				ConversationStates.ATTENDING, "I see you have brought everything I requested. Return in 30 hours and your double-notched golden axe will be ready. Say #/reward/.",
				new MultipleActions(ciupagaactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("remind", "Dragon Lord", "dragon", "przypomnij", "Władca Smoków", "wladca smokow"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "start"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("blue dragon claw",1),
												  new PlayerHasItemWithHimCondition("red dragon claw",1),
												  new PlayerHasItemWithHimCondition("black dragon claw",1),
												  new PlayerHasItemWithHimCondition("golden highlander axe with notch",1),
												  new PlayerHasItemWithHimCondition("gold bar",150),
												  new PlayerHasItemWithHimCondition("money",1200000),
												  new PlayerHasItemWithHimCondition("wood",10),
												  new PlayerHasItemWithHimCondition("seraph feather",2)))),
				ConversationStates.ATTENDING, "Tak wiem Władca Smoków mówił mi o tobie. Zajmuję się udoskonalaniem złotej ciupagi.\n"
									+"Do jej udoskonalenia potrzebuję:\n"
									+"#'1 pazur niebieskiego smoka'\n"
									+"#'1 red dragon claw'\n"
									+"#'1 black dragon's claw'\n"
									+"#'1 złota ciupaga z wąsem'\n"
									+"#'150 sztabek złota'\n"
									+"#'1200000 money'\n"
									+"#'10 polan' oraz\n"
									+"#'2 pióra serafina'\n"
									+"Proszę przynieś mi to wszystko naraz. Jeżeli zapomnisz co masz przynieść to powiedz #przypomnij. Dziękuję!", null);

	}

	private void step_3() { 
		final SpeakerNPC npc = npcs.get("Dwarf Blacksmith");

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("reward", "nagroda"),
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
			new QuestStateStartsWithCondition(QUEST_SLOT, "forging;")),
			ConversationStates.IDLE, null, new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {

					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");

					final long delay = REQUIRED_MINUTES * TimeUtil.SECONDS_IN_MINUTE; 
					final long timeRemaining = (Long.parseLong(tokens[1]) + delay)
							- System.currentTimeMillis();

					if (timeRemaining > 0L) {
						raiser.say("I am still working on your axe. Return in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ " to collect it.");
						return;
					}

					raiser.say("It was worth the wait! Behold, isn't it magnificent!");
					player.addXP(20000);
					player.addKarma(100);
					final Item zlotaCiupagaZDwomaWasami = SingletonRepository.getEntityManager().getItem("golden highlander axe with two notches");
					zlotaCiupagaZDwomaWasami.setBoundTo(player.getName());
					player.equipOrPutOnGround(zlotaCiupagaZDwomaWasami);
					player.notifyWorldAboutChanges();
					player.setQuest(QUEST_SLOT, "done");
				}
			});
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Double-Notched Golden Highlander Axe",
			"The Dwarf Blacksmith can enhance your notched golden highlander axe.",
			true);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		final String questState = player.getQuest(QUEST_SLOT);
		res.add("Spotkałem się z Władcą Smoków.");
		res.add("Poprosił mnie abym zabijał wszystkie smoki, które wejdą mi w drogę. Zdradził mi też tajemnice. Podobno Krasnalud ulepsza złote ciupagi.");
		if ("rejected".equals(questState)) {
			res.add("Mam stracha przed smokami więc będę schodził im z drogi.");
			return res;
		}
		res.add("Udałem się do Krasnoluda w celu ulepszenia ciupagi. Kazał mi przynnieść kilka przedmiotów. Gdybym zapomniał co mam przynieść mam mu powiedzieć: przypomnij."); 
		if ("start".equals(questState)) {
			return res;
		} 
		res.add("Dostarczyłem potrzebne przedmioty! Krasnal zabrał się za ulepszenie mojej ciupagi.");
		if (questState.startsWith("forging")) {
			if (new TimePassedCondition(QUEST_SLOT,1,REQUIRED_MINUTES).fire(player, null, null)) {
				res.add("Podobno Krasnalud skończył moją ciupagę. Hasło: nagroda.");
			} else {
				res.add("Po ciupagę mam zgłosić się za 30 godzin. Hasło: ciupaga.");
			}
			return res;
		} 
		res.add("Warto było czekać na ciupagę z dwoma wąsami. Ta potężna i piękna broń należy teraz do mnie.");
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
		return "ZlotaCiupagaDwaWasy";
	}

	@Override
	public String getNPCName() {
		return "Dragon Lord";
	}
}