package aibrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Plan {

	private List<List<Action>> plannedActions;
	private List<Reasoning> reasonings;
	
	public static Plan emptyPlan() {
		Plan retval = new Plan();
		for(int index=0; index<100; index++) {
			retval.plannedActions.add(new ArrayList<Action>());
			retval.reasonings.add(new Reasoning("empty action list"));
		}
		return retval;
	}
	
	public Plan() {
		plannedActions = new ArrayList<List<Action>>();
		reasonings = new ArrayList<Reasoning>();
	}

	public Plan(Plan other) {
		this();
		for(List<Action> current: other.plannedActions) {
			this.plannedActions.add(new ArrayList<Action>(current));
		}
		this.reasonings = new ArrayList<Reasoning>(other.reasonings);
	}
	
	public List<List<Action>> getPlannedActions() {
		return plannedActions;
	}

	public void addActionListFront(List<Action> plannedActions) {
		this.plannedActions.add(0, plannedActions);
		if(this.plannedActions.size() > this.reasonings.size()) {
			System.err.println("more steps in plan than reasonings");
		}
	}
	
	public void addActionListToEnd(List<Action> plannedActions) {
		this.plannedActions.add(plannedActions);
		if(this.plannedActions.size() > this.reasonings.size()) {
			System.err.println("more steps in plan than reasonings");
		}
	}
	
	public void removeActionListFromFront() {
		this.plannedActions.remove(0);
		this.reasonings.remove(0);
	}

	public void removeActionListFromEnd() {
		this.plannedActions.remove(this.plannedActions.size()-1);
		this.reasonings.remove(this.plannedActions.size()-1);
	}
	
	public List<Reasoning> getReasonings() {
		return reasonings;
	}

	public void addReasoning(Reasoning reasoning) {
		this.reasonings.add(reasoning);
	}
}
