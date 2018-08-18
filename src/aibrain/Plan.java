package aibrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import actions.Action;

public class Plan {

	private List<List<Action>> plannedActions;
	private List<Reasoning> reasonings;
	
	public Plan() {
		plannedActions = new ArrayList<List<Action>>();
		reasonings = new ArrayList<Reasoning>();
	}

	public Plan(Plan other) {
		this.plannedActions = new ArrayList<List<Action>>(other.plannedActions);
		this.reasonings = new ArrayList<Reasoning>(other.reasonings);
	}
	
	public List<List<Action>> getPlannedActions() {
		return plannedActions;
	}

	public void addActionList(List<Action> plannedActions) {
		this.plannedActions.add(plannedActions);
	}

	public List<Reasoning> getReasonings() {
		return reasonings;
	}

	public void addReasoning(Reasoning reasoning) {
		this.reasonings.add(reasoning);
	}
}
