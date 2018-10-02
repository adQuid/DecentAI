package medciv.model;

import medciv.aiconstructs.MedcivAction;

public interface Item {

	public void applyAction(MedcivAction action);
	
	public void endRound(Villager owner);
	
	public Item clone();
	
	public String description();
	
	public void focusOnItem();
}
