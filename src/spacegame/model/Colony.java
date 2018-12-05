package spacegame.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aibrain.Action;
import aibrain.Player;
import exceptions.IllegalActionException;
import refdata.NameList;
import spacegame.Empire;
import spacegame.SpaceGameAction;

public class Colony {

	Game game;
	String name;
	Player owner;
	int population;
	int defense;
	double industry;
	double power;
	int id;
	
	public Colony(){
		
	}
	
	public Colony(Player owner, Game game){
		this.game = game;
		this.name = NameList.randomColonyName();
		this.owner = owner;
		this.id = game.generateID();
		population = 0;
		industry = 0;
		power = 0;
		defense = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Colony other = (Colony) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public Colony(Colony other, Game game){
		this.game = game;
		this.name = other.name;
		this.owner = new Empire(game.findMatchingPlayer(other.owner));
		this.id = other.id;
		this.population = other.population;
		this.industry = other.industry;
		this.power = other.power;
		this.defense = other.defense;
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public double getIndustry() {
		return industry;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}
		
	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGame(Game game){
		this.game = game;
	}
	
	public int getDefense() {
		return defense;
	}
	
	public int getId() {
		return id;
	}
	
	public String toString() {
		return getName();
	}
	
	public List<Action> returnActions(Player player){
		Empire empire = game.findMatchingPlayer(player);
		
		List<Action> retval = new ArrayList<Action>();
		
		if(empire.equals(this.getOwner())){
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("colony", this);
			if(empire.getMinerals() >= 2 + (1*industry)){
				retval.add(new SpaceGameAction(ActionType.develop,params));
			}
			if(empire.getMinerals() >= 12 + (1*industry)){
				retval.add(new SpaceGameAction(ActionType.develop2,params));
			}
			if(empire.getMinerals() >= 2){
				retval.add(new SpaceGameAction(ActionType.developPower,params));
			}
			if(empire.getMinerals() >= 5){
				retval.add(new SpaceGameAction(ActionType.cashNow,params));
			}
			if(empire.getMinerals() >= 3) {
				retval.add(new SpaceGameAction(ActionType.defend,params));
			}
			if(empire.getMinerals() >= 5) {
				for(Colony current: game.fetchAllColonies()) {
					if(!current.getOwner().equals(this.owner)) {
						Map<String,Object> attackParams = new HashMap<String,Object>();
						attackParams.put("from", this);
						attackParams.put("target", current);
						
						retval.add(new SpaceGameAction(ActionType.pillage,attackParams));
					}
				}
			}
		}
		return retval;
	}
	
	public void processActions(Action current) {
		Empire fetchedOwner = this.game.findMatchingPlayer(owner);		

		//it BETTER be this type of action...
		SpaceGameAction action = (SpaceGameAction)current;
		
		if(action.getType() == ActionType.develop&&
				((Colony)(action.getParams().get("colony"))).equals(this)){
			try{
				fetchedOwner.addMinerals(-2 - (1*industry));
				this.industry++;
			}catch(IllegalActionException e){
				//Do nothing, you just can't afford this
			}
		}
		if(action.getType() == ActionType.develop2&&
				((Colony)(action.getParams().get("colony"))).equals(this)){
			try{
				fetchedOwner.addMinerals(-12 - (1*industry));
				this.industry+=7;
			}catch(IllegalActionException e){
				//Do nothing, you just can't afford this
			}
		}
		if(action.getType() == ActionType.developPower&&
				((Colony)(action.getParams().get("colony"))).equals(this)){
			try{
				fetchedOwner.addMinerals(-2);
				this.power+=1;
			}catch(IllegalActionException e){
				//Do nothing, you just can't afford this
			}
		}
		if(action.getType() == ActionType.cashNow&&
				((Colony)(action.getParams().get("colony"))).equals(this)){
			try{
				fetchedOwner.addMinerals(-5);
				fetchedOwner.addEnergy(6);
			}catch(IllegalActionException e){
				//Do nothing, you just can't afford this
			}
		}
		if(action.getType() == ActionType.defend&&
				((Colony)(action.getParams().get("colony"))).equals(this)){
			try{
				fetchedOwner.addMinerals(-3);
				this.defense++;
			}catch(IllegalActionException e){
				//Do nothing, you just can't afford this
			}
		}
		if(action.getType() == ActionType.pillage&&
				((Colony)(action.getParams().get("from"))).equals(this)){
			try{
				Colony target = null;
				
				for(Colony col: game.fetchAllColonies()) {
					if(col.equals(action.getParam("target"))) {
						target = col;
					}
				}
				
				if(target == null) {
					throw new IllegalActionException();
				}
				
				//debug
				if(game.isLive() && !action.isContingency()) {
					System.out.println(((Empire)owner).getName()+" attacked "+action.getParam("target").toString());
				}
				
				fetchedOwner.addMinerals(-5);
				if(target.defense < 1) {
					fetchedOwner.addEnergy(target.industry*2);
					target.industry = Math.max(0, target.industry - 5);
				}else{
					if(game.isLive()) {
						System.out.println("...but it was defended!");
					}
				}
			}catch(IllegalActionException e){
				//debug
				if(game.isLive()) {
					System.out.println("...but it failed");
				}
				
				//Do nothing, you just can't afford this
			}
		}
		
	}
	
	public void getResourceProfile() {
		
	}
	
	public void endRound() {
		Empire fetchedOwner = (Empire)this.game.findMatchingPlayer(owner);
		try{
			fetchedOwner.addMinerals(Math.max(Math.min(this.industry,this.power),0));
		}catch(IllegalActionException e){
			System.out.println("COLONY FOR "+fetchedOwner.getName()+" has negative industry!");
		}
	}
	
}
