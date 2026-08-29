/* $Id: JagdaNPC.java,v 1.6 2025/08/06 20:58:01 davvids Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.bandit.waterfall;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.mapstuff.sign.Sign;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Map;

public class JagdaNPC implements ZoneConfigurator {
    

    @Override
    public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
        buildJagda(zone);
    }

    private void buildJagda(final StendhalRPZone zone) {
        final SpeakerNPC Jagda = new SpeakerNPC("Jagda") {
            @Override
            protected void createPath() {
                setPath(null); // NPC stoi jak skała, nie rusza butem
            }

            @Override
            protected void createDialog() {
                addGreeting("Witoj, wędrołcze! Co ci dzisio z oscypków podać?");
                addJob("Mam prawdziwe góralskie oscypki, dojrzewają na górskim wietrze. Każdy kosztuje 2000 złota.");
                games.stendhal.server.entity.npc.shop.ShopsList.get().configureNPC(this, "oscypki", games.stendhal.server.entity.npc.shop.ShopType.ITEM_SELL, true);
                addGoodbye("No, jedź ze smakiem i wracaj po świeży oscypek. Otwarte tu mamy od brzasku do zmroku!");
            }
        };

        Jagda.setEntityClass("npcjagienka");
        Jagda.setPosition(21, 10);
        Jagda.setDirection(Direction.DOWN);
        Jagda.initHP(100);
        zone.add(Jagda);

        final Sign board = new Sign();
        board.setPosition(19, 10);
        board.setText(" -- Jagda sprzedaje --	oscypek za 2000 złota");
        board.setEntityClass("blackboard");
        board.setResistance(10);
        zone.add(board);
    }
}

