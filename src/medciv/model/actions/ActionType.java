package medciv.model.actions;

import medciv.model.MedcivGame;

public interface ActionType {

	public void doAction(MedcivGame game);
	
	public int getTimeCost();
	
	public boolean equals(Object other);
	
}
