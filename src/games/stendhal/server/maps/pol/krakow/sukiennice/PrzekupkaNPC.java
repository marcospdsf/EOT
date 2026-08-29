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
public class PrzekupkaNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Przekupka") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Good day! What can I get for you?");
				addHelp("I purchase a wide variety of fresh garden vegetables.");
				ShopsList.get().configureNPC(this, "buywarzywa", ShopType.ITEM_BUY, false);
				addOffer("The ledger on the table details everything I buy and the prices I offer.");
				addGoodbye();
			}
		};

		npc.setDescription("You see the market vendor.");
		npc.setEntityClass("curatornpc");
		npc.setPosition(26, 11);
		npc.setDirection(Direction.LEFT);
		npc.initHP(100);
		zone.add(npc);

		final Sign book = new Sign();
		book.setPosition(24, 10);
		book.setText(" -- Buying -- \n carrot\t\t 2\n lettuce\t\t 2\n button mushroom\t 4\n tomato\t\t 5\n chinese cabbage\t 6\n yellow mushroom\t 6\n porcini\t\t 6\n spinach\t\t 6\n broccoli\t\t 7\n leek\t\t 7\n cauliflower\t\t 8\n onion\t\t 8\n zucchini\t\t 10");
		book.setEntityClass("book_blue");
		book.setResistance(10);
		zone.add(book);
	}
}
