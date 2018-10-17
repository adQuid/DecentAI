package medciv.aiconstructs;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Game;
import aibrain.IdeaGenerator;
import aibrain.Player;
import medciv.model.actions.TendAnimal;

public class MedcivBaseIdeaGen implements IdeaGenerator{

	@Override
	public List<List<Action>> generateIdeas(Game game, Player empire, List<Action> possibilities, int iteration) {
		List<List<Action>> retval = new ArrayList<List<Action>>();
		
		for(Action current: possibilities) {
			MedcivAction castAction = (MedcivAction)current;
			
			if(castAction.getType() instanceof TendAnimal) {
				List<Action> toAdd = new ArrayList<Action>();
				toAdd.add(castAction);
				
				retval.add(toAdd);
				continue; 
			}
		}
		
		return retval;
	}

	@Override
	public boolean hasFurtherIdeas(Game game, Player empire, List<Action> possibilities, List<Action> committedActions,
			int iteration) {
		// TODO Auto-generated method stub
		return false;
	}

}
