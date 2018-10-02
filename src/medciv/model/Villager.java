package medciv.model;

import java.util.ArrayList;
import java.util.List;

import medciv.aiconstructs.MedcivPlayer;
import medciv.model.items.Stackable;

public class Villager{

	private MedcivPlayer owner;
	private String name;
	private Town location;
	private List<Item> ownedItems;
	
	public Villager(Town location, String name) {
		this.name = name;
		this.location = location;
		this.ownedItems = new ArrayList<Item>();
	}

	public Villager clone(MedcivGame game) {
		Villager retval = new Villager(location,name);
		
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

	public void addItems(Item item) {
		if(item instanceof Stackable) {
			for(Item current: ownedItems) {
				if(current instanceof Stackable && current.getClass() == item.getClass()) {
					((Stackable)current).combine((Stackable)item);
					return;
				}
			}
		}
		this.ownedItems.add(item);
	}

	public void setName(String name) {
		this.name = name;
	}

	public MedcivPlayer getOwnwer() {
		return owner;
	}

	public void setOwnwer(MedcivPlayer ownwer) {
		this.owner = ownwer;
	}
	
	public void endRound() {
		for(Item current: ownedItems) {
			current.endRound(this);
		}
	}
}
