package aibrain;

import java.util.List;

/**
 * The model representing the state of a game. This must be mutable, and must be able to be deeply copied by the GameCloner 
 * you provide. 
 */
public interface Game {

	/**
	 * @return true if the game is the "real" version that the player will see. Used mostly for debugging.
	 */
	public boolean isLive();
	
	
	public List<Player> getPlayers();
	
	public void setLive(boolean live);
	
	
	public void setActionsForPlayer(List<Action> actions, Player player);
	
	public void appendActionsForPlayer(List<Action> actions, Player player);
	
	/**
	 * End one round of the game, as though the "end turn" button was pressed. This must modify the instance, and must 
	 * clear out all player actions (or ensure they aren't carried into the next round by other means), as the AI brain 
	 * will not do this automatically in order to avoid interfering with any other layers of AI.
	 */
	public void endRound();
	
	/**
	 * @return A copy of this game where endRound() has been called. Unlike endRound, this must NOT modify the instance.
	 */
	public Game nextRound();
	
	/**
	 * @param player Player who's perspective the game is viewed from.
	 * @return A modified copy of the game, accounting for any information that player is not privy to.
	 */
	public Game imageForPlayer(Player player);
}
