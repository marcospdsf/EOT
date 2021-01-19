package games.stendhal.server.script;

import java.util.List;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.scripting.ScriptImpl;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.npc.action.EnableFeatureAction;
import games.stendhal.server.entity.player.Player;
/**
 * Script to change the enemies xp on the run
 *
 * @author Marcos
 */
public class ChangeXPMultiplier extends ScriptImpl {
	private Creature creature = new Creature();
	@Override
	public void execute(final Player admin, final List<String> args) {
		if (args == null || args.size() != 1) {
			admin.sendPrivateText("Set the xp multiplier value: [value]");
			return;
		}

		final float xpMultiplier = Float.parseFloat(args.get(0));
		creature.SERVER_XP_INCREASE = xpMultiplier;
		admin.sendPrivateText(Float.toString(creature.SERVER_XP_INCREASE));
		
		
	}
}
