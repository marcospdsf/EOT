package games.stendhal.server.maps.pol.zakopane.home;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Ados MithrilForger (Inside / Level 0).
 *
 * @author kymara
 */
public class JuhasNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildJuhas(zone);
	}

	private void buildJuhas(final StendhalRPZone zone) {
		final SpeakerNPC juhas = new SpeakerNPC("Juhas") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings.");
				addJob("I sell #magic #scrolls. Ask me for an #offer.");
				addHelp("I sell #scrolls that can save your life in perilous situations.");

				ShopsList.get().configureNPC(this, "juhas", ShopType.ITEM_SELL, true);

				add(ConversationStates.ATTENDING, ConversationPhrases.QUEST_MESSAGES,
						null,
						ConversationStates.ATTENDING,
						"I have no quest for you at present. However, I offer #'magic scrolls' such as the #'Tatra scroll', #'Krakow scroll', #'Wieliczka scroll', #'Ados scroll', #'Fado scroll', #'Kalavan scroll', #'Kirdneh scroll', and #'travel ticket'.", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("magic scrolls", "scrolls", "magiczne zwoje", "zwoje"),
						null,
						ConversationStates.ATTENDING,
						"I offer scrolls and tickets to travel swiftly across the lands: #'Tatra scroll', #'Krakow scroll', #'Wieliczka scroll', #'Ados scroll', #'Fado scroll', #'Kalavan scroll', #'Kirdneh scroll', and #'travel ticket'! Just say: buy <amount> <scroll name>.", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("tatra", "tatra scroll", "tatrzański", "zwój tatrzański"),
						null,
						ConversationStates.ATTENDING,
						"The Tatra scroll returns you instantly to Zakopane. A reliable way to escape sudden danger!", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("krakow", "krakow scroll", "krakowski", "zwój krakowski"),
						null,
						ConversationStates.ATTENDING,
						"The Krakow scroll transports you to the historic royal city of Kraków, rich with marvels and mysteries.", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("ados", "ados scroll", "zwój ados"),
						null,
						ConversationStates.ATTENDING,
						"The Ados city scroll transports you directly to the port city of Ados far to the east!", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("semos", "semos scroll", "zwój semos"),
						null,
						ConversationStates.ATTENDING,
						"The Semos scroll transports you to the starter town in central Faiumoni.", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("nalwor", "nalwor scroll", "zwój nalwor"),
						null,
						ConversationStates.ATTENDING,
						"The Nalwor scroll teleports you to the elven city of Nalwor, nestled deep in the forest south of Semos!", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("fado", "fado scroll", "zwój fado"),
						null,
						ConversationStates.ATTENDING,
						"The Fado scroll takes you to the port city of Fado, situated southwest of Semos!", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("kalavan", "kalavan scroll", "zwój kalavan"),
						null,
						ConversationStates.ATTENDING,
						"The Kalavan scroll transports you far southwest to the fortress city of Kalavan, beyond Fado!", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("kirdneh", "kirdneh scroll", "zwój kirdneh"),
						null,
						ConversationStates.ATTENDING,
						"The Kirdneh scroll brings you to the desert gateway city of Kirdneh, guarded by the River Orril!", null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("ticket", "travel ticket", "turystyczny", "bilet turystyczny"),
						null,
						ConversationStates.ATTENDING,
						"The travel ticket teleports you to the great sands near the ancient pyramids. Be sure to stock up on water, and beware the sweltering days, freezing nights, and deadly sandstorms!", null);

				addGoodbye("Farewell and safe travels!");
				setDirection(Direction.UP);

			}
		};

		juhas.setEntityClass("npcjuhas");
		juhas.setPosition(10, 6);
		juhas.setDirection(Direction.UP);
		juhas.initHP(100);
		zone.add(juhas);
	}
}
