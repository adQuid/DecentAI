package spacegame;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Player;
import exceptions.IllegalActionException;

public class Empire implements Player{

	private String name;
	private double minerals;
	private double currency;
	private List<Action> actionsThisTurn = new ArrayList<Action>();
	
	public Empire(){
		
	}
	
	public Empire(String name){
		this.name = name;
		this.minerals = 11;
		this.currency = 0;
	}
	
	public Empire(Empire other){
		this.name = other.name;
		this.minerals = other.minerals;
		this.currency = other.currency;
		for(Action current: other.actionsThisTurn){
			actionsThisTurn.add(current);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMinerals() {
		return minerals;
	}

	public void setMinerals(double minerals) {
		this.minerals = minerals;
	}
		
	public double getCurrency() {
		return currency;
	}

	public void setCurrency(double energy) {
		this.currency = energy;
	}
	
	public List<Action> getActionsThisTurn() {
		return actionsThisTurn;
	}

	public void setActionsThisTurn(List<Action> actionsThisTurn) {
		this.actionsThisTurn = actionsThisTurn;
	}
	
	public void addMinerals(double minerals) throws IllegalActionException{
		if(this.minerals + minerals < 0){
			throw new IllegalActionException();
		}
		this.minerals += minerals;
	}
	
	public void addEnergy(double energy) throws IllegalActionException{
		if(this.currency + energy < 0){
			throw new IllegalActionException();
		}
		this.currency += energy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Empire other = (Empire) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
