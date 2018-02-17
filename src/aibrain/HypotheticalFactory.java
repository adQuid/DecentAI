package aibrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import actions.Action;
import model.Empire;
import model.Game;
import model.Planet;
import model.Tile;

public class HypotheticalFactory {

	//temp
	public static boolean shouldPrint = false;
	
	HashMap<Integer,List<Hypothetical>> knownPaths = new HashMap<Integer,List<Hypothetical>>();
	
	public HypotheticalFactory(){
		for(int i=0; i<=25; i++){
			knownPaths.put(i, new ArrayList<Hypothetical>());
		}
	}
	
	public Hypothetical generateHypothetical(Game game, AIBrain parent, List<Action> actions, int ttl, Empire empire){
		game.setActionsForEmpire(actions, empire);
		
		game.endRound();
		
		for(int i = 0; i <= 25; i++){ 
			if(knownPaths.get(i) != null){
				for(Hypothetical current: knownPaths.get(i)){
					if(!gamesSignificantlyDifferent(game, current.getGame(), empire)){
						if(i==ttl){
							//System.out.println("Match found at level");
							return current;
						}else{
							//this is okay for now because this game is always positive (things always get better)
							return current;
						}
					}
				}
			}
		}
		Hypothetical retval = new Hypothetical(game, parent, actions, ttl,empire).getOptimalActions(true);
		if(retval == null){
			int x=5;
		}
		knownPaths.get(ttl).add(retval);
		//temp code
		if(shouldPrint){
			for(int i=0; i<=25; i++){
				if(knownPaths.get(i).size() > 30000){
					
				}
				if(knownPaths.get(i) != null){
					//System.out.print(knownPaths.get(i).size()+",");
				}
			}
			//System.out.println("");
		}
		return retval;
	}

	private boolean gamesSignificantlyDifferent(Game hyp1, Game hyp2, Empire empire){

		return SpaceGameEvaluator.gamesSignificantlyDifferent(hyp1, hyp2, empire);
	}
	
}
