package aibrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cloners.GameCloner;

public class AIBrain {
	
	private Player self;
	private int maxTtl = 0;
	private int lookAheadSecondary = 0;
	private int tailLength = 0;
	private HypotheticalResult lastIdea = null;
	private double decayRate = 0.8;
	private IdeaGenerator ideaGenerator;
	private ContingencyGenerator contingencyGenerator;
	private GameEvaluator gameEvaluator;
	
	private Game trueGame;
	
	//for debugging
	private List<String> logs = new ArrayList<String>();
	
	public AIBrain(Player self, int lookAhead, int lookAheadSecondary, int lookAheadTail,IdeaGenerator ideaGenerator, ContingencyGenerator contingencyGenerator, GameEvaluator gameEvaluator) {
		this.self = self;
		this.maxTtl = lookAhead;
		this.lookAheadSecondary = lookAheadSecondary;
		this.tailLength = lookAheadTail;
		this.ideaGenerator = ideaGenerator;
		this.contingencyGenerator = contingencyGenerator;
		this.gameEvaluator = gameEvaluator;
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
			//now we add a new final step to keep the same length
			HypotheticalResult appendResult = runIterations(runGame(trueGame,lastIdea.getPlan()),maxTtl/2 + 1);

			//more debug
			addLog("what I got in mind...");
			for(Reasoning current: appendResult.getPlan().getReasonings()) {
				addLog(">"+current.toString());
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			lastIdea.appendActionsEnd(appendResult.getImmediateActions(),appendResult.getPlan().getReasonings().get(0));
			//is my last idea still working?
			Score latestScore = runPath(GameCloner.cloneGame(trueGame), lastIdea.getPlan()).getScore();
			double assumedScore = lastIdea.getScore().totalScore();
			if(latestScore.totalScore() < assumedScore) {
				addLog("this plan got worse: "+latestScore.totalScore()+" vs "+assumedScore);
				lastIdea = runIterations(trueGame, maxTtl);
			} else {
				addLog("this plan is just as good or better: "+latestScore.totalScore()+" vs "+assumedScore);				
				lastIdea.setScore(latestScore);
				
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
	
	private HypotheticalResult runIterations(Game game, int forcast) {
		int iteration = 1;
		HypotheticalResult result = null;//this SHOULD fail if the variable is not changed
		List<Action> possibilities = game.returnActions(self);
		List<Action> committedActions = new ArrayList<Action>();
		Plan plan = Plan.emptyPlan();
		while(this.ideaGenerator.hasFurtherIdeas(game, self, possibilities, committedActions, iteration)) {
			for(Player current: game.getEmpires()) {
				//remove existing actions. Should this be done here?
				//game.setActionsForEmpire(new ArrayList<Action>(), current);
			}			
			Hypothetical hypothetical = new Hypothetical(game, new ArrayList<Modifier>(), 
					this, new ArrayList<Action>(), new ArrayList<Action>(), plan.getPlannedActions(), forcast,
                    lookAheadSecondary, tailLength, self, new Score(), iteration,ideaGenerator);
			result = hypothetical.calculate();
			committedActions = result.getImmediateActions();
			possibilities = game.returnActions(self);
			plan = result.getPlan();
			
			iteration++;
		}
		
		if(result == null) {
			System.err.println("Idea Generator generated no ideas on iteration 1");

			Hypothetical hypothetical = new Hypothetical(game, new ArrayList<Modifier>(), 
					this, new ArrayList<Action>(), new ArrayList<Action>(), plan.getPlannedActions(), forcast,
                    lookAheadSecondary, tailLength, self, new Score(), 1,ideaGenerator);
			return hypothetical.calculate();
		}
		
		return result;
	}
	
	//TODO: merge this with the one above
	private HypotheticalResult immediateIteration(Game game, List<Action> committedActions, Plan plan) {
		
		List<Action> possibilities = game.returnActions(self);
		if(ideaGenerator.hasFurtherIdeas(game, self, possibilities, committedActions, 1)) {
			
			for(Player current: game.getEmpires()) {
				//remove existing actions. Should this be done here?
				game.setActionsForEmpire(new ArrayList<Action>(), current);
			}
			
			Hypothetical hypothetical = new Hypothetical(game, new ArrayList<Modifier>(), 
					this, new ArrayList<Action>(), new ArrayList<Action>(), plan.getPlannedActions(), maxTtl/2 + 1,
                    lookAheadSecondary, tailLength, self, new Score(), 1,ideaGenerator);
			HypotheticalResult result = hypothetical.calculate();
			committedActions = result.getImmediateActions();
			possibilities = game.returnActions(self);
			plan = result.getPlan();
			
			return result;
		}else {
			return null; //is this a good use of a null? Is anything?
		}
	}
	
	//like runPath, but it doesn't track score
	private Game runGame(Game game, Plan plan) {
		Game copyGame = GameCloner.cloneGame(game);
		
		for(int actionIndex = 0; actionIndex < plan.getPlannedActions().size(); actionIndex++) {
			
			applyContingencies(copyGame,this.self,1);//what should iteration be here?
			
			copyGame.setActionsForEmpire(plan.getPlannedActions().get(actionIndex), this.self);
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
	
	public HypotheticalResult runPath(Game game, Plan plan, boolean debug) {
		Game copyGame = GameCloner.cloneGame(game);
		
		//clear any existing actions; we only look at the plan
		for(Player current: copyGame.getEmpires()) {
			copyGame.setActionsForEmpire(new ArrayList<Action>(), current);
		}
		
		Score scoreAccumulator = new Score(new HashMap<String,Double>());
		
		if(debug)System.err.print("[");
		
		for(int actionIndex = 0; actionIndex < maxTtl + lookAheadSecondary; actionIndex++) {
			scoreAccumulator.add(new HypotheticalResult(copyGame,this.self,plan,gameEvaluator).getScore());
			
			//debug
			if(debug)System.err.print(scoreAccumulator.getCategories()+", ");
			
			applyContingencies(copyGame,this.self,1);//what should iteration be here?
			
			copyGame.setActionsForEmpire(plan.getPlannedActions().get(actionIndex), this.self);
			copyGame.endRound();
			
			//if we are in loose forcast phase, skip a round
			if(actionIndex >= maxTtl) {
				scoreAccumulator.add(new HypotheticalResult(copyGame, this.self,gameEvaluator).getScore().decay(getDecayRate()));
				copyGame.endRound();
			}
			
			scoreAccumulator.decay(getDecayRate());
		}
		
		for(int index = 0; index < tailLength; index++) {
			copyGame.endRound();
			scoreAccumulator.add(new HypotheticalResult(copyGame, this.self,gameEvaluator).getScore());
			if(debug)System.err.print(scoreAccumulator.getCategories()+", ");
		}
		
		if(debug)System.err.println();
		
		HypotheticalResult retval = new HypotheticalResult(copyGame, self, plan,gameEvaluator);
		retval.setScore(retval.getScore().add(scoreAccumulator));
		return retval;
	}
	
	//not sure I really want this method here, it's kind of a strange utility
	public void applyContingencies(Game game, Player empire, int iteration) {
		List<Action> contingencyPossibilities = new ArrayList<Action>();
		for(Player current: game.getEmpires()) {
			//remove existing actions. Should this be done here?
			if(current.getActionsThisTurn().size() > 0) {
				System.err.println("actions are already in place when I add contingencies");
			}
			contingencyPossibilities.addAll(game.returnActions(current));
		}
		
		List<Contingency> contingencies = contingencyGenerator.generateContingencies(game, empire, contingencyPossibilities, iteration);
		
		
		for(Contingency current: contingencies) {
			Game contingencyGame = GameCloner.cloneGame(game);

			double value1 = gameEvaluator.getValue(contingencyGame, current.getPlayer()).totalScore();
			
			contingencyGame.setActionsForEmpire(current.getActions(), current.getPlayer());
			contingencyGame.endRound();			
			
			double value2 = gameEvaluator.getValue(contingencyGame, current.getPlayer()).totalScore();
			
			//if this empire does better when doing this contingency, we assume it will choose to do so.
			if(value2 > value1) {
				game.appendActionsForEmpire(current.getActions(), current.getPlayer());
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
		return GameCloner.cloneGame(trueGame);
	}
	
	public Player getSelf() {
		return self;
	}
	
	public GameEvaluator getEvaluator() {
		return gameEvaluator;
	}
	
	public void addLog(String log) {
		logs.add(log);
	}
	
	public List<String> getLogs(){
		return logs;
	}
}
