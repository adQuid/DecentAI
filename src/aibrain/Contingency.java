package aibrain;

import java.util.List;

public class Contingency {

	private Player player; 
	private List<Action> actions;
	
	public Contingency(Player empire, List<Action> actions) {
		this.player = empire;
		this.actions = actions;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
	
}
