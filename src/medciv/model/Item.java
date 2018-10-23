package medciv.model;

import java.util.List;

import aibrain.Action;
import medciv.aiconstructs.MedcivAction;

public abstract class Item {
	
	protected MedcivGame game;
	private static int lastId = 0;
	private int id;
	protected int ownerId;
	
	public Item(MedcivGame game, int ownerId) {
		this(game, lastId++, ownerId);
	}
	
	public Item(MedcivGame game, int id, int ownerId) {
		this.game = game;
		this.id = id;
		this.ownerId = ownerId;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract void endRound(Villager owner);
	
	public abstract Item clone(MedcivGame game);
	
	public abstract String description();
	
	public abstract void focusOnItem();
	
	public abstract boolean isDead();
	
	public abstract List<Action> getAssociatedActions(Villager villager);
}
