package games.stendhal.server.entity.creature;

import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A creature that will be attacked by normal Creatures.
 * 
 * @author hendrik
 */

// extends AttackableCreature 會被 monster 攻擊，但是不會反擊，
//而且必需靠近或清除攻擊的 monstr 才會回擊 player，不過會有一些問題
//public class TrainingCreature extends AttackableCreature {
// extends Creature 不會被 monster 攻擊，可正常回擊 player
public class TrainingCreature extends Creature {
  
  private RPEntity master;
  private boolean specialMaster = false;
  
	/**
	 * Class Constructor.
	 * 
	 * @param copy
	 */
	public TrainingCreature(final Creature copy) {
		super(copy);
	}
	
	@Override
	public List<RPEntity> getEnemyList() {
		List<RPEntity> res = this.getAttackingRPEntities();

		if (master != null) {
			if (res.isEmpty()) {
				res = master.getAttackingRPEntities();
			} else {
				res = new ArrayList<RPEntity>();
				res.addAll(this.getAttackingRPEntities());
				res.addAll(master.getAttackingRPEntities());
			}
		}
		return res;
	}
	
	public void setMaster(final RPEntity master) {
		this.master = master;
	}

	@Override
	public Creature getNewInstance() {
		return new TrainingCreature(this);
	}
	
	@Override
	public void setTarget(final RPEntity target) {
	  if (target instanceof Player) {
	    super.setTarget(target);
	  }
	}
	
	public void setSpecialMaster() {
	  this.specialMaster = true;
	}
	
	// 攻擊速度
	//*
	@Override
	public int getAttackTurn() {
	  if (specialMaster) {
  		return 1;
  	} else {
  	  return super.getAttackTurn();
  	}
	}
	
	@Override
	public boolean isAttackTurn(final int turn) {
	  // 可以在這裡判斷 player 的血量?
	  //*
	  final Player player = (Player) getAttackTarget();
	  if (player != null) {
	    if (player.getHP() < 200 && specialMaster) {
	      //stopAttack();
	      return false;
	    }
	  }
	  //*/
	  
	  if (specialMaster) {
  		return true;
  	} else {
  	  return super.isAttackTurn(turn);
  	}
	}
  //*/
}
