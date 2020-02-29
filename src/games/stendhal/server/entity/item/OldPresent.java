package games.stendhal.server.entity.item;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.ItemTools;
import games.stendhal.common.Rand;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.player.Player;

import java.util.Map;

/**
 * A old present which can be unwrapped.
 * 
 * @author kymara
 */
public class OldPresent extends Box {

	private static final String[] ITEMS = { "greater potion", "pie", "power arrow",  "toadstool", "onion",
			"sandwich", "pizza", "cherry", "blue elf cloak", "summon scroll", "empty scroll",
			"greater potion", "pie", "power arrow", "sandwich", "pizza", "cherry", "toadstool", "onion",
			"easter egg", "egg", "pizza", "ados city scroll", "home scroll", "fado city scroll",
			"easter egg", "egg", "pizza", "ados city scroll", "home scroll", "fado city scroll" };

	/**
	 * Creates a new old present.
	 * 
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public OldPresent(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);

		setContent(ITEMS[Rand.rand(ITEMS.length)]);
	}

	/**
	 * Sets content.
	 * @param type of item to be produced.
	 */
	public void setContent(final String type) {
		setInfoString(type);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param item
	 *            item to copy
	 */
	public OldPresent(final OldPresent item) {
		super(item);
	}

	@Override
	protected boolean useMe(final Player player) {
		this.removeOne();

		final String itemName = getInfoString();
		final Item item = SingletonRepository.getEntityManager().getItem(itemName);
		player.sendPrivateText("Congratulations, you've got " 
				+ Grammar.a_noun(ItemTools.itemNameToDisplayName(itemName)));

		//player.equip(item, true);
		//==================================
		if (itemName.equals("summon scroll")) {
		  item.setInfoString("ice giant");
		}
		player.equipOrPutOnGround(item);
		//==================================
		player.notifyWorldAboutChanges();

		return true;
	}

}
