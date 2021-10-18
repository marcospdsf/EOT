package games.stendhal.server.entity.item;


import games.stendhal.common.Rand;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.player.Player;
//import games.stendhal.server.entity.item.StackableItem;

import java.util.Map;


/**
 * A golden present which can be unwrapped.
 * 
 * @author yamaka 2009/01/16
 */
public class MammonPresent extends Box {
  private static final String[][] presentList = {{"golden present", "1"}, {"golden present", "1"}, {"golden present", "1"}, {"power scroll", "4"}};
	/**
	 * Creates a new MammonPresent.
	 * 
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public MammonPresent(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
		
		setContent(Integer.toString(Rand.rand(presentList.length)));
	}
	
	public void setContent(final String type) {
		setInfoString(type);
	}

	public MammonPresent(final MammonPresent item) {
		super(item);
	}

	@Override
	protected boolean useMe(final Player player) {
		//this.removeOne();
		final String newInfoString = "～Dont know what this line is～";
		if (has("infostring") && getInfoString().equals("Open fast!")) {
		  this.setInfoString(newInfoString);
      for (int idx=0; idx < presentList.length; idx++) {
  	  	final String itemName = presentList[idx][0];
  	  	final int itemAmount = Integer.parseInt(presentList[idx][1]);
        
  	  	final Item item = SingletonRepository.getEntityManager().getItem(itemName);
  		  
  	  	if (itemAmount > 1) {
  	  	  ((StackableItem) item).setQuantity(itemAmount);
    		} else {
  	  	  item.setBoundTo(player.getName());
      	}
      	
      	player.equipOrPutOnGround(item);
      }
        
  		player.notifyWorldAboutChanges();
  	} else {
  	  this.setInfoString(newInfoString);
  	  this.setBoundTo(player.getName());
  	  player.sendPrivateText(getInfoString());
  	}
    
		return true;
	}
	
	@Override
	public String describe() {
		String text = super.describe();

		final String infostring = getInfoString();

		if (infostring != null) {
			text += " Upon it is written: " + infostring;
		}
		return (text);
	}

}
