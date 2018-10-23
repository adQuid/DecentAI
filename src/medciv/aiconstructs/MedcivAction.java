package medciv.aiconstructs;

import aibrain.Action;
import medciv.model.Villager;
import medciv.model.actions.ActionType;

public class MedcivAction implements Action{

	private boolean contingency;
	private ActionType type;
	private int villagerId;
	
	public MedcivAction(ActionType type, int villager) {
		this.type = type;
		this.villagerId = villager;
		this.contingency = false;
	}
	
	public ActionType getType() {
		return type;
	}
	
	@Override
	public boolean isContingency() {
		return contingency;
	}

	public void setContingency(boolean contingency) {
		this.contingency = contingency;
	}
	
	public int getVillagerId() {
		return villagerId;
	}
	
	public String toString() {
		return type.toString();
	}
}
