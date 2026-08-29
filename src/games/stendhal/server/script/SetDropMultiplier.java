package games.stendhal.server.script;

import java.util.List;

import games.stendhal.common.NotificationType;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.scripting.AbstractAdminScript;
import games.stendhal.server.entity.creature.Creature;

/**
 * Sets the global creature item drop rate multiplier at runtime.
 *
 * Usage: /script SetDropMultiplier.class <multiplier>
 */
public class SetDropMultiplier extends AbstractAdminScript {

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
		double multiplier;
		try {
			multiplier = Double.parseDouble(args.get(0));
			if (multiplier < 0.0) {
				sendError("Drop multiplier cannot be negative.");
				return;
			}
		} catch (final NumberFormatException e) {
			sendError("Invalid multiplier: " + args.get(0) + ". Please enter a valid number (e.g. 1.0, 2.0).");
			return;
		}

		Creature.setServerDropMultiplier(multiplier);

		final String msg = String.format("[Event] Server Drop Rate Multiplier has been set to %.1fx!", multiplier);
		sendText("Success: Creature drop rate multiplier is now " + multiplier + "x.");

		SingletonRepository.getRuleProcessor().getOnlinePlayers().tellAllOnlinePlayers(NotificationType.SERVER, msg);
	}
}
