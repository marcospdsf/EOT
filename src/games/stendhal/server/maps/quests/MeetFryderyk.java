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
// Based on MeetMonogomes
package games.stendhal.server.maps.quests;

import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.ExamineChatAction;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;
import games.stendhal.server.entity.npc.condition.TriggerInListCondition;
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * QUEST: Speak with Monogenes PARTICIPANTS: - Fryderyk
 * 
 * STEPS: - Talk to Fryderyk to activate the quest and keep speaking with
 * Fryderyk. - Be polite and say "bye" at the end of the conversation to get a
 * small reward.
 * 
 * REWARD: broken (- 10 XP (check that user's level is lesser than 2) - No money)
 * 
 * REPETITIONS: - None
 * 
 */
public class MeetFryderyk extends AbstractQuest {
	@Override
	public String getSlotName() {
		return "meetFryderyk";
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Meet Fryderyk",
				"Listen carefully to the wise elder in Zakopane. His town knowledge can help you find your way around.",
				false);
		final SpeakerNPC npc = npcs.get("Fryderyk");

		npc.addGreeting(null, new SayTextAction("Welcome back, [name]. How may I #help you this time?"));

		// A little trick to make NPC remember if it has met
		// player before and react accordingly
		// NPC_name quest doesn't exist anywhere else neither is
		// used for any other purpose
		npc.add(ConversationStates.IDLE, 
				ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(
						new GreetingMatchesNameCondition(npc.getName()),
						new QuestNotCompletedCondition("Fryderyk")),  
				ConversationStates.INFORMATION_1, 
				"Greetings, stranger! Don't feel discouraged when folks around here seem quiet or preoccupied... " +
				"the dread of the mountain brigands has cast a shadow over all of Zakopane. We are " +
				"all rather on edge. I can offer you a few friendly pointers about getting along with the townsfolk. Would you like to hear them?",
				new SetQuestAction("Fryderyk", "done"));

		npc.add(
			ConversationStates.ATTENDING,
			ConversationPhrases.HELP_MESSAGES,
			null,
			ConversationStates.INFORMATION_1,
			"I can offer you some advice on conversing with the residents of Zakopane, if you wish?",
			null);

		npc.add(
			ConversationStates.INFORMATION_1,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"First, you should greet them by saying \"hi\" or \"hello\". Then keep the conversation going by bringing up highlighted topics #like #this. " +
			"Generally safe topics include asking about their #job, asking for #help, asking if they have an #offer, or inquiring about a #quest to undertake. " +
			"Would you also like to hear a brief description of the #buildings in Zakopane?",
			null);

		npc.add(
			ConversationStates.INFORMATION_1,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"And how do you intend to know what is happening around here? Reading the town notices? Hah! " +
			"Pay attention to the wooden signposts as well. Farewell!",
			null);

		final List<String> yesnotriggers = new ArrayList<String>();
		yesnotriggers.addAll(ConversationPhrases.YES_MESSAGES);
		yesnotriggers.addAll(ConversationPhrases.NO_MESSAGES);

		npc.add(
				ConversationStates.INFORMATION_1,
				"",
				new NotCondition(new TriggerInListCondition(yesnotriggers)),
				ConversationStates.INFORMATION_1,
				"I asked you 'yes' or 'no': I can offer you a few pointers. Would you like to hear them?",
			null);

		// he puts 'like this' into blue and so many people try that first
		npc.addReply(
				Arrays.asList("like this", "jak to"),
				"Indeed, just like that! Now I can show you a #map or directions to the #bank, #library, #tavern, #temple, #blacksmith, #bakery, or the old #village.");

		npc.addReply(
			Arrays.asList("buildings", "budynki", "budynków"),
			"I can show you a map or give you directions to places such as: the #bank, #blacksmith, #hospital, the #mayor of Zakopane, the sports #field, how to reach #Kraków, the #gamehouse, or #Kościelisko.");

		npc.add(
			ConversationStates.ATTENDING,
			Arrays.asList("map", "mapa"), null, ConversationStates.ATTENDING, "Map of Zakopane Town\n"
			+ "1 Cottage (where you take your first steps),   2 Bank,   3 Blacksmith forge,   4 Sports Field,\n"
			+ "A Mayor's Office,   B Hospital,   C Gamehouse,\n"
			+ "D The Long Road straight to Kraków,\n"
			+ "E Westwards toward Kościelisko",
			new ExamineChatAction("map-zakopane-city.png", "Zakopane Town", "Map of Zakopane Town"));

		npc.addReply(
			Arrays.asList("bank", "Bank"),
			"Step outside this building and looking east (to the right on your map), you will see a small stream and a bridge. Crossing the bridge brings you past the sports field where the Herald walks. Continuing further east, you will see a building with a large sign that says BANK.");

		npc.addReply(  
			Arrays.asList("hospital", "szpital", "Szpital"),
			"Head north, but instead of crossing the bridge, follow along the right bank of the river upwards. The river will narrow and to your right you will find a building marked with a red cross. That is our town hospital.");

		npc.addReply(
			Arrays.asList("mayor", "mayor of Zakopane", "burmistrz", "burmistrz Zakopanego", "Burmistrz Zakopanego"),
			"North of here is a bridge. Don't cross it—head slightly up and right along the river eastwards until you see a sturdy timber manor. Inside you will find Gazda Wojtek, the respected mayor of Zakopane.");

		npc.addReply(
			Arrays.asList("gamehouse", "game house", "dom gier", "Dom gier"),
			"Heading north, pass to the right of the bridge and keep walking east to the small brook crossed by a wooden footbridge. Cross it and head north to reach the cozy gamehouse.");

		npc.addReply(
			Arrays.asList("field", "sports field", "boisko", "Boisko"),
			"Just outside this building to the east, across the little stream. The Herald frequently walks around there renting signs.");

		npc.addReply(
			Arrays.asList("blacksmith", "forge", "kuźnia", "Kuźnia"),
			"Head northwest from here to reach the blacksmith. Look for the hanging anvil sign above the doorway.");

		npc.addReply(
			Arrays.asList("kościelisko", "koscielisko", "Kościelisko"),
			"Leaving this building, follow the path to the left. Heading south leads to Kościelisko SE, while heading west leads along the river path toward Kościelisko E and NE.");

		npc.addReply(
			Arrays.asList("kraków", "krakow", "Kraków"),
			"Head north, cross the bridge and follow the left side of the road past the railway passage. Keep heading north for quite a stretch until you reach the east-west crossroads. Turn east and follow that road straight to Kraków.");

		npc.addGoodbye();
	}

	@Override
	public String getName() {
		return "MeetFryderyk";
	}

	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest("Fryderyk")) {
				return res;
			}
			if (isCompleted(player)) {
				res.add("I spoke with Fryderyk and he offered me directions and guidance around Zakopane.");
			} 
			return res;
	}
	@Override
	public String getNPCName() {
		return "Fryderyk";
	}
}
