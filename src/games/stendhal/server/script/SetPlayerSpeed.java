package games.stendhal.server.script;

import java.util.List;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.scripting.AbstractAdminScript;
import games.stendhal.server.entity.player.Player;

/**
 * Sets the movement speed for a player.
 *
 * Usage:
 *   /script SetPlayerSpeed.class <speed>
 *   /script SetPlayerSpeed.class <player_name> <speed>
 */
public class SetPlayerSpeed extends AbstractAdminScript {

	@Override
	protected int getMinParams() {
		return 1;
	}

	@Override
	protected int getMaxParams() {
		return 2;
	}

	@Override
	protected void run(final List<String> args) {
		Player target = admin;
		String speedStr = args.get(0);

		if (args.size() == 2) {
			final String playerName = args.get(0);
			target = SingletonRepository.getRuleProcessor().getPlayer(playerName);
			if (target == null) {
				sendError("Player '" + playerName + "' is not online.");
				return;
			}
			speedStr = args.get(1);
		}

		if (target == null) {
			sendError("No target player specified.");
			return;
		}

		double speed;
		try {
			speed = Double.parseDouble(speedStr);
			if (speed <= 0.0) {
				sendError("Speed must be greater than 0.0 (default is 1.0).");
				return;
			}
		} catch (final NumberFormatException e) {
			sendError("Invalid speed: " + speedStr + ". Please enter a valid number (e.g. 1.0, 1.5, 2.0).");
			return;
		}

		final boolean wasMoving = !target.stopped();
		target.setBaseSpeed(speed);
		if (wasMoving) {
			target.setSpeed(speed);
		}
		target.notifyWorldAboutChanges();

		sendText("Movement speed of " + target.getName() + " set to " + speed + " tiles/turn.");
		if (target != admin) {
			target.sendPrivateText("Your movement speed has been set to " + speed + " tiles/turn by " + admin.getName() + ".");
		}
	}
}
