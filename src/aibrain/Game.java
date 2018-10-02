package aibrain;

import java.util.List;

public interface Game {

	public boolean isLive();
	
	public List<Player> getEmpires();
	
	public void setLive(boolean live);
	
	public List<Action> returnActions(Player player);
	
	public void setActionsForPlayer(List<Action> actions, Player player);
	
	public void appendActionsForPlayer(List<Action> actions, Player player);
	
	//clears out all actions for each empire
	public void endRound();
	
	//returns a new game based on the results of endRound
	public Game nextRound();
	
	public Game imageForPlayer(Player player);
}
