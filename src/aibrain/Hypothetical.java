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
	private int tail;
	private Game game;
	private List<Modifier> modifiers;
	//this is a list of the actions the parent COULD have taken, not what it did
	private List<Action> possibleParentActions;
	//this is a list of the actions actually selected by the parent step, and avalible to try again
	private List<Action> usedParentActions;
	//actions this hypothetical is already commited to taking
	private List<Action> actions;

	private double scoreAccumulator;
	private final double DECAY_RATE = 0.8;
	
	public Hypothetical(Game game, List<Modifier> modifiers, AIBrain parent, List<Action> possibleParentActions, List<Action> usedParentActions, List<Action> actions, int ttl, int tail, Empire empire, double scoreAccumulator){
		
		this.game = GameCloner.cloneGame(game);
		this.modifiers = modifiers;
		this.parent = parent;
		this.possibleParentActions = possibleParentActions;
		this.usedParentActions = usedParentActions;
		this.actions = actions;
		this.ttl = ttl;
		this.tail = tail;
		this.empire = empire;
		this.scoreAccumulator = scoreAccumulator;
	}
	
	public HypotheticalResult calculate() {
		
		//base case where I'm out of time
		if(ttl == 0) {
			Game futureGame = GameCloner.cloneGame(game);
			for(int turn = 0; turn < tail; turn++) {
				futureGame.endRound();
				scoreAccumulator += new HypotheticalResult(game, actions, empire).getScore();
			}
			HypotheticalResult retval = new HypotheticalResult(game,actions,empire);
			retval.setScore(retval.getScore() + scoreAccumulator);
			return retval;
		}
		
		List<Action> possibleActions = game.returnActions();
		List<Action> passdownActions = game.returnActions();
		passdownActions.addAll(possibleParentActions);
		List<HypotheticalResult> allOptions = new ArrayList<HypotheticalResult>();
		
		HypotheticalResult thisLevelResult = new HypotheticalResult(game,actions,empire);
		
		//remove all actions that the parent could have done, but didn't do
		possibleParentActions.removeAll(usedParentActions);
		possibleActions.removeAll(possibleParentActions);

		List<List<Action>> ideas = SpaceGameIdeaGenerator.generateIdeas(possibleActions, (model.Game)game);
		
		//base case where I'm a top level hypothetical, and I can't even do anything this turn
		if(parent.getMaxTtl() == ttl && ideas.size() == 1) {//a size one list of actions will only have the "do nothing" action
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
			allOptions.add(packResult(new Hypothetical(futureGame,modifiers,parent,
					toPass,new ArrayList<Action>(current), new ArrayList<Action>(),ttl-1,tail,empire, scoreAccumulator*DECAY_RATE).calculate(),current));	
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
