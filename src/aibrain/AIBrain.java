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
	
	private int maxTtl = 0;
	
	public List<Action> runAI(Game trueGame, Empire self, int lookAhead){
		maxTtl = lookAhead;
		Hypothetical root = new Hypothetical(trueGame, this, new ArrayList<Action>(), new ArrayList<Action>(), lookAhead, self, 0.0);
		return root.calculate().getActions();
	}
		
	public boolean valueIsValid(double value){
		return true;
	}
	
	public int getMaxTtl() {
		return maxTtl;
	}
}
