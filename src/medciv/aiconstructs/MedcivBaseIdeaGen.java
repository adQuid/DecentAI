package medciv.aiconstructs;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Game;
import aibrain.IdeaGenerator;
import aibrain.Player;
import medciv.model.actions.TendAnimal;
import medciv.model.items.Chicken;
import medciv.model.items.Cow;
import medciv.model.Item;
import medciv.model.MedcivGame;
import medciv.model.Villager;

public class MedcivBaseIdeaGen implements IdeaGenerator{

	@Override
	public List<List<Action>> generateIdeas(Game game, Player player,  int iteration) {
		MedcivGame castGame = (MedcivGame)game;
		List<List<Action>> retval = new ArrayList<List<Action>>();
		
		Villager villager = castGame.firstVillagerOwnedByPlayer((MedcivPlayer)player);
		
		for(Item current: villager.getOwnedItems()) {
			if(current instanceof Cow) {
				Cow cow = (Cow)current;
				List<Action> milkAndTend = new ArrayList<Action>();
				milkAndTend.add(cow.tendAction(villager));
				milkAndTend.add(cow.milkAction(villager));
				retval.add(milkAndTend);
			}
			if(current instanceof Chicken) {
				Chicken chicken = (Chicken)current;
				List<Action> tend = new ArrayList<Action>();
				tend.add(chicken.tendAction(villager));
				retval.add(tend);
			}
		}
		
		return retval;
	}

	@Override
	public boolean hasFurtherIdeas(Game game, Player empire, List<Action> committedActions,
			int iteration) {
		return iteration < 10;
	}

}
