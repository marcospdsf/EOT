package games.stendhal.server.maps.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;

/**
 * Quest to unlock the Scroll Bag.
 */
public class TheScrollBag extends AbstractQuest {

	private static final String QUEST_SLOT = "the_scroll_bag";
	private static final String NPC_NAME = "Haizen";
	private static final List<String> TRIGGER_WORDS = Arrays.asList("scroll bag", "scrollbag", "scrolls bag", "scroll pouch");

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I spoke with Magician Haizen in his house outside Ados about obtaining a scroll bag.");
		final String state = player.getQuest(QUEST_SLOT);
		if (player.isQuestInState(QUEST_SLOT, "started", "done")) {
			res.add("Haizen requested 2 empty scrolls, 2 small feathers, 2 pelts, and 2 silk glands from giant spiders to enchant a specialized scroll bag for me.");
		}
		if ("done".equals(state)) {
			res.add("I delivered the arcane components to Haizen, and he enchanted and unlocked the scroll bag for me!");
		}
		return res;
	}

	private void buildQuestDialog() {
		final SpeakerNPC npc = npcs.get(NPC_NAME);
		if (npc == null) {
			return;
		}

		// 1. Not started: asking about "scroll bag" starts it immediately
		npc.add(ConversationStates.ATTENDING,
				TRIGGER_WORDS,
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Carrying delicate spell scrolls unprotected risks dispelling their magic! Bring me 2 #empty #scrolls, 2 #small #feathers, 2 #pelts, and 2 #silk #glands (from giant spiders), and I will enchant a dedicated #scroll #bag for you.",
				new SetQuestAction(QUEST_SLOT, "started"));

		// 2. Turn-in condition & action supporting both empty scroll and blank scroll
		final ChatCondition hasAllMaterials = new ChatCondition() {
			@Override
			public boolean fire(final Player player, final Sentence sentence, final Entity entity) {
				if (!player.isQuestInState(QUEST_SLOT, "started")) {
					return false;
				}
				final boolean hasScrolls = player.isSubmittableEquipped("empty scroll", 2) || player.isSubmittableEquipped("blank scroll", 2);
				final boolean hasFeathers = player.isSubmittableEquipped("small feather", 2);
				final boolean hasPelts = player.isSubmittableEquipped("pelt", 2);
				final boolean hasGlands = player.isSubmittableEquipped("silk gland", 2);
				return hasScrolls && hasFeathers && hasPelts && hasGlands;
			}
		};

		final ChatAction completeQuestAction = new ChatAction() {
			@Override
			public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
				if (player.isSubmittableEquipped("empty scroll", 2)) {
					player.drop("empty scroll", 2);
				} else if (player.isSubmittableEquipped("blank scroll", 2)) {
					player.drop("blank scroll", 2);
				}
				player.drop("small feather", 2);
				player.drop("pelt", 2);
				player.drop("silk gland", 2);

				player.setFeature("scrollbag", "3 4");
				player.addXP(500);
				player.addKarma(25.0);
				player.setQuest(QUEST_SLOT, "done");

				raiser.say("Splendid work! *Haizen chants an incantation in an ancient tongue as mystical energy binds the parchment, feathers, pelts, and spider silk* ... Behold! Your scroll bag is ready and bound to your spirit!");
				player.notifyWorldAboutChanges();
			}
		};

		npc.add(ConversationStates.ATTENDING,
				TRIGGER_WORDS,
				hasAllMaterials,
				ConversationStates.ATTENDING,
				null,
				completeQuestAction);

		// 3. Started & missing materials: reminder
		npc.add(ConversationStates.ATTENDING,
				TRIGGER_WORDS,
				new QuestInStateCondition(QUEST_SLOT, "started"),
				ConversationStates.ATTENDING,
				"You do not possess all required materials yet. I still require 2 #empty #scrolls (or #blank #scrolls), 2 #small #feathers, 2 #pelts, and 2 #silk #glands.",
				null);

		// 4. Completed:
		npc.add(ConversationStates.ATTENDING,
				TRIGGER_WORDS,
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Your scroll bag is already active! Keep your teleportation and spell scrolls safely stored within.",
				null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"The Scroll Bag",
				"Magician Haizen in his house outside Ados can enchant a dedicated scroll bag if you bring him 2 empty scrolls, 2 small feathers, 2 pelts, and 2 silk glands.",
				false);
		buildQuestDialog();
	}

	@Override
	public boolean removeFromWorld() {
		return false;
	}

	@Override
	public String getName() {
		return "TheScrollBag";
	}

	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}

	@Override
	public String getNPCName() {
		return NPC_NAME;
	}
}
