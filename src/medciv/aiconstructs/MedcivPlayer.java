package medciv.aiconstructs;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Player;

public class MedcivPlayer implements Player{

	private static int lastId = 0;
	private int id;
	private List<Action> actions = new ArrayList<Action>();
	
	public MedcivPlayer() {
		id = lastId++;
	}
	
	public MedcivPlayer(int id) {
		this.id = id;
	}
	
	public MedcivPlayer clone() {
		MedcivPlayer retval = new MedcivPlayer(id);
		for(Action current: actions) {
			retval.actions.add(current);
		}
		return retval;
	}
	
	@Override
	public List<Action> getActionsThisTurn() {
		return actions;
	}

	@Override
	public void setActionsThisTurn(List<Action> actionsThisTurn) {
		this.actions = actionsThisTurn;
	}
	
	public void addActionThisTurn(Action actionThisTurn) {
		this.actions.add(actionThisTurn);
	}

	public boolean matches(MedcivPlayer other) {
		return this.id == other.id;
	}
}
