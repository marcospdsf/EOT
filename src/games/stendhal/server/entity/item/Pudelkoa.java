/* $Id: Pudelkoa.java $ */
package games.stendhal.server.entity.item;

import java.util.Map;
import games.stendhal.common.Rand;

/**
 * Potion package that unwraps into potions.
 */
public class Pudelkoa extends Box {

	private static final String[] ITEMS = {
		"minor potion", "potion", "greater potion", "super potion",
		"antidote", "minor antidote"
	};

	public Pudelkoa(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
		setContent(ITEMS[Rand.rand(ITEMS.length)]);
	}

	public void setContent(final String type) {
		setItemData(type);
	}

	public Pudelkoa(final Pudelkoa item) {
		super(item);
	}
}
