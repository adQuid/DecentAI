package spacegame;

import aibrain.Game;
import aibrain.GameCloner;
import spacegame.model.Map;

public class SpaceGameCloner implements GameCloner{

	static SpaceGameCloner instance = new SpaceGameCloner();
	
	public static SpaceGameCloner getInstance() {
		return instance;
	}
	
	public Game cloneGame(Game other){
		if(other instanceof spacegame.model.Game) {
			spacegame.model.Game converted = (spacegame.model.Game)other;
			spacegame.model.Game retval = new spacegame.model.Game(converted);

			retval.setMap(new Map(converted.getMap(), retval));
			
			retval.setLive(false);

			return retval;
		}
		return null;
	}
	
}
