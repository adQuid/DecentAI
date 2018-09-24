package aibrain;

import java.util.List;

import model.Empire;

public class Contingency {

	private Empire player; 
	private List<Action> actions;
	
	public Contingency(Empire empire, List<Action> actions) {
		this.player = empire;
		this.actions = actions;
	}

	public Empire getPlayer() {
		return player;
	}

	public void setPlayer(Empire player) {
		this.player = player;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
}
