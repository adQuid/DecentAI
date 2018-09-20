package spacegame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import actions.Action;
import aibrain.IdeaGenerator;
import cloners.GameCloner;
import model.ActionType;
import model.Colony;
import model.Empire;
import aibrain.Game;

public class SpaceGameIdeaGenerator implements IdeaGenerator{

	public static SpaceGameIdeaGenerator instance = new SpaceGameIdeaGenerator();
	
	public static SpaceGameIdeaGenerator instance() {
		return instance;
	}
	
	public List<List<Action>> generateIdeas(Game game, Empire empire, List<Action> possibilities, int iteration){
		List<List<Action>> retval = new ArrayList<List<Action>>();

		model.Game castGame = (model.Game)game;

		List<SpaceGameAction> castPossibilities = new ArrayList<SpaceGameAction>();
		
		for(Action current: possibilities) {
			castPossibilities.add((SpaceGameAction)current);
		}
		
		//debug
		Map<String,Object> dummyParams = new TreeMap<String,Object>();//this is debug; treemap is for lols
		dummyParams.put("iteration",iteration);
		
		List<Action> emptyIdea = new ArrayList<Action>();
		retval.add(emptyIdea);

		if(iteration % 2 == 0) {
			//idea 1: dump everything on an even build for one planet
			for(SpaceGameAction current: castPossibilities) {
				if(current.getType() == ActionType.develop) {
					List<Action> toAdd = new ArrayList<Action>();
					for(int i = 0; i < castGame.fetchCurrentEmpire().getMinerals(); i+=4) {
						toAdd.add(current);
						toAdd.add(new SpaceGameAction(ActionType.developPower,current.getParams()));
					}
					toAdd.add(new SpaceGameAction(ActionType.dummy, dummyParams));
					retval.add(toAdd);
				}
			}

			//idea 2: just make a develop 2 and support it best you can
			for(SpaceGameAction current: castPossibilities) {
				if(current.getType() == ActionType.develop2) {
					Colony colony = (Colony)current.getParams().get(0);
					List<Action> toAdd = new ArrayList<Action>();
					toAdd.add(current);
					for(int i = 6; i < Math.min(castGame.fetchCurrentEmpire().getMinerals(),12); i+=2) {
						toAdd.add(new SpaceGameAction(ActionType.developPower,current.getParams()));
					}
					toAdd.add(new SpaceGameAction(ActionType.dummy, dummyParams));
					retval.add(toAdd);
				}
			}

			//idea 3: make hella power to compensate for other development for something else
			for(SpaceGameAction current: castPossibilities) {
				if(current.getType() == ActionType.developPower) {
					List<Action> toAdd = new ArrayList<Action>();
					for(int i = 0; i < castGame.fetchCurrentEmpire().getMinerals(); i+=2) {
						toAdd.add(current);
					}
					toAdd.add(new SpaceGameAction(ActionType.dummy, dummyParams));
					retval.add(toAdd);
				}
			}

			//idea 4: cash out
			for(SpaceGameAction current: castPossibilities) {
				if(current.getType() == ActionType.cashNow) {
					List<Action> toAdd = new ArrayList<Action>();
					for(int i = 1; i < castGame.fetchCurrentEmpire().getMinerals()/5; i++) {
						toAdd.add(current);
					}
					toAdd.add(new SpaceGameAction(ActionType.dummy, dummyParams));
					retval.add(toAdd);
				}
			}
		}
		for(Colony current: castGame.fetchAllColonies()) {
			if(current.getOwner().equals(empire)) {
				if(current.getIndustry() > current.getPower()) {
					List<Action> toAdd = new ArrayList<Action>();

					Map<String,Object> params = new HashMap<String,Object>();
					params.put("colony",current);

					for(int i=0; i < current.getIndustry()-current.getPower(); i++) {
						toAdd.add(new SpaceGameAction(ActionType.developPower,params));
					}
					toAdd.add(new SpaceGameAction(ActionType.dummy,dummyParams));
					retval.add(toAdd);
				}
				if(current.getIndustry() < current.getPower()) {
					List<Action> toAdd = new ArrayList<Action>();

					Map<String,Object> params = new HashMap<String,Object>();
					params.put("colony",current);

					for(int i=0; i < current.getPower()-current.getIndustry(); i++) {
						toAdd.add(new SpaceGameAction(ActionType.develop,params));
					}
					toAdd.add(new SpaceGameAction(ActionType.dummy,dummyParams));
					retval.add(toAdd);
				}
			}				
		}
				
		return retval;
	}

	@Override
	public boolean hasFurtherIdeas(Game game, Empire empire, List<Action> possibilities, List<Action> committedActions, int iteration) {
		
		if(iteration > 5) {
			return false;
		}
		
		Game futureGame = GameCloner.cloneGame(game);
		futureGame.setActionsForEmpire(committedActions, empire);
		futureGame.endRound();
		List<Empire> empires = futureGame.getEmpires();
		Empire me = null;
		for(Empire current: empires) {
			if(current.equals(empire)) {
				me = current;
			}
		}
		if(me.getMinerals() < 2) {
			return false;
		}
		if(iteration > 1 && futureGame.returnActions(empire).size() == 0) {
			return false;
		}
		return true;
	}
	
}
