package games.stendhal.tools;

import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import games.stendhal.server.core.config.CreatureGroupsXMLLoader;
import games.stendhal.server.core.config.ItemGroupsXMLLoader;
import games.stendhal.server.core.rule.defaultruleset.DefaultCreature;
import games.stendhal.server.core.rule.defaultruleset.DefaultItem;
import games.stendhal.server.entity.creature.impl.DropItem;

public class CreateHTML {

	public static void main(String[] args) {
		try {
			generateCreatures();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void generateCreatures() throws Exception {
		final CreatureGroupsXMLLoader loader = new CreatureGroupsXMLLoader("C://creatures.xml");
		final List<DefaultCreature> creatures = loader.load();

		Collections.sort(creatures, new Comparator<DefaultCreature>() {

			@Override
			public int compare(final DefaultCreature o1, final DefaultCreature o2) {
				return o1.getLevel() - o2.getLevel();
			}
		});

		int level = -1;

		for (final DefaultCreature creature : creatures) {
			System.out.println(creature.getLevel() + ";" + creature.getAtk()
					+ ";" + creature.getDef() + ";" + creature.getHP() + ";"
					+ creature.getXP());
		}
		System.out.println();

		System.exit(0);

		for (final DefaultCreature creature : creatures) {
			if (creature.getLevel() != level) {
				level = creature.getLevel();
				System.out.println("= Level " + level + "=");
			}

			final String name = creature.getCreatureName();
			System.out.println("== " + name + " ==");
			System.out.println("{{Creature|");
			System.out.println("|name= " + name + "");
			System.out.println("|image= " + name + "");
			System.out.println("|hp= " + creature.getHP() + "");
			System.out.println("|atk= " + creature.getAtk() + "");
			System.out.println("|def= " + creature.getDef() + "");
			System.out.println("|exp= " + creature.getXP() / 20 + "");
			System.out.println("|behavior = '''(TODO)'''.");
			System.out.println("|location = '''(TODO)'''.");
			System.out.println("|strategy = '''(TODO)'''.");
			System.out.println("|loot = ");

			for (final DropItem item : creature.getDropItems()) {
				System.out.println(item.min + "-" + item.max + " " + item.name
						+ "<br>");
			}

			System.out.println("}}");
			System.out.println("");
		}
	}

}

