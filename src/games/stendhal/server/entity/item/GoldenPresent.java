package games.stendhal.server.entity.item;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.ItemTools;
import games.stendhal.common.Rand;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.item.StackableItem;

import java.util.Map;


/**
 * A golden present which can be unwrapped.
 * 
 * @author yamaka 2008/12/03
 */
public class GoldenPresent extends Box {
  
  private static final String[][] presentList = {
    {"greater potion", "200"}, {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"fairy sword", "1"},
    {"ados city scroll", "200"}, {"home scroll", "250"}, {"fish pie", "800"}, {"empty scroll", "50"}, {"money", "66000"}, {"xeno shield", "1"},
    {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"fairy shield", "1"},
    {"greater potion", "200"}, {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"mainio shield", "1"},
    {"ados city scroll", "200"}, {"home scroll", "250"}, {"fish pie", "800"}, {"empty scroll", "50"}, {"money", "66000"}, {"green dragon armor", "1"},
    {"greater potion", "200"}, {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"mithril armor", "1"},
    {"home scroll", "250"}, {"fish pie", "800"}, {"empty scroll", "50"}, {"money", "66000"}, {"blue dragon shield", "1"},
    {"greater potion", "200"}, {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"red dragon shield", "1"},
    {"greater potion", "200"}, {"mega potion", "100"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"magic plate shield", "1"},
    {"greater potion", "200"}, {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"magic twoside axe", "1"},
    {"ados city scroll", "200"}, {"home scroll", "250"}, {"fish pie", "800"}, {"empty scroll", "50"}, {"money", "66000"}, {"golden dagger", "1"},
    {"greater potion", "200"}, {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"soul dagger", "1"},
    {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"black dragon golden cloak", "1"},
    {"greater potion", "200"}, {"mega potion", "100"}, {"sandwich", "500"}, {"pizza", "400"}, {"easter egg", "200"}, {"money", "66000"}, {"dark elf cloak", "1"} };
	/**
	 * Creates a new Golden present.
	 * 
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public GoldenPresent(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);

		setContent(Integer.toString(Rand.rand(presentList.length)));
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
	public GoldenPresent(final GoldenPresent item) {
		super(item);
	}

	@Override
	protected boolean useMe(final Player player) {
		this.removeOne();
    
    final int itemNum = Integer.parseInt(getInfoString(), 10);
		final String itemName = presentList[itemNum][0];
		final int itemAmount = Integer.parseInt(presentList[itemNum][1]);

		final Item item = SingletonRepository.getEntityManager().getItem(itemName);
		
		// 2008/12/15
		if (itemAmount > 1) {
		  ((StackableItem) item).setQuantity(itemAmount);
		  player.sendPrivateText("Congratulations, you've got " + itemAmount + " " + itemName);
		} else {
		  item.setBoundTo(player.getName());
		  player.sendPrivateText("Congratulations, you've got " + Grammar.a_noun(ItemTools.itemNameToDisplayName(itemName)));
  	}
  	
  	player.equipOrPutOnGround(item);

		player.notifyWorldAboutChanges();

		return true;
	}

}
