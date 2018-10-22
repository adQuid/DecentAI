package aibrain;

import java.util.List;

import aibrain.Game;

public interface IdeaGenerator {

	public List<List<Action>> generateIdeas(Game game, Player empire, int iteration);
	
	public boolean hasFurtherIdeas(Game game, Player empire, List<Action> committedActions, int iteration);
	
}
