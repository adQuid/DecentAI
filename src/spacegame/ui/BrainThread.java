package spacegame.ui;

import java.util.List;

import aibrain.AIBrain;
import aibrain.Action;
import spacegame.Empire;
import spacegame.SpaceGameAction;
import spacegame.SpaceGameEvaluator;
import spacegame.model.Planet;

public class BrainThread implements Runnable{
	
	@Override
	public void run() {
		for(AIBrain brain: Mainmenu.brains) {
			brain.addLog("Resources this turn: minerals "+((Empire)Mainmenu.liveGame.getEmpires().get(0)).getMinerals()+", credits "+((Empire)Mainmenu.liveGame.getEmpires().get(0)).getCurrency()+", industry "+((Planet)Mainmenu.liveGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)Mainmenu.liveGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getPower()+","+((Planet)Mainmenu.liveGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)Mainmenu.liveGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getPower()+". Score: "+SpaceGameEvaluator.getInstance().getValue(Mainmenu.liveGame, Mainmenu.liveGame.getEmpires().get(0)).totalScore());
			List<Action> actions = brain.runAI(Mainmenu.liveGame).getImmediateActions();
			
			Mainmenu.liveGame.setActionsForPlayer(actions, brain.getSelf());
			
			Mainmenu.playersReady.add(((Empire)brain.getSelf()).getName());
		}
		
		//debug
		for(int index = 0; index < Mainmenu.liveGame.getEmpires().get(0).getActionsThisTurn().size(); index++) {
			SpaceGameAction action = (SpaceGameAction)Mainmenu.liveGame.getEmpires().get(0).getActionsThisTurn().get(index);

			if(Mainmenu.liveGame.getEmpires().get(1).getActionsThisTurn().size() <= index) {
				System.err.println("empire 0 has a "+action.getType()+" that empire 1 doesn't have");
			} else {
				SpaceGameAction action2 = (SpaceGameAction)Mainmenu.liveGame.getEmpires().get(1).getActionsThisTurn().get(index);

				if(action.getType() != action2.getType()) {
					System.err.println(action.getType()+" is not "+action2.getType());
				}
			}
		}
		
		Mainmenu.liveGame.endRound();
		Mainmenu.currentTurn++;		
		TopMenuButtons.updateTurn();
		Mainmenu.playersReady.clear();
	}

}
