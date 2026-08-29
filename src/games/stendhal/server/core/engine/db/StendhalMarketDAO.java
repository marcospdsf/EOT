/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.core.engine.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.trade.BuyRequest;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.entity.trade.Offer;
import marauroa.server.db.DBTransaction;

/**
 * Database Access Object (DAO) for managing persistent market buy requests,
 * sell offers, and trade history for the Stendhal game engine and website.
 */
public class StendhalMarketDAO {

	private static final Logger logger = Logger.getLogger(StendhalMarketDAO.class);

	/**
	 * Logs a completed market trade (buy offer purchase or buy request fulfillment).
	 *
	 * @param transaction DBTransaction
	 * @param tradeType type of trade ('SELL_OFFER_BUY' or 'BUY_ORDER_FULFILL')
	 * @param buyer buyer character name
	 * @param seller seller character name
	 * @param itemName name of item traded
	 * @param quantity quantity traded
	 * @param pricePerPiece price per piece
	 * @param totalPrice total gold exchanged
	 * @param feePaid trading fee paid
	 * @throws SQLException on database error
	 */
	public void logMarketTrade(final DBTransaction transaction, final String tradeType,
			final String buyer, final String seller, final String itemName,
			final int quantity, final int pricePerPiece, final int totalPrice,
			final int feePaid) throws SQLException {
		final String sql = "INSERT INTO market_trade_history (trade_type, buyer_name, seller_name, "
				+ "item_name, quantity, price_per_piece, total_price, fee_paid, traded_at) "
				+ "VALUES ('[trade_type]', '[buyer_name]', '[seller_name]', '[item_name]', "
				+ "[quantity], [price_per_piece], [total_price], [fee_paid], '[traded_at]')";

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("trade_type", tradeType);
		params.put("buyer_name", buyer);
		params.put("seller_name", seller);
		params.put("item_name", itemName);
		params.put("quantity", quantity);
		params.put("price_per_piece", pricePerPiece);
		params.put("total_price", totalPrice);
		params.put("fee_paid", feePaid);
		params.put("traded_at", new Timestamp(System.currentTimeMillis()));

		transaction.execute(sql, params);
	}

	/**
	 * Synchronizes an active or completed buy request to the database.
	 *
	 * @param transaction DBTransaction
	 * @param request BuyRequest
	 * @param active whether request is currently active
	 * @throws SQLException on database error
	 */
	public void syncBuyRequest(final DBTransaction transaction, final BuyRequest request, final boolean active)
			throws SQLException {
		final String sql = "INSERT INTO market_buy_requests (buyer_name, item_name, item_class, "
				+ "total_quantity, fulfilled_quantity, price_per_piece, active, created_at, expires_at) "
				+ "VALUES ('[buyer_name]', '[item_name]', '[item_class]', [total_quantity], "
				+ "[fulfilled_quantity], [price_per_piece], [active], '[created_at]', '[expires_at]')";

		final long now = System.currentTimeMillis();
		final long expiry = now + (7L * 24L * 60L * 60L * 1000L); // 7 days default

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("buyer_name", request.getBuyer());
		params.put("item_name", request.getItemName());
		params.put("item_class", request.getItemClass());
		params.put("total_quantity", request.getTotalQuantity());
		params.put("fulfilled_quantity", request.getFulfilledQuantity());
		params.put("price_per_piece", request.getPricePerPiece());
		params.put("active", active ? 1 : 0);
		params.put("created_at", new Timestamp(now));
		params.put("expires_at", new Timestamp(expiry));

		transaction.execute(sql, params);
	}

	/**
	 * Synchronizes an active or sold sell offer to the database.
	 *
	 * @param transaction DBTransaction
	 * @param offer Offer
	 * @param active whether offer is currently active
	 * @throws SQLException on database error
	 */
	public void syncSellOffer(final DBTransaction transaction, final Offer offer, final boolean active)
			throws SQLException {
		if (offer == null || !offer.hasItem()) {
			return;
		}
		final Item item = offer.getItem();
		int qty = 1;
		boolean stackable = false;
		if (item instanceof StackableItem) {
			qty = ((StackableItem) item).getQuantity();
			stackable = true;
		}

		String classPath = "";
		if (item.has("class")) {
			classPath = item.get("class");
			if (item.has("subclass")) {
				classPath += "/" + item.get("subclass");
			}
		}

		final String sql = "INSERT INTO market_sell_offers (seller_name, item_name, item_class, "
				+ "quantity, price, is_per_piece, active, created_at, expires_at) "
				+ "VALUES ('[seller_name]', '[item_name]', '[item_class]', [quantity], "
				+ "[price], [is_per_piece], [active], '[created_at]', '[expires_at]')";

		final long now = System.currentTimeMillis();
		final long expiry = now + (7L * 24L * 60L * 60L * 1000L);

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("seller_name", offer.getOfferer());
		params.put("item_name", offer.getItemName());
		params.put("item_class", classPath);
		params.put("quantity", qty);
		params.put("price", offer.getPrice().intValue());
		params.put("is_per_piece", stackable ? 1 : 0);
		params.put("active", active ? 1 : 0);
		params.put("created_at", new Timestamp(now));
		params.put("expires_at", new Timestamp(expiry));

		transaction.execute(sql, params);
	}

	/**
	 * Dumps complete active state of the market to the database for website exposure.
	 *
	 * @param transaction DBTransaction
	 * @param market Market instance
	 * @throws SQLException on database error
	 */
	public void dumpMarket(final DBTransaction transaction, final Market market) throws SQLException {
		if (market == null) {
			return;
		}

		// Clear stale entries for a clean live snapshot
		transaction.execute("DELETE FROM market_buy_requests", null);
		transaction.execute("DELETE FROM market_sell_offers", null);

		// Dump active buy requests
		for (final BuyRequest req : market.getBuyRequestsOlderThan(0)) {
			syncBuyRequest(transaction, req, true);
		}

		// Dump active sell offers
		for (final Offer offer : market.getOffersOlderThan(0)) {
			syncSellOffer(transaction, offer, true);
		}
	}
}
