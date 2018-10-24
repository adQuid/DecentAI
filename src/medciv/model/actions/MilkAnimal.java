package medciv.model.actions;

import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.model.items.Cow;
import medciv.model.items.Milk;

public class MilkAnimal implements ActionType{

	private int targetId;
	private int ownerId;
		
	public MilkAnimal(int targetId, int owner) {
		super();
		this.targetId = targetId;
		this.ownerId = owner;
	}

	public int getId() {
		return targetId;
	}
	
	public String toString() {
		return "Milk cow";
	}
	
	@Override
	public void doAction(MedcivGame game) {
		//a silly way to resolve this, but milking a cow has no effect if you or the cow are dead
		try {
			Villager owner = game.matchingVillager(ownerId);
			Cow target = (Cow)owner.getItemById(targetId);
			if(!target.isWasMilkedThisTurn()) {
				target.setWasMilkedThisTurn(true);
				if(game.isLive()) {
					int roll = game.getRandom().nextInt(3) + game.getRandom().nextInt(3);
					owner.addItems(new Milk(roll,ownerId));
				}else{
					owner.addItems(new Milk(2,ownerId));
				}
			}
		}catch(NullPointerException e) {
			//do nothing; cow is dead
		}
	}

	@Override
	public int getTimeCost() {
		return 1;
	}	
}
