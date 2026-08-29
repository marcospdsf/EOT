package games.stendhal.server.maps.semos.mines;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

public class ExchangeNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC npc = new SpeakerNPC("Miner Trader") {
			@Override
			public void createDialog() {
				addGreeting("Greetings, traveler! Seeking mineral exchanges in the deep mines?");
				addJob("I trade ores and supplies with subterranean explorers.");
				addHelp("Watch out for the aggressive kobolds deeper in the tunnels.");
				addGoodbye("Safe mining to you!");
			}
		};
		npc.setPosition(15, 15);
		zone.add(npc);
	}
}
