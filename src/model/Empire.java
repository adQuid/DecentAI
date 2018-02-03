package model;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import exceptions.IllegalActionException;

public class Empire {

	private String name;
	private double minerals;
	private double energy;
	private List<Action> actionsThisTurn = new ArrayList<Action>();
	
	public Empire(){
		
	}
	
	public Empire(String name){
		this.name = name;
		this.minerals = 6;
		this.energy = 0;
	}
	
	public Empire(Empire other){
		this.name = other.name;
		this.minerals = other.minerals;
		this.energy = other.energy;
		for(Action current: other.actionsThisTurn){
			actionsThisTurn.add(new Action(current));
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
		
	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
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
		if(this.energy + energy < 0){
			throw new IllegalActionException();
		}
		this.energy += energy;
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
