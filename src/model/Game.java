package model;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import aibrain.Event;
import cloners.GameCloner;
import spacegame.PowerOverload;

public class Game implements aibrain.Game{

	private Map map;
	
	//these are quick refrence lists. the map is the source of truth
	private List<Colony> allColonies = new ArrayList<Colony>();
	
	private int nextId=0;
	
	private List<Empire> empires = new ArrayList<Empire>();
	
	private List<Event> events = new ArrayList<Event>();
	
	private boolean live;
	
	public Game() {		
		live = true;
		this.empires = new ArrayList<Empire>();
		for(int i=0; i<10; i++){
			empires.add(new Empire("test empire "+i));
		}
		
		events.add(new PowerOverload(0.3));
		
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
	}
	
	public List<Empire> getEmpires() {
		return empires;
	}

	public void setEmpires(List<Empire> empires) {
		this.empires = empires;
	}
	
	public List<Event> getEvents() {
		return events;
	}
	
	public void setEvents(List<Event> events) {
		this.events = events;
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
	
	
	
	public List<Action> returnActions(){
		List<Action> retval = map.returnActions();
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
		for(Event current: events) {
			current.applyTo(this,live);
		}
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
