/* $Id: Pudelkob.java $ */
package games.stendhal.server.entity.item;

import java.util.Map;
import games.stendhal.common.Rand;

/**
 * Sweet package that unwraps into sweets/candies.
 */
public class Pudelkob extends Box {

	private static final String[] ITEMS = {
		"chocolate", "cookie", "cake", "ice cream", "candy"
	};

	public Pudelkob(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
		setContent(ITEMS[Rand.rand(ITEMS.length)]);
	}

	public void setContent(final String type) {
		setItemData(type);
	}

	public Pudelkob(final Pudelkob item) {
		super(item);
	}
}
