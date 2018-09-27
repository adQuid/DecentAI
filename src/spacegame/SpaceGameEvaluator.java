package spacegame;

import java.util.HashMap;
import java.util.Map;

import aibrain.Game;
import aibrain.GameEvaluator;
import aibrain.Player;
import aibrain.Score;
import spacegame.model.Planet;
import spacegame.model.Tile;

public class SpaceGameEvaluator implements GameEvaluator{

	private static SpaceGameEvaluator instance = new SpaceGameEvaluator();
	
	public static SpaceGameEvaluator getInstance() {
		return instance;
	}
	
	//Given a game state, how good do I find this outcome?
	public Score getValue(Game game, Player empire){
		
		spacegame.model.Game castGame = (spacegame.model.Game)game;
		
		double mineralScore = Math.pow(castGame.findMatchingEmpire(empire).getMinerals()*1.0,1.0);
		double currencyScore = Math.pow(castGame.findMatchingEmpire(empire).getCurrency() * 1.0,1.0);

		double productionPotentialScore = 0;
		
		//debugging; getting pillaged isn't good
		double raidScore = 0.0;
		
		for(Tile[] row: castGame.getMap().getGrid()){
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
