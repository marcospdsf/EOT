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
public class RybakMiloradNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Fisherman Milorad") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("I warmly greet you, traveler.");
				addHelp("I purchase fresh fish. The ledger before me lists the types of fish and prices I pay.");
				ShopsList.get().configureNPC(this, "buyryby", ShopType.ITEM_BUY, false);
				addOffer("I buy fresh fish; my prices are noted in the ledger.");
				addGoodbye("Farewell. Have a wonderful day.");
			}
		};

		npc.setDescription("You see Fisherman Milorad. He is very polite.");
		npc.setEntityClass("man_008_npc");
		npc.setPosition(26, 36);
		npc.setDirection(Direction.LEFT);
		npc.initHP(100);
		zone.add(npc);

		// Add a book with the shop offers
		final Sign book = new Sign();
		book.setPosition(24, 38);
		book.setText(" -- Buying -- \n cod\t\t 6\n char\t\t 7\n roach\t\t 7\n mackerel\t 8\n perch\t\t 9\n surgeonfish\t 9\n trout\t\t 9\n clownfish\t 11");
		book.setEntityClass("book_blue");
		book.setResistance(10);
		zone.add(book);
	}
}
