/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.core.engine.dbcommand;

import java.io.IOException;
import java.sql.SQLException;

import games.stendhal.server.core.engine.db.StendhalMarketDAO;
import games.stendhal.server.entity.trade.Market;
import marauroa.server.db.DBTransaction;
import marauroa.server.db.command.AbstractDBCommand;
import marauroa.server.game.db.DAORegister;

/**
 * Asynchronously synchronizes the market's active buy requests and sell offers
 * to the database for website exposure.
 */
public class SyncMarketDataCommand extends AbstractDBCommand {

	private final Market market;

	public SyncMarketDataCommand(final Market market) {
		this.market = market;
	}

	@Override
	public void execute(final DBTransaction transaction) throws SQLException, IOException {
		if (market != null && market.getZone() != null) {
			String zoneName = market.getZone().getName();
			if (zoneName != null && (zoneName.startsWith("test_") || zoneName.contains("test"))) {
				// Ignore mock unit test zones to prevent polluting live MySQL DB
				return;
			}
		}
		final StendhalMarketDAO dao = DAORegister.get().get(StendhalMarketDAO.class);
		if (dao != null && market != null) {
			dao.dumpMarket(transaction, market);
		}
	}
}
