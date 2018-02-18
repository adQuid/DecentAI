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
			
	public List<Action> runAI(Game trueGame, Empire self, int lookAhead){
		//factory = new HypotheticalFactory();
		Hypothetical root = new Hypothetical(trueGame, this, new ArrayList<Action>(), new ArrayList<Action>(), lookAhead, self);
		return root.calculate().getActions();
	}
		
	public boolean valueIsValid(double value){
		return true;
	}
}
