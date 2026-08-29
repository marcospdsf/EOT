/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import games.stendhal.common.constants.SoundID;
import games.stendhal.common.constants.SoundLayer;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.core.rule.EntityManager;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.NPC;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;
import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;
import games.stendhal.server.entity.npc.behaviour.journal.MerchantsRegister;
import games.stendhal.server.entity.npc.shop.ItemShopInventory;
import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.events.OpenNpcShopEvent;
import games.stendhal.server.events.SoundEvent;
import marauroa.common.Pair;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPSlot;

/**
 * Handles NPC shop interactions (open shop, close session, and direct GUI trade buy/sell).
 * Tracks active shoppers per NPC and pauses/resumes NPC movement accordingly.
 */
public class NpcShopAction implements ActionListener {

	private static final Logger logger = Logger.getLogger(NpcShopAction.class);

	public static final String ACTION_TYPE = "npc_shop_action";
	public static final String SUB_ACTION_OPEN = "open";
	public static final String SUB_ACTION_CLOSE = "close";
	public static final String SUB_ACTION_TRADE = "trade";

	public static final int MAX_TRADE_DISTANCE = 4;
	public static final int MIN_QUANTITY = 1;
	public static final int MAX_QUANTITY = 1000;
	public static final long MAX_SESSION_TIME_MS = 300_000L; // 5 minutes max per session

	// Active shoppers: NPC name (lowercase) -> Set of player names
	private static final Map<String, Set<String>> activeShoppers = new ConcurrentHashMap<>();
	private static final Map<String, Long> shopperJoinTime = new ConcurrentHashMap<>();
	private static boolean cleanupScheduled = false;

	public static void register() {
		final NpcShopAction action = new NpcShopAction();
		CommandCenter.register(ACTION_TYPE, action);
		ensureCleanupTask();
	}

	private static synchronized void ensureCleanupTask() {
		if (!cleanupScheduled) {
			cleanupScheduled = true;
			schedulePeriodicCleanup();
		}
	}

	private static void schedulePeriodicCleanup() {
		SingletonRepository.getTurnNotifier().notifyInSeconds(10, new TurnListener() {
			@Override
			public void onTurnReached(final int currentTurn) {
				performGlobalShopperCleanup();
				schedulePeriodicCleanup();
			}
		});
	}

	@Override
	public void onAction(final Player player, final RPAction action) {
		final String subAction = action.has("action") ? action.get("action") : action.get("type");
		if (SUB_ACTION_OPEN.equals(subAction) || "open_npc_shop".equals(subAction)) {
			handleOpenShop(player, action);
		} else if (SUB_ACTION_CLOSE.equals(subAction)) {
			handleCloseShop(player, action);
		} else if (SUB_ACTION_TRADE.equals(subAction) || "npc_shop_trade".equals(subAction)) {
			handleTrade(player, action);
		}
	}

	/**
	 * Opens NPC shop for player if proximity and merchant requirements are met.
	 */
	private void handleOpenShop(final Player player, final RPAction action) {
		String targetName = action.has("target") ? action.get("target") : (action.has("npc") ? action.get("npc") : null);
		if (targetName == null || targetName.trim().isEmpty()) {
			return;
		}
		targetName = targetName.trim();

		final NPC npc = findNearbyNpc(player, targetName);
		if (npc == null) {
			player.sendPrivateText("You are not near " + targetName + ".");
			return;
		}

		// Collect sell offers (what NPC sells to player) and buy offers (what NPC buys from player)
		final Map<String, Integer> buyItems = new LinkedHashMap<>();  // items player can buy from NPC
		final Map<String, Integer> sellItems = new LinkedHashMap<>(); // items player can sell to NPC

		collectMerchantOffers(npc.getName(), buyItems, sellItems);

		if (buyItems.isEmpty() && sellItems.isEmpty()) {
			player.sendPrivateText(npc.getName() + " does not trade items.");
			return;
		}

		// Register active shopper and pause NPC movement
		final String npcKey = npc.getName().toLowerCase();
		final Set<String> shoppers = activeShoppers.computeIfAbsent(npcKey, k -> Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>()));
		shoppers.add(player.getName());
		shopperJoinTime.put(player.getName(), System.currentTimeMillis());

		// Halt NPC and face player
		npc.stop();
		npc.faceto(player.getX(), player.getY());

		final String buyData = serializeOffers(buyItems);
		final String sellData = serializeOffers(sellItems);

		player.addEvent(new OpenNpcShopEvent(npc.getName(), buyData, sellData));
		player.notifyWorldAboutChanges();
	}

	/**
	 * Closes the player's shopping session with an NPC.
	 */
	private void handleCloseShop(final Player player, final RPAction action) {
		String npcName = action.has("npc") ? action.get("npc") : (action.has("target") ? action.get("target") : null);
		if (npcName == null || npcName.trim().isEmpty()) {
			return;
		}
		npcName = npcName.trim();
		removeShopperAndCheckResume(player.getName(), npcName);
	}

	/**
	 * Handles a Buy or Sell transaction from the NPC Shop GUI.
	 */
	private void handleTrade(final Player player, final RPAction action) {
		final String npcName = action.get("npc");
		final String mode = action.get("mode"); // "buy" or "sell"
		final String itemName = action.get("item");
		int quantity = action.has("quantity") ? action.getInt("quantity") : 1;

		if (npcName == null || mode == null || itemName == null) {
			return;
		}

		if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
			player.sendPrivateText("Invalid trade quantity: " + quantity);
			return;
		}

		final NPC npc = findNearbyNpc(player, npcName);
		if (npc == null) {
			player.sendPrivateText("You moved too far from " + npcName + ".");
			removeShopperAndCheckResume(player.getName(), npcName);
			return;
		}

		// Refresh shopper presence and keep NPC still
		final String npcKey = npc.getName().toLowerCase();
		final Set<String> shoppers = activeShoppers.computeIfAbsent(npcKey, k -> Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>()));
		shoppers.add(player.getName());
		shopperJoinTime.put(player.getName(), System.currentTimeMillis());
		npc.stop();

		final Map<String, Integer> buyItems = new LinkedHashMap<>();
		final Map<String, Integer> sellItems = new LinkedHashMap<>();
		collectMerchantOffers(npc.getName(), buyItems, sellItems);

		if ("buy".equalsIgnoreCase(mode)) {
			handlePlayerBuy(player, npc, itemName, quantity, buyItems);
		} else if ("sell".equalsIgnoreCase(mode)) {
			handlePlayerSell(player, npc, itemName, quantity, sellItems);
		}
	}

	private void handlePlayerBuy(final Player player, final NPC npc, final String itemName, final int quantity, final Map<String, Integer> buyItems) {
		Integer unitPrice = buyItems.get(itemName.toLowerCase());
		if (unitPrice == null) {
			for (final Map.Entry<String, Integer> entry : buyItems.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(itemName)) {
					unitPrice = entry.getValue();
					break;
				}
			}
		}

		if (unitPrice == null || unitPrice <= 0) {
			player.sendPrivateText(npc.getName() + " does not sell " + itemName + ".");
			return;
		}

		final int totalPrice = unitPrice * quantity;
		if (!player.isEquipped("money", totalPrice)) {
			player.sendPrivateText("You do not have enough money (need " + totalPrice + " coins).");
			return;
		}

		final Item sampleItem = SingletonRepository.getEntityManager().getItem(itemName);
		if (sampleItem == null) {
			player.sendPrivateText("Unknown item: " + itemName);
			return;
		}

		if (sampleItem instanceof StackableItem) {
			final RPSlot slot = player.getSlotToEquip(sampleItem);
			if (slot == null) {
				player.sendPrivateText("You do not have enough inventory space.");
				return;
			}

			player.drop("money", totalPrice);
			final StackableItem bought = (StackableItem) sampleItem;
			bought.setQuantity(quantity);
			player.equipOrPutOnGround(bought);
		} else {
			final int freeSlots = countFreeSlotsForItem(player, sampleItem);
			if (freeSlots < quantity) {
				player.sendPrivateText("You do not have enough inventory space (need " + quantity + " free slots, but only have " + freeSlots + ").");
				return;
			}

			player.drop("money", totalPrice);
			for (int i = 0; i < quantity; i++) {
				final Item singleItem = SingletonRepository.getEntityManager().getItem(itemName);
				player.equipOrPutOnGround(singleItem);
			}
		}

		player.sendPrivateText("You bought " + quantity + " " + itemName + " for " + totalPrice + " coins.");
		player.addEvent(new SoundEvent(SoundID.COMMERCE, SoundLayer.AMBIENT_SOUND));
		player.notifyWorldAboutChanges();
	}

	private void handlePlayerSell(final Player player, final NPC npc, final String itemName, final int quantity, final Map<String, Integer> sellItems) {
		Integer unitPrice = sellItems.get(itemName.toLowerCase());
		if (unitPrice == null) {
			for (final Map.Entry<String, Integer> entry : sellItems.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(itemName)) {
					unitPrice = entry.getValue();
					break;
				}
			}
		}

		if (unitPrice == null || unitPrice <= 0) {
			player.sendPrivateText(npc.getName() + " does not buy " + itemName + ".");
			return;
		}

		if (!player.isEquipped(itemName, quantity)) {
			player.sendPrivateText("You don't have " + quantity + " " + itemName + " to sell.");
			return;
		}

		final int totalPrice = unitPrice * quantity;
		if (!player.drop(itemName, quantity)) {
			player.sendPrivateText("Failed to remove " + itemName + " from your inventory.");
			return;
		}

		final StackableItem money = (StackableItem) SingletonRepository.getEntityManager().getItem("money");
		money.setQuantity(totalPrice);
		player.equipOrPutOnGround(money);

		player.sendPrivateText("You sold " + quantity + " " + itemName + " for " + totalPrice + " coins.");
		player.addEvent(new SoundEvent(SoundID.COMMERCE, SoundLayer.AMBIENT_SOUND));
		player.notifyWorldAboutChanges();
	}

	private NPC findNearbyNpc(final Player player, final String npcName) {
		if (player.getZone() == null) {
			return null;
		}
		for (final NPC npc : player.getZone().getNPCList()) {
			if (npc.getName().equalsIgnoreCase(npcName) || (npc.getTitle() != null && npc.getTitle().equalsIgnoreCase(npcName))) {
				final int dx = Math.abs(player.getX() - npc.getX());
				final int dy = Math.abs(player.getY() - npc.getY());
				if (dx <= MAX_TRADE_DISTANCE && dy <= MAX_TRADE_DISTANCE) {
					return npc;
				}
			}
		}
		return null;
	}

	private static NPC findNpcByName(final String npcName) {
		final SpeakerNPC speaker = SingletonRepository.getNPCList().get(npcName);
		if (speaker != null) {
			return speaker;
		}
		for (final SpeakerNPC s : SingletonRepository.getNPCList()) {
			if (s.getName().equalsIgnoreCase(npcName) || (s.getTitle() != null && s.getTitle().equalsIgnoreCase(npcName))) {
				return s;
			}
		}
		return null;
	}

	public static boolean hasActiveShoppers(final String npcName) {
		if (npcName == null) {
			return false;
		}
		final Set<String> shoppers = activeShoppers.get(npcName.toLowerCase());
		return shoppers != null && !shoppers.isEmpty();
	}

	private static void removeShopperAndCheckResume(final String playerName, final String npcName) {
		final String npcKey = npcName.toLowerCase();
		final Set<String> shoppers = activeShoppers.get(npcKey);
		if (shoppers != null) {
			shoppers.remove(playerName);
			shopperJoinTime.remove(playerName);
			cleanDistantOrOfflineShoppers(npcKey, shoppers);
			if (shoppers.isEmpty()) {
				activeShoppers.remove(npcKey);
				resumeNpcMovement(npcName);
			}
		}
	}

	private static void resumeNpcMovement(final String npcName) {
		final NPC npc = findNpcByName(npcName);
		if (npc != null && npc.hasPath()) {
			npc.setSpeed(npc.getBaseSpeed());
		}
	}

	private static void cleanDistantOrOfflineShoppers(final String npcKey, final Set<String> shoppers) {
		final long now = System.currentTimeMillis();
		final List<String> toRemove = new ArrayList<>();
		final NPC npc = findNpcByName(npcKey);

		for (final String shopperName : shoppers) {
			final Player p = SingletonRepository.getRuleProcessor().getPlayer(shopperName);
			final Long joinTime = shopperJoinTime.get(shopperName);
			if (p == null || p.isDisconnected() || npc == null || p.getZone() != npc.getZone()) {
				toRemove.add(shopperName);
			} else {
				final int dx = Math.abs(p.getX() - npc.getX());
				final int dy = Math.abs(p.getY() - npc.getY());
				if (dx > MAX_TRADE_DISTANCE || dy > MAX_TRADE_DISTANCE) {
					toRemove.add(shopperName);
				} else if (joinTime != null && (now - joinTime > MAX_SESSION_TIME_MS)) {
					toRemove.add(shopperName);
				}
			}
		}

		for (final String name : toRemove) {
			shoppers.remove(name);
			shopperJoinTime.remove(name);
		}
	}

	private static void performGlobalShopperCleanup() {
		final Iterator<Map.Entry<String, Set<String>>> iter = activeShoppers.entrySet().iterator();
		while (iter.hasNext()) {
			final Map.Entry<String, Set<String>> entry = iter.next();
			final String npcKey = entry.getKey();
			final Set<String> shoppers = entry.getValue();
			cleanDistantOrOfflineShoppers(npcKey, shoppers);
			if (shoppers.isEmpty()) {
				iter.remove();
				resumeNpcMovement(npcKey);
			}
		}
	}

	private void collectMerchantOffers(final String npcName, final Map<String, Integer> buyItems, final Map<String, Integer> sellItems) {
		// 1. Check MerchantsRegister
		final MerchantsRegister reg = SingletonRepository.getMerchantsRegister();
		if (reg != null) {
			for (final Pair<String, SellerBehaviour> p : reg.getSellers()) {
				if (p.first().equalsIgnoreCase(npcName)) {
					final SellerBehaviour sb = p.second();
					for (final String item : sb.dealtItems()) {
						buyItems.put(item, sb.getUnitPrice(item));
					}
				}
			}
			for (final Pair<String, BuyerBehaviour> p : reg.getBuyers()) {
				if (p.first().equalsIgnoreCase(npcName)) {
					final BuyerBehaviour bb = p.second();
					for (final String item : bb.dealtItems()) {
						sellItems.put(item, bb.getUnitPrice(item));
					}
				}
			}
		}

		// 2. Check ShopsList (ItemShopInventory)
		final ShopsList shops = ShopsList.get();
		if (shops != null) {
			final ItemShopInventory sellerShop = shops.get(npcName, ShopType.ITEM_SELL);
			if (sellerShop != null) {
				for (final Map.Entry<String, Integer> e : sellerShop.entrySet()) {
					buyItems.putIfAbsent(e.getKey(), e.getValue());
				}
			}
			final ItemShopInventory buyerShop = shops.get(npcName, ShopType.ITEM_BUY);
			if (buyerShop != null) {
				for (final Map.Entry<String, Integer> e : buyerShop.entrySet()) {
					sellItems.putIfAbsent(e.getKey(), e.getValue());
				}
			}
		}
	}

	private String serializeOffers(final Map<String, Integer> offers) {
		final StringBuilder sb = new StringBuilder();
		final EntityManager em = SingletonRepository.getEntityManager();
		for (final Map.Entry<String, Integer> entry : offers.entrySet()) {
			if (sb.length() > 0) {
				sb.append(";");
			}
			final String itemName = entry.getKey();
			final int price = entry.getValue();
			String classPath = "";
			try {
				if (em != null) {
					final Item item = em.getItem(itemName);
					if (item != null) {
						if (item.has("subclass")) {
							classPath = item.getItemClass() + "/" + item.getItemSubclass();
						} else {
							classPath = item.getItemClass();
						}
					}
				}
			} catch (final Exception ignored) {}

			sb.append(itemName).append(":").append(price);
			if (!classPath.isEmpty()) {
				sb.append(":").append(classPath);
			}
		}
		return sb.toString();
	}

	public static int countFreeSlotsForItem(final Player player, final Item item) {
		return countFreeSlotsInSlots(player, item, player.slots());
	}

	private static int countFreeSlotsInSlots(final Player player, final Item item, final Iterable<RPSlot> slots) {
		int count = 0;
		for (final RPSlot slot : slots) {
			if (slot.getName().startsWith("!")) {
				continue;
			}
			if (item.getPossibleSlots().contains(slot.getName())) {
				count += Math.max(0, slot.getCapacity() - slot.size());
			}
			for (final RPObject obj : slot) {
				count += countFreeSlotsInSlots(player, item, obj.slots());
			}
		}
		return count;
	}
}
