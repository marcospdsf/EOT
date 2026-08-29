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
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class Ratownik extends AbstractQuest {

	private static final String QUEST_SLOT = "ratownik";
	private static Logger logger = Logger.getLogger(Ratownik.class);
  
	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	private void step1() {
		final SpeakerNPC npc = npcs.get("Ratownik Mariusz");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED, 
				"A hiker has gone missing up in the mountain ridges. I need to organize a search and rescue team. All rescuers are ready except for the Juhas shepherd. Could you track him down and notify him?",
				null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Thank you for your help! Remember, whenever you head out on the mountain trails, leave a note at the hostel with your intended route. That saves precious hours if a rescue is ever needed.",
				null);

		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.ATTENDING,
				"Wonderful! I heard he was heading toward Kościelisko. The #Old #Baca might know where he went.",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "start", 20.0));

		// Player says no, they've lost karma.
		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES, null, ConversationStates.IDLE,
				"As you wish, but remember: you too might find yourself stranded in the high peaks one day!",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -20.0));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("Old Baca", "old baca", "Stary baca", "stary baca"),
				null,
				ConversationStates.ATTENDING, "You can find him in the eastern part of Kościelisko selling sheepdogs. Tell him: looking for Juhas. And when you find Juhas, tell him we have a rescue operation underway.",
				null);
	}

	private void step2() {
		final SpeakerNPC npc = npcs.get("Stary Baca");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "start"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Looking for Juhas? I haven't seen him recently. He might have taken the path across the valley. That is a favorite trail of #Brzezdom. Go ask him.");
					player.addKarma(10);
					player.addXP(100);
					player.setQuest(QUEST_SLOT, "krok_brzezdom");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("Brzezdom", "brzezdom", "Brzezdoma"),
			new QuestInStateCondition(QUEST_SLOT, "krok_brzezdom"),
			ConversationStates.ATTENDING, "He often walks beyond that forest. You'll need to circle around the trees to find him.",
			null);
	}

	private void step3() {
		final SpeakerNPC npc = npcs.get("Brzezdom");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_brzezdom"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("I'm sorry, but he didn't come this way. Here is a piece of advice: go to the tavern and ask #Jagna."
					+ " If anyone knows where he wandered, she will. Usually when folks go into the mountains they leave word where they're headed.");
					player.addKarma(15);
					player.addXP(500);
					player.setQuest(QUEST_SLOT, "krok_jagna");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("Jagna", "jagna", "Jagny"),
				new QuestInStateCondition(QUEST_SLOT, "krok_jagna"),
				ConversationStates.ATTENDING, "You don't know Jagna? She is the waitress at the tavern in eastern Zakopane.",
				null);
	}

	private void step4() {
		final SpeakerNPC npc = npcs.get("Jagna");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_jagna"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("That rogue was here a couple of days ago. From what I recall, he mentioned visiting the #Giant.");
					player.addKarma(10);
					player.addXP(200);	
					player.setQuest(QUEST_SLOT, "krok_wielkolud");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("giant", "Giant", "wielkolud", "wielkoluda"),
				new QuestInStateCondition(QUEST_SLOT, "krok_wielkolud"),
				ConversationStates.ATTENDING, "You can find the Giant on the hill in northeastern Kościelisko. Watch out for avalanches and rolling boulders up there.",
				null);
	}

	private void step5() {
		final SpeakerNPC npc = npcs.get("Wielkolud");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_wielkolud"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Aye, he was here! We were chatting about this and that when suddenly those cursed mountain penitents ambushed us. He sustained some scrapes and headed to the #hospital in Zakopane.");
					player.addKarma(20);
					player.addXP(1000);
					player.setQuest(QUEST_SLOT, "krok_szpital");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("hospital", "szpital", "szpitala"),
			new QuestInStateCondition(QUEST_SLOT, "krok_szpital"),
			ConversationStates.ATTENDING, "You will find it above Radzimir's shop—it has a prominent red cross on its sign.",
			null);
	}

	private void step6() {
		final SpeakerNPC npc = npcs.get("Boguś");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_szpital"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Sorry, I haven't seen him, but go ask the #healer.");
					player.setQuest(QUEST_SLOT, "krok_zielarka");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("healer", "herbalist", "zielarka", "zielarki"),
			new QuestInStateCondition(QUEST_SLOT, "krok_zielarka"),
			ConversationStates.ATTENDING, "Go down the hallway to the right. Gaździna Jadźka is our resident herbalist and healer.",
			null);
	}

	private void step7() {
		final SpeakerNPC npc = npcs.get("Gaździna Jadźka");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_zielarka"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Let me see... Aye, he arrived around 9 o'clock with minor cuts. He said mountain wraiths ambushed him and the Giant."
						+ " I bandaged his wounds and he hurried off to see #Gaździna #Maryśka.");
					player.addKarma(5);
					player.addXP(100);
					player.setQuest(QUEST_SLOT, "krok_maryska");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("Gaździna Maryśka", "gazdzina maryska", "gaździna Maryśka", "gaździny Maryśki", "Maryśka", "maryska"),
			new QuestInStateCondition(QUEST_SLOT, "krok_maryska"),
			ConversationStates.ATTENDING, "Don't tell me you haven't met her! She's the wife of shepherd Baca Zbyszek. Their cottage is in Kościelisko, neighboring the Old Baca.",
			null);
	}

	private void step8() {
		final SpeakerNPC npc = npcs.get("Gaździna Maryśka");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_maryska"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Of course that restless wanderer was here! Rushed in like a thunderstorm and bolted out before I could exchange two words with him. He needed something from my husband, Baca #Zbyszek.");
					player.addKarma(10);
					player.addXP(150);		
					player.setQuest(QUEST_SLOT, "krok_zbyszek");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("Zbyszek", "zbyszek", "Zbyszka"),
			new QuestInStateCondition(QUEST_SLOT, "krok_zbyszek"),
			ConversationStates.ATTENDING, "You can find him at the sheepfold in southern Zakopane. Juhas might still be with him.",
			null);
	}

	private void step9() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_zbyszek"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("He was here barely three hours ago. He mentioned having business with #Gerwazy.");
					player.addKarma(10);
					player.addXP(100);
					player.setQuest(QUEST_SLOT, "krok_gerwazy");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("Gerwazy", "gerwazy", "Gerwazego"),
			new QuestInStateCondition(QUEST_SLOT, "krok_gerwazy"),
			ConversationStates.ATTENDING, "Gerwazy is the water carrier here in Zakopane.",
			null);
	}

	private void step10() {
		final SpeakerNPC npc = npcs.get("Nosiwoda Gerwazy");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_gerwazy"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Aye, indeed he was here! I filled four waterskins for him, we exchanged a few words, and then he #departed.");
					player.addKarma(10);
					player.addXP(100);
					player.setQuest(QUEST_SLOT, "krok_fryderyk");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("departed", "left", "gone", "poszedł", "poszedl"),
			new QuestInStateCondition(QUEST_SLOT, "krok_fryderyk"),
			ConversationStates.ATTENDING, "He mentioned needing to deliver two waterskins of fresh water to #Fryderyk.",
			null);

		npc.add(ConversationStates.ATTENDING, Arrays.asList("Fryderyk", "fryderyk", "Fryderykowi"),
			new QuestInStateCondition(QUEST_SLOT, "krok_fryderyk"),
			ConversationStates.ATTENDING, "He walks near the town center here in Zakopane.",
			null);
	}

	private void step11() {
		final SpeakerNPC npc = npcs.get("Fryderyk");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("looking for juhas", "searching juhas", "searching", "juhas", "szukam juhasa", "szukam", "juhasa"),
			new QuestInStateCondition(QUEST_SLOT, "krok_fryderyk"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Aye, he was here not so #long ago.");
					player.addKarma(10);
					player.addXP(100);
					player.setQuest(QUEST_SLOT, "krok_juhas");
				};
			});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("long ago", "long", "dawno"),
			new QuestInStateCondition(QUEST_SLOT, "krok_juhas"),
			ConversationStates.ATTENDING, "About 30 minutes ago, I reckon. He set off for the town of #Semos.",
			null);

		npc.add(ConversationStates.ATTENDING, Arrays.asList("Semos", "semos"),
				new QuestInStateCondition(QUEST_SLOT, "krok_juhas"),
				ConversationStates.ATTENDING, "Cross the bridge to the left of the ski lift and follow the forest trail north. Step into the grove where the portal lies, and look for him in the Semos tavern!",
				null);
	}

	private void step12() {
		final SpeakerNPC npc = npcs.get("Juhas");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("rescue", "rescue operation", "action", "akcja", "akcję", "akcje", "gopr", "ratunek", "mariusz", "ratownik mariusz"),
			new QuestInStateCondition(QUEST_SLOT, "krok_juhas"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("You say Mariusz called a rescue operation? Go tell him I am #indisposed."
								+ " As you can see, I've had a few drinks. And one must never venture into the mountains under the influence!");
					player.addKarma(15);
					player.addXP(500);
					player.setQuest(QUEST_SLOT, "krok_mariusz");
				};
			});
	}

	private void step13() {
		final SpeakerNPC npc = npcs.get("Ratownik Mariusz");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("indisposed", "drunk", "seedy", "niedysponowany"),
			new QuestInStateCondition(QUEST_SLOT, "krok_mariusz"),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Aye, if he has been drinking, he cannot join our rescue party under any circumstances. Let me think how to reward you..."
								+ " Take this rare dragon claw. A traveler gifted it to me; I have no use for such relics, but I hear you adventurers value them."
								+ " And remember: never underestimate the mountain peaks!");
					final Item odznaka = SingletonRepository.getEntityManager().getItem("green dragon claw");
					odznaka.setBoundTo(player.getName());
					player.equipOrPutOnGround(odznaka);
					player.setBaseHP(10 + player.getBaseHP());
					player.heal(10, true);
					player.addKarma(25);
					player.addXP(2500);
					player.setQuest(QUEST_SLOT, "done");
				};
			});
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Mountain Rescuer",
			"Help organize the mountain search and rescue party in Zakopane.",
			false);

		step1();
		step2();
		step3();
		step4();
		step5();
		step6();
		step7();
		step8();
		step9();
		step10();
		step11();
		step12();
		step13();
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		final String questState = player.getQuest(QUEST_SLOT);
		res.add("I met Rescuer Mariusz on the outskirts of Zakopane.");
		res.add("Rescuer Mariusz asked me to find Juhas for a mountain search and rescue mission. He was rumored to be visiting Old Baca.");
		if ("rejected".equals(questState)) {
			res.add("I did not have time to look for Juhas.");
			return res;
		} 
		if ("start".equals(questState)) {
			return res;
		} 
		res.add("I found Old Baca, but Juhas had already left. He advised me to look for Brzezdom.");
		if ("krok_brzezdom".equals(questState)) {
			return res;
		} 
		res.add("I tracked down Brzezdom. He hadn't seen Juhas, but suggested checking with Jagna at the Zakopane tavern.");
		if ("krok_jagna".equals(questState)) {
			return res;
		} 
		res.add("Jagna informed me that Juhas had headed to visit the Giant in Kościelisko.");
		if ("krok_wielkolud".equals(questState)) {
			return res;
		} 
		res.add("The Giant told me they were ambushed by mountain wraiths and Juhas went to the hospital to treat minor cuts.");
		if ("krok_szpital".equals(questState)) {
			return res;
		} 
		res.add("Boguś directed me to herbalist Gaździna Jadźka.");
		if ("krok_szpital".equals(questState)) {
			return res;
		} 
		res.add("Gaździna Jadźka bandaged Juhas's wounds and told me he had gone to see Gaździna Maryśka.");
		if ("krok_zielarka".equals(questState)) {
			return res;
		}
		res.add("Gaździna Maryśka said Juhas hurried off to speak with Baca Zbyszek.");
		if ("krok_maryska".equals(questState)) {
			return res;
		}
		res.add("Baca Zbyszek told me Juhas was fetching water from Water Carrier Gerwazy.");
		if ("krok_zbyszek".equals(questState)) {
			return res;
		}
		res.add("Water Carrier Gerwazy filled Juhas's waterskins and noted he was delivering them to Fryderyk.");
		if ("krok_gerwazy".equals(questState)) {
			return res;
		}
		res.add("Fryderyk saw Juhas set off for the Semos tavern.");
		if ("krok_fryderyk".equals(questState)) {
			return res;
		}
		res.add("I finally located Juhas in Semos, but he was indisposed after drinking. I returned to report to Rescuer Mariusz.");
		if ("krok_mariusz".equals(questState)) {
			return res;
		} 
		res.add("Rescuer Mariusz commended my dedication and rewarded me with a rare green dragon claw.");
		if (isCompleted(player)) {
			return res;
		}

		final List<String> debug = new ArrayList<String>();
		debug.add("Quest state is: " + questState);
		return debug;
	}

	@Override
	public String getName() {
		return "Ratownik";
	}
	@Override
	public String getNPCName() {
		return "Ratownik Mariusz";
	}
}
