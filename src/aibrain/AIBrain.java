package aibrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import actions.Action;
import actions.ActionType;
import model.Empire;
import testers.GameRunTestser;

public class AIBrain {
	
	private int maxTtl = 0;
	
	public List<Action> runAI(Game trueGame, Empire self, int lookAhead, int lookAheadTail){
		maxTtl = lookAhead;
		Hypothetical root = new Hypothetical(trueGame, new ArrayList<Modifier>(), this, new ArrayList<Action>(), new ArrayList<Action>(), new ArrayList<Action>(), lookAhead, lookAheadTail, self, 0.0);
		return root.calculate().getActions();
	}
		
	public boolean valueIsValid(double value){
		return true;
	}
	
	public int getMaxTtl() {
		return maxTtl;
	}
}
