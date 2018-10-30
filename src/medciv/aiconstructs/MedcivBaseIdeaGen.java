package medciv.aiconstructs;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Game;
import aibrain.IdeaGenerator;
import aibrain.Player;
import medciv.model.actions.GiveItem;
import medciv.model.actions.TendAnimal;
import medciv.model.items.Chicken;
import medciv.model.items.Cow;
import medciv.model.items.Livestock;
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
			retval.addAll(livestockActions(current,villager));
		}
		
		//because we need to tend to animals the same turn we get them, we also check for animals other people will give us
		for(MedcivPlayer curPlayer: castGame.getPlayers()) {
			for(Action action: curPlayer.getActionsThisTurn()) {
				MedcivAction castAction = (MedcivAction)action;
				if(castAction.getType() instanceof GiveItem) {
					GiveItem giveAction = (GiveItem)castAction.getType();
					
					Item item = castGame.matchingVillager(giveAction.getGiveFrom()).getItemById(giveAction.getItem());
					
					retval.addAll(livestockActions(item, castGame.matchingVillager(giveAction.getGiveTo())));
				}
			}
		}
		
		return retval;
	}

	private List<List<Action>> livestockActions(Item item, Villager villager){
		List<List<Action>> retval = new ArrayList<List<Action>>();
		if(item instanceof Cow) {
			Cow cow = (Cow)item;
			List<Action> milkAndTend = new ArrayList<Action>();
			milkAndTend.add(cow.tendAction(villager));
			milkAndTend.add(cow.milkAction(villager));
			retval.add(milkAndTend);
		}
		if(item instanceof Chicken) {
			Chicken chicken = (Chicken)item;
			List<Action> tend = new ArrayList<Action>();
			tend.add(chicken.tendAction(villager));
			retval.add(tend);
		}
		
		return retval;
	}
	
	@Override
	public boolean hasFurtherIdeas(Game game, Player empire, List<Action> committedActions,
			int iteration) {
		return iteration < 10;
	}

}
