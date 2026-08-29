/* $Id: ZbyszekNPC.java,v 1.10 2025/06/16 00:28:00 davvids Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2025 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.bandit.tavern;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

public class ZbyszekNPC implements ZoneConfigurator {

    @Override
    public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
        buildZbyszekNPC(zone);
    }

    private void buildZbyszekNPC(final StendhalRPZone zone) {
        final SpeakerNPC ZbyszekNPC = new SpeakerNPC("Zbyszek") {

            @Override
            protected void createPath() {
                setPath(null); // stoi w tawernie i nie rusza się
            }

            @Override
            protected void createDialog() {
                // opowiadacz legendy o Belzebubie
                addGreeting("Witaj wojowniku... Siądź przy stole, a opowiem Ci o zadaniu, jakie Belzebub powierzył pewnemu śmiałkowi oraz o sile #pomocy.");

                addJob("Pewien wojownik został wezwany przez Belzebuba na wysokie skały i rozkazał mu strzeć bezcenny klejnot górski. " +
                    "Miał go pilnować do świtu, lecz wilgotne powietrze i górski wiatr szeptały mu kuszące słowa. " +
                    "Klejnot ten, opisują kroniki jako lśniący niczym zorza, miał moc uspokajać burze i chronić doliny przed wichurą. " +
                    "Wódz demonów powierzył to zadanie tylko najodważniejszym, tłumacząc, że bez jego ochrony halny zabierze ze sobą życie niewinnych. " +
                    "Śmiałek wędrował przez zdradliwe przełęcze, łamał lody na górskich stawach i staczał bój z cieniem samego siebie. " +
                    "Gdy jednak nadszedł świt, a klejnot miał wrócić do Belzebuba, wojownik poczuł wyrzuty sumienia – zrozumiał, że moc kryje się nie w klejnocie, ale w sercu, które się nie poddaje.");

                addHelp("Pewna historia głosi, że gdy wojownik przekroczył próg karczmy, usłyszał głos Belzebuba szepczący mu o ratowaniu przyjaciół z opresji. " +
                    "Słowa te poprowadziły go w doliny, gdzie lud osiedlił się pod zrujnowanym zamkiem. " +
                    "Nieznając rysów twarzy zbawionych, niósł pomoc chorym i rannym, by odkupić ciężar winy. " +
                    "Lecz im bardziej ratował, tym bardziej Burza nadciągała – aż popełnił błąd, podsłuchując demoniczny szept. " +
                    "Klejnot zadrżał w jego dłoniach, gdy śpiew wiatru zastygł, a mroźne echa zamieniły się w cichy szept wdzięczności. " +
                    "Belzebub, widząc tę łagodność serca, powstrzymał burzę halnego, która od wieków niszczyła doliny, i obdarzył śmiałka darem spokoju. " +
                    "Odtąd każda opowieść przy ogniu przypomina, że największą siłą jest współczucie, a prawdziwy klejnot kryje się w ofierze.");

                addGoodbye("Niech te legendy pozostaną z Tobą. Gdy zapragniesz kolejnych legend Belzebuba, wróć do mnie do karczmy.");
            }
        };

        ZbyszekNPC.setDescription("Oto Zbyszek – gawędziarz z duszą barda, mistrz opowieści o Belzebubie.");
        ZbyszekNPC.setEntityClass("npcguslarz");
        ZbyszekNPC.setPosition(18, 13);
        ZbyszekNPC.initHP(67);
        zone.add(ZbyszekNPC);
    }
}
