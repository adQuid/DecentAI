package aibrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import actions.Action;
import cloners.GameCloner;
import model.ActionType;
import model.Empire;

//theses SHOULD NOT exist in final version
import spacegame.SpaceGameIdeaGenerator;
import spacegame.SpaceGameAction;

public class AIBrain {
	
	private Empire self;
	private int maxTtl = 0;
	private int lookAheadSecondary = 0;
	private int tailLength = 0;
	private HypotheticalResult lastIdea = null;
	private double decayRate = 0.8;
	
	//kind of a weird variable, since this changes with every run. Not thread safe
	private Game trueGame;
	
	//for debugging
	private List<String> logs = new ArrayList<String>();
	
	public AIBrain(Empire self, int lookAhead, int lookAheadSecondary, int lookAheadTail) {
		this.self = self;
		this.maxTtl = lookAhead;
		this.lookAheadSecondary = lookAheadSecondary;
		this.tailLength = lookAheadTail;
	}
	
	//run one turn of the AI
	public HypotheticalResult runAI(Game trueGame){
		
		this.trueGame = trueGame;
		
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
					addLog("...but I'm ammending the immedate actions");
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
		while(SpaceGameIdeaGenerator.instance().hasFurtherIdeas(game, self, possibilities, committedActions, iteration)) {
			Hypothetical hypothetical = new Hypothetical(game, new ArrayList<Modifier>(), 
					this, new ArrayList<Action>(), new ArrayList<Action>(), plan.getPlannedActions(), forcast,
                    lookAheadSecondary, tailLength, self, new Score(), iteration);
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
                    lookAheadSecondary, tailLength, self, new Score(), 1);
			return hypothetical.calculate();
		}
		
		return result;
	}
	
	//TODO: merge this with the one above
	private HypotheticalResult immediateIteration(Game game, List<Action> committedActions, Plan plan) {
		
		List<Action> possibilities = game.returnActions(self);
		if(SpaceGameIdeaGenerator.instance().hasFurtherIdeas(game, self, possibilities, committedActions, 1)) {
			Hypothetical hypothetical = new Hypothetical(game, new ArrayList<Modifier>(), 
					this, new ArrayList<Action>(), new ArrayList<Action>(), plan.getPlannedActions(), maxTtl/2 + 1,
                    lookAheadSecondary, tailLength, self, new Score(), 1);
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
		Game copyGame = GameCloner.cloneGame(game);
		Score scoreAccumulator = new Score(new HashMap<String,Double>());
		
		for(int actionIndex = 0; actionIndex < maxTtl + lookAheadSecondary; actionIndex++) {
			scoreAccumulator.add(new HypotheticalResult(copyGame,this.self,plan).getScore());
			copyGame.setActionsForEmpire(plan.getPlannedActions().get(actionIndex), this.self);
			copyGame.endRound();
			
			//if we are in loose forcast phase, skip a round
			if(actionIndex >= maxTtl) {
				scoreAccumulator.add(new HypotheticalResult(copyGame, this.self).getScore().decay(getDecayRate()));
				copyGame.endRound();
			}
			
			scoreAccumulator.decay(getDecayRate());
		}
		
		for(int index = 0; index < tailLength; index++) {
			copyGame.endRound();
			scoreAccumulator.add(new HypotheticalResult(copyGame, this.self).getScore());
		}
		
		HypotheticalResult retval = new HypotheticalResult(copyGame, self, plan);
		retval.setScore(retval.getScore().add(scoreAccumulator));
		return retval;
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
	
	public Empire getSelf() {
		return self;
	}
	
	public void addLog(String log) {
		logs.add(log);
	}
	
	public List<String> getLogs(){
		return logs;
	}
}
