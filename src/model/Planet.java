package model;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import refdata.NameList;

public class Planet extends SpaceObject{

	private Game game;
	private String name = "unnamed planet";
	private List<Colony> activeColonies;
		
	public Planet(){
		
	}
	
	public Planet(Tile tile, Game game){
		this.game = game;
		name = NameList.generatePlanetName(tile);
		activeColonies = new ArrayList<Colony>();
		
	}
	
	public Planet(Planet other, Game game){
		this.name = other.name;
		this.game = game;
		this.activeColonies = new ArrayList<Colony>();
	
		for(Colony current: other.activeColonies){
			this.activeColonies.add(new Colony(current, game));
		}
	}

	public void startColony(Empire founder){
		activeColonies.add(new Colony(founder, game));
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setGame(Game game){
		this.game = game;
		
		for(Colony current: this.activeColonies){
			current.setGame(game);
		}
	}

	public List<Colony> getActiveColonies() {
		return activeColonies;
	}

	public Colony fetchColonyForEmpire(Empire empire){
		for(Colony current: activeColonies){
			if(current.getOwner().equals(empire)){
				return current;
			}
		}
		return null;
	}
	
	public void setActiveColonies(List<Colony> activeColonies) {
		this.activeColonies = activeColonies;
	}
	
	@Override
	public List<Action> returnActions(Empire empire){
		List<Action> retval = new ArrayList<Action>();
		for(Colony current: activeColonies){
			return current.returnActions(empire);
		}
		return retval;
	}
	
	public void processActions(Action action) {
		for(Colony current: activeColonies){
			current.processActions(action);
		}	
	}
	
	public void getResourceProfile() {
		
	}
	
	public void endRound(){
		for(Colony current: activeColonies){
			current.endRound();
		}
	}
}
