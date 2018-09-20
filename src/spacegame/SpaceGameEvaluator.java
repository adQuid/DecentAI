package spacegame;

import java.util.HashMap;
import java.util.Map;

import aibrain.GameEvaluator;
import aibrain.Score;
import model.Colony;
import model.Empire;
import model.Game;
import model.Planet;
import model.Star;
import model.Tile;

public class SpaceGameEvaluator implements GameEvaluator{

	private static SpaceGameEvaluator instance = new SpaceGameEvaluator();
	
	public static SpaceGameEvaluator getInstance() {
		return instance;
	}
	
	//Given a game state, how good do I find this outcome?
	public Score getValue(Game game, Empire empire){
		double mineralScore = Math.pow(game.findMatchingEmpire(empire).getMinerals()*1.0,1.0);
		double currencyScore = Math.pow(game.findMatchingEmpire(empire).getCurrency() * 1.0,1.0);

		double productionPotentialScore = 0;
		for(Tile[] row: game.getMap().getGrid()){
			for(Tile currentTile: row){
				if(currentTile.getObject() != null && currentTile.getObject() instanceof Planet && ((Planet)currentTile.getObject()).fetchColonyForEmpire(empire) != null){
					productionPotentialScore += 3 * (Math.min(((Planet)currentTile.getObject()).fetchColonyForEmpire(empire).getIndustry(),((Planet)currentTile.getObject()).fetchColonyForEmpire(empire).getPower()));
				}
			}
		}

		Map<String,Double> map = new HashMap<String,Double>();
		map.put("minerals", mineralScore);
		map.put("currency", currencyScore);
		map.put("production potential", productionPotentialScore);
		
		return new Score(map);
	}
	
	
		
	
}
