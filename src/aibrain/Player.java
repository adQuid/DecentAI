package aibrain;

import java.util.List;

public interface Player {
		
	public List<Action> getActionsThisTurn();

	public void setActionsThisTurn(List<Action> actionsThisTurn);
	
	@Override
	public int hashCode();

	@Override
	public boolean equals(Object obj);
	
}
