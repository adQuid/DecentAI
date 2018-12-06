package aibrain;

import java.util.ArrayList;
import java.util.List;

import aibrain.Game;

/**
 * A representation of a path the AI could take, as well as the expected score from taking that path.
 */
public class HypotheticalResult {

	private Score score;
	private Plan plan = new Plan();
	
	//keeps no logs or reasonings. Only for quickly determining the value of a game state
	HypotheticalResult(Game game, Player empire, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
	}
	
	//keeps no logs or reasonings. Only for quickly determining the value of a game state
	HypotheticalResult(Game game, Player empire, Plan plan, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
		this.plan = plan;				
	}	
	
	HypotheticalResult(Game game, List<Action> actions, Player empire, List<Action> nextActions, Reasoning reason, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
		this.plan.addActionListToEnd(actions);
		this.plan.addActionListToEnd(nextActions);
	}

	HypotheticalResult(Game game, List<Action> actions, Player empire, Plan plan, List<Action> nextActions, Reasoning newReason, GameEvaluator evaluator) {
		this.score = evaluator.getValue(game, empire);
		this.plan.addActionListToEnd(actions);
		this.plan = plan;
		plan.addActionListToEnd(nextActions);
	}
	
	HypotheticalResult(Score score, List<Action> actions) {
		super();
		this.score = score;
		this.plan.addActionListToEnd(actions);
	}
	
	HypotheticalResult(HypotheticalResult other){
		this.score = new Score(other.score);
		this.plan = new Plan(other.plan);		
	}
	
	public Score getScore() {
		return score;
	}
	
	void setScore(Score score) {
		this.score = score;
	}
	
	public List<List<Action>> getActions() {
		return plan.getPlannedActions();
	}
	
	/**
	 * @return The actions from this plan that the AI would take this upcoming turn.
	 */
	public List<Action> getImmediateActions() {
		//I don't like this, but I'm going to put off fixing this for now
		if(plan.getPlannedActions().size() > 0) {
		  return plan.getPlannedActions().get(0);
		} else {
		  return new ArrayList<Action>();
		}
	}
	
	void appendActionsFront(List<Action> actions) {
		this.plan.addActionListFront(actions);
	}
	
	void appendActionsEnd(List<Action> actions) {
		this.plan.addActionListToEnd(actions);;
	}

	void appendActionsEnd(List<Action> actions, Reasoning reasonings) {
		this.plan.addActionListToEnd(actions);
	}
	
	public Plan getPlan() {
		return plan;
	}

	void setPlan(Plan plan) {
		this.plan = plan;
	}
		
}
