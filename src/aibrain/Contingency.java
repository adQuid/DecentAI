package aibrain;

import java.util.List;

/**
 * A set of actions someone else might do, which the AI should take into consideration. * 
 */
public class Contingency {

	private Player player; 
	private List<Action> actions;
	
	/**
	 * @param player The player who would be taking these actions.
	 * @param actions
	 */
	public Contingency(Player player, List<Action> actions) {
		this.player = player;
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
