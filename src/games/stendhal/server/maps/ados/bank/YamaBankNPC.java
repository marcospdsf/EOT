package games.stendhal.server.maps.ados.bank;

import java.util.Map;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

/**
 * Builds the Yama bank npc in Ados Bank floor 1.
 */
public class YamaBankNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Yama") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to the Yama Bank branch in Ados!");
				addJob("I am the manager of the Yama banking services here.");
				addHelp("You can store your items safely in our personal chests.");
				addReply("chest", "Our personal chests are secure and linked directly to your account.");
			}
		};

		npc.setPosition(11, 7);
		npc.setDirection(Direction.DOWN);
		npc.initHP(100);
		npc.setDescription("You see Yama, the bank manager.");
		npc.setEntityClass("human");
		npc.setOutfit("body=0,hair=14,dress=12,shoes=1");

		zone.add(npc);
	}
}