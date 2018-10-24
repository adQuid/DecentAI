package medciv.model.items;

import medciv.model.Item;
import medciv.model.MedcivGame;

public abstract class Edible extends Stackable{

	public Edible(int ownerId) {
		super(ownerId);
	}
	
	public Edible(int id, int ownerId) {
		super(id, ownerId);
	}
	
	public abstract int foodPriority();
	
}
