package aibrain;

import model.Colony;
import model.Empire;
import model.Game;
import model.Planet;
import model.Star;
import model.Tile;

public class SpaceGameEvaluator {

	public static boolean gamesSignificantlyDifferent(Game hyp1, Game hyp2, Empire empire){

		if(hyp1.findMatchingEmpire(empire).getActionsThisTurn().size() != hyp2.findMatchingEmpire(empire).getActionsThisTurn().size()){
			return true;
		}
		
		if(Math.abs(hyp1.findMatchingEmpire(empire).getMinerals() - hyp2.findMatchingEmpire(empire).getMinerals()) > 2){
			return true;
		}
		
		if(Math.abs(hyp1.findMatchingEmpire(empire).getEnergy() - hyp2.findMatchingEmpire(empire).getEnergy()) > 2){
			return true;
		}
		
		int industry1 = 0;
		for(int x=0; x < hyp1.getMap().getGrid().length; x++){
			for(int y=0; y < hyp1.getMap().getGrid().length; y++){
				Tile tile1 = hyp1.getMap().getGrid()[x][y];
				Tile tile2 = hyp2.getMap().getGrid()[x][y];
				
				if(compareTiles(tile1,tile2,empire)){
					return true;
				}
			}
		}
		
		int industry2 = 0;
		for(Tile[] current: hyp2.getMap().getGrid()){
			for(Tile tile: current){
				if(tile.getObject() != null && tile.getObject() instanceof Planet
						&&((Planet)tile.getObject()).fetchColonyForEmpire(empire) != null){
					industry2 += ((Planet)tile.getObject()).fetchColonyForEmpire(empire).getIndustry();
				}
			}
		}
				
		return false;
	}

	private static boolean compareTiles(Tile tile1, Tile tile2, Empire empire){
		
		if(tile1.getObject() == null){
			return tile2.getObject() != null;
		}
		if(tile1.getObject().getClass() == Star.class){
			return tile2.getObject().getClass() != Star.class;
		}
		if(tile1.getObject().getClass() == Planet.class){
			if(tile2.getObject().getClass() != Planet.class){
				//System.out.println("flag1");
				return true;
			}
			Planet planet1 = (Planet)tile1.getObject();
			Planet planet2 = (Planet)tile2.getObject();
			
			Colony col1 = planet1.fetchColonyForEmpire(empire);
			Colony col2 = planet2.fetchColonyForEmpire(empire);
			
			if((col1 == null) != (col2 == null)){
				//System.out.println("flag2");
				return true;
			}
			
			if(col1.getIndustry() != col2.getIndustry() ||
				col1.getPower() != col2.getPower()){
				//System.out.println("flag3");
				return true;
			}
		}
		
		
		return false;
	}
		
	
}
