/***************************************************************************
 *                   (C) Copyright 2003-2016 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.semos.city;

//import java.util.LinkedList;
//import java.util.List;
import java.util.Map;

import games.stendhal.common.MathHelper;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
//import games.stendhal.server.core.pathfinder.FixedPath;
//import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.TimeUtil;

public class PkRemoverNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildSemosTempleArea(zone);
	}

	private void buildSemosTempleArea(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Wiser") {

//			@Override
//			protected void createPath() {
//				final List<Node> nodes = new LinkedList<Node>();
//				nodes.add(new Node(8, 19));
//				nodes.add(new Node(8, 20));
//				nodes.add(new Node(15, 20));
//				nodes.add(new Node(15, 19));
//				nodes.add(new Node(16, 19));
//				nodes.add(new Node(16, 14));
//				nodes.add(new Node(15, 14));
//				nodes.add(new Node(15, 13));
//				nodes.add(new Node(12, 13));
//				nodes.add(new Node(8, 13));
//				nodes.add(new Node(8, 14));
//				nodes.add(new Node(7, 14));
//				nodes.add(new Node(7, 19));
//				setPath(new FixedPath(nodes, true));
//			}

			@Override
			protected void createDialog() {

				// player has met io before and has a pk skull
				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new AndCondition(new GreetingMatchesNameCondition(getName()),
								new QuestStartedCondition("pk_removal"),
								new ChatCondition() {
									@Override
									public boolean fire(final Player player, final Sentence sentence, final Entity entity) {
										return player.isBadBoy() ;
									}
								}),
				        ConversationStates.QUESTION_1,
				        null,
				        new SayTextAction("Hi [name]. I see that you have been branded with the mark of a killer. Do you wish to have it removed?"));

				// player has met io before and has not got a pk skull
				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new AndCondition(new GreetingMatchesNameCondition(getName()),
								new QuestStartedCondition("pk_removal"),
								new ChatCondition() {
									@Override
									public boolean fire(final Player player, final Sentence sentence, final Entity entity) {
										return !player.isBadBoy() ;
									}
								}),
				        ConversationStates.ATTENDING,
				        null,
				        new SayTextAction("Hi [name]. How can I #help you this time?"));

				// first meeting with player
				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new AndCondition(new GreetingMatchesNameCondition(getName()),
								new QuestNotStartedCondition("pk_removal")),
						ConversationStates.ATTENDING,
				        null,
				        new MultipleActions(
				        		new SayTextAction("Hello. How are you doing in this fine day?"),
				        		new SetQuestAction("pk_removal", "start")));

				add(ConversationStates.QUESTION_1, ConversationPhrases.YES_MESSAGES, null, ConversationStates.ATTENDING,
				        null, new ChatAction() {

					        @Override
							public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						       	if ((player.getLastPVPActionTime() > System.currentTimeMillis()
											- 10 * MathHelper.MILLISECONDS_IN_ONE_MINUTE)) {
									// player attacked another within the last two weeks
									long timeRemaining = player.getLastPVPActionTime() - System.currentTimeMillis()
										+ 10 * MathHelper.MILLISECONDS_IN_ONE_MINUTE;
									raiser.say("You will have to abstain from even attacking other people for 10 minutes. So come back in " + TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L)) + ". And remember, I can see when you atack someone");
								} else {
									// player has fulfilled all requirements to be rehabilitated
									raiser.say("DO you really want to remove it?");
									raiser.setCurrentState(ConversationStates.QUESTION_2);
								}
							}
					    }
				);
				// player didn't want pk icon removed, offer other help
				add(ConversationStates.QUESTION_1, ConversationPhrases.NO_MESSAGES, null, ConversationStates.ATTENDING, "Fine! Then state your business.", null);
				// player satisfied the pk removal requirements and said yes they were sorry
				add(ConversationStates.QUESTION_2, ConversationPhrases.YES_MESSAGES, null, ConversationStates.ATTENDING,
				        "Sure, I will remove.", new ChatAction() {

					        @Override
							public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
								player.rehabilitate();
							} });
				// player said no they are not really sorry
				add(ConversationStates.QUESTION_2, ConversationPhrases.NO_MESSAGES, null, ConversationStates.IDLE, "Okay then...", null);
				//addJob("I am committed to harnessing the total power of the human mind. I have already made great advances in telepathy and telekinesis; however, I can't yet foresee the future, so I don't know if we will truly be able to destroy Blordrough's dark legion...");
				//addQuest("Well, there's not really much that I need anyone to do for me right now. And I... Hey! Were you just trying to read my private thoughts? You should always ask permission before doing that!");
				addGoodbye();
				// further behaviour is defined in the MeetIo quest.
			}
		};

		npc.setEntityClass("floattingladynpc");
		npc.setDescription("You see a Wise man. He will help you to solve your trouble.");
		npc.setPosition(8, 3);
		npc.initHP(100);
		zone.add(npc);
	}
}
