package aibrain;

import java.util.List;

import actions.Action;
import model.Game;

public interface IdeaGenerator {

	public List<List<Action>> generateIdeas(List<Action> possibilities, Game game);
	
}
