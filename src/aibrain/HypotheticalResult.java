package aibrain;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import model.Empire;
import aibrain.Game;
import spacegame.SpaceGameEvaluator;

public class HypotheticalResult {

	private Score score;
	private Plan plan = new Plan();
	
	//keeps no logs or reasonings. Only for quickly determining the value of a game state
	public HypotheticalResult(Game game, List<Action> actions, Empire empire) {
		this.score = SpaceGameEvaluator.getInstance().getValue((model.Game)game, empire);
		this.plan.addActionListToEnd(actions);
	}
	
	public HypotheticalResult(Game game, List<Action> actions, Empire empire, List<Action> nextActions, Reasoning reason) {
		this.score = SpaceGameEvaluator.getInstance().getValue((model.Game)game, empire);
		this.plan.addActionListToEnd(actions);
		this.plan.addActionListToEnd(nextActions);
		this.plan.addReasoning(reason);
	}

	public HypotheticalResult(Game game, List<Action> actions, Empire empire, Plan plan) {
		this.score = SpaceGameEvaluator.getInstance().getValue((model.Game)game, empire);
		this.plan.addActionListToEnd(actions);
		this.plan = plan;
	}	
	
	public HypotheticalResult(Game game, List<Action> actions, Empire empire, Plan plan, List<Action> nextActions, Reasoning newReason) {
		this.score = SpaceGameEvaluator.getInstance().getValue((model.Game)game, empire);
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

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
		
}
