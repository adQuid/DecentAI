package medciv.aiconstructs;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Player;
import medciv.model.MedcivGame;

public class MedcivPlayer implements Player{

	private MedcivGame game;
	private static int lastId = 0;
	private int id;
	private List<Action> actions = new ArrayList<Action>();
	
	public MedcivPlayer(MedcivGame game) {
		this.game = game;
		id = lastId++;
	}
	
	private MedcivPlayer(int id) {
		this.id = id;
	}
	
	public MedcivPlayer clone(MedcivGame game) {
		this.game = game;
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
		//debug
		if(this.game.isLive()) {
			System.out.println("added "+actionThisTurn);
		}
		this.actions.add(actionThisTurn);
	}
	
	public void addActionThisTurn(List<Action> actions2) {
		this.actions.addAll(actions2);
		
	}

	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		if(other instanceof MedcivPlayer) {
			return this.matches((MedcivPlayer)other);
		} else {
			return false;
		}
		
	}
	
	public boolean matches(MedcivPlayer other) {
		return this.id == other.id;
	}

}
