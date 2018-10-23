package medciv.model.items;

import medciv.model.Item;
import medciv.model.MedcivGame;

public abstract class Edible extends Stackable{

	public Edible(MedcivGame game, int ownerId) {
		super(game, ownerId);
	}
	
	public Edible(MedcivGame game, int id, int ownerId) {
		super(game, id, ownerId);
	}
	
	public abstract int foodPriority();
	
}
