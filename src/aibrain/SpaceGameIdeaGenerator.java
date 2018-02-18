package aibrain;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import model.Game;

public class SpaceGameIdeaGenerator {

	public static List<List<Action>> generateIdeas(List<Action> possibilities, Game game){
		List<List<Action>> retval = new ArrayList<List<Action>>();
		
		List<Action> emptyIdea = new ArrayList<Action>();
		retval.add(emptyIdea);
		
		for(Action current: possibilities) {
			List<Action> defaultIdea = new ArrayList<Action>();
			defaultIdea.add(current);
			retval.add(defaultIdea);
			
			if(current.getType() == ActionType.develop) {
				List<Object> params = new ArrayList<Object>();
				params.add(current.getParams().get(0));
				Action getPower = new Action(ActionType.developPower,params);
				
				List<Action> toAdd = new ArrayList<Action>();
				toAdd.add(current);
				toAdd.add(getPower);
				retval.add(toAdd);
			}			
		}
		
		return retval;
	}
	
}
