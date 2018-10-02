package aibrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spacegame.SpaceGameCloner;
import utils.ListUtils;

public class Hypothetical {

	private static int numMade = 0;
	
	private int iteration;
	private AIBrain parent;
	private Player empire;
	private int tightForcastLength;
	private int looseForcastLength;
	private int tail;
	private Game game;
	private List<Modifier> modifiers;
	//this is a list of the actions the parent COULD have taken, not what it did
	private List<Action> possibleParentActions;
	//this is a list of the actions actually selected by the parent step, and available to try again
	private List<Action> usedParentActions;
	//actions this hypothetical is already committed to taking
	private List<List<Action>> actions;
	private Plan plan;
	
	private IdeaGenerator ideaGenerator; 
	
	private Score scoreAccumulator;
	
	public Hypothetical(Game game, List<Modifier> modifiers, AIBrain parent, List<Action> possibleParentActions, 
			List<Action> usedParentActions, List<List<Action>> actions, int tightForcastLength, int looseForcastLength, 
			int tail, Player empire, Score scoreAccumulator, int iteration, IdeaGenerator ideaGen){
		this(game, modifiers, parent, possibleParentActions, usedParentActions, actions, tightForcastLength, looseForcastLength, tail, empire, scoreAccumulator, iteration, ideaGen, new Plan());
	}
	
	public Hypothetical(Game game, List<Modifier> modifiers, AIBrain parent, List<Action> possibleParentActions, 
			List<Action> usedParentActions, List<List<Action>> actions, int tightForcastLength, int looseForcastLength, 
			int tail, Player empire, Score scoreAccumulator, int iteration,  IdeaGenerator ideaGen, Plan plan){
		
		this.game = parent.getCloner().cloneGame(game);
		this.iteration = iteration;
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
		this.ideaGenerator = ideaGen;
		this.plan = plan;
	}
	
	public HypotheticalResult calculate() {
		return calculate(false);
	}
	
	public HypotheticalResult calculate(boolean debug) {
				
		//these are the actions I look at when trying out new actions this turn
		List<Action> possibleActions = game.returnActions(empire);
		//these are the actions I pass down to the next level to not consider if I don't do them at this level
		List<Action> passdownActions = game.returnActions(empire);
		passdownActions.addAll(possibleParentActions);
		List<HypotheticalResult> allOptions = new ArrayList<HypotheticalResult>();
			
		HypotheticalResult thisLevelResult = new HypotheticalResult(game,empire,plan,parent.getEvaluator());
		
		//remove all actions that the parent could have done, but didn't do
		possibleParentActions.removeAll(usedParentActions);		
		possibleActions.removeAll(possibleParentActions);
		
		//TODO: genericise this
		List<List<Action>> ideas = ideaGenerator.generateIdeas(game, empire, possibleActions, iteration);

		Game futureGame = parent.getCloner().cloneGame(game);
				
		//base case where I'm out of time
		if(tightForcastLength == 0 && looseForcastLength == 0) {
			for(int index = 0; index < tail; index++) {
				futureGame.endRound();
				scoreAccumulator.add(new HypotheticalResult(futureGame, this.empire,parent.getEvaluator()).getScore());
			}
			
			HypotheticalResult retval = new HypotheticalResult(futureGame, this.empire, plan,parent.getEvaluator());
			retval.setScore(retval.getScore().add(scoreAccumulator));
			return retval;
		}
							
		//add score from this round
		scoreAccumulator.add(thisLevelResult.getScore());
					
		parent.applyContingencies(futureGame,this.empire,1);
		
		//try adding a new action
		for(List<Action> current: ideas) {
						
			Score scoreToPass = new Score(scoreAccumulator);
			Game branchGame = parent.getCloner().cloneGame(futureGame);
			List<Action> combinedIdeas = ListUtils.combine(actions.get(0),current);
			branchGame.setActionsForPlayer(combinedIdeas, empire);
			branchGame.endRound();
			//skip a round when we are in loose forecasting
			if(isInLooseForcastPhase()) {
				scoreToPass.add(new HypotheticalResult(branchGame, empire,parent.getEvaluator()).getScore().decay(parent.getDecayRate()));
				futureGame.endRound();
			}
			List<Action> toPass = current.size()==0?passdownActions:branchGame.returnActions(empire);
			Plan planToPass = new Plan(plan);
			planToPass.addReasoning(new Reasoning("adding actions "+Arrays.toString(combinedIdeas.toArray())));
			planToPass.addActionListToEnd(combinedIdeas);
			allOptions.add(packResult((tightForcastLength == 0?
						new Hypothetical(branchGame,modifiers,parent,toPass,
							new ArrayList<Action>(current), actions.subList(1, actions.size()),tightForcastLength,
							looseForcastLength-1,tail,empire,
							scoreToPass.decay(parent.getDecayRate()),iteration,ideaGenerator,planToPass).calculate(debug)
						:new Hypothetical(branchGame,modifiers,parent,toPass,
							new ArrayList<Action>(current), actions.subList(1, actions.size()),tightForcastLength-1,
							looseForcastLength,tail,empire,
							scoreToPass.decay(parent.getDecayRate()), iteration,ideaGenerator,planToPass).calculate(debug))
					,combinedIdeas));	
		}
		
		//pick best option
		double bestScore = 0;
		HypotheticalResult retval = null;
		for(HypotheticalResult current: allOptions) {
			//warning for debugging, since there are two ways of rating the same game which is likely to break
			if(!debug && isAtTopOfForecast() && current.getScore().totalScore() != parent.runPath(parent.getCloner().cloneGame(game), current.getPlan()).getScore().totalScore()) {
				double result = parent.runPath(parent.getCloner().cloneGame(game), current.getPlan(), true).getScore().totalScore();
				System.err.println("rates as "+current.getScore().totalScore()+" vs "+result);
			}
			if(retval == null || current.getScore().totalScore() > bestScore) {
				bestScore = current.getScore().totalScore();
				retval = current;
			}
		}
		
		return retval;
	}
		
	private HypotheticalResult packResult(HypotheticalResult result, List<Action> actions) {
		return result;
	}
	
	private boolean isInLooseForcastPhase() {
		return tightForcastLength == 0;
	}
	
	private boolean isAtTopOfForecast() {
		return tightForcastLength == parent.getMaxTtl();
	}
}
