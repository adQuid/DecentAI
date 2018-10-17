package medciv.model;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import medciv.aiconstructs.MedcivAction;
import medciv.aiconstructs.MedcivPlayer;
import medciv.model.items.Edible;
import medciv.model.items.Stackable;

public class Villager{

	private MedcivPlayer owner;
	private String name;
	private Town location;
	private List<Item> ownedItems;
	private int foodToEat = 2;
	
	public Villager(Town location, MedcivPlayer owner, String name) {
		this.name = name;
		this.location = location;
		this.owner = owner;
		this.ownedItems = new ArrayList<Item>();
	}

	public Villager clone(MedcivGame game) {
		Villager retval = new Villager(location,owner,name);
		
		for(Item current: ownedItems) {
			retval.ownedItems.add(current.clone());
		}
		this.owner = game.findMatchingPlayer(owner);
		return retval;
	}

	public String getName() {
		return name;
	}

	public Town getLocation() {
		return location;
	}

	public void setLocation(Town location) {
		this.location = location;
	}

	public List<Item> getOwnedItems() {
		return ownedItems;
	}

	public int timeLeft() {
		return timeLeft(owner.getActionsThisTurn());
	}
		
	public int timeLeft(List<Action> actions) {
		int totalTime = 20; //this will be based on food in the future;
		for(Action current: actions) {
			MedcivAction castAction = (MedcivAction) current;
			totalTime -= castAction.getType().getTimeCost();
		}
		return totalTime;
	}
	
	public boolean canAffordAction(Action action) {
		List<Action> testActions = new ArrayList<Action>(owner.getActionsThisTurn());
		testActions.add(action);
		return timeLeft(testActions) > 0;
	}
	
	public void addItems(Item item) {
		if(item instanceof Stackable) {
			for(Item current: ownedItems) {
				if(current instanceof Stackable && ((Stackable)current).isEquivelent((Stackable)item)) {
					((Stackable)current).combine((Stackable)item);
					return;
				}
			}
		}
		this.ownedItems.add(item);
	}
	
	//returns true if the villager actually had that item
	public boolean removeItem(Item item) {
		Item toRemove = null;
		for(Item current: ownedItems) {
			if(item.getClass() == current.getClass()) {
				if(item instanceof Stackable) {
					Stackable castCurrent = (Stackable)current;
					if(castCurrent.getStackSize() >= ((Stackable)item).getStackSize()) {
						castCurrent.splitStack(((Stackable)item).getStackSize());
						return true;
					} else {
						castCurrent.splitStack(castCurrent.getStackSize());
						return false;
					}
				} else {
					toRemove = current;
				}
			}
		}
		ownedItems.remove(toRemove);
		return toRemove != null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MedcivPlayer getOwner() {
		return owner;
	}

	public void setOwner(MedcivPlayer owner) {
		this.owner = owner;
	}
	
	public FoodGrouping getPlannedFood(){	
		return new FoodGrouping(ownedItems, foodToEat);
	}
	
	public List<Action> returnActions(){
		List<Action> retval = new ArrayList<Action>();
		
		for(Item current: ownedItems) {
			retval.addAll(current.getAssociatedActions(this));
		}
		
		return retval;
	}
	
	public void endRound() {
		for(Item current: getPlannedFood().getFood()) {
			removeItem(current);//later we need to check if these actually get removed. If not, we go down a food tier
		}
		
		for(Item current: ownedItems) {
			current.endRound(this);
		}
		
		List<Item> newOwnedItems = new ArrayList<Item>();
		for(Item current: ownedItems) {
			if(!current.isDead()) {
				newOwnedItems.add(current);
			}
		}		
		ownedItems = newOwnedItems;
	}
}
