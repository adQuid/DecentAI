package medciv.model;

import java.util.List;

import aibrain.Action;
import medciv.aiconstructs.MedcivAction;

public interface Item {
	
	public void endRound(Villager owner);
	
	public Item clone();
	
	public String description();
	
	public void focusOnItem();
	
	public boolean isDead();
	
	public List<Action> getAssociatedActions(Villager villager);
}
