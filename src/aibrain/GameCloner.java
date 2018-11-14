package aibrain;

public interface GameCloner {

	/**
	 * @param other Game to clone.
	 * @return Exact copy of the Game object provided.
	 */
	public Game cloneGame(Game other);
	
}
