package aibrain;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import model.Empire;
import aibrain.Game;
import spacegame.SpaceGameEvaluator;

public class HypotheticalResult {

	private double score;
	private List<List<Action>> actions;
	
	public HypotheticalResult(Game game, List<Action> actions, Empire empire) {
		this.score = SpaceGameEvaluator.getValue((model.Game)game, empire);
		this.actions = new ArrayList<List<Action>>();
		this.actions.add(actions);
	}
	public HypotheticalResult(double score, List<Action> actions) {
		super();
		this.score = score;
		this.actions = new ArrayList<List<Action>>();
		this.actions.add(actions);
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
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
	
}
