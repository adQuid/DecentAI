package medciv.model;

import java.util.ArrayList;
import java.util.List;

import medciv.model.items.Edible;
import medciv.model.items.Stackable;

public class FoodGrouping {

	private List<Edible> food = new ArrayList<Edible>();
		
	public FoodGrouping(List<Item> possibleFood, int foodGoal) {
		for(Item current: possibleFood) {
			if(current instanceof Edible &&
					belowFoodGoal(foodGoal)) {
				if(current instanceof Stackable) {
					food.add((Edible)((Stackable)current)
							.hypotheticalStack(Math.min(foodGoal,((Stackable)current).getStackSize())));
				}
			}
		}
	}
	
	public List<Edible> getFood(){
		return food;
	}
	
	public boolean belowFoodGoal(int foodSize) {
		int totalFood = 0;
		for(Edible current: food) {
			if(current instanceof Stackable) {
				totalFood += ((Stackable)current).getStackSize();
			}else {
				totalFood++;
			}
		}
		return totalFood < foodSize;
	}
}
