/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.semos.tavern.market;

import java.util.ArrayList;
import java.util.List;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.events.ShowMarketOffersEvent;

/**
 * Handles claiming delivered items from Harold's delivery / collection box.
 */
public class ClaimItemsHandler {

	public void add(SpeakerNPC npc) {
		npc.add(ConversationStates.ATTENDING, "claim", null, ConversationStates.ATTENDING, null,
				new ClaimItemsChatAction());
	}

	private class ClaimItemsChatAction implements ChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			Market shop = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if (shop == null) {
				return;
			}

			List<Item> claimed = shop.claimItems(player);
			if (claimed.isEmpty()) {
				npc.say("You have no items waiting in your collection box to claim.");
			} else {
				List<String> descriptions = new ArrayList<>();
				for (Item item : claimed) {
					int qty = 1;
					if (item instanceof StackableItem) {
						qty = ((StackableItem) item).getQuantity();
					}
					descriptions.add(Grammar.quantityplnoun(qty, item.getName(), "a"));
				}
				npc.say("Here are the items you bought through your requests: " + Grammar.enumerateCollection(descriptions) + ".");
				player.sendPrivateText("You claimed your items from Harold.");
				player.addEvent(new ShowMarketOffersEvent(shop, player));
			}
		}
	}
}
