package aibrain;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import cloners.GameCloner;
import model.Empire;
import model.Game;
import model.Planet;
import model.Tile;

public class Hypothetical {

	private static int numMade = 0;
	
	private AIBrain parent;
	private Empire empire;
	private int ttl;
	private Game game;
	private List<Action> actions;

	//temporary
	private List<Action> fullPlan = new ArrayList<Action>();
	
	public Hypothetical(Game game, AIBrain parent, List<Action> actions, int ttl, Empire empire){
		
		this.game = GameCloner.cloneGame(game);
		this.parent = parent;
		this.actions = actions;
		this.ttl = ttl;
		this.empire = empire;
	}
	
	public Hypothetical getOptimalActions(boolean isFromFactory){		
		
		List<Hypothetical> allOptions = new ArrayList<Hypothetical>();
		Hypothetical retval = this;
		Double bestValue = null;
								
		//Check all paths where I take no further actions on this turn (vertical simulation)
		Game baseGame = GameCloner.cloneGame(game);
		Game oldGame;
		baseGame.setActionsForEmpire(actions, baseGame.findMatchingEmpire(empire));
		for(int i=ttl-1; i>=0; i--){
			oldGame = GameCloner.cloneGame(baseGame);
			baseGame.endRound();
			//actions where I wait before doing anything more (perhaps saving up cash)
			if(actionsSignificantlyDifferent(baseGame.returnActions(),(oldGame.returnActions()))){
				Hypothetical nextOp = genHypothetical(baseGame, this.parent, new ArrayList<Action>(),i, empire, isFromFactory);
				nextOp.actions = this.actions;
				allOptions.add(nextOp);
			}
		}
		Hypothetical noOp = new Hypothetical(baseGame, this.parent, actions, 0, empire);
		allOptions.add(noOp);//we don't do getOptimalActions here because this assumes we do nothing
		
		//check paths where I do act this turn (horizontal simulation)
		if(ttl > 1){
			for(Action current: game.returnActions()){
				if(Math.random() * 0.0 < ttl && actionHasEffect(game,actions,current)){//randomly ignore options later in the tree, since things are uncertain at this point
					List<Action> tempActions = new ArrayList<Action>(actions);
					tempActions.add(current);
					Hypothetical toAdd = genHypothetical(game, this.parent, tempActions,ttl, empire, isFromFactory);
					allOptions.add(toAdd);
				}else{
					//System.out.println("random skip");
				}
			}
		}
				
		for(Hypothetical current: allOptions){
			if(bestValue == null || current.getValue(current.game) > bestValue){
				retval = current;
				bestValue = current.getValue(current.game);
			}
		}
		
		retval.ttl = ttl;
		
		return retval;
	}
	
	public List<Action> getActions(){
		return actions;
	}
	
	public Game getGame(){
		return game;
	}
	
	public List<Action> getPlan(){
		return fullPlan;
	}
	
	//Given a game state, how good do I find this outcome?
	public double getValue(Game game){
		double retval = game.findMatchingEmpire(empire).getMinerals()*1.0;
		retval += game.findMatchingEmpire(empire).getEnergy() * 1.0;
		
		for(Tile[] row: game.getMap().getGrid()){
			for(Tile currentTile: row){
				if(currentTile.getObject() != null && currentTile.getObject() instanceof Planet && ((Planet)currentTile.getObject()).fetchColonyForEmpire(game.fetchCurrentEmpire()) != null){
					retval += 0.1 * ((Planet)currentTile.getObject()).fetchColonyForEmpire(game.fetchCurrentEmpire()).getIndustry();
				}
			}
		}
		
		return retval;
	}
	
	private boolean actionsSignificantlyDifferent(List<Action> list1, List<Action> list2){
		return list1.size() > list2.size();
	}
		
	private boolean actionHasEffect(Game game, List<Action> currentActions, Action newAction){
		Game noChange = GameCloner.cloneGame(game);
		Game withChange = GameCloner.cloneGame(game);
		
		List<Action> myActions = new ArrayList<Action>(currentActions);
		
		noChange.setActionsForEmpire(myActions, noChange.fetchCurrentEmpire());
		noChange.endRound();
		
		myActions.add(newAction);
		withChange.setActionsForEmpire(myActions, withChange.fetchCurrentEmpire());
		withChange.endRound();
		
		return getValue(noChange) != getValue(withChange);
	}
	
	private Hypothetical genHypothetical(Game game, AIBrain parent, List<Action> actions, int ttl, Empire empire, boolean factory){
		//System.out.println("runnig this");
		/*if(factory){
			return new Hypothetical(game, parent, actions, ttl, empire).getOptimalActions(true);
		}else{*/
			return parent.getFactory().generateHypothetical(game, parent, actions, ttl, empire);
		//}
	}
	
}
