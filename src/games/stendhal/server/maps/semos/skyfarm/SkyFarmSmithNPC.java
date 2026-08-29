package games.stendhal.server.maps.semos.skyfarm;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

public class SkyFarmSmithNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC npc = new SpeakerNPC("Sky Farm Blacksmith") {
			@Override
			public void createDialog() {
				addGreeting("Hello there! Welcome to the sky farm forge.");
				addJob("I craft and repair farming tools for the sky fields.");
				addHelp("If you need farming equipment, you've come to the right place.");
				addGoodbye("Take care on the sky bridges!");
			}
		};
		npc.setPosition(17, 15);
		zone.add(npc);
	}
}
