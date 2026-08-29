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
// Based on HelpMrsYeti.
 
package games.stendhal.server.maps.quests;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.creature.Pet;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.IncreaseBaseHPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.OrCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasPetOrSheepCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
 

public class HelpKrasnolud extends AbstractQuest {

	private static final String QUEST_SLOT = "krasnolud";
	private static final int DELAY_IN_MINUTES = 60*48;

	private static Logger logger = Logger.getLogger(HelpKrasnolud.class);

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	
	private void startQuest() {
		final SpeakerNPC npc = npcs.get("Dwarf Blacksmith");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED, 
				"I am in dire need of sturdy, rare armor. Would you be willing to retrieve it for me?",
				null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Splendid! At last I have the glorious armor I always dreamed of.",
				null);


		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.ATTENDING,
				"Thank you for your noble heart! You must visit the Mountain Giant in Kościelisko and tell him you need #armor. He will know what it is about.",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "start", 10.0));

		// Player says no, they've lost karma.
		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES, null, ConversationStates.IDLE,
				"No? So be it, someone else of noble spirit will surely aid me...",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -15.0));
	}

	private void makeArmor() {

	final SpeakerNPC npc = npcs.get("Wielkolud");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("krasnolud", "armor", "zbroja","zbroję"),
				new QuestInStateCondition(QUEST_SLOT, "start"),
				ConversationStates.ATTENDING, "That dwarf sent you for the azure armor, you say... Someone brought something like that to me recently. "
				+ " But nothing in this world is free! I desperately need a #'blue helmet'. Bring me one and perhaps I will trade that armor... "
				+ " Head to Gulimo in the Ados mountains; rumor has it he knows where to find such a helmet. "
				+ " Just mention my name #/Wielkolud/ to him and he will understand.",
				new SetQuestAction(QUEST_SLOT, "gulimo"));

		npc.add(
			ConversationStates.ATTENDING, Arrays.asList("wielkolud","helmet", "hełm"),
			new NotCondition(new QuestInStateCondition(QUEST_SLOT, "helmet")),
			ConversationStates.ATTENDING,
			"Before I assist you, retrieve that helmet and tell him my name so he knows who sent you...",
			null);

		npc.add(ConversationStates.ATTENDING,  Arrays.asList("wielkolud", "helmet", "zbroja", "armor"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "helmet"),
				new PlayerHasItemWithHimCondition("blue helmet")),
				ConversationStates.ATTENDING, "Very good! Now you must bring me crafting materials from my list before I give you the #/armor/.\n"
				+ "I require:\n"
				+ "#'30 red dragon leathers'\n"
				+ "#'30 green dragon leathers'\n"
				+ "#'30 blue dragon leathers'\n"
				+ "#'60 black dragon leathers'\n"
				+ "and #'10 golden dragon leathers'",
				new MultipleActions(new SetQuestAction(QUEST_SLOT, "armor"), new DropItemAction("blue helmet")));

		npc.add(ConversationStates.ATTENDING,  Arrays.asList("wielkolud", "hełm", "zbroja", "armor"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "helmet"),
				new NotCondition(new PlayerHasItemWithHimCondition("blue helmet"))),
				ConversationStates.ATTENDING, "I take it you visited Gulimo. Where is the blue helmet I asked for?",
				null);

		final List<ChatAction> armoractions = new LinkedList<ChatAction>();
		armoractions.add(new DropItemAction("red dragon leather",30));
		armoractions.add(new DropItemAction("green dragon leather",30));
		armoractions.add(new DropItemAction("blue dragon leather",30));
		armoractions.add(new DropItemAction("black dragon leather",60));
		armoractions.add(new DropItemAction("golden dragon leather",10));
		armoractions.add(new EquipItemAction("blue armor"));
		armoractions.add(new IncreaseXPAction(1000));
		armoractions.add(new SetQuestAction(QUEST_SLOT, "gotarmor"));

		// don't make player wait for potion - could add this in later if wanted
		npc.add(ConversationStates.ATTENDING,  Arrays.asList("wielkolud", "zbroja", "armor"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "armor"),
								new PlayerHasItemWithHimCondition("red dragon leather",30),
								new PlayerHasItemWithHimCondition("green dragon leather",30),
								new PlayerHasItemWithHimCondition("blue dragon leather",30),
								new PlayerHasItemWithHimCondition("black dragon leather",60),
								new PlayerHasItemWithHimCondition("golden dragon leather",10)),
				ConversationStates.ATTENDING, "I see you have gathered all the leathers! Here is the azure armor. Give my regards to the Dwarf in Zakopane!",
				new MultipleActions(armoractions));

		npc.add(ConversationStates.ATTENDING,  Arrays.asList("wielkolud", "zbroja", "armor"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "armor"),
								new NotCondition(
												new AndCondition(new PlayerHasItemWithHimCondition("red dragon leather",30),
																new PlayerHasItemWithHimCondition("green dragon leather",30),
																new PlayerHasItemWithHimCondition("blue dragon leather",30),
																new PlayerHasItemWithHimCondition("black dragon leather",60),
																new PlayerHasItemWithHimCondition("golden dragon leather",10)))),
				ConversationStates.ATTENDING, "I require 30 red dragon leathers, 30 green dragon leathers, 30 blue dragon leathers, 60 black dragon leathers, and 10 golden dragon leathers. Deliver them all together. Best of luck!", null);
	}

	private void makeHelmet() {
		// although the player does end up just taking an ordinary knife to salva, this step must be completed 
		// (must be in quest state 'knife' when they take the knife)
	final SpeakerNPC npc = npcs.get("Gulimo");
		npc.add(ConversationStates.ATTENDING, "wielkolud",
				new QuestInStateCondition(QUEST_SLOT, "gulimo"),
				ConversationStates.ATTENDING, "You say the Giant sent you? Ah, the blue helmet... Where on earth did I place it... My memory fades with age. "
				+ " While I search for it, could you fetch 100 #dragon #tusks for my collection?",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "kiel_smoka", 1.0));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("wielkolud", "kieł", "smok", "tusk", "tusks"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "kiel_smoka"),
				new PlayerHasItemWithHimCondition("dragon tusk",100)),
				ConversationStates.ATTENDING, "Thank you kindly for the tusks! Alas, I couldn't find the helmet in my chests... But wait! On the upper floor dwells an orc named Hagnurk who deals in rare equipment. He should have a blue helmet in stock!",
				new MultipleActions(new SetQuestAndModifyKarmaAction(QUEST_SLOT, "helmet", 1.0), new DropItemAction("dragon tusk",100)));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("wielkolud", "kieł", "smok", "tusk", "tusks"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "kiel_smoka"),
				new NotCondition(new PlayerHasItemWithHimCondition("dragon tusk",100))),
				ConversationStates.ATTENDING, "You can harvest dragon tusks by slaying dragons across the realm!",
				null);

	}

	private void bringArmor() {
	final SpeakerNPC npc = npcs.get("Dwarf Blacksmith");
		final String extraTrigger = "armor";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		final List<ChatAction> tookarmoractions = new LinkedList<ChatAction>();
		tookarmoractions.add(new DropItemAction("blue armor"));
		tookarmoractions.add(new IncreaseKarmaAction(100.0));
		tookarmoractions.add(new IncreaseXPAction(10000));
		tookarmoractions.add(new SetQuestAction(QUEST_SLOT, "dragon"));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "gotarmor"),
				new PlayerHasItemWithHimCondition("blue armor")),
				ConversationStates.ATTENDING, "Thank you! This armor looks truly magnificent. I have one more special favor: I adore baby #dragons. Please be so kind as to bring me one!", 
				new MultipleActions(tookarmoractions));

		npc.add(
			ConversationStates.ATTENDING, questTrigger,
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, "gotarmor"), new NotCondition(new PlayerHasItemWithHimCondition("blue armor"))),
			ConversationStates.ATTENDING,
			"Where have you misplaced my armor?",
			null);

		npc.add(ConversationStates.ATTENDING,
				questTrigger, 
				new OrCondition(new QuestInStateCondition(QUEST_SLOT, "start"), 
								new QuestInStateCondition(QUEST_SLOT, "kiel_smoka"), 
								new QuestInStateCondition(QUEST_SLOT, "helmet")),
				ConversationStates.ATTENDING,
				"I am waiting for you to deliver the azure armor from the Giant.",
				null);
	}

	private void bringDragon() {
	final SpeakerNPC npc = npcs.get("Dwarf Blacksmith");

		final String extraTrigger = "smoki";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		// easy to check if they have a pet or sheep at all
		npc.add(
				ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "dragon"), 
								new NotCondition(new PlayerHasPetOrSheepCondition())),
				ConversationStates.ATTENDING,
				"Baby dragons hatch from mythical eggs! You can obtain one from Morgrin in the magic academy. Terry, the dragon tamer living in the caves east of Semos, can incubate the egg for you.",
				null);

		// if they have any pet or sheep, then check if it's a baby dragon
		npc.add(
				ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "dragon"), 
								new PlayerHasPetOrSheepCondition()),
				ConversationStates.ATTENDING,
				null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence,
									 final EventRaiser npc) {
						if(!player.hasPet()){
							npc.say("What a lovely sheep! But I require a baby dragon. Talk to Morgrin at the magic school.");
							return;
						}
						Pet pet = player.getPet();
						String petType = pet.get("type");
						if("baby_dragon".equals(petType)) {
							player.removePet(pet);
							npc.say("You brought a baby dragon! It will make a delectable dragon stew—my absolute culinary specialty! Return in two days and you shall receive your #reward.");
							player.addKarma(5.0);
							player.addXP(500);
							pet.delayedDamage(pet.getHP(), "Krasnolud");
							player.setQuest(QUEST_SLOT,"reward;"+System.currentTimeMillis());
							player.notifyWorldAboutChanges();
						} else {
							npc.say("What a cute pet! But I specifically require a baby dragon. Speak to Morgrin about acquiring a mystic egg.");  
						}
					}
			});

	}

	private void getReward() {

	final SpeakerNPC npc = npcs.get("Dwarf Blacksmith");

		final String extraTrigger = "reward";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(
				ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "reward"), 
								// delay is in minutes, last parameter is argument of timestamp
								new NotCondition(new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING,
				null,
				new SayTimeRemainingAction(QUEST_SLOT,1,DELAY_IN_MINUTES, "Greetings! I am still simmering my dragon stew; I will bestow your reward once it is ready. Please return in "));


		npc.add(
				ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "reward"), 
								// delay is in minutes, last parameter is argument of timestamp
								new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"Thank you! As your reward, I bestow upon you an increase to your vitality and resilience in battle (+50 Max HP)!",
				new MultipleActions(new SetQuestAction(QUEST_SLOT,"done"), new IncreaseBaseHPAction(50), new IncreaseKarmaAction(100.0), new IncreaseXPAction(10000)));

	}


	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Aid the Dwarf",
				"The brave Dwarf, an old ally of the Dragon Lord, has a quest for you.",
				true);
		startQuest();
		makeArmor();
		makeHelmet();
		bringArmor();
		bringDragon();
		getReward();
	}

	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("I met the Dwarf in Zakopane smithy.");
			res.add("The Dwarf asked me to retrieve azure armor from the Mountain Giant.");
			if ("rejected".equals(questState)) {
				res.add("I refused to act as a messenger.");
				return res;
			} 
			if ("start".equals(questState)) {
				return res;
			} 
			res.add("The Giant demanded a blue helmet from Gulimo in exchange for the armor.");
			if ("gulimo".equals(questState)) {
				return res;
			} 
			res.add("Gulimo asked for 100 dragon tusks in exchange for the helmet.");
			if ("kiel_smoka".equals(questState)) {
				return res;
			} 
			res.add("I gave Gulimo the tusks; he advised me to purchase the helmet from Hagnurk.");
			if ("helmet".equals(questState)) {
				return res;
			} 
			res.add("I brought the helmet to the Giant, who then asked for dragon leathers of every color.");
			if ("armor".equals(questState)) {
				return res;
			} 
			res.add("I delivered all the leathers to the Giant and received the azure armor for the Dwarf.");
			if ("gotarmor".equals(questState)) {
				return res;
			} 
			res.add("The Dwarf was pleased with the armor and asked me to bring him a baby dragon.");
			if ("dragon".equals(questState)) {
				return res;
			} 
			res.add("To my astonishment, the Dwarf decided to brew a hearty dragon stew!");
			if (questState.startsWith("reward")) {
				if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
					res.add("The Dwarf asked me to return in a few days for my reward.");
				} else {
					res.add("The Dwarf told me to wait while the stew finishes.");
				}
				return res;
			} 
			res.add("The Dwarf permanently increased my base HP by +50 and granted extensive karma and XP.");
			if (isCompleted(player)) {
				return res;
			}

			// if things have gone wrong and the quest state didn't match any of the above, debug a bit:
			final List<String> debug = new ArrayList<String>();
			debug.add("Stan zadania to: " + questState);
			logger.error("Historia nie pasuje do stanu poszukiwania " + questState);
			return debug;
	}

	@Override
	public String getName() {
		return "HelpKrasnolud";
	}

		@Override
	public int getMinLevel() {
		return 250;
	}
	@Override
	public String getNPCName() {
		return "Dwarf Blacksmith";
	}
}

