/* $Id: Pudelkoc.java $ */
package games.stendhal.server.entity.item;

import java.util.Map;
import games.stendhal.common.Rand;

/**
 * Fruit package that unwraps into fruits.
 */
public class Pudelkoc extends Box {

	private static final String[] ITEMS = {
		"apple", "orange", "banana", "pear", "cherries", "strawberries"
	};

	public Pudelkoc(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
		setContent(ITEMS[Rand.rand(ITEMS.length)]);
	}

	public void setContent(final String type) {
		setItemData(type);
	}

	public Pudelkoc(final Pudelkoc item) {
		super(item);
	}
}
