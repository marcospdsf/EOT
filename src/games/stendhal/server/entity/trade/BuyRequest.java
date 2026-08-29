/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.entity.trade;

import java.util.Map;

import games.stendhal.server.actions.CStatusAction;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.Definition.Type;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;

/**
 * Represents a Buy Order / Request in the {@link Market}.
 * A player deposits money in escrow to buy a desired quantity of an item at a fixed price per piece.
 * Other players can fulfill all or part of the requested quantity.
 */
public class BuyRequest extends Entity implements Dateable {

	public static final String BUY_REQUEST_RPCLASS_NAME = "buy_request";

	private static final String BUYER_ATTRIBUTE_NAME = "buyer";
	private static final String BUYER_CID_ATTRIBUTE = "buyer_cid";
	private static final String ITEM_NAME_ATTRIBUTE = "item_name";
	private static final String ITEM_CLASS_ATTRIBUTE = "item_class";
	private static final String TOTAL_QUANTITY_ATTRIBUTE = "total_quantity";
	private static final String FULFILLED_QUANTITY_ATTRIBUTE = "fulfilled_quantity";
	private static final String PRICE_PER_PIECE_ATTRIBUTE = "price_per_piece";
	private static final String ESCROW_MONEY_ATTRIBUTE = "escrow_money";
	private static final String TIMESTAMP_ATTRIBUTE = "timestamp";

	public static void generateRPClass() {
		final RPClass clazz = new RPClass(BUY_REQUEST_RPCLASS_NAME);
		clazz.isA("entity");
		clazz.addAttribute(BUYER_ATTRIBUTE_NAME, Type.STRING);
		clazz.addAttribute(BUYER_CID_ATTRIBUTE, Type.STRING);
		clazz.addAttribute(ITEM_NAME_ATTRIBUTE, Type.STRING);
		clazz.addAttribute(ITEM_CLASS_ATTRIBUTE, Type.STRING);
		clazz.addAttribute(TOTAL_QUANTITY_ATTRIBUTE, Type.INT);
		clazz.addAttribute(FULFILLED_QUANTITY_ATTRIBUTE, Type.INT);
		clazz.addAttribute(PRICE_PER_PIECE_ATTRIBUTE, Type.INT);
		clazz.addAttribute(ESCROW_MONEY_ATTRIBUTE, Type.INT);
		clazz.addAttribute(TIMESTAMP_ATTRIBUTE, Type.STRING);
	}

	public BuyRequest(final Player buyer, final String itemName, final String itemClass,
			final int totalQuantity, final int pricePerPiece) {
		super();
		setRPClass(BUY_REQUEST_RPCLASS_NAME);
		hide();

		put(BUYER_ATTRIBUTE_NAME, buyer.getName());
		put(BUYER_CID_ATTRIBUTE, getPlayerCID(buyer));
		put(ITEM_NAME_ATTRIBUTE, itemName);
		put(ITEM_CLASS_ATTRIBUTE, itemClass != null ? itemClass : "");
		put(TOTAL_QUANTITY_ATTRIBUTE, totalQuantity);
		put(FULFILLED_QUANTITY_ATTRIBUTE, 0);
		put(PRICE_PER_PIECE_ATTRIBUTE, pricePerPiece);
		put(ESCROW_MONEY_ATTRIBUTE, totalQuantity * pricePerPiece);
		updateTimestamp();
	}

	public BuyRequest(final RPObject object) {
		super(object);
		setRPClass(BUY_REQUEST_RPCLASS_NAME);
		hide();
	}

	private String getPlayerCID(final Player player) {
		Map<String, String> nameList = CStatusAction.nameList;
		String cid = nameList.get(player.getName());
		if (cid == null) {
			return "";
		}
		return cid;
	}

	public String getBuyer() {
		return get(BUYER_ATTRIBUTE_NAME);
	}

	public String getBuyerCID() {
		return get(BUYER_CID_ATTRIBUTE);
	}

	public String getItemName() {
		return get(ITEM_NAME_ATTRIBUTE);
	}

	public String getItemClass() {
		return get(ITEM_CLASS_ATTRIBUTE);
	}

	public int getTotalQuantity() {
		return getInt(TOTAL_QUANTITY_ATTRIBUTE);
	}

	public int getFulfilledQuantity() {
		return getInt(FULFILLED_QUANTITY_ATTRIBUTE);
	}

	public int getRemainingQuantity() {
		return Math.max(0, getTotalQuantity() - getFulfilledQuantity());
	}

	public int getPricePerPiece() {
		return getInt(PRICE_PER_PIECE_ATTRIBUTE);
	}

	public int getEscrowMoney() {
		return getInt(ESCROW_MONEY_ATTRIBUTE);
	}

	public boolean isFulfilled() {
		return getFulfilledQuantity() >= getTotalQuantity();
	}

	/**
	 * Records partial or full fulfillment of this buy request.
	 *
	 * @param quantity amount of items fulfilled
	 * @return amount of escrow money consumed
	 */
	public int fulfill(final int quantity) {
		final int remaining = getRemainingQuantity();
		final int toFulfill = Math.min(quantity, remaining);
		final int newFulfilled = getFulfilledQuantity() + toFulfill;
		final int moneyPaid = toFulfill * getPricePerPiece();
		final int newEscrow = Math.max(0, getEscrowMoney() - moneyPaid);

		put(FULFILLED_QUANTITY_ATTRIBUTE, newFulfilled);
		put(ESCROW_MONEY_ATTRIBUTE, newEscrow);
		return moneyPaid;
	}

	public void updateTimestamp() {
		put(TIMESTAMP_ATTRIBUTE, Long.toString(System.currentTimeMillis()));
	}

	@Override
	public long getTimestamp() {
		if (has(TIMESTAMP_ATTRIBUTE)) {
			try {
				return Long.parseLong(get(TIMESTAMP_ATTRIBUTE));
			} catch (NumberFormatException e) {
				return 0L;
			}
		}
		return 0L;
	}

	@Override
	public String describe() {
		return getRemainingQuantity() + " " + getItemName() + " @ " + getPricePerPiece() + " gold each ("
				+ getFulfilledQuantity() + "/" + getTotalQuantity() + " fulfilled)";
	}
}
