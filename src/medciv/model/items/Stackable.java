package medciv.model.items;

import medciv.model.Item;
import medciv.model.MedcivGame;

public abstract class Stackable extends Item{

	protected int stackSize = 1;
	
	public Stackable(int ownerId) {
		super(ownerId);
	}
	
	public Stackable(int id, int ownerId) {
		super(id, ownerId);
	}
	
	public abstract Stackable clone();
	
	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}
	
	//NOT the same as equals
	public abstract boolean isEquivelent(Stackable other);
	
	//this has no protections for type safety right now
	public void combine(Stackable other) {
		this.stackSize += other.stackSize;
		other.stackSize = 0; //just in case
	}
	
	public Stackable hypotheticalStack(int size) {
		Stackable retval = this.clone();
		retval.stackSize = size;
		return retval;
	}
	
	public Stackable splitStack(int size) {
		Stackable retval = this.clone();
		if(size >= stackSize) {
			this.stackSize = 0;
		}else {
			this.stackSize -= size;
			retval.stackSize = size;
		}
		return retval;
	}
		
}
