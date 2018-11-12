package medciv.model;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import medciv.aiconstructs.MedcivAction;
import medciv.aiconstructs.MedcivPlayer;
import medciv.model.items.Edible;
import medciv.model.items.Stackable;

public class Villager{

	private MedcivGame game;
	private static int lastId = 0;
	private int id;
	private MedcivPlayer owner;
	private String name;
	private int location;
	private List<Item> ownedItems;
	private int foodToEat = 0;
	private boolean starving = false;
	
	public Villager(MedcivGame game, int location, MedcivPlayer owner, String name) {
		this(game, location,owner,name,lastId++);
	}
	
	public Villager(MedcivGame game, int location, MedcivPlayer owner, String name, int id) {
		this.game = game;
		this.id = id;
		this.name = name;
		this.location = location;
		this.owner = owner;
		this.ownedItems = new ArrayList<Item>();
	}

	public Villager clone(MedcivGame game) {
		Villager retval = new Villager(game,location,owner,name,id);
		
		for(Item current: ownedItems) {
			retval.ownedItems.add(current.clone());
		}
		retval.owner = this.owner;
		retval.starving = this.starving;
		return retval;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Villager other = (Villager) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public List<Item> getOwnedItems() {
		return ownedItems;
	}
	
	public Item getItemById(int id) {
		for(Item current: ownedItems) {
			if(current.getId() == id) {
				return current;
			}
		}
		return null;
	}

	public int timeLeft(MedcivGame game) {
		return timeLeft(game.findMatchingPlayer(owner).getActionsThisTurn());
	}
		
	public int timeLeft(List<Action> actions) {
		int totalTime = 20; //this will be based on food in the future;
		for(Action current: actions) {
			MedcivAction castAction = (MedcivAction) current;
			if(game.matchingVillager(castAction.getVillagerId()).equals(this)) {
				totalTime -= castAction.getType().getTimeCost();
			}
		}
		return totalTime;
	}
	
	public boolean canAffordAction(Action action) {
		List<Action> testActions = new ArrayList<Action>(owner.getActionsThisTurn());
		testActions.add(action);
		return timeLeft(testActions) > 0;
	}
	
	public void addItem(Item item) {
		item.setOwnerId(getId());
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
	
	//returns true if the villager actually had an item of that class
	public boolean removeItem(Item item) {
		if(item == null) {
			System.err.println("tried to remove null item!");
			return false;
		}
		Item toRemove = null;
		for(Item current: ownedItems) {
			if(item.getId() == current.getId()) {
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
	
	public boolean isStarving() {
		return starving;
	}
	
	public List<Action> returnActions(){
		List<Action> retval = new ArrayList<Action>();
		
		for(Item current: ownedItems) {
			retval.addAll(current.getAssociatedActions(this));
		}
		
		return retval;
	}
	
	public void endRound(MedcivGame game) {
		if(getPlannedFood().belowFoodGoal(foodToEat)) {
			starving = true;
		}else {
			starving = false;
		}
		for(Item current: getPlannedFood().getFood()) {
			removeItem(current);//later we need to check if these actually get removed. If not, we go down a food tier
		}
		
		//this makes sure that new items added while resolving other items are themselves resolved
		List<Item> processedItems = new ArrayList<Item>();
		List<Item> currentItems = new ArrayList<Item>(ownedItems);
		while(currentItems.size() > 0) {
			for(Item current: currentItems) {
				current.endRound(game, this);
			}
			processedItems.addAll(currentItems);
			currentItems = new ArrayList<Item>(ownedItems);
			currentItems.removeAll(processedItems);
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
