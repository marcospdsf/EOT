package games.stendhal.server.maps.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EnableFeatureAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;

/**
 * Quest to unlock the Weapon Bag.
 */
public class TheWeaponBag extends AbstractQuest {

	private static final String QUEST_SLOT = "the_weapon_bag";
	private static final String NPC_NAME = "Hayunn Naratha";
	private static final List<String> TRIGGER_WORDS = Arrays.asList("weapon bag", "weaponbag", "weapons bag", "weapon pouch");

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
		res.add("I spoke with Hayunn Naratha in the Semos Guardhouse about acquiring a weapon bag.");
		final String state = player.getQuest(QUEST_SLOT);
		if (player.isQuestInState(QUEST_SLOT, "started", "done")) {
			res.add("Hayunn asked me to bring 1 dagger, 2 iron bars, and 2 pelts so he can craft a weapon bag for me.");
		}
		if ("done".equals(state)) {
			res.add("I brought the materials to Hayunn Naratha, and he crafted and unlocked the weapon bag for me!");
		}
		return res;
	}

	private void buildQuestDialog() {
		final SpeakerNPC npc = npcs.get(NPC_NAME);
		if (npc == null) {
			return;
		}

		// Turn-in reward definition
		final List<ChatAction> rewards = new ArrayList<ChatAction>();
		rewards.add(new DropItemAction("dagger", 1));
		rewards.add(new DropItemAction("iron bar", 2));
		rewards.add(new DropItemAction("pelt", 2));
		rewards.add(new EnableFeatureAction("weaponbag", "3 1"));
		rewards.add(new IncreaseXPAction(500));
		rewards.add(new SetQuestAndModifyKarmaAction(QUEST_SLOT, "done", 25));

		// 1. When quest is NOT started: asking about "weapon bag" introduces and starts it immediately
		npc.add(ConversationStates.ATTENDING,
				TRIGGER_WORDS,
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"A true adventurer needs quick access to multiple weapons! Bring me 1 #dagger, 2 #iron #bars, and 2 #pelts, and I'll assemble a specialized #weapon #bag for you.",
				new SetQuestAction(QUEST_SLOT, "started"));

		// 2. When quest is STARTED and player HAS all materials: turn-in
		npc.add(ConversationStates.ATTENDING,
				TRIGGER_WORDS,
				new AndCondition(
						new QuestInStateCondition(QUEST_SLOT, "started"),
						new PlayerHasItemWithHimCondition("dagger", 1),
						new PlayerHasItemWithHimCondition("iron bar", 2),
						new PlayerHasItemWithHimCondition("pelt", 2)),
				ConversationStates.ATTENDING,
				"Excellent! Give me just a moment ... hammering the buckles ... stitching the straps ... There you go! Your weapon bag is unlocked and ready to use!",
				new MultipleActions(rewards));

		// 3. When quest is STARTED but player is MISSING materials: reminder
		npc.add(ConversationStates.ATTENDING,
				TRIGGER_WORDS,
				new QuestInStateCondition(QUEST_SLOT, "started"),
				ConversationStates.ATTENDING,
				"You don't have all the materials yet. I still need 1 #dagger, 2 #iron #bars, and 2 #pelts.",
				null);

		// 4. When quest is DONE: completed message
		npc.add(ConversationStates.ATTENDING,
				TRIGGER_WORDS,
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Your weapon bag should serve you well in battle. Keep your blades sharp!",
				null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"The Weapon Bag",
				"Hayunn Naratha in the Semos Guardhouse can craft a specialized weapon bag if you bring him a dagger, 2 iron bars, and 2 pelts.",
				false);
		buildQuestDialog();
	}

	@Override
	public boolean removeFromWorld() {
		return false;
	}

	@Override
	public String getName() {
		return "TheWeaponBag";
	}

	@Override
	public String getRegion() {
		return Region.SEMOS_CITY;
	}

	@Override
	public String getNPCName() {
		return NPC_NAME;
	}
}
