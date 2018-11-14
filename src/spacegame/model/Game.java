package spacegame.model;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;
import aibrain.Player;
import spacegame.Empire;
import spacegame.SpaceGameCloner;
import spacegame.PowerOverload;
import spacegame.SpaceGameAction;

public class Game implements aibrain.Game{

	private Map map;
	
	//these are quick refrence lists. the map is the source of truth
	private List<Colony> allColonies = new ArrayList<Colony>();
	
	private int nextId=0;
	
	private List<Player> empires = new ArrayList<Player>();
	
	PowerOverload event = new PowerOverload(0.0);
	
	private boolean live;
	
	public Game() {		
		live = true;
		this.empires = new ArrayList<Player>();

		empires.add(new Empire("Kolosed Empire"));
		empires.add(new Empire("Torellite Imperium"));
		
		this.map = new Map(this);
		map.populateQuickRefrenceLists(allColonies);
	}

	public Game(Game other){
		for(Player current: other.empires){
			empires.add(new Empire((Empire)current));
		}
		
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
		map.populateQuickRefrenceLists(allColonies);
	}
	
	public List<Player> getPlayers() {
		return empires;
	}

	public void setEmpires(List<Player> empires) {
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
	public Empire findMatchingPlayer(Player oldEmp){
		for(Player current: empires){
			if(current.equals(oldEmp)){
				return (Empire)current;
			}
		}
		
		System.err.println("EMPIRE "+((Empire)oldEmp).getName()+" NOT FOUND!");
		return null;
	}
	
	public Player findMatchingEmpire(int oldEmp){
		return empires.get(oldEmp);
	}
	
	public Player fetchCurrentEmpire(){
		return empires.get(0);
	}
	
	
	
	public List<Action> returnActions(Player empire){
		List<Action> retval = map.returnActions(empire);
		return retval;
	}
	
	public void setActionsForPlayer(List<Action> actions, Player empire){
		Player matchingEmp = findMatchingPlayer(empire);
		matchingEmp.setActionsThisTurn(actions);
	}
	
	public void appendActionsForPlayer(List<Action> actions, Player empire){
		Player matchingEmp = findMatchingPlayer(empire);
		matchingEmp.getActionsThisTurn().addAll(actions);
	}
	
	public void endRound(){
		for(int phase = 0; phase < 10; phase++) {
			for(Player curEmpire: empires){
				for(Action curAction: curEmpire.getActionsThisTurn()){
					if(((SpaceGameAction)curAction).getOrder() == phase) {
						this.map.processActions(curAction);
					}
				}
			}
		}
		event.applyTo(this,live);
		this.map.getResourceProfile();
		this.map.endRound();
		for(Player current: empires){
			current.setActionsThisTurn(new ArrayList<Action>());
		}
	}
	
	public Game nextRound() {
		Game retval = (Game) SpaceGameCloner.getInstance().cloneGame(this);
		retval.endRound();
		return retval;
	}

	@Override
	public aibrain.Game imageForPlayer(Player player) {
		Game retval = (Game) SpaceGameCloner.getInstance().cloneGame(this);
		
		for(Player current: this.empires) {
			retval.setActionsForPlayer(new ArrayList<Action>(), current);
		}
		return retval;
	}
}
