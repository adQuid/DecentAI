package medciv.aiconstructs;

import aibrain.Game;
import aibrain.GameCloner;
import medciv.model.MedcivGame;

public class MedcivCloner implements GameCloner{

	@Override
	public Game cloneGame(Game other) {
		MedcivGame retval = ((MedcivGame)other).clone();
				
		return retval;
	}

}
