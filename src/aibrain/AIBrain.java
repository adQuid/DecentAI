package aibrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import actions.Action;
import actions.ActionType;
import model.Empire;
import model.Game;
import testers.GameRunTestser;

public class AIBrain {
		
	HypotheticalFactory factory = new HypotheticalFactory();

	public HypotheticalFactory getFactory(){
		return factory;
	}
	
	public List<Action> runAI(Game trueGame, Empire self, int lookAhead){
		//factory = new HypotheticalFactory();
		Hypothetical root = new Hypothetical(trueGame, this, new ArrayList<Action>(), lookAhead, self).getOptimalActions(false);
		
		return root.getActions();
	}
		
	public boolean valueIsValid(double value){
		return true;
	}
}
