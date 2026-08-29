/* $Id: SmallForcesRaid.java,v 1.2 2010/10/07 17:10:26 nhnb Exp $ */
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
package games.stendhal.server.script;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gummipferd
 */
public class SmallForcesRaid extends CreateRaid {

	@Override
	protected Map<String, Integer> createArmy() {
		final Map<String, Integer> attackArmy = new HashMap<String, Integer>();
		attackArmy.put("gnome", 12);
		attackArmy.put("mage gnome", 7);
		attackArmy.put("infantry gnome", 10);
		attackArmy.put("cavalryman gnome", 10);
		attackArmy.put("dark gargoyle", 2);

		return attackArmy;
	}
}
