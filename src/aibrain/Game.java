package aibrain;

import java.util.List;

import actions.Action;
import model.Empire;

public interface Game {

	public boolean isLive();
	
	public void setLive(boolean live);
	
	public List<Action> returnActions();
	
	public void setActionsForEmpire(List<Action> actions, Empire empire);
	
	//clears out all actions for each empire
	public void endRound();
	
	//returns a new game based on the results of endRound
	public Game nextRound();
	
}
