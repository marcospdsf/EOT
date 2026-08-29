/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.entity.trade;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.Log4J;
import marauroa.server.game.db.DatabaseFactory;
import utilities.PlayerTestHelper;
import utilities.RPClass.ItemTestHelper;

public class MarketBuyRequestTest {

	@BeforeClass
	public static void beforeClass() {
		Log4J.init();
		try {
			new DatabaseFactory().initializeDatabase();
		} catch (Exception e) {}
		MockStendlRPWorld.get();
		ItemTestHelper.generateRPClasses();
	}

	@Test
	public void testCreateBuyRequestAndEscrowDeduction() {
		StendhalRPZone zone = new StendhalRPZone("test_market_zone_1");
		Market market = Market.createShop();
		zone.add(market);

		Player buyer = PlayerTestHelper.createPlayer("alice");
		PlayerTestHelper.registerPlayer(buyer, zone);
		buyer.equipToInventoryOnly(ItemTestHelper.createItem("money", 30000));

		// Alice wants 1,000 meat @ 25 gold each = 25,000 escrow + 250 fee = 25,250 total
		BuyRequest req = market.createBuyRequest(buyer, "meat", "food/meat", 1000, 25);
		assertThat(req, notNullValue());
		assertEquals("alice", req.getBuyer());
		assertEquals("meat", req.getItemName());
		assertEquals(1000, req.getTotalQuantity());
		assertEquals(0, req.getFulfilledQuantity());
		assertEquals(1000, req.getRemainingQuantity());
		assertEquals(25, req.getPricePerPiece());
		assertEquals(25000, req.getEscrowMoney());

		// Verify escrow money was deducted from buyer
		int remainingMoney = buyer.getTotalNumberOf("money");
		assertEquals(30000 - 25250, remainingMoney);
		assertTrue(market.contains(req));
	}

	@Test
	public void testPartialFulfillmentsAndClaiming() {
		StendhalRPZone zone = new StendhalRPZone("test_market_zone_2");
		Market market = Market.createShop();
		zone.add(market);

		Player buyer = PlayerTestHelper.createPlayer("buyer_bob");
		PlayerTestHelper.registerPlayer(buyer, zone);
		buyer.equipToInventoryOnly(ItemTestHelper.createItem("money", 30000));

		// Create buy request for 1,000 meat @ 25 gold each
		BuyRequest req = market.createBuyRequest(buyer, "meat", "food/meat", 1000, 25);
		assertThat(req, notNullValue());

		// Seller 1 sells 200 meat
		Player seller1 = PlayerTestHelper.createPlayer("seller_sam");
		PlayerTestHelper.registerPlayer(seller1, zone);
		seller1.equipToInventoryOnly(ItemTestHelper.createItem("meat", 200));

		int fulfilled1 = market.fulfillBuyRequest(seller1, req, 200);
		assertEquals(200, fulfilled1);
		assertEquals(0, seller1.getTotalNumberOf("meat"));
		assertEquals(200, req.getFulfilledQuantity());
		assertEquals(800, req.getRemainingQuantity());
		assertEquals(20000, req.getEscrowMoney());

		// Seller 1 receives money immediately upon fulfillment (200 * 25 = 5,000 gold)
		assertEquals(5000, seller1.getTotalNumberOf("money"));

		// Seller 2 sells 300 meat
		Player seller2 = PlayerTestHelper.createPlayer("seller_sue");
		PlayerTestHelper.registerPlayer(seller2, zone);
		seller2.equipToInventoryOnly(ItemTestHelper.createItem("meat", 500));

		int fulfilled2 = market.fulfillBuyRequest(seller2, req, 300);
		assertEquals(300, fulfilled2);
		assertEquals(200, seller2.getTotalNumberOf("meat")); // 500 - 300 = 200 remaining
		assertEquals(500, req.getFulfilledQuantity());
		assertEquals(500, req.getRemainingQuantity());
		assertEquals(12500, req.getEscrowMoney());

		// Seller 2 receives money immediately upon fulfillment (300 * 25 = 7,500 gold)
		assertEquals(7500, seller2.getTotalNumberOf("money"));

		// Buyer claims delivered items (200 + 300 = 500 meat)
		List<Item> claimed = market.claimItems(buyer);
		assertEquals(2, claimed.size());
		assertEquals(500, buyer.getTotalNumberOf("meat"));

		// Buyer cancels remaining 500 meat wanted
		int refund = market.cancelBuyRequest(buyer, req);
		assertEquals(12500, refund);

		// Buyer fetches refund money
		market.fetchEarnings(buyer);
		// Initial 30,000 - 25,250 (escrow+fee) + 12,500 (refund) = 17,250
		assertEquals(17250, buyer.getTotalNumberOf("money"));
	}

	@Test
	public void testFullFulfillmentRemovesFromActive() {
		StendhalRPZone zone = new StendhalRPZone("test_market_zone_3");
		Market market = Market.createShop();
		zone.add(market);

		Player buyer = PlayerTestHelper.createPlayer("buyer_dave");
		PlayerTestHelper.registerPlayer(buyer, zone);
		buyer.equipToInventoryOnly(ItemTestHelper.createItem("money", 5000));

		BuyRequest req = market.createBuyRequest(buyer, "short sword", "weapon/sword/short_sword", 2, 500);
		assertThat(req, notNullValue());

		Player seller = PlayerTestHelper.createPlayer("seller_dan");
		PlayerTestHelper.registerPlayer(seller, zone);
		seller.equipToInventoryOnly(ItemTestHelper.createItem("short sword"));

		int fulfilled = market.fulfillBuyRequest(seller, req, 1);
		assertEquals(1, fulfilled);
		assertEquals(1, req.getRemainingQuantity());
		assertEquals(500, req.getEscrowMoney());
		assertTrue(market.contains(req));

		seller.equipToInventoryOnly(ItemTestHelper.createItem("short sword"));
		int fulfilled2 = market.fulfillBuyRequest(seller, req, 1);
		assertEquals(1, fulfilled2);
		assertTrue(req.isFulfilled());
		assertEquals(0, req.getRemainingQuantity());
		assertEquals(0, req.getEscrowMoney());

		// Request is now complete and removed from active requests
		assertThat(Boolean.valueOf(market.contains(req)), is(Boolean.FALSE));
	}

	@Test
	public void testAutomaticBuyRequestExpirationAndRefund() {
		StendhalRPZone zone = new StendhalRPZone("test_market_zone_4");
		Market market = Market.createShop();
		zone.add(market);

		Player buyer = PlayerTestHelper.createPlayer("buyer_expirer");
		PlayerTestHelper.registerPlayer(buyer, zone);
		buyer.equipToInventoryOnly(ItemTestHelper.createItem("money", 10000));

		BuyRequest req = market.createBuyRequest(buyer, "leather armor", "armor/chest/leather_armor", 1, 500);
		assertThat(req, notNullValue());
		assertTrue(market.contains(req));

		// Fake timestamp to 8 days ago (> 7 days)
		req.put("timestamp", String.valueOf(System.currentTimeMillis() - 8 * 86400 * 1000L));

		games.stendhal.server.maps.semos.tavern.market.OfferExpirer expirer =
				new games.stendhal.server.maps.semos.tavern.market.OfferExpirer(market);
		expirer.checkExpiredBuyRequests();

		// Verify buy request is removed from active slot
		assertThat(Boolean.valueOf(market.contains(req)), is(Boolean.FALSE));

		// Verify remaining deposit of 500 money is in buyer's earnings
		assertTrue(market.hasEarningsFor(buyer));
		market.fetchEarnings(buyer);
		// Initial 10,000 - 505 (500 + 5 fee) + 500 (refund) = 9,995
		assertEquals(9995, buyer.getTotalNumberOf("money"));
	}
}
