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
	
	public int getInitiative() {
		return type.getInitiative();
	}
	
	public String toString() {
		return type.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MedcivAction other = (MedcivAction) obj;
		if (contingency != other.contingency)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (villagerId != other.villagerId)
			return false;
		return true;
	}	
}
