package games.stendhal.server.maps.nalwor.skyforest;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

public class SummonSeller implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC npc = new SpeakerNPC("Sky Forest Summoner") {
			@Override
			public void createDialog() {
				addGreeting("Welcome to the high reaches of the sky forest!");
				addJob("I summon creatures and guide travelers through the mystic skies.");
				addHelp("Be careful of the strange winds and creatures up here.");
				addGoodbye("May the winds protect your path.");
			}
		};
		npc.setPosition(60, 60);
		zone.add(npc);
	}
}
