package aibrain;

import java.util.List;

import actions.Action;
import model.Empire;
import model.Game;

public class HypotheticalResult {

	private double score;
	private List<Action> actions;
	
	public HypotheticalResult(Game game, List<Action> actions, Empire empire) {
		this.score = SpaceGameEvaluator.getValue(game, empire);
		this.actions = actions;
	}
	public HypotheticalResult(double score, List<Action> actions) {
		super();
		this.score = score;
		this.actions = actions;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
}
