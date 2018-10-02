package medciv.model.items;

public abstract class Stackable {

	protected int stackSize = 1;
	
	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}
	
	//this has no protections for type safety right now
	public void combine(Stackable other) {
		this.stackSize += other.stackSize;
		other.stackSize = 0; //just in case
	}
	
}
