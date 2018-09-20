package ui;

import java.util.List;

import actions.Action;
import aibrain.AIBrain;
import model.ActionType;
import model.Colony;
import model.Empire;
import model.Game;
import model.Planet;
import spacegame.SpaceGameEvaluator;

public class BrainThread implements Runnable{
	
	@Override
	public void run() {
		for(AIBrain brain: Mainmenu.brains) {
			brain.addLog("Resources this turn: minerals "+Mainmenu.liveGame.getEmpires().get(0).getMinerals()+", credits "+Mainmenu.liveGame.getEmpires().get(0).getCurrency()+", industry "+((Planet)Mainmenu.liveGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)Mainmenu.liveGame.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getPower()+","+((Planet)Mainmenu.liveGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)Mainmenu.liveGame.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getPower()+". Score: "+SpaceGameEvaluator.getInstance().getValue(Mainmenu.liveGame, Mainmenu.liveGame.getEmpires().get(0)).totalScore());
			List<Action> actions = brain.runAI(Mainmenu.liveGame).getImmediateActions();
			
			Mainmenu.liveGame.setActionsForEmpire(actions, brain.getSelf());
			
			Mainmenu.playersReady.add(brain.getSelf().getName());
		}
		
		Mainmenu.liveGame.endRound();
		Mainmenu.currentTurn++;		
		TopMenuButtons.updateTurn();
		Mainmenu.playersReady.clear();
	}

}
