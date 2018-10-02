package medciv.model.actions;

import medciv.aiconstructs.ActionType;
import medciv.model.MedcivGame;
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
	
	public Cow getTarget() {
		return target;
	}
	
	public String toString() {
		return "Milk cow";
	}
	
	@Override
	public void doAction(MedcivGame game) {
		if(!target.isWasMilkedThisTurn()) {
			target.setWasMilkedThisTurn(true);
			if(game.isLive()) {
				int roll = game.getRandom().nextInt(3);
				owner.addItems(new Milk(roll));
			}else{
				owner.addItems(new Milk(1));
			}
		}
	}

	@Override
	public int getTimeCost() {
		return 1;
	}	
}
