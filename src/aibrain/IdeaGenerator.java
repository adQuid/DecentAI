package aibrain;

import java.util.List;

import aibrain.Game;

public interface IdeaGenerator {

	
	public List<List<Action>> generateIdeas(Game game, Player empire, int iteration);
	
	/**
	 * Used to determine if the AI brain should keep trying out adding more ideas to the plan.
	 * @param game 
	 * @param player 
	 * @param committedActions A List of actions the AI has already decided to take, based on previous iterations.
	 * @param iteration
	 * @return true if AI should generate and attempt further ideas, false otherwise.
	 */
	public boolean hasFurtherIdeas(Game game, Player player, List<Action> committedActions, int iteration);
	
}
