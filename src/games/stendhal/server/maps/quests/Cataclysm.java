/* $Id: Cataclysm.java,v 1.16 2010/12/29 17:44:35 kymara Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.quests;

import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Tell the player that Cataclysm is ahead.
 *
 * @author kymara
 */
public class Cataclysm extends AbstractQuest {
	@Override
	public String getSlotName() {
		return "cataclysm";
	}

	/**
	 * Makes Carmen tell you that she can sense big changes.
	 */
	private void carmen() {
		final SpeakerNPC npc = npcs.get("Carmen");
		if (npc == null) return;

		npc.addReply(Arrays.asList("troubled", "times", "trudnych", "czasach"),
				"I see many impending changes. I know that the #Cataclysm approaches.");
		npc.addReply("kataklizm",
				"Tak, jakieś wstrząsy, może odrodzenie starych duchów. Krainy mogą się zmienić i mogą powstać nowe szlaki.");
	}

	/**
	 * Makes Diogenes tell you to ask Carmen what's happening.
	 */
	private void diogenes() {
		final SpeakerNPC npc = npcs.get("Diogenes");
		if (npc == null) return;

		npc.addReply(Arrays.asList("cataclysm", "kataklizm"),
				"Jako mój przyjaciel. Sądzę, że młoda Carmen powie coś Tobie.");
	}

	/**
	 * Makes Hayunn Naratha refer to the Cataclysm.
	 */
	private void hayunn() {
		final SpeakerNPC npc = npcs.get("Hayunn Naratha");
		if (npc == null) return;

		npc.addReply(
			Arrays.asList("unwell", "niedobrze", "cataclysm", "kataklizm"),
			"Mam wrażenie, że to od dymu. Mam nadzieję, że to nic poważnego. W każdym razie daj mi znać, gdybyś czegoś potrzebował.");
	}

	/**
	 * Makes Monogenes speak of the fire and Cataclysm.
	 */
	private void monogenes() {
		final SpeakerNPC npc = npcs.get("Monogenes");
		if (npc == null) return;

		npc.addReply(
			Arrays.asList("fire", "Ogień", "ogień"),
			"Zaczął się w nocy, a teraz Semos świeci jak pochodnia. Mówią, że #kataklizm się zbliża.");

		npc.addReply(
			Arrays.asList("Cataclysm", "kataklizm"),
			"Nie widziałem nigdy takiego, ale mój wielki dziadek mówił mi o takich rzeczach. Inni widzą to jako katastrofę. Inni mówią, że takie zdarzenia pozwolą zacząć nowe życie i powstaną nowe szlaki.");
	}

	/**
	 * Makes Nomyr Ahba tell you rumours of the Cataclysm.
	 */
	private void nomyr() {
		final SpeakerNPC npc = npcs.get("Nomyr Ahba");
		if (npc == null) return;

		npc.addReply(
			Arrays.asList("information", "informację", "informacje"),
			"Cóż mój przyjacielu ogień, który rozprzestrzenia się w Semos powoduje, że zaczynamy chorować. Ludzie mówią, że zaczyna się #Kataklizm...");

		npc.addReply(
			Arrays.asList("Cataclysm", "kataklizm"),
			"Nie pytaj mnie dlaczego, ale świat będzie wyglądał inaczej w najbliższej przyszłości. Na szczęście nie mam domu, który mógłbym stracić. Naprawdę.");
	}

	/**
	 * Makes Sato tell you that Carmen can sense big changes.
	 */
	private void sato() {
		SpeakerNPC npc = npcs.get("Sato");
		if (npc == null) {
			npc = npcs.get("Nishiya");
		}
		if (npc == null) return;

		npc.addReply(Arrays.asList("times", "czasach", "cataclysm", "kataklizm"),
			"Z tego co wiem to moja owca zaczyna chorować. Może #Carmen będzie wiedziała co się tutaj dzieje.");

		npc.addReply(
			"Carmen",
			"Ona jest uzdrowicielem. Za pomocą swoich mocy może wyczuć wszystko co dziwne. Natomiast ja jestem tylko prostym handlarzem owiec.");
	}

	@Override
	public void addToWorld() {
		//super.addToWorld();

		carmen();
		diogenes();
		hayunn();
		monogenes();
		nomyr();
		sato();
	}

	@Override
	public String getName() {
		return "Cataclysm";
	}
	
	@Override
	public int getMinLevel() {
		return 0;
	}
	
	@Override
	public boolean isVisibleOnQuestStatus() {
		return false;
	}
	
	@Override
	public List<String> getHistory(final Player player) {
		return new ArrayList<String>();
	}
	
}
