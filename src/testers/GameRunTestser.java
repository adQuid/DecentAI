package testers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import actions.Action;
import actions.ActionType;
import aibrain.AIBrain;
import model.Colony;
import model.Game;
import model.Planet;

public class GameRunTestser {

	public static void main(String[] args){
		Game testGame = new Game();
		
		AIBrain brain = new AIBrain();
		
		final int totalTurns = 30;
		final int lookAhead = 12;
		
		for(int i = 0; i<totalTurns; i++){
			
			System.out.println("minerals "+testGame.getEmpires().get(0).getMinerals()+", energy "+testGame.getEmpires().get(0).getEnergy()+", industry "+((Planet)testGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)testGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getPower()+","+((Planet)testGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)testGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getPower());
			List<Action> actions = brain.runAI(testGame, testGame.fetchCurrentEmpire(), lookAhead);
			System.out.println("Actions this turn (ttl "+Math.min(lookAhead,totalTurns-i)+"):");
			for(Action current: actions){
				System.out.print(""+current.getType());
				if(current.getType() == ActionType.develop ||
						current.getType() == ActionType.developPower){
					System.out.print(" "+((Colony)current.getParams().get(0)).getName());
				}
				System.out.println();
			}
			testGame.setActionsForEmpire(actions, testGame.fetchCurrentEmpire());
			testGame.endRound();
		}
		System.out.println("minerals "+testGame.getEmpires().get(0).getMinerals()+", energy "+testGame.getEmpires().get(0).getEnergy()+", industry "+((Planet)testGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)testGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getPower()+","+((Planet)testGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)testGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getPower());
	}
		
}
