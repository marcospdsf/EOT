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
public class ZielarkaNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Herbalist") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Greetings.");
				addHelp("I buy medicinal and rare herbs. The ledger on the counter lists the herbs and prices I pay.");
				ShopsList.get().configureNPC(this, "buyziola", ShopType.ITEM_BUY, false);
				addOffer("I buy fresh herbs; my prices are listed in the book.");
				addGoodbye();
			}
		};

		npc.setDescription("You see the Herbalist. She knows everything about wild flora and healing plants.");
		npc.setEntityClass("confectionerapplepienpc");
		npc.setPosition(15, 44);
		npc.setDirection(Direction.UP);
		npc.initHP(100);
		zone.add(npc);

		// Add a book with the shop offers
		final Sign book = new Sign();
		book.setPosition(16, 42);
		book.setText(" -- Buying Herbs -- \n arandula\t 12\n kekik\t\t 28\n sclaria\t\t 28\n kokuda\t\t 250");
		book.setEntityClass("book_red");
		book.setResistance(10);
		zone.add(book);
	}
}
