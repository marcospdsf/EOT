/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.core.engine.transformer;

import games.stendhal.server.entity.trade.BuyRequest;
import marauroa.common.game.RPObject;

public class BuyRequestTransformer implements Transformer {
	@Override
	public RPObject transform(RPObject object) {
		return new BuyRequest(object);
	}
}
