/***************************************************************************
 *                 Copyright © 2022-2026 - Faiumoni e. V.                  *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.core.engine.generateini;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import marauroa.common.crypto.RSAKey;

/**
 * Class representing the configuration within a server.ini file.
 */
public class ServerIniConfiguration {

	private final DatabaseConfiguration databaseConfiguration;
	private final String gameName = "stendhal";
	private final String databaseImplementation = "games.stendhal.server.core.engine.StendhalPlayerDatabase";
	private final String tcpPort = "32160";
	private final String worldImplementation = "games.stendhal.server.core.engine.StendhalRPWorld";
	private final String ruleprocessorImplementation = "games.stendhal.server.core.engine.StendhalRPRuleProcessor";
	private final String factoryImplementation = "games.stendhal.server.core.engine.StendhalRPObjectFactory";
	private final String turnLength = "300";
	private final String statisticsFilename = "./server_stats.xml";
	private final RSAKey rsakey;
	private final Date generationDate;

	private Float xpMultiplier = null;
	private Float skillMultiplier = null;
	private Double dropMultiplier = null;
	private Double dropQuantityMultiplier = null;

	public ServerIniConfiguration(DatabaseConfiguration databaseConfiguration, Integer keySize) {
		this(databaseConfiguration, RSAKey.generateKey(keySize), new Date());
	}

	public ServerIniConfiguration(DatabaseConfiguration databaseConfiguration, Integer keySize,
			Float xpMultiplier, Float skillMultiplier, Double dropMultiplier, Double dropQuantityMultiplier) {
		this(databaseConfiguration, RSAKey.generateKey(keySize), new Date());
		this.xpMultiplier = xpMultiplier;
		this.skillMultiplier = skillMultiplier;
		this.dropMultiplier = dropMultiplier;
		this.dropQuantityMultiplier = dropQuantityMultiplier;
	}

	/**
	 * Constructor for testing purposes with fixed {@link RSAKey} and generation {@link Date}.
	 *
	 * @param databaseConfiguration
	 * @param rsakey
	 * @param generationDate
	 */
	ServerIniConfiguration(
			DatabaseConfiguration databaseConfiguration,
			RSAKey rsakey,
			Date generationDate) {
		this.databaseConfiguration = databaseConfiguration;
		this.rsakey = rsakey;
		this.generationDate = generationDate;
	}

	public void setRates(Float xpMult, Float skillMult, Double dropMult, Double dropQtyMult) {
		this.xpMultiplier = xpMult;
		this.skillMultiplier = skillMult;
		this.dropMultiplier = dropMult;
		this.dropQuantityMultiplier = dropQtyMult;
	}

	/**
	 * Writes the actual configuration to the given {@link PrintWriter}.
	 * @param out
	 *  The {@link PrintWriter} to write on.
	 */
	public void write(PrintWriter out) {
		final DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
		out.println("# Generated .ini file for Test Game at " + format.format(this.generationDate));
		out.println("# Database and factory classes. Don't edit.");
		out.println("database_implementation=" + this.databaseImplementation);
		out.println("factory_implementation=" + this.factoryImplementation);
		out.println();
		out.println("# Database information. Edit to match your configuration.");
		databaseConfiguration.write(out);
		out.println();
		out.println("# TCP port stendhald will use. ");
		out.println("tcp_port=" + this.tcpPort);
		out.println();
		out.println("# World and RP configuration. Don't edit.");
		out.println("world=" + this.worldImplementation);
		out.println("ruleprocessor=" + this.ruleprocessorImplementation);
		out.println();
		out.println("turn_length=" + this.turnLength);
		out.println();
		out.println("server_typeGame=" + this.gameName);
		out.println("server_name=" + this.gameName + " Marauroa server");
		out.println("server_version=1.41.5");
		out.println("server_contact=https://sourceforge.net/tracker/?atid=514826&group_id=66537&func=browse");
		out.println();
		out.println("# Gameplay Rates & Multipliers");
		out.println("xp_multiplier=" + (xpMultiplier != null ? xpMultiplier : 1.0f));
		out.println("skill_multiplier=" + (skillMultiplier != null ? skillMultiplier : 1.0f));
		out.println("drop_multiplier=" + (dropMultiplier != null ? dropMultiplier : 1.0));
		out.println("drop_quantity_multiplier=" + (dropQuantityMultiplier != null ? dropQuantityMultiplier : 1.0));
		out.println();
		out.println("# Extensions configured on the server. Enable at will.");
		out.println("#server_extension=xxx");
		out.println("#xxx=some.package.Classname");
		out.println();
		out.println("statistics_filename=" + this.statisticsFilename);
		out.println();
		this.rsakey.print(out);
	}
}
