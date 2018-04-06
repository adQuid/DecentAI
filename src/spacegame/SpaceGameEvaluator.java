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
		double retval = Math.pow(game.findMatchingEmpire(empire).getMinerals()*1.0,0.75);
		retval += Math.pow(game.findMatchingEmpire(empire).getEnergy() * 1.0,0.75);

		for(Tile[] row: game.getMap().getGrid()){
			for(Tile currentTile: row){
				if(currentTile.getObject() != null && currentTile.getObject() instanceof Planet && ((Planet)currentTile.getObject()).fetchColonyForEmpire(game.fetchCurrentEmpire()) != null){
					retval += 2 * ((Planet)currentTile.getObject()).fetchColonyForEmpire(game.fetchCurrentEmpire()).getIndustry();
				}
			}
		}

		return retval;
	}
	
	
		
	
}
