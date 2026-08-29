package games.stendhal.server.maps.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.stendhal.common.NotificationType;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;

/**
 * Quest / Ritual to purify the Dark Excalibur into the legendary Excalibur.
 */
public class PurifyDarkExcalibur extends AbstractQuest {

	private static final String QUEST_SLOT = "purify_dark_excalibur";
	private static final String NPC_NAME = "Erastus";
	public static final int REQUIRED_ABSORBED_HP = 50000;

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
		res.add("Archmage Erastus in the Semos Wizard Tower spoke of purifying the cursed Dark Excalibur once it has absorbed 50,000 HP of vitality.");
		if (player.isQuestInState(QUEST_SLOT, "done")) {
			res.add("I brought a fully saturated Dark Excalibur to Archmage Erastus, and he cleansed the curse, restoring the legendary Excalibur!");
		}
		return res;
	}

	private Item getEquippedOrHeldDarkExcalibur(final Player player) {
		Item item = player.getFirstEquipped("dark excalibur");
		if (item == null) {
			item = player.getFirstEquipped("darker excalibur");
		}
		return item;
	}

	private int getAbsorbedHP(final Item sword) {
		if (sword == null || !sword.has("itemdata")) {
			return 0;
		}
		try {
			return Integer.parseInt(sword.get("itemdata"));
		} catch (final NumberFormatException e) {
			return 0;
		}
	}

	private void buildQuestDialog() {
		final SpeakerNPC npc = npcs.get(NPC_NAME);
		if (npc == null) {
			return;
		}

		final List<String> triggers = Arrays.asList("excalibur", "dark excalibur", "darker excalibur", "purify");

		// Information / Inquiry (No dark excalibur carried)
		npc.add(ConversationStates.ATTENDING,
				triggers,
				new ChatCondition() {
					@Override
					public boolean fire(final Player player, final Sentence sentence, final Entity entity) {
						final Item sword = getEquippedOrHeldDarkExcalibur(player);
						return sword == null;
					}
				},
				ConversationStates.ATTENDING,
				"The Dark Excalibur is a blade born of ancient betrayal. It drains its wielder's vitality in battle. If you ever possess one that has absorbed at least 50,000 HP of vitality, bring it to me and I will #purify it into the true #Excalibur.",
				null);

		// Not enough absorbed HP
		npc.add(ConversationStates.ATTENDING,
				triggers,
				new ChatCondition() {
					@Override
					public boolean fire(final Player player, final Sentence sentence, final Entity entity) {
						final Item sword = getEquippedOrHeldDarkExcalibur(player);
						if (sword == null) {
							return false;
						}
						return getAbsorbedHP(sword) < REQUIRED_ABSORBED_HP;
					}
				},
				ConversationStates.ATTENDING,
				null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						final Item sword = getEquippedOrHeldDarkExcalibur(player);
						final int absorbed = getAbsorbedHP(sword);
						raiser.say("I sense the cursed power within your blade, but it has only absorbed " + absorbed + " / " + REQUIRED_ABSORBED_HP + " HP. You must wield it in battle until it is fully saturated!");
					}
				});

		// Purification Ready
		npc.add(ConversationStates.ATTENDING,
				triggers,
				new ChatCondition() {
					@Override
					public boolean fire(final Player player, final Sentence sentence, final Entity entity) {
						final Item sword = getEquippedOrHeldDarkExcalibur(player);
						if (sword == null) {
							return false;
						}
						return getAbsorbedHP(sword) >= REQUIRED_ABSORBED_HP;
					}
				},
				ConversationStates.ATTENDING,
				null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						final Item sword = getEquippedOrHeldDarkExcalibur(player);
						if (sword == null) {
							return;
						}

						// Remove the dark excalibur
						player.drop(sword);

						// Grant the purified Excalibur
						new EquipItemAction("excalibur").fire(player, sentence, raiser);

						player.setQuest(QUEST_SLOT, "done");
						player.addKarma(50);
						player.addXP(1000);
						player.sendPrivateText(NotificationType.POSITIVE, "The curse shatters! You receive the legendary Excalibur!");

						raiser.say("Incredible! The blade has drunk " + REQUIRED_ABSORBED_HP + " HP of vitality, breaking its ancient blood curse! *Erastus channels blinding radiant light through the steel* ... Behold, the true Excalibur is reborn!");
					}
				});
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Purification of Excalibur",
				"Archmage Erastus in the Semos Wizard Tower can cleanse the cursed Dark Excalibur once it has absorbed 50,000 HP of vitality in battle.",
				false);
		buildQuestDialog();
	}

	@Override
	public boolean removeFromWorld() {
		return false;
	}

	@Override
	public String getName() {
		return "PurifyDarkExcalibur";
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
