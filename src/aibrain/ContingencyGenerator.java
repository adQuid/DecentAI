package aibrain;

import java.util.List;

public interface ContingencyGenerator {

	/**
	 * Given a game state, the player who the AI is thinking for, and the iteration of the AI, return possible actions 
	 * from OTHER players that might change the AI's own plans.
	 * @param game
	 * @param player The player who the AI is playing as. There should NOT be any contingencies returned from this player!
	 * @param iteration
	 * @return
	 */
	public List<Contingency> generateContingencies(Game game, Player player, int iteration);
	
}
