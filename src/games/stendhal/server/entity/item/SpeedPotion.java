package games.stendhal.server.entity.item;

import java.util.Map;

import games.stendhal.common.NotificationType;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.player.Player;

/**
 * Speed potion that temporarily increases player movement speed and stacks in inventory.
 */
public class SpeedPotion extends StackableItem {

	public SpeedPotion(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	public SpeedPotion(final SpeedPotion item) {
		super(item);
	}

	@Override
	public boolean onUsed(final RPEntity user) {
		if (!(user instanceof Player)) {
			return false;
		}

		final Player player = (Player) user;
		double speedMultiplier = 1.2;
		if (has("itemdata")) {
			try {
				speedMultiplier = Double.parseDouble(get("itemdata"));
			} catch (final NumberFormatException e) {
				speedMultiplier = 1.2;
			}
		}

		int durationSeconds = 60;
		if (has("amount") && getInt("amount") > 0) {
			durationSeconds = getInt("amount");
		}

		final double targetSpeed = Math.round(speedMultiplier * 100.0) / 100.0;

		final boolean wasMoving = !player.stopped();
		player.setBaseSpeed(targetSpeed);
		if (wasMoving) {
			player.setSpeed(targetSpeed);
		}
		player.notifyWorldAboutChanges();

		player.sendPrivateText(NotificationType.POSITIVE,
				String.format("You drink the %s! Your movement speed is increased to %.2f for %d seconds.",
						getName(), targetSpeed, durationSeconds));

		// Remove 1 potion from inventory/stack
		this.removeOne();

		// Schedule expiration
		SingletonRepository.getTurnNotifier().notifyInSeconds(durationSeconds, new TurnListener() {
			@Override
			public void onTurnReached(final int currentTurn) {
				final Player target = SingletonRepository.getRuleProcessor().getPlayer(player.getName());
				if (target != null && target.getZone() != null) {
					final boolean targetWasMoving = !target.stopped();
					target.setBaseSpeed(1.0);
					if (targetWasMoving) {
						target.setSpeed(1.0);
					}
					target.notifyWorldAboutChanges();
					target.sendPrivateText(NotificationType.INFORMATION, "The potion's swiftness effect has worn off.");
				}
			}
		});

		return true;
	}
}
