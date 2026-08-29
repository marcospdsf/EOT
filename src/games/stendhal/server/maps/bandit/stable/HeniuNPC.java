/* $Id: HeniuNPC.java,v 1.10 2025/06/08 23:10:00 davvids Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2025 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.bandit.stable;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

public class HeniuNPC implements ZoneConfigurator {

    @Override
    public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
        buildHeniu(zone);
    }

    private void buildHeniu(final StendhalRPZone zone) {
        final SpeakerNPC Heniu = new SpeakerNPC("Heniu") {

            @Override
            protected void createPath() {
                setPath(null); // stoi w stajni i nie rusza się
            }

            @Override
            protected void createDialog() {
                // gracz wpisuje #zerknij i widzi #money jako podświetlone
                addGreeting("Ej, wędrowcze! Jeżeli chcesz to #zerknij na mojego rumaka!");

                addJob("Tylko raz na tydzień i za 1000 złota. Bez tego ani grzywy nie poruszy.");

                addHelp("Rumak to mój jedyny dochód – portfel pusty, to i grzywa stoi w miejscu.");

                addGoodbye("Dziękuję za wizytę. Wróć, gdy złoto zbierzesz, bo inaczej nie zobaczysz.");
            }
        };

        Heniu.setDescription("Stajenny Heniu – skąpy i zawzięty, ale zna się na rumakach.");
        Heniu.setEntityClass("npc_kptjacekwrobel");
        Heniu.setPosition(5, 19);
        Heniu.initHP(100);
        zone.add(Heniu);
    }
}
