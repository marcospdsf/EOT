package games.stendhal.server.maps.pol.zakopane.blacksmith;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Builds a Healer NPC for the magic city.
 *
 * @author kymara
 */
public class KrasnoludNPC implements ZoneConfigurator {

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
		final SpeakerNPC npc = new SpeakerNPC("Dwarf Blacksmith") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				// walks along the aqueduct path, roughly
				nodes.add(new Node(8, 3));
				nodes.add(new Node(7, 3));
				nodes.add(new Node(7, 7));
				nodes.add(new Node(8, 7));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
			ShopsList.get().configureNPC(this, "sellkopalnia", ShopType.ITEM_SELL, true);

			// Dwarf crafts arrows from wood, iron ore, and feather
			final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();	
			requiredResources.put("wood", 1);
			requiredResources.put("iron ore", 1);
			requiredResources.put("little feather", 1);
			requiredResources.put("money", 1);

			final ProducerBehaviour behaviour = new ProducerBehaviour("krasnolud_cast_arrow",
				"make", "wooden arrow", requiredResources, 1 * 60);

			new ProducerAdder().addProducer(this, behaviour,
				"Greetings! I can craft arrows for you, or perhaps you're interested in my mining tools? Just say #make.");
				addGreeting();
				addJob("I craft sturdy wooden arrows and sell mining equipment.");

				addReply("wood",
						"I need quality wood for the arrow shafts. Speak to the woodcutter—he can tell you where logging is permitted.");
				addReply(Arrays.asList("ore", "iron", "iron ore", "ruda żelaza"),
						"Iron ore can be found near the springs east of the cabin below the brigand cave. I need it for the arrowheads.");
				addReply(Arrays.asList("feather", "feathers", "little feather", "piórko"),
						"I need feathers for the fletching.");
				addReply("pick",
						"A pick is essential for mining coal, sulfur, and precious ores.");
				addReply("shovel", "A sturdy shovel for excavating gravel and loose earth.");
				addReply("rope", "Crucial if you intend to descend into deeper dungeon levels.");
				addHelp("If you bring me #wood, #'iron ore', and a #'little feather', I can craft arrows for you. Just say #make.");
				addGoodbye();
			}
		};

		npc.setEntityClass("dwarfnpc");
		npc.setPosition(8, 3);
		npc.initHP(100);
		zone.add(npc);
	}
}
