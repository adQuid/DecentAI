package testers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import actions.Action;
import actions.ActionType;
import aibrain.AIBrain;
import aibrain.HypotheticalResult;
import aibrain.Reasoning;
import model.Colony;
import model.Game;
import model.Planet;

public class GameRunTestser {

	public static void main(String[] args){
		Game testGame = new Game();
		
		AIBrain brain = new AIBrain();
		
		final int totalTurns = 30;
		final int lookAhead = 4;
		final int lookAheadSecondary = 4;
		final int tailLength = 5;
		long time = System.currentTimeMillis();
		
		for(int i = 0; i<totalTurns; i++){
			time = System.currentTimeMillis();
			System.out.println("minerals "+testGame.getEmpires().get(0).getMinerals()+", credits "+testGame.getEmpires().get(0).getCurrency()+", industry "+((Planet)testGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)testGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getPower()+","+((Planet)testGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)testGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getPower());
			HypotheticalResult result = brain.runAI(testGame, testGame.fetchCurrentEmpire(), lookAhead, lookAheadSecondary, tailLength);
			List<Action> actions = result.getImmediateActions();
			System.out.println("Actions this turn:");
			for(Action current: actions){
				System.out.print(""+current.getType());
				if(current.getType() == ActionType.develop ||
						current.getType() == ActionType.developPower){
					System.out.print(" "+((Colony)current.getParams().get(0)).getName());
				}
				System.out.println();
			}
			System.out.println("Reasoning this turn (ttl "+Math.min(lookAhead,totalTurns-i)+"):");
			for(Reasoning current: result.getPlan().getReasonings()) {
				System.out.println(">"+current.toString());
			}
			testGame.setActionsForEmpire(actions, testGame.fetchCurrentEmpire());
			testGame.endRound();
			System.out.println("ran in "+(System.currentTimeMillis()-time)+" ms");
		}
		System.out.println("minerals "+testGame.getEmpires().get(0).getMinerals()+", credits "+testGame.getEmpires().get(0).getCurrency()+", industry "+((Planet)testGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)testGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getPower()+","+((Planet)testGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)testGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getPower());
	}
		
}
