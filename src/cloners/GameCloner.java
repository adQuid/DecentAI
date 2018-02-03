package cloners;

import model.Game;
import model.Map;

public class GameCloner {

	public static Game cloneGame(Game other){
		Game retval = new Game(other);
		
		retval.setMap(new Map(other.getMap(), retval));
		
		return retval;
	}
	
}
