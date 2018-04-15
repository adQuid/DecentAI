package aibrain;

import java.util.List;

import actions.Action;
import actions.ActionType;
import model.Colony;
import model.Empire;
import model.Game;
import model.Planet;
import ui.Mainmenu;
import ui.TopMenuButtons;

public class BrainThread implements Runnable{

	private Game game;
	private Empire empire;
	private int lookahead;
	private int tail;
	
	public BrainThread(Game game, Empire empire, int lookahead, int tail){
		this.empire = empire;
		this.game = game;
		this.lookahead = lookahead;
		this.tail = tail;
	}
	
	@Override
	public void run() {
		AIBrain brain = new AIBrain();
		
		List<Action> actions = brain.runAI(game, empire, this.lookahead, this.tail);
		
		game.setActionsForEmpire(actions, empire);
		
		Mainmenu.playersReady.add(empire.getName());
		
		if(Mainmenu.playersReady.size() == game.getEmpires().size()){
			game.endRound();
			Mainmenu.currentTurn++;		
			TopMenuButtons.updateTurn();
			Mainmenu.playersReady.clear();
			
			//temp code
			System.out.println("Actions this turn:");
			for(Action current: actions){
				System.out.print(""+current.getType());
				if(current.getType() == ActionType.develop){
					System.out.print(" "+((Colony)current.getParams().get(0)).getName());
				}
				System.out.println();
			}
			System.out.println("minerals "+game.getEmpires().get(0).getMinerals()+", energy "+game.getEmpires().get(0).getCurrency()+", industry "+((Planet)game.getMap().getGrid()[5][8].getObject()).getActiveColonies().get(0).getIndustry()+"/"+((Planet)game.getMap().getGrid()[5][9].getObject()).getActiveColonies().get(0).getIndustry());
		}
	}

}
