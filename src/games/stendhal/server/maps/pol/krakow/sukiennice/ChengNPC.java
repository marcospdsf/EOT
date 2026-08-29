package games.stendhal.server.maps.pol.krakow.sukiennice;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.mapstuff.sign.Sign;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to buy previously un bought armor.
 *
 * @author kymara
 */
public class ChengNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Cheng") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings, traveler.");
				addJob("I seek rare gems, precious stones, and fine jewelry. You may #offer something.");
				addHelp("I purchase rare stones and handcrafted jewelry. If you have any, #offer it to me.");
				addOffer("Take a look at the ledger here to see my purchasing prices.");
				addQuest("Hm... tell me of the customs and laws of this land.");
				ShopsList.get().configureNPC(this, "buycheng", ShopType.ITEM_BUY, false);
				addGoodbye("Farewell.");
			}
		};

		npc.setDescription("You see Cheng, a traveling merchant from the Far East.");
		npc.setEntityClass("npcwikary");
		npc.setPosition(26, 30);
		npc.setDirection(Direction.LEFT);
		npc.initHP(100);
		zone.add(npc);
		
		final Sign book = new Sign();
		book.setPosition(24, 32);
		book.setText(" -- Buying -- \n amber stone\t\t 20\n giant pearl\t 1500\n mithril nugget\t 10");
		book.setEntityClass("book_red");
		book.setResistance(10);
		zone.add(book);
	}
}
