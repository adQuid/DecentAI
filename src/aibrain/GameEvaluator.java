package aibrain;

import aibrain.Game;

public interface GameEvaluator {

	/**
	 * @param game
	 * @param empire
	 * @return A Score object representing the value of that state.
	 */
	public Score getValue(Game game, Player empire);
	
}
