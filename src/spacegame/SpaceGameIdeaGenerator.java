package spacegame;

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
			
			switch(current.getType()) {
			case develop:
				List<Object> params = new ArrayList<Object>();
				params.add(current.getParams().get(0));
				Action getPower = new Action(ActionType.developPower,params);
				Action fortify1 = new Action(ActionType.developPower,params);
				
				List<Action> toAdd = new ArrayList<Action>();
				toAdd.add(current);
				toAdd.add(getPower);
				retval.add(new ArrayList<Action>(toAdd));
				toAdd.add(fortify1);
				retval.add(toAdd);
				break;
			case developPower:
				params = new ArrayList<Object>();
				params.add(current.getParams().get(0));
				Action fortify3 = new Action(ActionType.developPower,params);
				Action fortify4 = new Action(ActionType.developPower,params);
				Action develop = new Action(ActionType.develop,params);
				
				toAdd = new ArrayList<Action>();
				toAdd.add(current);
				toAdd.add(fortify3);
				toAdd.add(fortify4);
				retval.add(new ArrayList<Action>(toAdd));
				toAdd.add(develop);
				retval.add(toAdd);
				break;
			case cashNow:
				params = new ArrayList<Object>();
				params.add(current.getParams().get(0));
				Action secondCashNow = new Action(ActionType.cashNow,params);
				
				toAdd = new ArrayList<Action>();
				toAdd.add(current);
				toAdd.add(secondCashNow);
				retval.add(toAdd);
				break;
			
			}
		}
		
		return retval;
	}
	
}
