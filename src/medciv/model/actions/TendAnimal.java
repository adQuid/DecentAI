package medciv.model.actions;

import medciv.aiconstructs.ActionType;
import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.model.items.Cow;
import medciv.model.items.Milk;

public class TendAnimal implements ActionType{

	private Cow target;
	private Villager owner;
	
	public TendAnimal(Cow target, Villager owner) {
		this.target = target;
		this.owner = owner;
	}
	
	public Cow getTarget() {
		return target;
	}
	
	public String toString() {
		return "Tend to cow";
	}
	
	@Override
	public void doAction(MedcivGame game) {
		target.setTendedToThisTurn(true);
	}

	@Override
	public int getTimeCost() {
		return 1;
	}	
}
