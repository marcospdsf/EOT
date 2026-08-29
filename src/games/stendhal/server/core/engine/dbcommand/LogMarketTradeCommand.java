/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.core.engine.dbcommand;

import java.io.IOException;
import java.sql.SQLException;

import games.stendhal.server.core.engine.db.StendhalMarketDAO;
import marauroa.server.db.DBTransaction;
import marauroa.server.db.command.AbstractDBCommand;
import marauroa.server.game.db.DAORegister;

/**
 * Logs a completed market trade (sell offer purchase or buy request fulfillment)
 * to the database asynchronously for website and market history tracking.
 */
public class LogMarketTradeCommand extends AbstractDBCommand {

	private final String tradeType;
	private final String buyer;
	private final String seller;
	private final String itemName;
	private final int quantity;
	private final int pricePerPiece;
	private final int totalPrice;
	private final int feePaid;

	public LogMarketTradeCommand(final String tradeType, final String buyer, final String seller,
			final String itemName, final int quantity, final int pricePerPiece,
			final int totalPrice, final int feePaid) {
		this.tradeType = tradeType;
		this.buyer = buyer;
		this.seller = seller;
		this.itemName = itemName;
		this.quantity = quantity;
		this.pricePerPiece = pricePerPiece;
		this.totalPrice = totalPrice;
		this.feePaid = feePaid;
	}

	@Override
	public void execute(final DBTransaction transaction) throws SQLException, IOException {
		final StendhalMarketDAO dao = DAORegister.get().get(StendhalMarketDAO.class);
		if (dao != null) {
			dao.logMarketTrade(transaction, tradeType, buyer, seller, itemName, quantity, pricePerPiece, totalPrice, feePaid);
		}
	}
}
