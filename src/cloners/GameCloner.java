package cloners;

import aibrain.Game;

import spacegame.model.Map;

public class GameCloner {

	public static Game cloneGame(Game other){
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
