package spacegame;

import model.Colony;
import model.Empire;
import model.Game;
import model.Planet;
import model.Star;
import model.Tile;

public class SpaceGameEvaluator {

	//Given a game state, how good do I find this outcome?
	public static double getValue(Game game, Empire empire){
		double retval = Math.pow(game.findMatchingEmpire(empire).getMinerals()*1.0,1.0);
		retval += Math.pow(game.findMatchingEmpire(empire).getCurrency() * 1.0,1.0);

		for(Tile[] row: game.getMap().getGrid()){
			for(Tile currentTile: row){
				if(currentTile.getObject() != null && currentTile.getObject() instanceof Planet && ((Planet)currentTile.getObject()).fetchColonyForEmpire(game.fetchCurrentEmpire()) != null){
					retval += 3 * (Math.min(((Planet)currentTile.getObject()).fetchColonyForEmpire(game.fetchCurrentEmpire()).getIndustry(),((Planet)currentTile.getObject()).fetchColonyForEmpire(game.fetchCurrentEmpire()).getPower()));
				}
			}
		}

		return retval;
	}
	
	
		
	
}
