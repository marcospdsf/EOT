package games.stendhal.server.entity.item;

import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.player.Player;
import games.stendhal.common.NotificationType;

import java.util.Map;

public class TripleJadeRingOfLife extends Ring {

	public TripleJadeRingOfLife(final String name, final String clazz, final String subclass, final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	public TripleJadeRingOfLife(final TripleJadeRingOfLife item) {
		super(item);
	}
	
	public TripleJadeRingOfLife() {
		super("triple jade ring", "jewellery", "jade_ring", null);
		put("amount", 1);
		  
	}

	@Override
	public boolean onUsed(final RPEntity user) {

	  Player player = (Player) user;
	  //if (getBoundTo().equals(player.getName())) {
	  if (player.getHP() < player.getBaseHP()) {
	    if (player.isEquipped("carbuncle") && player.isEquipped("sapphire") && player.isEquipped("emerald")) {
	      int deltaHP = player.getBaseHP() - player.getHP();
	      player.drop("carbuncle", 1);
	      player.drop("sapphire", 1);
	      player.drop("emerald", 1);
	      player.setHP(player.getBaseHP());
	      setBoundTo(player.getName());
	      //player.sendPrivateText("使用了三色生命戒指與三種寶石 使血在一瞬間補滿!!")
	      player.sendPrivateText(NotificationType.NEGATIVE, "After using this ring, I fell a great power trough my body(HP+"+ deltaHP + ")!");
	    } else {
	      player.sendPrivateText("You need carbuncle, sapphire and emerald");
	    }
	  }

		return true;
	}
	
	
	public boolean isBroken() {
		return  getInt("amount") == 0;
	}

	public void damage() {
		put("amount", 0);
	}
	
	@Override
	public void repair() {
		put("amount", 1);
	}
	

	/**
	 * Gets the description.
	 * 
	 * The description of TripleJadeRingOfLife depends on the ring's state.
	 * 
	 * @return The description text.
	 */
	@Override
	public String describe() {
		String text;
		if (isBroken()) {
			text = "You see the triple jade ring of life. The gleam is lost from the stone and it has no powers.";
		} else {
		text = "You see the triple jade ring of life. Wear it, and you will retrieve HP from environment!";
		}
		
		if (isBound()) {
			text = text + " It is a special quest reward for " + getBoundTo()
					+ ", and cannot be used by others.";
		}
		return text;
	}
}
