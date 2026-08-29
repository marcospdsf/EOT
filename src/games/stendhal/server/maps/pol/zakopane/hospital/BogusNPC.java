package games.stendhal.server.maps.pol.zakopane.hospital;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

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
public class BogusNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Boguś") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(4, 4));
				nodes.add(new Node(10, 4));
				nodes.add(new Node(4, 4));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings, hunter.");
				addJob("I purchase animal pelts, skins, and trophies. Perhaps you have some with you to #offer?");
				addHelp("I buy hides, pelts, and monster skins. If you have any, make me an #offer.");
				addOffer("You will find everything I trade in these ledger books.");
				addQuest("Thank you, but I have no tasks at this time.");
				ShopsList.get().configureNPC(this, "buyskin", ShopType.ITEM_BUY, false);
				ShopsList.get().configureNPC(this, "sellskin", ShopType.ITEM_SELL, false);
				addGoodbye("Farewell.");
			}
		};

		npc.setDescription("You see Boguś, an honest mountain furrier and merchant.");
		npc.setEntityClass("npcjuhasboguslaw");
		npc.setPosition(4, 4);
		npc.initHP(100);
		zone.add(npc);

		// Add a book with the shop offers
		final Sign book = new Sign();
		book.setPosition(9, 5);
		book.setText(" -- Buying -- \n little feather\t 3\n lion skin\t 1500\n tiger skin\t 1000\n white tiger skin\t 250\n green dragon skin\t 2600\n blue dragon skin\t 2600\n"+
		" red dragon skin\t 2600\n golden dragon skin\t 2800\n black dragon skin\t 3300\n xenocium skin\t 450");
		book.setEntityClass("book_red");
		book.setResistance(10);
		zone.add(book);

		final Sign book2 = new Sign();
		book2.setPosition(7, 5);
		book2.setText(" -- Selling -- \n slim bottle\t 5\n eared bottle\t 7\n short sword\t 500\n battle axe\t 800\n scythe\t 2000");
		book2.setEntityClass("book_blue");
		book2.setResistance(10);
		zone.add(book2);
	}
}
