package games.stendhal.server.entity.item;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.ItemTools;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.item.StackableItem;

import java.util.Map;

/**
 * A golden present which can be unwrapped.
 * 
 * @author yamaka 2009/01/06
 */
public class SupplyCase extends Box {
  
  private static final String[][] list1 = {{"greater potion", "100"}, {"fish pie", "200"}, {"easter egg", "50"}, {"home scroll", "5"}, {"greater antidote", "10"}, {"empty scroll", "2"}};
  private static final String[][] list2 = {{"mega potion", "100"}, {"pizza", "200"}, {"easter egg", "50"}, {"home scroll", "5"}, {"greater antidote", "10"}, {"empty scroll", "2"}};
	/**
	 * Creates a new SupplyCase.
	 * 
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public SupplyCase(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
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
	public SupplyCase(final SupplyCase item) {
		super(item);
	}

	@Override
	protected boolean useMe(final Player player) {
		this.removeOne();
		
		int level = player.getLevel();
		String itemName;
		int itemAmount;
		
		for (int idx=0; idx<list1.length; idx++) {
  		if (level < 100) {
    		itemName = list1[idx][0];
    		itemAmount = Integer.parseInt(list1[idx][1], 10);
    	} else {
    	  itemName = list2[idx][0];
    		itemAmount = Integer.parseInt(list2[idx][1], 10);
    	}

  		final Item item = SingletonRepository.getEntityManager().getItem(itemName);
  		
  		if (itemAmount > 1) {
  		  ((StackableItem) item).setQuantity(itemAmount);
  		  player.sendPrivateText("You've got " + itemAmount + " " + itemName);
  		} else {
  		  item.setBoundTo(player.getName());
  		  player.sendPrivateText("You've got " + Grammar.a_noun(ItemTools.itemNameToDisplayName(itemName)));
    	}
    	
    	player.equipOrPutOnGround(item);
    	player.notifyWorldAboutChanges();
    }
		return true;
	}

}
