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
public class IrekNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Irek") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Just doing business like anyone else, as long as there are customers.");
				addHelp("I sell various fresh vegetables. There is a ledger on the table with my full price list.");
				ShopsList.get().configureNPC(this, "sellwarzywairek", ShopType.ITEM_SELL, false);
				addOffer("I sell fresh produce. You can find my current stock in the ledger on the table.");
				addQuest("I have no time for chores right now.");
				addGoodbye();
			}
		};

		npc.setDescription("You see Irek the greengrocer.");
		npc.setEntityClass("man_000_npc");
		npc.setPosition(26, 4);
		npc.setDirection(Direction.LEFT);
		npc.initHP(100);
		zone.add(npc);

		// Add a book with the shop offers
		final Sign book = new Sign();
		book.setPosition(24, 4);
		book.setText(" -- For Sale -- \n carrot\t 5\n lettuce\t 10\n tomato\t 20\n chinese cabbage\t 25\n zucchini\t 30\n porcini\t 35");
		book.setEntityClass("book_red");
		book.setResistance(10);
		zone.add(book);
	}
}
