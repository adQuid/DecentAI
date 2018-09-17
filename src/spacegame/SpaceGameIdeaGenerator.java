package spacegame;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import aibrain.IdeaGenerator;
import model.Colony;
import model.Game;

public class SpaceGameIdeaGenerator implements IdeaGenerator{

	public static SpaceGameIdeaGenerator instance = new SpaceGameIdeaGenerator();
	
	public static SpaceGameIdeaGenerator instance() {
		return instance;
	}
	
	public List<List<Action>> generateIdeas(List<Action> possibilities, Game game){
		List<List<Action>> retval = new ArrayList<List<Action>>();
		
		List<Action> emptyIdea = new ArrayList<Action>();
		retval.add(emptyIdea);
		
		//idea 1: dump everything on an even build for one planet
		for(Action current: possibilities) {
			if(current.getType() == ActionType.develop) {
				List<Action> toAdd = new ArrayList<Action>();
				for(int i = 0; i < game.fetchCurrentEmpire().getMinerals(); i+=4) {
					toAdd.add(current);
					toAdd.add(new Action(ActionType.developPower,current.getParams()));
				}
				retval.add(toAdd);
			}
		}
		
		//idea 2: just make a develop 2 and support it best you can
		for(Action current: possibilities) {
			if(current.getType() == ActionType.develop2) {
				Colony colony = (Colony)current.getParams().get(0);
				List<Action> toAdd = new ArrayList<Action>();
				toAdd.add(current);
				for(int i = 6; i < Math.min(game.fetchCurrentEmpire().getMinerals(),12); i+=2) {
					toAdd.add(new Action(ActionType.developPower,current.getParams()));
				}
				retval.add(toAdd);
			}
		}
		
		//idea 2b: just make a develop 2 and compensate for gaps
				for(Action current: possibilities) {
					if(current.getType() == ActionType.develop2) {
						Colony colony = (Colony)current.getParams().get(0);
						List<Action> toAdd = new ArrayList<Action>();
						toAdd.add(current);
						for(int i = 6; i < Math.min(game.fetchCurrentEmpire().getMinerals(),colony.getIndustry()-colony.getPower() + 12); i+=2) {
							toAdd.add(new Action(ActionType.developPower,current.getParams()));
						}
						retval.add(toAdd);
					}
				}
		
		//idea 3: make hella power to compensate for other development for something else
		for(Action current: possibilities) {
			if(current.getType() == ActionType.developPower) {
				List<Action> toAdd = new ArrayList<Action>();
				for(int i = 0; i < game.fetchCurrentEmpire().getMinerals(); i+=2) {
					toAdd.add(current);
				}
				retval.add(toAdd);
			}
		}
		
		//idea 4: cash out
		for(Action current: possibilities) {
			if(current.getType() == ActionType.cashNow) {
				List<Action> toAdd = new ArrayList<Action>();
				for(int i = 1; i < game.fetchCurrentEmpire().getMinerals()/5; i++) {
					toAdd.add(current);
				}
				retval.add(toAdd);
			}
		}
	
		//idea 5: make enough power to compensate a gap
		for(Action current: possibilities) {
			if(current.getType() == ActionType.developPower) {
				Colony colony = (Colony)current.getParams().get(0);
				List<Action> toAdd = new ArrayList<Action>();
				for(int i = 0; i < colony.getIndustry()-colony.getPower(); i++) {
					toAdd.add(current);
				}
				retval.add(toAdd);
			}
		}
		
		return retval;
	}
	
}
