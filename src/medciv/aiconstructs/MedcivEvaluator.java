package medciv.aiconstructs;

import java.util.HashMap;
import java.util.Map;

import aibrain.Game;
import aibrain.GameEvaluator;
import aibrain.Player;
import aibrain.Score;

public class MedcivEvaluator implements GameEvaluator{

	@Override
	public Score getValue(Game game, Player empire) {
		Score retval = new Score();
		
		Map<String,Double> categories = new HashMap<String,Double>();
		categories.put("test",1.0);//all games are created equal for now
		
		retval.setCategories(categories);
		return retval;
	}	
}
