package aibrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Plan {

	private List<List<Action>> plannedActions;
	
	public static Plan emptyPlan(int size) {
		Plan retval = new Plan();
		for(int index=0; index<size; index++) {
			retval.plannedActions.add(new ArrayList<Action>());
		}
		
		return retval;
	}
	
	public Plan() {
		plannedActions = new ArrayList<List<Action>>();
	}

	public Plan(Plan other) {
		this();
		for(List<Action> current: other.plannedActions) {
			this.plannedActions.add(new ArrayList<Action>(current));
		}
	}
	
	public Plan addTo(Plan other) {
		for(int index = 0; index < Math.max(plannedActions.size(),other.plannedActions.size()); index++) {
			plannedActions.get(index).addAll(other.getLayer(index));	
		}
		
		return this;
	}
	
	//TODO: get rid of this and make it so you just select one layer at a time, defaulting to empty list
	public List<List<Action>> getPlannedActions() {
		return plannedActions;
	}

	public List<Action> getLayer(int layer) {
		if(layer < plannedActions.size()) {
			return new ArrayList<Action>(plannedActions.get(layer));
		} else {
			return new ArrayList<Action>();
		}
	}
	
	public void addActionListFront(List<Action> plannedActions) {
		this.plannedActions.add(0, plannedActions);
	}
	
	public void addActionListToEnd(List<Action> plannedActions) {
		this.plannedActions.add(plannedActions);
	}
	
	public void removeActionListFromFront() {
		this.plannedActions.remove(0);
	}

	public void removeActionListFromEnd() {
		this.plannedActions.remove(this.plannedActions.size()-1);
	}
	
	public Plan withoutImmediateActions() {
		Plan retval = new Plan(this);
		
		retval.plannedActions.remove(0);
		
		retval.plannedActions.add(new ArrayList<Action>());
		
		return retval;
	}
}
