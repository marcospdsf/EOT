package games.stendhal.server.script;

import java.util.List;

import games.stendhal.common.NotificationType;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.scripting.AbstractAdminScript;
import games.stendhal.server.entity.RPEntity;

/**
 * Sets the global combat & skill XP reward multiplier at runtime.
 *
 * Usage: /script SetSkillMultiplier.class <multiplier>
 */
public class SetSkillMultiplier extends AbstractAdminScript {

	@Override
	protected int getMinParams() {
		return 1;
	}

	@Override
	protected int getMaxParams() {
		return 1;
	}

	@Override
	protected void run(final List<String> args) {
		float multiplier;
		try {
			multiplier = Float.parseFloat(args.get(0));
			if (multiplier < 0.0f) {
				sendError("Skill multiplier cannot be negative.");
				return;
			}
		} catch (final NumberFormatException e) {
			sendError("Invalid multiplier: " + args.get(0) + ". Please enter a valid number (e.g. 1.0, 2.0).");
			return;
		}

		RPEntity.setServerSkillMultiplier(multiplier);

		final String msg = String.format("[Event] Server Skill Multiplier has been set to %.1fx!", multiplier);
		sendText("Success: Combat & skill XP multiplier is now " + multiplier + "x.");
		
		SingletonRepository.getRuleProcessor().getOnlinePlayers().tellAllOnlinePlayers(NotificationType.SERVER, msg);
	}
}
