package games.stendhal.server.script;

import java.util.List;

import games.stendhal.server.core.engine.db.StendhalHallOfFameDAO;
import games.stendhal.server.core.scripting.AbstractAdminScript;
import marauroa.server.game.db.DAORegister;

/**
 * Executes the hall of fame archive generation to immediately refresh website rankings.
 *
 * Usage: /script UpdateHallOfFame.class
 */
public class UpdateHallOfFame extends AbstractAdminScript {

	@Override
	protected int getMinParams() {
		return 0;
	}

	@Override
	protected int getMaxParams() {
		return 0;
	}

	@Override
	protected void run(final List<String> args) {
		sendText("Updating Hall of Fame and Best Player archive tables...");
		final boolean success = DAORegister.get().get(StendhalHallOfFameDAO.class).updateHallOfFameArchive();
		if (success) {
			sendText("Success: Hall of Fame archive has been updated for today! Website rankings are now refreshed.");
		} else {
			sendError("Failed to update Hall of Fame archive. Please check server log for details.");
		}
	}
}
