/**
 * 
 */
package games.stendhal.server.maps.quests.houses;

import games.stendhal.common.parser.ExpressionType;
import games.stendhal.common.parser.JokerExprMatcher;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.condition.AgeGreaterThanCondition;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.TextHasNumberCondition;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

final class ZakopaneHouseSeller extends HouseSellerNPCBase {
	/** Cost to buy house in zakopane. */
	private static final int COST_ZAKOPANE = 500000;
	private static final String WOJTEK_QUEST_SLOT = "gazda_wojtek_daily_item";
	private static final String JADZKA_QUEST_SLOT = "herbs_for_jadzka";
	private static final String ADAS_QUEST_SLOT = "pomoc_adasiowi";
	private static final String FRYDERYK_QUEST_SLOT = "scythe_fryderyk";
	private static final String BERCIK_QUEST_SLOT = "cech_gornika";
	private static final String ANDRZEJ_QUEST_SLOT = "andrzej_make_zlota_ciupaga";
	private static final String KAZIMIERZ_QUEST_SLOT = "zakopane_bank";

	ZakopaneHouseSeller(final String name, final String location, final HouseTax houseTax) {
		super(name, location, houseTax);
		init();
	}

	private void init() {
		// Other than the condition that you must not already own a house, there are a number of conditions a player must satisfy. 
		// For definiteness we will check these conditions in a set order. 
		// So then the NPC doesn't have to choose which reason to reject the player for (appears as a WARN from engine if he has to choose)

		// player is not old enough
		add(ConversationStates.ATTENDING, 
				 Arrays.asList("cost", "house", "buy", "purchase", "koszt", "dom", "kupić", "cenę", "cena"),
				 new NotCondition(new AgeGreaterThanCondition(HouseSellerNPCBase.REQUIRED_AGE)),
				 ConversationStates.ATTENDING,
				 "The price for a new home in Zakopane is "
				 + getCost()
				 + " money. But I cannot trust you yet. Return once you have spent at least " 
				 + Integer.toString((HouseSellerNPCBase.REQUIRED_AGE / 60)) + " hours in the realm.",
					null);


		// player doesn't have a house and is old enough but has not done required quests
		add(ConversationStates.ATTENDING, 
				 Arrays.asList("cost", "house", "buy", "purchase", "koszt", "dom", "kupić", "cenę", "cena"),
				 new AndCondition(new AgeGreaterThanCondition(HouseSellerNPCBase.REQUIRED_AGE), 
								  new QuestNotStartedCondition(HouseSellerNPCBase.QUEST_SLOT),
								  new NotCondition(
													  new AndCondition(
																	   new QuestCompletedCondition(ZakopaneHouseSeller.WOJTEK_QUEST_SLOT),
																	   new QuestCompletedCondition(ZakopaneHouseSeller.JADZKA_QUEST_SLOT),
																	   new QuestCompletedCondition(ZakopaneHouseSeller.ADAS_QUEST_SLOT),
																	   new QuestCompletedCondition(ZakopaneHouseSeller.FRYDERYK_QUEST_SLOT),
																	   new QuestCompletedCondition(ZakopaneHouseSeller.BERCIK_QUEST_SLOT),
																	   new QuestCompletedCondition(ZakopaneHouseSeller.ANDRZEJ_QUEST_SLOT),
																	   new QuestCompletedCondition(ZakopaneHouseSeller.KAZIMIERZ_QUEST_SLOT)))),
				 ConversationStates.ATTENDING, 
				 "The price for a new home in Zakopane is "
				 + getCost()
				 + " money. But before I can sell you property, you must earn the trust of our town and prove your #citizenship.",
				 null);

		// player is eligible to buy a house
		add(ConversationStates.ATTENDING, 
					Arrays.asList("cost", "house", "buy", "purchase", "koszt", "dom", "kupić", "cenę", "cena"),
				 new AndCondition(new QuestNotStartedCondition(HouseSellerNPCBase.QUEST_SLOT),
									new AgeGreaterThanCondition(HouseSellerNPCBase.REQUIRED_AGE), 
									new QuestCompletedCondition(ZakopaneHouseSeller.WOJTEK_QUEST_SLOT),
									new QuestCompletedCondition(ZakopaneHouseSeller.JADZKA_QUEST_SLOT),
									new QuestCompletedCondition(ZakopaneHouseSeller.ADAS_QUEST_SLOT),
									new QuestCompletedCondition(ZakopaneHouseSeller.FRYDERYK_QUEST_SLOT),
									new QuestCompletedCondition(ZakopaneHouseSeller.BERCIK_QUEST_SLOT),
									new QuestCompletedCondition(ZakopaneHouseSeller.ANDRZEJ_QUEST_SLOT),
									new QuestCompletedCondition(ZakopaneHouseSeller.KAZIMIERZ_QUEST_SLOT)),
				 ConversationStates.QUEST_OFFERED,
				 "A new home in Zakopane costs "
				 + getCost()
				 + " money. In addition, property tax is " + HouseTax.BASE_TAX
				 + " money per month. If you have a house in mind, state its number and I will check availability. "
				 + "Homes in Zakopane are numbered from "
				 + getLowestHouseNumber() + " to " + getHighestHouseNumber() + ".",
				 null);

		// handle house numbers getLowestHouseNumber() - getHighestHouseNumber()
		addMatching(ConversationStates.QUEST_OFFERED,
				 // match for all numbers as trigger expression
				ExpressionType.NUMERAL, new JokerExprMatcher(),
				new TextHasNumberCondition(getLowestHouseNumber(), getHighestHouseNumber()),
				ConversationStates.ATTENDING, 
				null,
				new BuyHouseChatAction(getCost(), QUEST_SLOT));

		addJob("I am the local real estate agent—I manage property sales across Zakopane. Ask for the #price if you are interested in acquiring a home.");
		addReply(Arrays.asList("citizen", "obywatelstwo"), "To obtain citizenship, you must aid the prominent residents of Zakopane:\n"
		+"Blacksmith Andrzej, young Adaś, elder Fryderyk,\n"
		+"Mine Overseer Bercik, Vault Keeper Kazimierz,\n"
		+"Mayor Wojtek, and Healer Gaździna Jadźka in the hospital.\n"
		+"Once you have helped them all, I will gladly sell you a home.");

		setDescription("You see the Zakopane real estate agent, ready to sell property to reputable citizens.");
		setEntityClass("estateagent2npc");
		setPosition(24, 3);
		initHP(100);
	}

	protected int getCost() {
		return ZakopaneHouseSeller.COST_ZAKOPANE;
	}

	@Override
	protected void createPath() {
		final List<Node> nodes = new LinkedList<Node>();
		nodes.add(new Node(24, 3));
		nodes.add(new Node(24, 16));
		nodes.add(new Node(19, 16));
		nodes.add(new Node(19, 14));
		nodes.add(new Node(17, 14));
		nodes.add(new Node(17, 15));
		nodes.add(new Node(13, 15));
		nodes.add(new Node(13, 3));
		nodes.add(new Node(14, 3));
		nodes.add(new Node(14, 7));
		nodes.add(new Node(13, 7));
		nodes.add(new Node(13, 16));
		nodes.add(new Node(14, 16));
		nodes.add(new Node(14, 21));
		nodes.add(new Node(21, 21));
		nodes.add(new Node(21, 16));
		nodes.add(new Node(24, 16));
		nodes.add(new Node(24, 7));
		nodes.add(new Node(33, 7));
		nodes.add(new Node(33, 5));
		nodes.add(new Node(24, 5));
		setPath(new FixedPath(nodes, true));
	}

	@Override
	protected int getHighestHouseNumber() {
		return 215;
	}

	@Override
	protected int getLowestHouseNumber() {
		return 201;
	}
}
