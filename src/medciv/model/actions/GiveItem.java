package medciv.model.actions;

import medciv.model.Item;
import medciv.model.MedcivGame;
import medciv.model.Villager;

public class GiveItem implements ActionType{

	private int giveFrom;
	private int giveTo;
	private int item;
	private String itemName;
	
	public GiveItem(int giveFrom, int giveTo, int item, String itemName) {
		this.giveFrom = giveFrom;
		this.giveTo = giveTo;
		this.item = item;
		this.itemName = itemName;
	}
	
	public int getGiveFrom() {
		return giveFrom;
	}

	public int getGiveTo() {
		return giveTo;
	}

	public int getItem() {
		return item;
	}
	
	@Override
	public void doAction(MedcivGame game) {
		Villager fromVillager = game.matchingVillager(giveFrom);
		Villager toVillager = game.matchingVillager(giveTo);
		Item itemToGive = fromVillager.getItemById(item);

		fromVillager.removeItem(itemToGive);
		toVillager.addItem(itemToGive);
	}

	@Override
	public int getTimeCost() {
		return 0;
	}
	
	@Override 
	public String toString() {
		return "Give "+itemName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + giveFrom;
		result = prime * result + giveTo;
		result = prime * result + item;
		result = prime * result + ((itemName == null) ? 0 : itemName.hashCode());
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
		GiveItem other = (GiveItem) obj;
		if (giveFrom != other.giveFrom)
			return false;
		if (giveTo != other.giveTo)
			return false;
		if (item != other.item)
			return false;
		if (itemName == null) {
			if (other.itemName != null)
				return false;
		} else if (!itemName.equals(other.itemName))
			return false;
		return true;
	}


}
