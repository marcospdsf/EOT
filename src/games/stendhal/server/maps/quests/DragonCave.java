/* $Id: DragonCave.java,v 1.21 2011/11/13 17:13:16 kymara Exp $ */
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.CollectRequestedItemsAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseAtkXPAction;
import games.stendhal.server.entity.npc.action.IncreaseDefXPAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayRequiredItemsFromCollectionAction;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.LevelGreaterThanCondition;
import games.stendhal.server.entity.npc.condition.LevelLessThanCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.util.ItemCollection;

/**
 * QUEST: Dragon Cave
 *
 * author: davvids
 */
public class DragonCave extends AbstractQuest {
	
	private static final int REQUIRED_LEVEL = 100;

	public static final String QUEST_SLOT = "dragon_cave";

	/**
	 * required items for the quest.
	 */
	protected static final String NEEDED_ITEMS = "green dragon skin=2;skóra niebieskiego smoka=1;emerald=5;sapphire=3;bone dragon cloak=1";

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("Musze przynieść Terremu pare rzeczy aby dał mi klucz do smoczej groty.");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("Nie potrzebuje klucza do smoczej groty.");
		} else if (!"done".equals(questState)) {
			final ItemCollection missingItems = new ItemCollection();
			missingItems.addFromQuestStateString(questState);
			res.add("Wciąż muszę przynieść Terremu " + Grammar.enumerateCollection(missingItems.toStringList()) + ".");
		} else {
			res.add("Przyniosłem Terremu wszystkie rzeczy o które mnie poprosił.");
		}
		return res;
	}

	private void prepareRequestingStep() {
		final SpeakerNPC npc = npcs.get("Terry");

		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
			new AndCondition(
					new LevelGreaterThanCondition(2),
					new QuestNotStartedCondition(QUEST_SLOT),
					new NotCondition(new QuestInStateCondition(QUEST_SLOT,"rejected"))),
			ConversationStates.QUESTION_1, 
			"Hej ty! Tak, do ciebie mówię! Mógłbyś mi pomóc?", null);

		npc.add(ConversationStates.ATTENDING, 
			ConversationPhrases.QUEST_MESSAGES,
			new QuestInStateCondition(QUEST_SLOT,"rejected"),
			ConversationStates.QUEST_OFFERED, 
			"Hej, chcesz mi jakoś pomóc?", null);

		npc.add(
			ConversationStates.QUESTION_1,
			ConversationPhrases.YES_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"Świetnie, interesuje się wszystkim co jest związane ze smokami. Potrzebuje paru #przedmiotów do kolekcji. Jeśli przyniesiesz mi to czego potrzebuje dostaniesz odemnie klucz do smoczej groty",
			null);

		npc.add(
			ConversationStates.QUESTION_1,
			ConversationPhrases.NO_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"Jestem Terry. Wychowuje małe smoki. Bardzo interesuje się smokami i potrzebuje paru związanych z nimi #rzeczy do mojej kolekcji. Jeśli dostarczysz mi te przedmioty to dam ci klucz do groty z prawdziwymi smokami",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			Arrays.asList("things", "rzeczy", "przedmiotów"),
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.QUEST_OFFERED,
			"Tak... potrzebuje paru przedmiotów. Możesz mi przynieść pare rzeczy?",
			null);

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			null,
			new MultipleActions(new SetQuestAndModifyKarmaAction(QUEST_SLOT, NEEDED_ITEMS, 5.0),
								new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "Cudownie! Proszę przynieś mi te rzeczy: [items].")));

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Ech... Cóż, niedobrze... Poszukam kogoś innego.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));

		npc.add(
			ConversationStates.ATTENDING,
			"emerald",
			null,
			ConversationStates.ATTENDING,
			"emeraldy to bardzo cenne kryształy. Mają zielony kolor więc spróbuj ich poszukać przy zielonych smokach.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			"sapphire",
			null,
			ConversationStates.ATTENDING,
			"sapphirey to przepiękne kryształy. Są niebieskie więc powinieneś poszukać ich przy błękitnych smokach.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			Arrays.asList("green dragon skin", "skóra niebieskiego smoka"),
			null,
			ConversationStates.ATTENDING,
			"Naturalnie skóre możesz zdjąć z martwego smoka.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			"bone dragon cloak",
			null,
			ConversationStates.ATTENDING,
			"Nie mam pojęcia skąd go wziąć. Jest bardzo rzadki więc chciałbym go mieć w swojej kolekcji.",
			null);

	}

	private void prepareBringingStep() {
		final SpeakerNPC npc = npcs.get("Terry");
	
		npc.add(ConversationStates.INFORMATION_1, 
			Arrays.asList("rzeczy", "przedmioty", "zadanie"),
			new AndCondition(
					new QuestNotStartedCondition(QUEST_SLOT), 
					new LevelLessThanCondition(REQUIRED_LEVEL)),
			ConversationStates.ATTENDING, 
			"Jesteś zbyt młodym wojownikiem, nie dasz rady ze smokami. Wróc gdy podrośniesz!",
			null);
			
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestActiveCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING,
				"Witaj ponownie. Przyjdź do mnie jak będziesz miał wszystkie #rzeczy przy sobie.",
				null);

				
		/* player asks what exactly is missing (says ingredients) */
		npc.add(ConversationStates.ATTENDING, "rzeczy", null,
				ConversationStates.QUESTION_2, null,
				new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "Potrzebuję [items]. Czy masz coś ze sobą?"));

		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
			ConversationStates.QUESTION_2, 
			null, new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "Potrzebuję [items]. Czy masz to?"));		

		/* player says he has a required item with him (says yes) */
		npc.add(ConversationStates.QUESTION_2,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.QUESTION_2, "Dobrze, co masz ze sobą?",
				null);

		ChatAction completeAction = new  MultipleActions(
				new SetQuestAction(QUEST_SLOT, "done"),
				new SayTextAction("Wspaniale! Mam wszystko czego potrzebowałem! Moja kolekcja jest teraz przepiękna ! Ach tak... obiecałem ci mój klucz do smoczej groty. Dzięki niemu możesz teraz swobodnie wejść do smoczej groty. Miej się na baczności smoki to bardzo silne stworzenia !"),
				new IncreaseXPAction(10000),
				new IncreaseAtkXPAction(5000),
				new IncreaseDefXPAction(5000),
				new IncreaseKarmaAction(20),
				new EquipItemAction("klucz do smoczej groty", 1)
				);

		/* add triggers for the item names */
		final ItemCollection items = new ItemCollection();
		items.addFromQuestStateString(NEEDED_ITEMS);
		for (final Map.Entry<String, Integer> entry : items.entrySet()) {
			String itemName = entry.getKey();

			String singular = Grammar.singular(itemName);
			List<String> sl = new ArrayList<String>();
			sl.add(itemName);

			// handle the porcino/porcini singular/plural case with item name "borowik"
			if (!singular.equals(itemName)) {
				sl.add(singular);
			}
			// also allow to understand the misspelled "porcinis"
			if (itemName.equals("borowik")) {
				sl.add("borowik");
			}

			npc.add(ConversationStates.QUESTION_2, sl, null,
					ConversationStates.QUESTION_2, null,
					new CollectRequestedItemsAction(
							itemName, QUEST_SLOT,
							"Dobra, masz coś jeszcze?"," " +
							Grammar.quantityplnoun(entry.getValue(), itemName) + " już przyniosłeś dla mnie, ale dziękuję i tak.",
							completeAction, ConversationStates.ATTENDING));
		}

		/* player says he didn't bring any items (says no) */
		npc.add(ConversationStates.ATTENDING, ConversationPhrases.NO_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Dobrze i daj mi znać jeśli mogę ci kiedyś #pomóc..", 
				null);

		/* player says he didn't bring any items to different question */
		npc.add(ConversationStates.QUESTION_2,
				ConversationPhrases.NO_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Ok, i daj mi znać jeśli mogę #pomóc w czymkolwiek innym.", null);

    /* says quest and quest can't be started nor is active*/
		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				null,
			    ConversationStates.ATTENDING, 
			    "Nic nie potrzebuję teraz, dziękuję.",
			    null);
	}
	
	@Override
	public void addToWorld() {
		//super.addToWorld();
		fillQuestInfo(
				"Przedmioty do kolekcji Terrego",
				"Terry powiedział mi, że jeśli przyniose mu pare rzeczy to pomoże mi się dostać do smoczej groty.",
				true);
		prepareRequestingStep();
		prepareBringingStep();
	}

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public String getName() {
		return "DragonCave";
	}

	public String getTitle() {
		
		return "Dragon Cave";
	}
	
	@Override
	public int getMinLevel() {
		return 100;
	}
	
	@Override
	public String getRegion() {
		return Region.SEMOS_CITY;
	}

	@Override
	public String getNPCName() {
		return "Terry";
	}
}
