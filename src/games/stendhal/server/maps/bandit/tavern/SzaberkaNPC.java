/* $Id: SzaberkaNPC.java,v 1.10 2025/06/16 00:28:00 davvids Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2025 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.bandit.tavern;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

public class SzaberkaNPC implements ZoneConfigurator {

    @Override
    public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
        buildSzaberkaNPC(zone);
    }

    private void buildSzaberkaNPC(final StendhalRPZone zone) {
        final SpeakerNPC SzaberkaNPC = new SpeakerNPC("Szaberka") {

            @Override
            protected void createPath() {
                setPath(null); 
            }

            @Override
            protected void createDialog() {
                addGreeting("Hej, wojowniku! Masz coś po bandytach? Każdy łup to dla mnie cenna historia");

                addJob("Poszukuję rzadkich trofeów zbójeckich – ciupag, talizmanów czy pasów zbójnickich.");
                addHelp("Korale, kapelusze czy gorsety… każdy element ubioru mówi mi, jak zuchwały był jego właściciel. Podziel się ze mną tymi opowieściami.");

                addGoodbye("Dzięki za wizytę. Wracaj, gdy twój worek znów przepełni się zbójnickimi skarbami.");
            }
        };

        SzaberkaNPC.setDescription("Oto Szaberka – ekscentryczny kolekcjoner pamiątek po zbójnikach, dla którego każdy przedmiot skrywa opowieść o niebezpiecznych przygodach.");
        SzaberkaNPC.setEntityClass("npc_kptjacekwrobel");
        SzaberkaNPC.setPosition(16, 3);
        SzaberkaNPC.initHP(67);
        zone.add(SzaberkaNPC);
    }
}
