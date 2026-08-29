package games.stendhal.server.actions.equip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import games.stendhal.common.EquipActionConsts;
import games.stendhal.server.actions.ActionListener;
import games.stendhal.server.actions.CommandCenter;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.item.Corpse;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.slot.EntitySlot;
import games.stendhal.server.util.EntityHelper;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPSlot;

/**
 * Sorts the items within a container slot by type and name.
 */
public class SortSlotAction implements ActionListener {

	private static final Logger logger = Logger.getLogger(SortSlotAction.class);

	private static final List<String> sortableSlots = Arrays.asList(
			"content", "bag", "weaponbag", "scrollbag", "keyring", "portfolio");

	private static final Map<String, Long> lastSortTimes = new HashMap<String, Long>();

	public static void register() {
		CommandCenter.register("sort_slot", new SortSlotAction());
	}

	@Override
	public void onAction(final Player player, final RPAction action) {
		// Anti-spam cooldown: 1 second per player
		final String playerName = player.getName();
		final long now = System.currentTimeMillis();
		final Long lastSort = lastSortTimes.get(playerName);
		if (lastSort != null && (now - lastSort) < 1000) {
			return;
		}
		lastSortTimes.put(playerName, now);

		List<String> targetPath = null;
		if (action.has("target_path")) {
			targetPath = action.getList("target_path");
		} else if (action.has(EquipActionConsts.SOURCE_PATH)) {
			targetPath = action.getList(EquipActionConsts.SOURCE_PATH);
		}
		if (targetPath == null || targetPath.isEmpty()) {
			return;
		}

		final Entity containerEntity = EntityHelper.getEntityFromPath(player, targetPath);
		if (containerEntity == null) {
			return;
		}

		final String slotName = action.has("slot") ? action.get("slot") : "content";
		if (!sortableSlots.contains(slotName) || !containerEntity.hasSlot(slotName)) {
			return;
		}

		final RPSlot slot = containerEntity.getSlot(slotName);
		if (!mayAccess(player, containerEntity, slot)) {
			return;
		}

		sortSlot(containerEntity, slot);
	}

	private void sortSlot(final Entity containerEntity, final RPSlot slot) {
		if (slot.size() <= 1) {
			return;
		}

		final List<RPObject> objects = new ArrayList<RPObject>(slot.size());
		for (final RPObject obj : slot) {
			objects.add(obj);
		}

		Collections.sort(objects, new Comparator<RPObject>() {
			@Override
			public int compare(final RPObject o1, final RPObject o2) {
				final int cat1 = getCategoryRank(o1);
				final int cat2 = getCategoryRank(o2);
				if (cat1 != cat2) {
					return Integer.compare(cat1, cat2);
				}

				// Secondary: Name A-Z
				final String name1 = o1.has("name") ? o1.get("name") : "";
				final String name2 = o2.has("name") ? o2.get("name") : "";
				final int nameCompare = name1.compareToIgnoreCase(name2);
				if (nameCompare != 0) {
					return nameCompare;
				}

				// Tertiary: Quantity descending
				final int q1 = o1.has("quantity") ? o1.getInt("quantity") : 1;
				final int q2 = o2.has("quantity") ? o2.getInt("quantity") : 1;
				return Integer.compare(q2, q1);
			}
		});

		slot.clear();
		for (final RPObject obj : objects) {
			slot.addPreservingId(obj);
		}

		containerEntity.notifyWorldAboutChanges();
	}

	private int getCategoryRank(final RPObject obj) {
		final String clazz = obj.has("class") ? obj.get("class").toLowerCase() : "";
		final String subclass = obj.has("subclass") ? obj.get("subclass").toLowerCase() : "";

		if ("weapon".equals(clazz) || "sword".equals(clazz) || "axe".equals(clazz) || "club".equals(clazz) || "bow".equals(clazz) || "dagger".equals(clazz) || "spear".equals(clazz)) {
			return 1;
		}
		if ("shield".equals(clazz) || "shield".equals(subclass)) {
			return 2;
		}
		if ("armor".equals(clazz) || "helmet".equals(clazz) || "legs".equals(clazz) || "boots".equals(clazz) || "cloak".equals(clazz) || "gloves".equals(clazz) || "glove".equals(clazz)) {
			return 3;
		}
		if ("ring".equals(clazz) || "amulet".equals(clazz)) {
			return 4;
		}
		if ("potion".equals(clazz) || "drink".equals(clazz) || "food".equals(clazz)) {
			return 5;
		}
		if ("ore".equals(clazz) || "gem".equals(clazz) || "dust".equals(clazz) || "herb".equals(clazz) || "wood".equals(clazz) || "bar".equals(clazz)) {
			return 6;
		}
		if ("key".equals(clazz) || "tool".equals(clazz) || "scroll".equals(clazz) || "book".equals(clazz)) {
			return 7;
		}
		if ("money".equals(clazz) || "valuable".equals(clazz)) {
			return 8;
		}
		return 9;
	}

	private boolean mayAccess(final Player player, final Entity containerEntity, final RPSlot slot) {
		if (containerEntity == null || slot == null) {
			return false;
		}
		if (slot instanceof EntitySlot) {
			final EntitySlot entitySlot = (EntitySlot) slot;
			entitySlot.clearErrorMessage();
			if (!entitySlot.isReachableForTakingThingsOutOfBy(player)) {
				return false;
			}
		} else if (containerEntity != player) {
			return false;
		}

		RPObject current = containerEntity;
		while (current != null) {
			if (current instanceof Player && current != player) {
				return false;
			}
			if (current instanceof Corpse) {
				return false;
			}
			current = current.getContainer();
		}
		return true;
	}
}
