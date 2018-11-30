package medciv.aiconstructs;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import aibrain.Game;
import aibrain.GameEvaluator;
import aibrain.Player;
import aibrain.Score;
import medciv.model.Item;
import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.model.items.Eggs;
import medciv.model.items.Milk;

public class MedcivEvaluator implements GameEvaluator{

	@Override
	public Score getValue(Game game, Player empire) {		
		Map<String,BigDecimal> categories = new HashMap<String,BigDecimal>();
		
		MedcivGame castGame = (MedcivGame)game;
		
		for(Villager villager: castGame.getPeople()) {
			if(villager.getOwner().equals(empire)) {
				if(villager.isStarving()) {
					categories.put("Starvation", new BigDecimal(-10.0));
				}
				
				int totalMilk = 0;
				int totalEggs = 0;
				for(Item item: villager.getOwnedItems()) {
					if(item instanceof Milk) {
						totalMilk += ((Milk)item).getStackSize();
					}
					if(item instanceof Eggs) {
						totalEggs += ((Eggs)item).getStackSize();
					}
				}
				if(categories.containsKey("milk")) {
					categories.put("milk", categories.get("milk").add(new BigDecimal(totalMilk)));
				} else {
					categories.put("milk", new BigDecimal(totalMilk));
				}
				if(categories.containsKey("eggs")) {
					categories.put("eggs", categories.get("eggs").add(new BigDecimal(totalEggs)));
				} else {
					categories.put("eggs", new BigDecimal(totalEggs));
				}
			}
		}
				
		Score retval = new Score(categories);
		return retval;
	}	
}
