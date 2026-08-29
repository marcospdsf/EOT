/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.core.engine.transformer;

import games.stendhal.server.entity.trade.ItemCollection;
import marauroa.common.game.RPObject;

public class ItemCollectionTransformer implements Transformer {
	@Override
	public RPObject transform(RPObject object) {
		return new ItemCollection(object);
	}
}
