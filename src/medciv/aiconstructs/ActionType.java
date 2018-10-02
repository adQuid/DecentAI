package medciv.aiconstructs;

import medciv.model.MedcivGame;

public interface ActionType {

	public void doAction(MedcivGame game);
	
	public int getTimeCost();
	
}
