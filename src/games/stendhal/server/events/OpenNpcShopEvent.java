/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.events;

import org.apache.log4j.Logger;

import marauroa.common.game.Definition.DefinitionClass;
import marauroa.common.game.Definition.Type;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPEvent;
import marauroa.common.game.SyntaxException;

/**
 * Event sent by server to open the NPC Shop GUI on the client.
 */
public class OpenNpcShopEvent extends RPEvent {

	private static final Logger logger = Logger.getLogger(OpenNpcShopEvent.class);

	public static final String RPCLASS_NAME = "open_npc_shop";
	public static final String NPC_NAME = "npc_name";
	public static final String BUY_ITEMS = "buy_items";
	public static final String SELL_ITEMS = "sell_items";

	public OpenNpcShopEvent() {
		super(RPCLASS_NAME);
	}

	public OpenNpcShopEvent(final String npcName, final String buyItems, final String sellItems) {
		super(RPCLASS_NAME);
		put(NPC_NAME, npcName != null ? npcName : "");
		put(BUY_ITEMS, buyItems != null ? buyItems : "");
		put(SELL_ITEMS, sellItems != null ? sellItems : "");
	}

	public static void generateRPClass() {
		try {
			final RPClass rpclass = new RPClass(RPCLASS_NAME);
			rpclass.add(DefinitionClass.ATTRIBUTE, NPC_NAME, Type.STRING);
			rpclass.add(DefinitionClass.ATTRIBUTE, BUY_ITEMS, Type.LONG_STRING);
			rpclass.add(DefinitionClass.ATTRIBUTE, SELL_ITEMS, Type.LONG_STRING);
		} catch (final SyntaxException e) {
			logger.error("cannot generateRPClass for " + RPCLASS_NAME, e);
		}
	}
}
