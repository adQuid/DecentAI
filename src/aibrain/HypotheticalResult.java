package aibrain;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import model.Empire;
import aibrain.Game;
import spacegame.SpaceGameEvaluator;

public class HypotheticalResult {

	private Score score;
	private List<List<Action>> actions;	
	private Plan plan = new Plan();
	
	//keeps no logs or reasonings. Only for quickly determining the value of a game state
	public HypotheticalResult(Game game, List<Action> actions, Empire empire) {
		this.score = SpaceGameEvaluator.getInstance().getValue((model.Game)game, empire);
		this.actions = new ArrayList<List<Action>>();
		this.actions.add(actions);
	}
	
	public HypotheticalResult(Game game, List<Action> actions, Empire empire, List<Action> nextActions, Reasoning reason) {
		this.score = SpaceGameEvaluator.getInstance().getValue((model.Game)game, empire);
		this.actions = new ArrayList<List<Action>>();
		this.actions.add(actions);
		this.plan.addActionList(nextActions);
		this.plan.addReasoning(reason);
	}

	public HypotheticalResult(Game game, List<Action> actions, Empire empire, Plan plan) {
		this.score = SpaceGameEvaluator.getInstance().getValue((model.Game)game, empire);
		this.actions = new ArrayList<List<Action>>();
		this.actions.add(actions);
		this.plan = plan;
	}	
	
	public HypotheticalResult(Game game, List<Action> actions, Empire empire, Plan plan, List<Action> nextActions, Reasoning newReason) {
		this.score = SpaceGameEvaluator.getInstance().getValue((model.Game)game, empire);
		this.actions = new ArrayList<List<Action>>();
		this.actions.add(actions);
		this.plan = plan;
		plan.addActionList(nextActions);
		plan.addReasoning(newReason);
	}
	
	public HypotheticalResult(Score score, List<Action> actions) {
		super();
		this.score = score;
		this.actions = new ArrayList<List<Action>>();
		this.actions.add(actions);
	}
	
	public Score getScore() {
		return score;
	}
	
	public void setScore(Score score) {
		this.score = score;
	}
	
	public List<List<Action>> getActions() {
		return actions;
	}
	
	public List<Action> getImmediateActions() {
		return actions.get(0);
	}
	
	public void setActions(List<List<Action>> actions) {
		this.actions = actions;
	}
	
	public void appendActionsFront(List<Action> actions) {
		this.actions.add(0, actions);
	}
	
	public void appendActionsEnd(List<Action> actions) {
		this.actions.add(actions);
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}
		
}
