package medciv.model.actions;

import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.model.items.Cow;
import medciv.model.items.Livestock;
import medciv.model.items.Milk;

public class TendAnimal implements ActionType{

	private int targetId;
	private int ownerId;
	private String animalName;
			
	public TendAnimal(int targetId, int ownerId, String animalName) {
		super();
		this.targetId = targetId;
		this.ownerId = ownerId;
		this.animalName = animalName;
	}

	public int getTargetId() {
		return targetId;
	}
	
	public String toString() {
		return "Tend to "+animalName;
	}
	
	@Override
	public void doAction(MedcivGame game) {
		//a silly way to resolve this, but feeding a cow has no effect if you or the cow are dead
		try {
			((Livestock)game.matchingVillager(ownerId).getItemById(targetId)).setTendedToThisTurn(true);
		}catch(NullPointerException e) {
			//do nothing; the cow is dead, or you are
		}
	}

	@Override
	public int getTimeCost() {
		return 4;
	}	
}
