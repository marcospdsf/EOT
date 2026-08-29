package games.stendhal.server.maps.pol.krakow.planty;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
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
public class MysliwyNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Hunter") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(8, 127));
				nodes.add(new Node(16, 127));
				nodes.add(new Node(16, 122));
				nodes.add(new Node(27, 122));
				nodes.add(new Node(27, 126));
				nodes.add(new Node(33, 126));
				nodes.add(new Node(33, 121));
				nodes.add(new Node(37, 121));
				nodes.add(new Node(37, 127));
				nodes.add(new Node(44, 127));
				nodes.add(new Node(44, 120));
				nodes.add(new Node(37, 120));
				nodes.add(new Node(37, 116));
				nodes.add(new Node(27, 116));
				nodes.add(new Node(27, 117));
				nodes.add(new Node(8, 117));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings, stranger.");
				addJob("Bring me fangs, tusks, and teeth from all beasts. You may #offer something.");
				addHelp("I purchase teeth, fangs, and beaks. If you have any, #offer them to me.");
				addOffer("I buy wolf tusk 10, bear tusk 15, bird beak 200, monster tooth 400, dragon tusk 500, golden grim reaper 5000.");
				addQuest("Not right now, I am busy tracking game. I just picked up a fresh trail.");
				ShopsList.get().configureNPC(this, "buyzeby", ShopType.ITEM_BUY, false);
				addGoodbye("Farewell.");
			}
		};

		npc.setDescription("You see the royal hunter.");
		npc.setEntityClass("scarletarmynpc");
		npc.setPosition(8, 127);
		npc.initHP(100);
		zone.add(npc);
	}
}
