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
public class RzeznikNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Butcher") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Good day.");
				addHelp("You can help by selling me any fresh meats.");
				ShopsList.get().configureNPC(this, "buymieso", ShopType.ITEM_BUY, false);
				addOffer("My price list is in the ledger on the counter.");
				addGoodbye("Farewell.");
			}
		};

		npc.setDescription("You see the Butcher. He seems rather grumpy.");
		npc.setEntityClass("jailedbarbariannpc");
		npc.setPosition(26, 43);
		npc.setDirection(Direction.LEFT);
		npc.initHP(100);
		zone.add(npc);

		// Add a book with the shop offers
		final Sign book = new Sign();
		book.setPosition(24, 43);
		book.setText(" -- Meat Purchases -- \n chicken\t 12\n meat\t\t 15\n ham\t\t 25\n steak\t\t 35");
		book.setEntityClass("book_blue");
		book.setResistance(10);
		zone.add(book);
	}
}