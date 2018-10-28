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
		return 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((animalName == null) ? 0 : animalName.hashCode());
		result = prime * result + ownerId;
		result = prime * result + targetId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TendAnimal other = (TendAnimal) obj;
		if (animalName == null) {
			if (other.animalName != null)
				return false;
		} else if (!animalName.equals(other.animalName))
			return false;
		if (ownerId != other.ownerId)
			return false;
		if (targetId != other.targetId)
			return false;
		return true;
	}		
}
