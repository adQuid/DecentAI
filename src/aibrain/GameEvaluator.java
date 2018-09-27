package aibrain;

import aibrain.Game;

public interface GameEvaluator {

	//Given a game state, how good do I find this outcome?
	public Score getValue(Game game, Player empire);
	
}
