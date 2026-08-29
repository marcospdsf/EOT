/* $Id: MarekNPC.java,v 1.10 2025/06/16 00:28:00 davvids Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2025 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.bandit.tavern;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

public class MarekNPC implements ZoneConfigurator {

    @Override
    public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
        buildMarekNPC(zone);
    }

    private void buildMarekNPC(final StendhalRPZone zone) {
        final SpeakerNPC MarekNPC = new SpeakerNPC("Marek") {

            @Override
            protected void createPath() {
                setPath(null); // stoi w tawernie i nie rusza się
            }

            @Override
            protected void createDialog() {
                // opowiadacz legendy o Belzebubie
                addGreeting("Zbierzcie się, drodzy goście! Posłuchajcie historii o Belzebubie i sile #pomocy.");

                addJob("Dawno temu, gdy noc zakrywała świat gęstą zasłoną i gwiazdy błyszczały jak srebrne oczka, " +
                       "pojawił się on – Belzebub, pan mroku i cieni. Opowiadano, że jego oddech targał mgły i " +
                       "prowadził podróżnych na manowce, a kroki niosły echo w opuszczonych dolinach. Jednak nie był " +
                       "zły z natury – strzegł pradawnej tajemnicy, ukrytej w jaskiniach pod starymi dębami. " +
                       "Ci, którzy z sercem odważnym i czystym przemierzali te korytarze, znajdowali skarb mądrości, " +
                       "a ponure szepty przemieniały się w ciche wskazówki. Lecz każdy, kto z żądzą zysku wkroczył do środka, " +
                       "miał stanąć oko w oko z własnym lękiem.");

                addHelp("Dawno temu, gdy noc zakrywała świat gęstą zasłoną i gwiazdy błyszczały jak srebrne oczka, " +
                       "pojawił się on – Belzebub, pan mroku i cieni. Opowiadano, że jego oddech targał mgły i " +
                       "prowadził podróżnych na manowce, a kroki niosły echo w opuszczonych dolinach. Jednak nie był " +
                       "zły z natury – strzegł pradawnej tajemnicy, ukrytej w jaskiniach pod starymi dębami. " +
                       "Ci, którzy z sercem odważnym i czystym przemierzali te korytarze, znajdowali skarb mądrości, " +
                       "a ponure szepty przemieniały się w ciche wskazówki. Lecz każdy, kto z żądzą zysku wkroczył do środka, " +
                       "miał stanąć oko w oko z własnym lękiem.");

                addGoodbye("Niech tajemnice Belzebuba towarzyszą Ci w snach. Wróć, gdy zapragniesz posłuchać górskiej legendy.");
            }
        };

        MarekNPC.setDescription("Oto Marek – mędrzec z karczmy, znający legendy o mroku i światłości.");
        MarekNPC.setEntityClass("npcjuhasboguslaw");
        MarekNPC.setPosition(3, 8);
        MarekNPC.initHP(100);
        zone.add(MarekNPC);
    }
}
