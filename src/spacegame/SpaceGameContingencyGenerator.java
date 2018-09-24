package spacegame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aibrain.Action;
import aibrain.Contingency;
import aibrain.ContingencyGenerator;
import aibrain.Game;
import model.ActionType;
import model.Colony;
import model.Empire;

public class SpaceGameContingencyGenerator implements ContingencyGenerator{

	private static SpaceGameContingencyGenerator instance = new SpaceGameContingencyGenerator();
	
	public static SpaceGameContingencyGenerator instance() {
		return instance;
	}
	
	@Override
	public List<Contingency> generateContingencies(Game game, Empire empire, List<Action> possibilities,
			int iteration) {
		model.Game castGame = (model.Game)game;
		List<Contingency> retval = new ArrayList<Contingency>();
		
		//there HAS to be a way to do this with less loops...
		for(Empire curEmpire: castGame.getEmpires()) {
			if(!curEmpire.equals(empire)) {
				for(Colony curColony: castGame.fetchAllColonies()) {
					if(curColony.getOwner().equals(curEmpire) &&
							curEmpire.getMinerals() >=5) {
						for(Colony targetColony: castGame.fetchAllColonies()) {
							if(targetColony.getOwner().equals(empire)) {

								List<Action> toAdd = new ArrayList<Action>();

								Map<String,Object> params = new HashMap<String,Object>();

								params.put("from", curColony);
								params.put("target", targetColony);

								SpaceGameAction attack = new SpaceGameAction(ActionType.pillage,params);
								attack.setContingency(true);

								toAdd.add(attack);

								retval.add(new Contingency(curEmpire,toAdd));
							}
						}
					}
				}
			}
		}
		
		return retval;
	}

}
