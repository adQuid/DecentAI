package aibrain;

import java.util.List;

/**
 * An abstract representation of a player's action. The actual logic of what the action does isn't used by Decent AI, so
 * this can just be a placeholder rather than containing any business logic if desired. Physical equality between actions
 * is used frequently when comparing plans, however, so the Action class MUST be immutable. 
 */
public interface Action {
			
	/**
	 * Used for logging actions; valuable for debugging
	 * @return String representation of action
	 */
	public String toString();
	
	/**
	 * valuable for debugging
	 * @return boolean representing if this action was generated as part of a contingency
	 * @see ContingencyGenerator
	 */
	public boolean isContingency();
	
}
