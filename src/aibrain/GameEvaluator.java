package aibrain;

import model.Empire;
import model.Game;

public interface GameEvaluator {

	//Given a game state, how good do I find this outcome?
	public Score getValue(Game game, Empire empire);
	
}
