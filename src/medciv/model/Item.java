package medciv.model;

import java.util.List;

import aibrain.Action;
import medciv.aiconstructs.MedcivAction;

public abstract class Item {
	
	private static int lastId = 0;
	private int id;
	protected int ownerId;
	
	public Item(int ownerId) {
		this(lastId++, ownerId);
	}
	
	public Item(int id, int ownerId) {
		this.id = id;
		this.ownerId = ownerId;
	}
	
	public int getId() {
		return id;
	}
	
	public int getOwnerId() {
		return ownerId;
	}
	
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	
	public abstract void endRound(MedcivGame game, Villager owner);
	
	public abstract Item clone();
	
	public abstract String description();
	
	public abstract void focusOnItem();
	
	public abstract boolean isDead();
	
	public abstract List<Action> getAssociatedActions(Villager villager);
}
