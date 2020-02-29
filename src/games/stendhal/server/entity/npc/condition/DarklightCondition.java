package games.stendhal.server.entity.npc.condition;

import java.util.Arrays;
import java.util.List;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.annotations.Dev;
import games.stendhal.server.core.config.annotations.Dev.Category;
import games.stendhal.server.core.rp.DarklightPhase;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.player.Player;

/**
 * Checks the current dark light phase.
 *
 * @author hendrik
 */
@Dev(category=Category.ENVIRONMENT, label="Time?")
public class DarklightCondition implements ChatCondition {

	private final List<DarklightPhase> darklightPhases;

	/**
	 * creates a new DaytimeCondition
	 *
	 * @param daylightPhases one or more daylight phases
	 */
	public DarklightCondition(final DarklightPhase... darklightPhases) {
		super();
		this.darklightPhases = Arrays.asList(darklightPhases);
	}

	public boolean fire(final Player player, final Sentence sentence, final Entity npc) {
		return darklightPhases.contains(DarklightPhase.current());
	}

	//@Override
//	public int hashCode() {
//		return HashCodeBuilder.reflectionHashCode(this);
//	}
//
//	@Override
//	public boolean equals(final Object obj) {
//		return EqualsBuilder.reflectionEquals(this, obj, false,
//				DarklightCondition.class);
//	}

}
