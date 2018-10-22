package aibrain;

import java.util.ArrayList;
import java.util.List;

import aibrain.Game;

public class HypotheticalResult {

	private Score score;
	private Plan plan = new Plan();
	
	//keeps no logs or reasonings. Only for quickly determining the value of a game state
	public HypotheticalResult(Game game, Player empire, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
	}
	
	//keeps no logs or reasonings. Only for quickly determining the value of a game state
	public HypotheticalResult(Game game, Player empire, Plan plan, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
		this.plan = plan;				
	}	
	
	public HypotheticalResult(Game game, List<Action> actions, Player empire, List<Action> nextActions, Reasoning reason, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
		this.plan.addActionListToEnd(actions);
		this.plan.addActionListToEnd(nextActions);
		this.plan.addReasoning(reason);
	}


	
	public HypotheticalResult(Game game, List<Action> actions, Player empire, Plan plan, List<Action> nextActions, Reasoning newReason, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
		this.plan.addActionListToEnd(actions);
		this.plan = plan;
		plan.addActionListToEnd(nextActions);
		plan.addReasoning(newReason);
	}
	
	public HypotheticalResult(Score score, List<Action> actions) {
		super();
		this.score = score;
		this.plan.addActionListToEnd(actions);
	}
	
	public Score getScore() {
		return score;
	}
	
	public void setScore(Score score) {
		this.score = score;
	}
	
	public List<List<Action>> getActions() {
		return plan.getPlannedActions();
	}
	
	public List<Action> getImmediateActions() {
		//I don't like this, but I'm going to put off fixing this for now
		if(plan.getPlannedActions().size() > 0) {
		  return plan.getPlannedActions().get(0);
		} else {
		  return new ArrayList<Action>();
		}
	}
	
	public void appendActionsFront(List<Action> actions) {
		this.plan.addActionListFront(actions);
	}
	
	public void appendActionsEnd(List<Action> actions) {
		this.plan.addActionListToEnd(actions);;
	}

	public void appendActionsEnd(List<Action> actions, Reasoning reasonings) {
		this.plan.addReasoning(reasonings);
		this.plan.addActionListToEnd(actions);
	}
	
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
		
}
