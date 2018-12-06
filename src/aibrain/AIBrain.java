package aibrain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A stateful representation of a player's current plans. This will be the main point of interaction with the game 
 * you are integrating into.
 * 
 */
public class AIBrain {
	
	private Player self;
	private int maxTtl = 0;
	private int lookAheadSecondary = 0;
	private int tailLength = 0;
	private HypotheticalResult lastIdea = null;
	//the behavior of decay actually biases towards LATER turns, not earlier ones, with a value < 1
	private double decayRate = 0.8;
	
	private IdeaGenerator ideaGenerator;
	private ContingencyGenerator contingencyGenerator;
	private GameEvaluator gameEvaluator;
	private GameCloner gameCloner;
	
	private Game trueGame;
	
	//for debugging
	private List<String> logs = new ArrayList<String>();
	
	/**
	 * 
	 * @param self The player this AI is making decisions for
	 * @param lookAhead How many turns in advance the AI will think when making plans. Usually a higher lookAhead will result
	 * in smarter, but slower decision-making, but in situations where the actions of other player's have very large impacts, 
	 * a higher lookAhead may be a waste of time, or even hurt the AI as it works towards complex plans it can't hope to 
	 * complete without being interrupted.
	 * @param lookAheadSecondary A hybrid of lookAhead and lookAheadTail. This will probably be deprecated soon.
	 * @param lookAheadTail After the primary and secondary forecasts, the AI brain will continue to run the game a number of
	 * turns equal to the tail length without taking any more actions, to better see long-term consequences of a plan. In some
	 * games this is vital to making good plans in a reasonable amount of time, in others this is useless.
	 * @param ideaGenerator
	 * @param contingencyGenerator
	 * @param gameEvaluator
	 * @param gameCloner
	 */
	public AIBrain(Player self, int lookAhead, int lookAheadSecondary, int lookAheadTail,IdeaGenerator ideaGenerator,
			ContingencyGenerator contingencyGenerator, GameEvaluator gameEvaluator, GameCloner gameCloner) {
		this.self = self;
		this.maxTtl = lookAhead;
		this.lookAheadSecondary = lookAheadSecondary;
		this.tailLength = lookAheadTail;
		this.ideaGenerator = ideaGenerator;
		this.contingencyGenerator = contingencyGenerator;
		this.gameEvaluator = gameEvaluator;
		this.gameCloner = gameCloner;
	}
	
	/**
	 * Run one turn of the AI. This modifies the plan in the brain's internal memory, so WILL break if the turn does not end 
	 * in the real game.
	 * @param sourceGame The real game it is making plans for. This game will NOT be modified in any way; the controller using 
	 * Decent AI is left to either implement the suggested plan, or do something else with it.
	 * @return A HypotheticalResult representing the best path found by this Brain.
	 */
	public HypotheticalResult runAI(Game sourceGame){
		this.lastIdea = getNewPlan(sourceGame);
		return lastIdea;
	}
	
	public HypotheticalResult getNewPlan(Game sourceGame) {
		
		this.trueGame = sourceGame.imageForPlayer(self);
		
		HypotheticalResult retval = null;
		
		if(lastIdea == null) {
			addLog("I have no plan");
			lastIdea = runIterations(trueGame, maxTtl);
			retval = lastIdea;
		} else {
			retval = new HypotheticalResult(lastIdea);
			
			//we presumably did the first round of the last idea, so lets remove it
			retval.getPlan().removeActionListFromFront();

			//is my last idea still working?
			Score latestScore = runPath(gameCloner.cloneGame(trueGame), retval.getPlan()).getScore();
			Score assumedScore = retval.getScore().withoutFirstRound();
			if(latestScore.totalScore().compareTo(assumedScore.totalScore()) < 0) {
				addLog("this plan got worse: "+latestScore+" vs "+assumedScore);
				retval = runIterations(trueGame, maxTtl);
			} else {
				addLog("this plan is just as good or better: "+latestScore.totalScore()+" vs "+assumedScore);				
				
				//now we add a new final step to keep the same length
				HypotheticalResult appendResult = runIterations(runGame(trueGame,retval.getPlan()),maxTtl/2 + 1);
								
				//push the new action onto the end, and adjust score appropriately
				retval.appendActionsEnd(appendResult.getImmediateActions());
				retval.setScore(runPath(gameCloner.cloneGame(trueGame), retval.getPlan()).getScore());
				
				//just in case, let's see if there are any last minute opportunities or problems that came up just this turn
				HypotheticalResult opportunityResult = immediateIteration(trueGame, retval.getImmediateActions(), retval.getPlan());
				
				if(opportunityResult != null) {
					addLog("...but I'm ammending the immedate actions with:");
					for(Action current: opportunityResult.getImmediateActions()) {
						addLog(current.toString());
					}
					retval.getPlan().getPlannedActions().set(0, opportunityResult.getImmediateActions());
				}
			}	
		}

		//finally, try removing any of the actions from this turn and see if things end up better without them
		boolean shouldContinue = true;
		while(shouldContinue) {
			shouldContinue = false;
			innerLoop: for(int index=0; index < retval.getImmediateActions().size(); index++) { //in case people duplicate actions I want to remove by index
							
				List<Action> ammendedActions = new ArrayList<Action>(retval.getImmediateActions());
				ammendedActions.remove(index);
				
				Plan ammendedPlan = new Plan(retval.getPlan());
				ammendedPlan.getPlannedActions().set(0, ammendedActions);
				
				HypotheticalResult ammendedResult = runPath(trueGame, ammendedPlan);
				if(ammendedResult.getScore().totalScore().compareTo(retval.getScore().totalScore()) >= 0) {
					//this is safe because I immediately get out of the loop
					retval.getPlan().getPlannedActions().set(0, ammendedResult.getImmediateActions());
					shouldContinue = true;
					break innerLoop;
				}
			}
		}
		
		//pure debugging here
		addLog("Reasoning this turn:");
		for(List<Action> current: retval.getPlan().getPlannedActions()) {
			String toAdd = ">adding actions ";
			for(Action action: current) {
				toAdd += action.toString() + ", ";
			}
			addLog(toAdd);
		}
		
		return retval;
	}
	
	/**
	 * Returns marginal effect of accepting the deal suggested. A value of 0 represents no change, a value of -0.5 
	 * represents the player would do 50% worse by taking the deal, and so on. 
	 * @param sourceGame
	 * @param deal
	 * @return
	 */
	public BigDecimal evaluateDeal(Game sourceGame, Deal deal) {
		//if I don't know what I'm doing without the deal, figure it out real quick
		HypotheticalResult baseCase = getNewPlan(sourceGame);
		
		HypotheticalResult resultWithDeal = runIterations(sourceGame, maxTtl, deal);
		
		if(baseCase.getScore().totalScore().compareTo(new BigDecimal(0)) == 0) {
			if(resultWithDeal.getScore().totalScore().compareTo(new BigDecimal(0)) > 0) {
				return new BigDecimal(1);
			} else if (resultWithDeal.getScore().totalScore().compareTo(new BigDecimal(0)) < 0) {
				return new BigDecimal(-1);
			} else {
				return new BigDecimal(0);
			}
		} else {
			//value of deal is (score with deal - base) / (abs(base))
			BigDecimal scoreDifference = resultWithDeal.getScore().totalScore().subtract(baseCase.getScore().totalScore());
		
			//debug
			System.out.println(resultWithDeal.getScore()+" vs "+baseCase.getScore());
			return scoreDifference.divide(baseCase.getScore().totalScore().abs(),new MathContext(Score.PRECISION));		
		}
	}
	
	private HypotheticalResult runIterations(Game game, int forcast) {
		return runIterations(game,forcast,null);
	}
	
	private HypotheticalResult runIterations(Game game, int forcast, Deal dealToConsider) {
		int iteration = 1;
		HypotheticalResult result = null;//this SHOULD fail if the variable is not changed
		List<Action> committedActions = new ArrayList<Action>();
		Plan plan;
		Game copyGame = this.gameCloner.cloneGame(game);
		if(dealToConsider != null) {
			plan = Plan.emptyPlan(forcast).addTo(dealToConsider.offers.get(self));

			//this duplicates the plan for self, but it will be erased in the hypothetical's calculate method
			//also, this only works for one layer right now
			for(Player current: dealToConsider.offers.keySet()) {
				copyGame.setActionsForPlayer(dealToConsider.offers.get(current).getLayer(0), current);
			}
		
		}else{
			plan = Plan.emptyPlan(forcast);
		}
		while(this.ideaGenerator.hasFurtherIdeas(copyGame, self, committedActions, iteration)) {		
			Hypothetical hypothetical = new Hypothetical(copyGame, 
					this, plan.getPlannedActions(), forcast,
                    lookAheadSecondary, tailLength, self, iteration,ideaGenerator);
			result = hypothetical.calculate();
			committedActions = result.getImmediateActions();
			plan = result.getPlan();
			
			iteration++;
		}
		
		if(result == null) {
			System.err.println("Idea Generator generated no ideas on iteration 1");

			Hypothetical hypothetical = new Hypothetical(copyGame,
					this, plan.getPlannedActions(), forcast,
                    lookAheadSecondary, tailLength, self, 1,ideaGenerator);
			return hypothetical.calculate();
		}
		
		return result;
	}
	
	//TODO: merge this with the one above
	private HypotheticalResult immediateIteration(Game game, List<Action> committedActions, Plan plan) {
		
		if(ideaGenerator.hasFurtherIdeas(game, self, committedActions, 1)) {
			
			for(Player current: game.getPlayers()) {
				//remove existing actions. Should this be done here?
				game.setActionsForPlayer(new ArrayList<Action>(), current);
			}
			
			Hypothetical hypothetical = new Hypothetical(game, 
					this, plan.getPlannedActions(), maxTtl/2 + 1,
                    lookAheadSecondary, tailLength, self, 1,ideaGenerator);
			HypotheticalResult result = hypothetical.calculate();
			committedActions = result.getImmediateActions();
			plan = result.getPlan();
			
			return result;
		}else {
			return null; //is this a good use of a null? Is anything?
		}
	}
	
	//like runPath, but it doesn't track score
	private Game runGame(Game game, Plan plan) {
		Game copyGame = gameCloner.cloneGame(game);
		
		for(int actionIndex = 0; actionIndex < plan.getPlannedActions().size(); actionIndex++) {
			
			applyContingencies(copyGame,this.self,1);//what should iteration be here?
			
			copyGame.setActionsForPlayer(plan.getPlannedActions().get(actionIndex), this.self);
			copyGame.endRound();
			
			//if we are in loose forcast phase, skip a round
			if(actionIndex >= maxTtl) {
				copyGame.endRound();
			}
			
		}
		
		return copyGame;
	}
	
	HypotheticalResult runPath(Game game, Plan plan) {
		return runPath(game,plan,false);
	}
	
	//in kind of quirky behavior, this only runs path to the length of the plan provided
	HypotheticalResult runPath(Game game, Plan plan, boolean debug) {
		Game copyGame = gameCloner.cloneGame(game);
		
		//clear any existing actions; we only look at the plan
		for(Player current: copyGame.getPlayers()) {
			//copyGame.setActionsForPlayer(new ArrayList<Action>(), current);
		}
		
		Score scoreAccumulator = new Score();
		
		if(debug)System.err.print("[");
		
		for(int actionIndex = 0; actionIndex < plan.getPlannedActions().size(); actionIndex++) {
			scoreAccumulator.addLayer(new HypotheticalResult(copyGame,this.self,plan,gameEvaluator).getScore().getFirstLayer());
			
			//debug
			if(debug)System.err.print(scoreAccumulator.getLastLayer()+", ");
			
			applyContingencies(copyGame,this.self,1);//what should iteration be here?
			
			copyGame.setActionsForPlayer(plan.getPlannedActions().get(actionIndex), this.self);
			copyGame.endRound();
			
			//if we are in loose forcast phase, skip a round
			if(actionIndex >= maxTtl) {
				scoreAccumulator.addLayer(new HypotheticalResult(copyGame, this.self,gameEvaluator).getScore().decay(getDecayRate()).getFirstLayer());
				copyGame.endRound();
			}
			
			scoreAccumulator.decay(getDecayRate());
		}
		
		for(int index = 0; index < tailLength; index++) {
			copyGame.endRound();
			scoreAccumulator.addLayer(new HypotheticalResult(copyGame, this.self,gameEvaluator).getScore().getFirstLayer());
			if(debug)System.err.print(scoreAccumulator.getLastLayer()+", ");
		}
		
		if(debug)System.err.println();
		
		HypotheticalResult retval = new HypotheticalResult(copyGame, self, plan,gameEvaluator);
		//retval.setScore(scoreAccumulator.addLayer(retval.getScore().getFirstLayer()));
		retval.setScore(scoreAccumulator);
		return retval;
	}
	
	//not sure I really want this method here, it's kind of a strange utility
	void applyContingencies(Game game, Player empire, int iteration) {		
		List<Contingency> contingencies = contingencyGenerator.generateContingencies(game, empire, iteration);
		
		
		for(Contingency current: contingencies) {
			Game contingencyGame = gameCloner.cloneGame(game);

			BigDecimal value1 = gameEvaluator.getValue(contingencyGame, current.getPlayer()).totalScore();
			
			contingencyGame.setActionsForPlayer(current.getActions(), current.getPlayer());
			contingencyGame.endRound();			
			
			BigDecimal value2 = gameEvaluator.getValue(contingencyGame, current.getPlayer()).totalScore();
			
			//if this empire does better when doing this contingency, we assume it will choose to do so.
			if(value2.compareTo(value1) > 0) {
				game.appendActionsForPlayer(current.getActions(), current.getPlayer());
			}
		}
	}
	
	public boolean valueIsValid(double value){
		return true;
	}
	
	public int getMaxTtl() {
		return maxTtl;
	}	
	
	public double getDecayRate() {
		return decayRate;
	}
	
	public Game getParentGame() {
		return gameCloner.cloneGame(trueGame);
	}
	
	public Player getSelf() {
		return self;
	}
	
	public GameEvaluator getEvaluator() {
		return gameEvaluator;
	}
	
	public GameCloner getCloner() {
		return gameCloner;
	}
	
	public void addLog(String log) {
		logs.add(log);
	}
	
	public List<String> getLogs(){
		return logs;
	}
}
