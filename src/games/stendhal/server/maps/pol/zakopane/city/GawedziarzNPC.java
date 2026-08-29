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
 //zrobione na podstawie ExperiencedWarriorNPC z Semos/plains
 
package games.stendhal.server.maps.pol.zakopane.city;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.core.rule.defaultruleset.DefaultCreature;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.TriggerInListCondition;
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Experienced warrior knowing a lot about creatures (location zakopane_s).
 * Original name: Starkad
 *
 * @author johnnnny
 */

public class GawedziarzNPC implements ZoneConfigurator  {

	@Override
	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
		buildNPC(zone);
	}

	/**
	 * cost of the information for players. Final cost is: INFORMATION_BASE_COST +
	 * creatureLevel * INFORMATION_COST_LEVEL_FACTOR
	 */
	static final int INFORMATION_BASE_COST = 20;

	/**
	 * multiplier of the creature level for the information cost.
	 */
	static final double INFORMATION_COST_LEVEL_FACTOR = 2;

	/**
	 * literals for probabilities. %s is replaced with item description (name
	 * and amount)
	 */
	private static Map<Double, String> probabilityLiterals;

	/**
	 * literal for item amounts %s is replaced with singular item name, %a with
	 * "a/an item name" depending on the item name.
	 */
	private static Map<Integer, String> amountLiterals;

	/**
	 * literal for how dangerous a creature is based on the percentual
	 * difference to player level %s is replaced with singular creature name, %S
	 * with plural.
	 */
	private static Map<Double, String> dangerLiterals;

	/**
	 * literals for line starts. %s is replaced with singular creature name, %S
	 * plural.
	 */
	private static final String[] LINE_STARTS = new String[] { "Ah, aye! I know the beasts known as %s!",
			"Back when I was in my prime, many a %s fell by my hand!",
			"%s, ah how I loved tracking them through the peaks!",
			"Let an old hunter recall... %s... aye, now it comes back to me!",
			"Once I nearly met my end when I crossed paths with %a!",
			"I fought glorious battles against the beast called %S!", };

	static {
		probabilityLiterals = new LinkedHashMap<Double, String>();
		probabilityLiterals.put(100.0, "always %s");
		probabilityLiterals.put(99.99, "almost always %s");
		probabilityLiterals.put(75.0, "unusually often %s");
		probabilityLiterals.put(55.0, "more often than not %s");
		probabilityLiterals.put(40.0, "very often %s");
		probabilityLiterals.put(20.0, "often %s");
		probabilityLiterals.put(5.0, "sometimes %s");
		probabilityLiterals.put(1.0, "rarely %s");
		probabilityLiterals.put(0.1, "very rarely %s");
		probabilityLiterals.put(0.001, "extremely rarely %s");
		probabilityLiterals.put(0.0001, "one in a thousand carries %s");
		probabilityLiterals.put(0.00000001,
				"tales say it might hold %s, though I never saw it myself");
		probabilityLiterals.put(0.0, "never %s");

		amountLiterals = new LinkedHashMap<Integer, String>();
		amountLiterals.put(2000, "thousands of %s");
		amountLiterals.put(200, "hundreds of %s");
		amountLiterals.put(100, "many %s");
		amountLiterals.put(10, "a few %s");
		amountLiterals.put(2, "several %s");
		amountLiterals.put(1, "%a");

		dangerLiterals = new LinkedHashMap<Double, String>();
		dangerLiterals.put(40.0, "%s would strike you down in a single blow!");
		dangerLiterals.put(15.0,
				"%s would prove utterly lethal to you—do not dare face it alone!");
		dangerLiterals.put(2.0, "%s is exceedingly dangerous for you, beware!");
		dangerLiterals.put(1.8, "%S are terribly dangerous, watch yourself!");
		dangerLiterals.put(1.7,
				"%S are perilous for you, keep healing potions close at hand!");
		dangerLiterals.put(1.2,
				"It may prove dangerous for you, guard your life carefully!");
		dangerLiterals.put(0.8,
				"Slaying one would be an honorable feat for you!");
		dangerLiterals.put(0.5, "Defeating a %s should be like child's play for you!");
		dangerLiterals.put(0.3, "Defeating a %s will be effortless for you.");
		dangerLiterals.put(0.0, "%s poses no challenge whatsoever to you.");
	}

	/**
	 * %1 = time to respawn.
	 */
	private static final String[] RESPAWN_TEXTS = new String[] {
			"If you wait at the right hunting spot for %1, you might just spot another.",
			"Another will rise %1 after its defeat.", "Hunting one in %1 gives you another chance to claim trophies from its remains." };

	/**
	 * %1 = list of items dropped.
	 */
	private static final String[] CARRY_TEXTS = new String[] { "They carry %1.",
			"When felled, they yield %1.", "The carcass contains %1." };

	/**
	 * no attributes.
	 */
	private static final String[] CARRY_NOTHING_TEXTS = new String[] {
			"I know not whether they carry anything of worth.",
			"I never witnessed any of them bearing treasure." };

	/**
	 * %1 = list of locations.
	 */
	private static final String[] LOCATION_TEXTS = new String[] {
			"I have spotted %1.", "You ought to find %1.",
			"I hunted down several in %1." };

	/**
	 * %1 = name of the creature.
	 */
	private static final String[] LOCATION_UNKNOWN_TEXTS = new String[] { "Perhaps that is not the true name of the beast. I have never heard of %1." };

	private static CreatureInfoZ creatureInfoz = new CreatureInfoZ(probabilityLiterals,
																amountLiterals, dangerLiterals, LINE_STARTS, RESPAWN_TEXTS,
																CARRY_TEXTS, CARRY_NOTHING_TEXTS, LOCATION_TEXTS,
																LOCATION_UNKNOWN_TEXTS);
																
  private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Sabała") {

	@Override
	@SuppressWarnings("all") // "dead"
	public void createDialog() {
		class StateInfo {
			private String creatureName;

			private int informationCost;

			void setCreatureName(final String creatureName) {
				this.creatureName = creatureName;
			}

			String getCreatureName() {
				return creatureName;
			}

			void setInformationCost(final int informationCost) {
				this.informationCost = informationCost;
			}

			int getInformationCost() {
				return informationCost;
			}
		}

		final StateInfo stateInfo = new StateInfo();

		addGreeting();
		setLevel(600);

		addJob("My trade? I am a renowned highland storyteller and veteran hunter across these Tatra peaks. Surprised you haven't heard of old Sabała!");
		addQuest("I have no quests for you right now, young traveler.");
		addHelp("If you wish, I can share tales about the #creatures and beasts I tracked during my many hunts.");
		addOffer("For a few gold coins, I can share hunter's lore about any #creatures I have encountered in the wild.");

		add(ConversationStates.ATTENDING, Arrays.asList("creature", "creatures", "beast", "beasts", "potworach", "potwór"), null,
				ConversationStates.QUESTION_1,
				"Which creature would you like to hear about?", null);

		add(ConversationStates.QUESTION_1, "",
				new NotCondition(new TriggerInListCondition(ConversationPhrases.GOODBYE_MESSAGES)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					public void fire(final Player player, final Sentence sentence, final EventRaiser speakerNPC) {
						final String creatureName = sentence.getTriggerExpression().getNormalized();
						final DefaultCreature creature = SingletonRepository.getEntityManager().getDefaultCreature(creatureName);
						if (creature == null) {
							speakerNPC.say("I have never heard of such a beast! Perhaps you misspelled the name. Tell me once more what it was called.");
							speakerNPC.setCurrentState(ConversationStates.QUESTION_1);
						} else {
							stateInfo.setCreatureName(creatureName);
							if (INFORMATION_BASE_COST > 0) {
								final int informationCost = getCost(player, creature);
								stateInfo.setInformationCost(informationCost);
								speakerNPC.say("That lore will cost you "
										+ informationCost
										+ " gold. Have you got enough coin?");
								speakerNPC.setCurrentState(ConversationStates.BUY_PRICE_OFFERED);
							} else {
								speakerNPC.say(getCreatureInfoZ(player,
										stateInfo.getCreatureName())
										+ " If you wish to hear about another creature, just name it.");
								speakerNPC.setCurrentState(ConversationStates.QUESTION_1);
							}
						}
					}

					private int getCost(final Player player, final DefaultCreature creature) {
						return (int) (INFORMATION_BASE_COST + INFORMATION_COST_LEVEL_FACTOR
								* creature.getLevel());
					}
				});

		add(ConversationStates.BUY_PRICE_OFFERED,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					public void fire(final Player player, final Sentence sentence, final EventRaiser speakerNPC) {
						if (stateInfo.getCreatureName() != null) {
							if (player.drop("money",
									stateInfo.getInformationCost())) {
								String infoString = getCreatureInfoZ(player, stateInfo.getCreatureName());
								infoString += " If you wish to hear about another creature, just name it.";
								speakerNPC.say(infoString);
								speakerNPC.setCurrentState(ConversationStates.QUESTION_1);
							} else {
								speakerNPC.say("You don't have enough gold.");
							}
						}
					}
				});

		add(ConversationStates.BUY_PRICE_OFFERED,
				ConversationPhrases.NO_MESSAGES, null, ConversationStates.IDLE,
				"Come back later when you're ready to hear more hunting tales.", null);

		addGoodbye("Go with God!");
	}
	
	   @Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(35,46));
				nodes.add(new Node(35,82));
				nodes.add(new Node(29,82));
				nodes.add(new Node(29,97));
				nodes.add(new Node(23,97));
				nodes.add(new Node(23,106));
				nodes.add(new Node(2,106));
				nodes.add(new Node(2,77));
				nodes.add(new Node(1,77));
				nodes.add(new Node(1,43));
				nodes.add(new Node(21,43));
				nodes.add(new Node(21,46));

				setPath(new FixedPath(nodes, true));
			}

		};
		npc.setPosition(35, 46);
		npc.setEntityClass("npcstaryprzewodnik");
		npc.setDescription("You see Sabała, the legendary Podhale highlander, honorary Tatra guide, musician, master hunter, and folklorist.");
		zone.add(npc);	
	}	
				
	private static String getCreatureInfoZ(final Player player, final String creatureName) {
		String result;
		final DefaultCreature creature = SingletonRepository.getEntityManager().getDefaultCreature(creatureName);
		if (creature != null) {
			result = creatureInfoz.getCreatureInfoZ(player, creature, 3, 8, true);
		} else {
			result = "Nigdy nie słysołek o takim potworze!";
		}
		return result;
	}
}
