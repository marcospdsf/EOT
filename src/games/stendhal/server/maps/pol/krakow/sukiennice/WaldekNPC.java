package games.stendhal.server.maps.pol.krakow.sukiennice;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.mapstuff.sign.Sign;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Inside Semos Tavern - Level 0 (ground floor)
 */
public class WaldekNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Waldek") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Good day!");
				addHelp("I purchase orchard fruits. You can find my full offer in the book on the counter.");
				ShopsList.get().configureNPC(this, "buyowoce", ShopType.ITEM_BUY, false);
				addOffer("I buy fresh fruit; my price list is written in the ledger.");
				addGoodbye();
			}
		};

		npc.setDescription("You see Waldek the fruit merchant.");
		npc.setEntityClass("man_007_npc");
		npc.setPosition(26, 17);
		npc.setDirection(Direction.LEFT);
		npc.initHP(100);
		zone.add(npc);

		// Add a book with the shop offers
		final Sign book = new Sign();
		book.setPosition(24, 15);
		book.setText(" -- Buying -- \n cherry\t\t 8\n apple\t\t 5\n strawberry\t 8\n pear\t\t 6\n coconut\t\t 7\n pineapple\t 8");
		book.setEntityClass("book_red");
		book.setResistance(10);
		zone.add(book);
	}
}
