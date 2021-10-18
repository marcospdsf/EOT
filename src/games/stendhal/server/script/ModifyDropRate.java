package games.stendhal.server.script;

import java.util.List;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.scripting.ScriptImpl;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.npc.action.EnableFeatureAction;
import games.stendhal.server.entity.player.Player;

public class ModifyDropRate extends ScriptImpl {
	
	@Override
	public void execute(final Player admin, final List<String> args) {
		if (args == null || args.size() != 1) {
			admin.sendPrivateText("Set the xp multiplier value: [value]");
			return;
		}

		final float dropMultiplier = Float.parseFloat(args.get(0));
		Creature.SERVER_DROP_RATE = dropMultiplier;
		admin.sendPrivateText("New creature drop rate is: " + Double.toString(Creature.SERVER_DROP_RATE));
		
		
	}
}