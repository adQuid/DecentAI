package aibrain;

import java.util.List;

import model.Empire;
import aibrain.Game;

public interface IdeaGenerator {

	public List<List<Action>> generateIdeas(Game game, Empire empire, List<Action> possibilities, int iteration);
	
	public boolean hasFurtherIdeas(Game game, Empire empire, List<Action> possibilities, List<Action> committedActions, int iteration);
	
}
