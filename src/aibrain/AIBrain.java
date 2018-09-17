package aibrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import actions.Action;
import actions.ActionType;
import cloners.GameCloner;
import model.Empire;
import testers.GameRunTestser;

public class AIBrain {
	
	private Empire self;
	private int maxTtl = 0;
	private int lookAheadSecondary = 0;
	private int tailLength = 0;
	private HypotheticalResult lastIdea = null;
	private double decayRate = 0.8;
	
	//kind of a weird variable, since this changes with every run. Not thread safe
	private Game trueGame;
	
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
			System.out.println("I have no plan");
			Hypothetical root = new Hypothetical(trueGame, new ArrayList<Modifier>(), this, new ArrayList<Action>(), new ArrayList<Action>(), new ArrayList<Action>(), maxTtl, lookAheadSecondary, tailLength, self, new Score());
			lastIdea = root.calculate();
		} else {
			//we presumably did the first round of the last idea, so lets remove it
			lastIdea.getPlan().removeActionListFromFront();
			//now we add a new final step to keep the same length
			Hypothetical appendAction = new Hypothetical(runGame(trueGame,lastIdea.getPlan()), new ArrayList<Modifier>(), this, new ArrayList<Action>(), new ArrayList<Action>(), new ArrayList<Action>(), maxTtl/2 + 1, lookAheadSecondary, tailLength, self, new Score());
			HypotheticalResult appendResult = appendAction.calculate();

			//more debug
			System.err.println("what I got in mind...");
			for(Reasoning current: appendResult.getPlan().getReasonings()) {
				System.err.println(">"+current.toString());
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			lastIdea.appendActionsEnd(appendResult.getImmediateActions(),appendResult.getPlan().getReasonings().get(0));
			//is my last idea still working?
			double latestScore = runPath(GameCloner.cloneGame(trueGame), lastIdea.getPlan()).getScore().totalScore();
			double assumedScore = lastIdea.getScore().totalScore();
			if(latestScore < assumedScore) {
				System.out.println("this plan got worse: "+latestScore+" vs "+assumedScore);
				Hypothetical root = new Hypothetical(trueGame, new ArrayList<Modifier>(), this, new ArrayList<Action>(), new ArrayList<Action>(), new ArrayList<Action>(), maxTtl, lookAheadSecondary, tailLength, self, new Score());
				lastIdea = root.calculate();
			} else {
			System.out.println("this plan is just as good or better: "+latestScore+" vs "+assumedScore);
			}	
		}

		return lastIdea;
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
}
