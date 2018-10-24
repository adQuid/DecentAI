package aibrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



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
	
	//run one turn of the AI
	public HypotheticalResult runAI(Game sourceGame){
		
		this.trueGame = sourceGame.imageForPlayer(self);
		
		if(lastIdea == null) {
			addLog("I have no plan");
			lastIdea = runIterations(trueGame, maxTtl);
		} else {
			//we presumably did the first round of the last idea, so lets remove it
			lastIdea.getPlan().removeActionListFromFront();

			//is my last idea still working?
			Score latestScore = runPath(gameCloner.cloneGame(trueGame), lastIdea.getPlan()).getScore();
			Score assumedScore = lastIdea.getScore().withoutFirstRound();
			if(latestScore.totalScore() < assumedScore.totalScore()) {
				addLog("this plan got worse: "+latestScore.totalScore()+" vs "+assumedScore);
				lastIdea = runIterations(trueGame, maxTtl);
			} else {
				addLog("this plan is just as good or better: "+latestScore.totalScore()+" vs "+assumedScore);				
				
				//now we add a new final step to keep the same length
				HypotheticalResult appendResult = runIterations(runGame(trueGame,lastIdea.getPlan()),maxTtl/2 + 1);
				
				//more debug
				addLog("what I got in mind...");
				for(Reasoning current: appendResult.getPlan().getReasonings()) {
					addLog(">"+current.toString());
				}
				
				//push the new action onto the end, and adjust score appropriately
				lastIdea.appendActionsEnd(appendResult.getImmediateActions(),appendResult.getPlan().getReasonings().get(0));
				lastIdea.setScore(runPath(gameCloner.cloneGame(trueGame), lastIdea.getPlan()).getScore());
				
				//just in case, let's see if there are any last minute opportunities or problems that came up just this turn
				HypotheticalResult opportunityResult = immediateIteration(trueGame, lastIdea.getImmediateActions(), lastIdea.getPlan());
				
				if(opportunityResult != null) {
					addLog("...but I'm ammending the immedate actions with:");
					for(Action current: opportunityResult.getImmediateActions()) {
						addLog(current.toString());
					}
					lastIdea.getPlan().getPlannedActions().set(0, opportunityResult.getImmediateActions());
				}
			}	
		}

		//finally, try removing any of the actions from this turn and see if things end up better without them
		boolean shouldContinue = true;
		while(shouldContinue) {
			shouldContinue = false;
			innerLoop: for(int index=0; index < lastIdea.getImmediateActions().size(); index++) { //in case people duplicate actions I want to remove by index
							
				List<Action> ammendedActions = new ArrayList<Action>(lastIdea.getImmediateActions());
				ammendedActions.remove(index);
				
				Plan ammendedPlan = new Plan(lastIdea.getPlan());
				ammendedPlan.getPlannedActions().set(0, ammendedActions);
				
				HypotheticalResult ammendedResult = runPath(trueGame, ammendedPlan);
				if(ammendedResult.getScore().totalScore() >= lastIdea.getScore().totalScore()) {
					//this is safe because I immediately get out of the loop
					lastIdea.getPlan().getPlannedActions().set(0, ammendedResult.getImmediateActions());
					shouldContinue = true;
					break innerLoop;
				}
			}
		}
		
		//pure debugging here
		addLog("Reasoning this turn:");
		for(Reasoning current: lastIdea.getPlan().getReasonings()) {
			addLog(">"+current.toString());
		}
		
		return lastIdea;
	}
	
	public double evaluateDeal(Game sourceGame, Deal deal) {
		//if I don't know what I'm doing without the deal, figure it out real quick
		if(lastIdea == null) {
			runAI(sourceGame);
		}
		
		Score scoreWithDeal = runIterations(sourceGame, maxTtl, deal).getScore();
		
		return scoreWithDeal.totalScore() / lastIdea.getScore().totalScore();		
	}
	
	private HypotheticalResult runIterations(Game game, int forcast) {
		return runIterations(game,forcast,null);
	}
	
	private HypotheticalResult runIterations(Game game, int forcast, Deal dealToConsider) {
		int iteration = 1;
		HypotheticalResult result = null;//this SHOULD fail if the variable is not changed
		List<Action> committedActions = new ArrayList<Action>();
		Plan plan;
		if(dealToConsider != null) {
			plan = dealToConsider.offers.get(self);
		}else {
			plan = Plan.emptyPlan();
		}
		while(this.ideaGenerator.hasFurtherIdeas(game, self, committedActions, iteration)) {		
			Hypothetical hypothetical = new Hypothetical(game, 
					this, plan.getPlannedActions(), forcast,
                    lookAheadSecondary, tailLength, self, iteration,ideaGenerator);
			result = hypothetical.calculate();
			committedActions = result.getImmediateActions();
			plan = result.getPlan();
			
			iteration++;
		}
		
		if(result == null) {
			System.err.println("Idea Generator generated no ideas on iteration 1");

			Hypothetical hypothetical = new Hypothetical(game,
					this, plan.getPlannedActions(), forcast,
                    lookAheadSecondary, tailLength, self, 1,ideaGenerator);
			return hypothetical.calculate();
		}
		
		return result;
	}
	
	//TODO: merge this with the one above
	private HypotheticalResult immediateIteration(Game game, List<Action> committedActions, Plan plan) {
		
		if(ideaGenerator.hasFurtherIdeas(game, self, committedActions, 1)) {
			
			for(Player current: game.getEmpires()) {
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
	
	public HypotheticalResult runPath(Game game, Plan plan) {
		return runPath(game,plan,false);
	}
	
	//in kind of quirky behavior, this only runs path to the length of the plan provided
	public HypotheticalResult runPath(Game game, Plan plan, boolean debug) {
		Game copyGame = gameCloner.cloneGame(game);
		
		//clear any existing actions; we only look at the plan
		for(Player current: copyGame.getEmpires()) {
			copyGame.setActionsForPlayer(new ArrayList<Action>(), current);
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
	public void applyContingencies(Game game, Player empire, int iteration) {		
		List<Contingency> contingencies = contingencyGenerator.generateContingencies(game, empire, iteration);
		
		
		for(Contingency current: contingencies) {
			Game contingencyGame = gameCloner.cloneGame(game);

			double value1 = gameEvaluator.getValue(contingencyGame, current.getPlayer()).totalScore();
			
			contingencyGame.setActionsForPlayer(current.getActions(), current.getPlayer());
			contingencyGame.endRound();			
			
			double value2 = gameEvaluator.getValue(contingencyGame, current.getPlayer()).totalScore();
			
			//if this empire does better when doing this contingency, we assume it will choose to do so.
			if(value2 > value1) {
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
