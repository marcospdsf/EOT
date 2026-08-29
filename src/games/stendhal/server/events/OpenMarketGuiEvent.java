/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.events;

import org.apache.log4j.Logger;

import marauroa.common.game.Definition;
import marauroa.common.game.Definition.DefinitionClass;
import marauroa.common.game.Definition.Type;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPEvent;
import marauroa.common.game.SyntaxException;

/**
 * Event sent by server to instruct client to open Harold's market GUI window.
 */
public class OpenMarketGuiEvent extends RPEvent {

	public static final String RPCLASS_NAME = "open_market_gui";
	private static final String TITLE = "title";

	private static final Logger logger = Logger.getLogger(OpenMarketGuiEvent.class);

	public static void generateRPClass() {
		try {
			final RPClass rpclass = new RPClass(RPCLASS_NAME);
			rpclass.add(DefinitionClass.ATTRIBUTE, TITLE, Type.STRING, Definition.PRIVATE);
		} catch (final SyntaxException e) {
			logger.error("cannot generateRPClass for OpenMarketGuiEvent", e);
		}
	}

	public OpenMarketGuiEvent() {
		super(RPCLASS_NAME);
		put(TITLE, "Harold's offers");
	}
}
