package aibrain;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import cloners.GameCloner;
import model.Empire;
import model.Planet;
import model.Tile;
import spacegame.SpaceGameIdeaGenerator;

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

	private double scoreAccumulator;
	private final double DECAY_RATE = 0.8;
	
	public Hypothetical(Game game, AIBrain parent, List<Action> parentActions, List<Action> actions, int ttl, Empire empire, double scoreAccumulator){
		
		this.game = GameCloner.cloneGame(game);
		this.parent = parent;
		this.parentActions = parentActions;
		this.actions = actions;
		this.ttl = ttl;
		this.empire = empire;
		this.scoreAccumulator = scoreAccumulator;
	}
	
	public HypotheticalResult calculate() {
		
		List<Action> possibleActions = game.returnActions();
		List<Action> passdownActions = game.returnActions();
		passdownActions.addAll(parentActions);
		List<HypotheticalResult> allOptions = new ArrayList<HypotheticalResult>();
		
		HypotheticalResult thisLevelResult = new HypotheticalResult(game,actions,empire);
		
		possibleActions.removeAll(parentActions);
		List<List<Action>> ideas = SpaceGameIdeaGenerator.generateIdeas(possibleActions, (model.Game)game);
		
		//base case where I'm a top level hypothetical, and I can't even do anything this turn
		if(parent.getMaxTtl() == ttl && ideas.size() == 1) {
			return thisLevelResult;
		}
		
		//base case where I'm out of time
		if(ttl == 0) {
			thisLevelResult.setScore(thisLevelResult.getScore()+scoreAccumulator);
			return thisLevelResult;
		}
		
		//add score from this round
		scoreAccumulator += thisLevelResult.getScore();
		
		//try adding a new action
		for(List<Action> current: ideas) {
			Game futureGame = GameCloner.cloneGame(game);
			futureGame.setActionsForEmpire(current, empire);
			futureGame.endRound();
			List<Action> toPass = current.size()==0?passdownActions:futureGame.returnActions();
			allOptions.add(packResult(new Hypothetical(futureGame,parent,toPass, new ArrayList<Action>(),ttl-1,empire, scoreAccumulator*DECAY_RATE).calculate(),current));	
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
