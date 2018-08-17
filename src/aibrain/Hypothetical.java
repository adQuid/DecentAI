package aibrain;

import java.util.ArrayList;
import java.util.HashMap;
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
	private int tightForcastLength;
	private int looseForcastLength;
	private int tail;
	private Game game;
	private List<Modifier> modifiers;
	//this is a list of the actions the parent COULD have taken, not what it did
	private List<Action> possibleParentActions;
	//this is a list of the actions actually selected by the parent step, and avalible to try again
	private List<Action> usedParentActions;
	//actions this hypothetical is already commited to taking
	private List<Action> actions;

	private Score scoreAccumulator;
	private final double DECAY_RATE = 0.8;
	
	public Hypothetical(Game game, List<Modifier> modifiers, AIBrain parent, List<Action> possibleParentActions, List<Action> usedParentActions, List<Action> actions, int tightForcastLength, int looseForcastLength, int tail, Empire empire, Score scoreAccumulator){
		
		this.game = GameCloner.cloneGame(game);
		this.modifiers = modifiers;
		this.parent = parent;
		this.possibleParentActions = possibleParentActions;
		this.usedParentActions = usedParentActions;
		this.actions = actions;
		this.tightForcastLength = tightForcastLength;
		this.looseForcastLength = looseForcastLength;
		this.tail = tail;
		this.empire = empire;
		this.scoreAccumulator = scoreAccumulator;
	}
	
	public HypotheticalResult calculate() {
		
		//base case where I'm out of time
		if(tightForcastLength == 0 && looseForcastLength == 0) {
			Game futureGame = GameCloner.cloneGame(game);
			for(int turn = 0; turn < tail; turn++) {
				futureGame.endRound();
				scoreAccumulator.addTo(new HypotheticalResult(game, actions, empire).getScore());
			}
			HypotheticalResult retval = new HypotheticalResult(game,actions,empire);
			retval.setScore(retval.getScore().addTo(scoreAccumulator));
			return retval;
		}
		
		List<Action> possibleActions = game.returnActions();
		List<Action> passdownActions = game.returnActions();
		passdownActions.addAll(possibleParentActions);
		List<HypotheticalResult> allOptions = new ArrayList<HypotheticalResult>();
		
		HypotheticalResult thisLevelResult = new HypotheticalResult(game,actions,empire);
		
		//remove all actions that the parent could have done, but didn't do
		possibleParentActions.removeAll(usedParentActions);
		//possibleActions.removeAll(possibleParentActions);
		
		List<Action> list = new ArrayList<Action>();
		for(Action current: possibleParentActions) {
			if(current.getType() == ActionType.cashNow) {
				list.add(current);
			}
		}
		possibleActions.removeAll(possibleParentActions);
		//possibleActions.addAll(list);
		
		List<List<Action>> ideas = SpaceGameIdeaGenerator.generateIdeas(possibleActions, (model.Game)game);
		
		//base case where I'm a top level hypothetical, and I can't even do anything this turn
		if(parent.getMaxTtl() == tightForcastLength && ideas.size() == 1) {//a size one list of actions will only have the "do nothing" action
			return thisLevelResult;
		}
				
		//add score from this round
		scoreAccumulator.addTo(thisLevelResult.getScore());
		
		//try adding a new action
		for(List<Action> current: ideas) {
			Game futureGame = GameCloner.cloneGame(game);
			futureGame.setActionsForEmpire(current, empire);
			futureGame.endRound();
			if(isInLooseForcastPhase()) {
				scoreAccumulator.addTo(new HypotheticalResult(game, actions, empire).getScore());
				futureGame.endRound();
			}
			List<Action> toPass = current.size()==0?passdownActions:futureGame.returnActions();
			allOptions.add(packResult((tightForcastLength == 0?
					new Hypothetical(futureGame,modifiers,parent,toPass,new ArrayList<Action>(current), new ArrayList<Action>(),tightForcastLength,looseForcastLength-1,tail,empire, scoreAccumulator.decay(DECAY_RATE)).calculate()
					:new Hypothetical(futureGame,modifiers,parent,toPass,new ArrayList<Action>(current), new ArrayList<Action>(),tightForcastLength-1,looseForcastLength,tail,empire, scoreAccumulator.decay(DECAY_RATE)).calculate()),current));	
		}
		
		//pick best option
		double bestScore = 0;
		HypotheticalResult retval = null;
		for(HypotheticalResult current: allOptions) {
			if(retval == null || current.getScore().totalScore() > bestScore) {
				bestScore = current.getScore().totalScore();
				retval = current;
			}
		}
		
		//as a second pass, try replacing the first action in the list with alternatives
		List<HypotheticalResult> variations = new ArrayList<HypotheticalResult>();
		if(tightForcastLength == parent.getMaxTtl()) {			
			variations.add(retval);
			List<List<Action>> actions = new ArrayList<List<Action>>(retval.getActions());
			for(List<Action> current: ideas) {
				actions.set(0, current);
				Game futureGame = GameCloner.cloneGame(game);
				variations.add(runPath(futureGame, actions));
			}
		}
		
		for(HypotheticalResult current: variations) {
			if(retval == null || current.getScore().totalScore() > bestScore) {
				bestScore = current.getScore().totalScore();
				retval = current;
			}
		}
		
		return retval;
	}
	
	private HypotheticalResult runPath(Game game, List<List<Action>> actions) {
		Score scoreAccumulator = new Score(new HashMap<String,Double>());
		for(List<Action> current: actions) {
			scoreAccumulator.decay(DECAY_RATE);
			game.setActionsForEmpire(current, empire);
			game.endRound();
			scoreAccumulator.addTo(new HypotheticalResult(game, current, empire).getScore());
		}
		
		HypotheticalResult retval = new HypotheticalResult(game,actions.get(0),empire);
		retval.setScore(retval.getScore().addTo(scoreAccumulator));
		return retval;
	}
	
	private HypotheticalResult packResult(HypotheticalResult result, List<Action> actions) {
		if(isInLooseForcastPhase()) {
			result.appendActionsFront(new ArrayList<Action>());
		}
		result.appendActionsFront(actions);
		return result;
	}
	
	private boolean isInLooseForcastPhase() {
		return tightForcastLength == 0;
	}
}
