package aibrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import actions.Action;
import model.Empire;
import model.Game;
import model.Planet;
import model.Tile;

public class HypotheticalFactory {

	HashMap<Integer,HashMap<List<Action>,List<Hypothetical>>> knownPaths = new HashMap<Integer,HashMap<List<Action>,List<Hypothetical>>>();
	
	public HypotheticalFactory(){
		for(int i=0; i<=25; i++){
			knownPaths.put(i, new HashMap<List<Action>,List<Hypothetical>>());
		}
	}
	
	public Hypothetical generateHypothetical(Game game, AIBrain parent, List<Action> actions, int ttl, Empire empire){
		for(int i = 0; i <= 25; i++){ 
			if(knownPaths.get(i).get(actions) == null){
				knownPaths.get(i).put(actions, new ArrayList<Hypothetical>());
			}
			for(Hypothetical current: knownPaths.get(i).get(actions)){
				if(!gamesSignificantlyDifferent(game, current.getGame(), empire)){
					if(i==ttl){
						//System.out.println("Match found at level");
						return current;
					}else{
						//this is okay for now because this game is always positive
						return current;
					}
				}
			}
		}
		Hypothetical retval = new Hypothetical(game, parent, actions, ttl,empire).getOptimalActions(true);
		knownPaths.get(ttl).get(actions).add(retval);
		return retval;
	}

	private boolean gamesSignificantlyDifferent(Game hyp1, Game hyp2, Empire empire){

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
		for(Tile[] current: hyp1.getMap().getGrid()){
			for(Tile tile: current){
				if(tile.getObject() != null && tile.getObject() instanceof Planet){
					industry1 += ((Planet)tile.getObject()).fetchColonyForEmpire(empire).getIndustry();
				}
			}
		}
		int industry2 = 0;
		for(Tile[] current: hyp2.getMap().getGrid()){
			for(Tile tile: current){
				if(tile.getObject() != null && tile.getObject() instanceof Planet){
					industry2 += ((Planet)tile.getObject()).fetchColonyForEmpire(empire).getIndustry();
				}
			}
		}
		
		if(Math.abs(industry2-industry1) > 0){
			return true;
		}
		
		return false;
	}
	
}
