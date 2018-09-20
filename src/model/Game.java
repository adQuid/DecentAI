package model;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import cloners.GameCloner;
import spacegame.PowerOverload;

public class Game implements aibrain.Game{

	private Map map;
	
	//these are quick refrence lists. the map is the source of truth
	private List<Colony> allColonies = new ArrayList<Colony>();
	
	private int nextId=0;
	
	private List<Empire> empires = new ArrayList<Empire>();
	
	PowerOverload event = new PowerOverload(0.3);
	
	private boolean live;
	
	public Game() {		
		live = true;
		this.empires = new ArrayList<Empire>();

		empires.add(new Empire("Kolosed Empire"));
		empires.add(new Empire("Torellite Imperium"));
		
		this.map = new Map(this);
		map.populateQuickRefrenceLists(allColonies);
	}

	public Game(Game other){
		for(Empire current: other.empires){
			empires.add(new Empire(current));
		}
		
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
		map.populateQuickRefrenceLists(allColonies);
	}
	
	public List<Empire> getEmpires() {
		return empires;
	}

	public void setEmpires(List<Empire> empires) {
		this.empires = empires;
	}
		
	public boolean isLive() {
		return live;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public List<Colony> fetchAllColonies(){
		return this.allColonies;
	}
	
	public void setGame(){
		this.map.setGame(this);
	}
	
	public int getNextId() {
		return nextId;
	}

	public int generateID(){
		return nextId++;
	}
	
	public void setNextId(int nextId) {
		this.nextId = nextId;
	}

	//used for deep copies
	public Empire findMatchingEmpire(Empire oldEmp){
		for(Empire current: empires){
			if(current.equals(oldEmp)){
				return current;
			}
		}
		
		System.err.println("EMPIRE "+oldEmp.getName()+" NOT FOUND!");
		return null;
	}
	
	public Empire findMatchingEmpire(int oldEmp){
		return empires.get(oldEmp);
	}
	
	public Empire fetchCurrentEmpire(){
		return empires.get(0);
	}
	
	
	
	public List<Action> returnActions(Empire empire){
		List<Action> retval = map.returnActions(empire);
		return retval;
	}
	
	public void setActionsForEmpire(List<Action> actions, Empire empire){
		Empire matchingEmp = findMatchingEmpire(empire);
		matchingEmp.setActionsThisTurn(actions);
	}
	
	public void endRound(){
		for(Empire curEmpire: empires){
			for(Action curAction: curEmpire.getActionsThisTurn()){
				this.map.processActions(curAction);
			}
		}
		event.applyTo(this,live);
		this.map.getResourceProfile();
		this.map.endRound();
		for(Empire current: empires){
			current.setActionsThisTurn(new ArrayList<Action>());
		}
	}
	
	public Game nextRound() {
		Game retval = (Game) GameCloner.cloneGame(this);
		retval.endRound();
		return retval;
	}
}
