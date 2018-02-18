package aibrain;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import cloners.GameCloner;
import model.Empire;
import model.Game;
import model.Planet;
import model.Tile;

public class Hypothetical {

	private static int numMade = 0;
	
	private AIBrain parent;
	private Empire empire;
	private int ttl;
	private Game game;
	//this is a list of the actions the parent COULD have taken, not what it did
	private List<Action> parentActions;
	//actions this hypothetical is already commited to taking
	private List<Action> actions;

	
	public Hypothetical(Game game, AIBrain parent, List<Action> parentActions, List<Action> actions, int ttl, Empire empire){
		
		this.game = GameCloner.cloneGame(game);
		this.parent = parent;
		this.parentActions = parentActions;
		this.actions = actions;
		this.ttl = ttl;
		this.empire = empire;
	}
	
	public HypotheticalResult calculate() {
		
		List<Action> possibleActions = game.returnActions();
		List<HypotheticalResult> allOptions = new ArrayList<HypotheticalResult>();
		
		//base case where I'm out of time
		if(ttl == 0) {
			return new HypotheticalResult(game,actions,empire);
		}
		
		//try adding a new action
		possibleActions.removeAll(parentActions);
		for(List<Action> current: SpaceGameIdeaGenerator.generateIdeas(possibleActions, game)) {
			Game futureGame = GameCloner.cloneGame(game);
			futureGame.setActionsForEmpire(current, empire);
			futureGame.endRound();
			allOptions.add(packResult(new Hypothetical(futureGame,parent,possibleActions, new ArrayList<Action>(),ttl-1,empire).calculate(),current));	
		}
		
		//pick best option
		double bestScore = 0;
		HypotheticalResult retval = null;
		for(HypotheticalResult current: allOptions) {
			if(retval == null || current.getScore() > bestScore) {
				bestScore = current.getScore();
				retval = current;
			}
		}
		
		return retval;
	}
	
	private HypotheticalResult packResult(HypotheticalResult result, List<Action> actions) {
		result.setActions(actions);
		return result;
	}
	
}
