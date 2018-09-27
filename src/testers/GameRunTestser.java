package testers;

import java.util.List;

import aibrain.AIBrain;
import aibrain.Action;
import aibrain.HypotheticalResult;
import aibrain.Reasoning;
import spacegame.SpaceGameAction;
import spacegame.SpaceGameContingencyGenerator;
import spacegame.SpaceGameEvaluator;
import spacegame.SpaceGameIdeaGenerator;
import spacegame.model.ActionType;
import spacegame.model.Colony;
import spacegame.model.Game;

public class GameRunTestser {

	public static void main(String[] args){
		Game testGame = new Game();
				
		final int totalTurns = 30;
		final int lookAhead = 8;
		final int lookAheadSecondary = 0;
		final int tailLength = 12;
		long time = System.currentTimeMillis();
		
		AIBrain brain = new AIBrain(testGame.fetchCurrentEmpire(), lookAhead, lookAheadSecondary, tailLength,SpaceGameIdeaGenerator.instance(),SpaceGameContingencyGenerator.instance(),SpaceGameEvaluator.getInstance());
		
		for(int i = 0; i<totalTurns; i++){
			time = System.currentTimeMillis();
			HypotheticalResult result = brain.runAI(testGame);
			List<Action> actions = result.getImmediateActions();
			System.out.println("Actions this turn:");
			for(Action current: actions){
				System.out.print(""+((SpaceGameAction)current).getType());
				if(((SpaceGameAction)current).getType() == ActionType.develop ||
						((SpaceGameAction)current).getType() == ActionType.developPower){
					System.out.print(" "+((Colony)current.getParam("colony")).getName());
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
	}
		
}
