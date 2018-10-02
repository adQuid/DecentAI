package medciv.model.actions;

import medciv.aiconstructs.ActionType;
import medciv.model.Villager;
import medciv.model.items.Cow;
import medciv.model.items.Milk;

public class MilkAnimal implements ActionType{

	private Cow target;
	private Villager owner;
	
	public MilkAnimal(Cow target, Villager owner) {
		this.target = target;
		this.owner = owner;
	}
	
	public String toString() {
		return "Milk cow";
	}
	
	@Override
	public void doAction() {
		if(!target.isWasMilkedThisTurn()) {
			target.setWasMilkedThisTurn(true);
			owner.addItems(new Milk());
			target.setWasMilkedThisTurn(true);
		}
	}	
}
