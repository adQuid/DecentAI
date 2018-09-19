package spacegame;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import aibrain.IdeaGenerator;
import cloners.GameCloner;
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

		//debug
		List<Object> dummyParams = new ArrayList<Object>();
		dummyParams.add("iter "+iteration);
		
		List<Action> emptyIdea = new ArrayList<Action>();
		retval.add(emptyIdea);

		if(iteration % 2 == 0) {
			//idea 1: dump everything on an even build for one planet
			for(Action current: possibilities) {
				if(current.getType() == ActionType.develop) {
					List<Action> toAdd = new ArrayList<Action>();
					for(int i = 0; i < castGame.fetchCurrentEmpire().getMinerals(); i+=4) {
						toAdd.add(current);
						toAdd.add(new Action(ActionType.developPower,current.getParams()));
					}
					toAdd.add(new Action(ActionType.dummy, dummyParams));
					retval.add(toAdd);
				}
			}

			//idea 2: just make a develop 2 and support it best you can
			for(Action current: possibilities) {
				if(current.getType() == ActionType.develop2) {
					Colony colony = (Colony)current.getParams().get(0);
					List<Action> toAdd = new ArrayList<Action>();
					toAdd.add(current);
					for(int i = 6; i < Math.min(castGame.fetchCurrentEmpire().getMinerals(),12); i+=2) {
						toAdd.add(new Action(ActionType.developPower,current.getParams()));
					}
					toAdd.add(new Action(ActionType.dummy, dummyParams));
					retval.add(toAdd);
				}
			}

			//idea 3: make hella power to compensate for other development for something else
			for(Action current: possibilities) {
				if(current.getType() == ActionType.developPower) {
					List<Action> toAdd = new ArrayList<Action>();
					for(int i = 0; i < castGame.fetchCurrentEmpire().getMinerals(); i+=2) {
						toAdd.add(current);
					}
					toAdd.add(new Action(ActionType.dummy, dummyParams));
					retval.add(toAdd);
				}
			}

			//idea 4: cash out
			for(Action current: possibilities) {
				if(current.getType() == ActionType.cashNow) {
					List<Action> toAdd = new ArrayList<Action>();
					for(int i = 1; i < castGame.fetchCurrentEmpire().getMinerals()/5; i++) {
						toAdd.add(current);
					}
					toAdd.add(new Action(ActionType.dummy, dummyParams));
					retval.add(toAdd);
				}
			}
		}
		for(Colony current: castGame.fetchAllColonies()) {
			if(current.getOwner().equals(empire)) {
				List<Action> toAdd = new ArrayList<Action>();

				List<Object> params = new ArrayList<Object>();
				params.add(current);
								
				for(int i=0; i < current.getIndustry()-current.getPower(); i++) {
					toAdd.add(new Action(ActionType.developPower,params));
				}
				toAdd.add(new Action(ActionType.dummy,dummyParams));
				if(retval.size()>0) {
					retval.add(toAdd);
				}
			}
		}
		
		//debug
		for(List<Action> current: retval) {
			if(current.size() == 6 &&
					current.get(0).getType() == ActionType.developPower &&
					current.get(1).getType() == ActionType.developPower &&
					current.get(2).getType() == ActionType.developPower &&
					current.get(3).getType() == ActionType.developPower &&
					current.get(4).getType() == ActionType.developPower &&
					castGame.fetchAllColonies().get(0).getIndustry() == 7 &&
					castGame.fetchAllColonies().get(0).getPower() == 5 &&
					castGame.fetchAllColonies().get(1).getIndustry() == 7 &&
					castGame.fetchAllColonies().get(1).getPower() == 7) {
				System.out.println("aha!");
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
