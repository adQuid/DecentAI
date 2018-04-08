package cloners;

import model.Map;
import aibrain.Game;

public class GameCloner {

	public static Game cloneGame(Game other){
		if(other instanceof model.Game) {
			model.Game converted = (model.Game)other;
			model.Game retval = new model.Game(converted);

			retval.setMap(new Map(converted.getMap(), retval));
			retval.setEvents(converted.getEvents());
			
			retval.setLive(false);

			return retval;
		}
		return null;
	}
	
}
