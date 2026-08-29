package games.stendhal.server.maps.kikareukin;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

public class SwordManNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC npc = new SpeakerNPC("Kikareukin Sword Master") {
			@Override
			public void createDialog() {
				addGreeting("Greetings, warrior! Are you seeking mastery of the blade?");
				addJob("I teach the ancient sword techniques to worthy adventurers.");
				addHelp("Train hard, keep your blade sharp, and never hesitate in battle.");
				addGoodbye("Farewell, may your sword strike true!");
			}
		};
		npc.setPosition(15, 15);
		zone.add(npc);
	}
}
