package aibrain;

import java.util.List;

import model.Empire;

public interface Game {

	public boolean isLive();
	
	public List<Empire> getEmpires();
	
	public void setLive(boolean live);
	
	public List<Action> returnActions(Empire empire);
	
	public void setActionsForEmpire(List<Action> actions, Empire empire);
	
	public void appendActionsForEmpire(List<Action> actions, Empire empire);
	
	//clears out all actions for each empire
	public void endRound();
	
	//returns a new game based on the results of endRound
	public Game nextRound();
	
	public Game imageForPlayer(Empire player);
}
