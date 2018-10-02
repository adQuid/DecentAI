package medciv.aiconstructs;

import aibrain.Action;

public class MedcivAction implements Action{

	private boolean contingency;
	private ActionType type;
	
	public MedcivAction(ActionType type) {
		this.type = type;
		this.contingency = false;
	}
	
	public ActionType getType() {
		return type;
	}
	
	@Override
	public boolean isContingency() {
		return contingency;
	}

}
