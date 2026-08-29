package games.stendhal.server.script;

import java.util.List;

import games.stendhal.common.NotificationType;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.scripting.AbstractAdminScript;
import games.stendhal.server.entity.creature.Creature;

/**
 * Sets the global creature stack drop quantity multiplier at runtime (for money, arrows, resources, etc.).
 *
 * Usage: /script SetDropQuantityMultiplier.class <multiplier>
 */
public class SetDropQuantityMultiplier extends AbstractAdminScript {

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
			if (multiplier < 1.0) {
				sendError("Drop quantity multiplier must be at least 1.0 (default is 1.0).");
				return;
			}
		} catch (final NumberFormatException e) {
			sendError("Invalid multiplier: " + args.get(0) + ". Please enter a valid number (e.g. 1.0, 2.0, 3.0).");
			return;
		}

		Creature.setServerDropQuantityMultiplier(multiplier);

		final String msg = String.format("[Event] Server Stack Drop Quantity Multiplier has been set to %.1fx!", multiplier);
		sendText("Success: Creature stack drop quantity multiplier is now " + multiplier + "x.");

		SingletonRepository.getRuleProcessor().getOnlinePlayers().tellAllOnlinePlayers(NotificationType.SERVER, msg);
	}
}
