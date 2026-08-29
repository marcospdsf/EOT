/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.client.events;

import games.stendhal.client.entity.Entity;
import games.stendhal.client.gui.market.HaroldMarketController;

/**
 * Client-side event handler for opening Harold's market GUI window.
 */
public class OpenMarketGuiEvent extends Event<Entity> {

	@Override
	public void execute() {
		HaroldMarketController.get().openWindow();
	}
}
