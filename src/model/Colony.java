package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import actions.Action;
import exceptions.IllegalActionException;
import refdata.NameList;
import spacegame.SpaceGameAction;

public class Colony {

	Game game;
	String name;
	Empire owner;
	int population;
	double industry;
	double power;
	int id;
	
	public Colony(){
		
	}
	
	public Colony(Empire owner, Game game){
		this.game = game;
		this.name = NameList.randomColonyName();
		this.owner = owner;
		this.id = game.generateID();
		population = 0;
		industry = 0;
		power = 0;
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
		this.owner = new Empire(game.findMatchingEmpire(other.owner));
		this.id = other.id;
		this.population = other.population;
		this.industry = other.industry;
		this.power = other.power;
	}

	public Empire getOwner() {
		return owner;
	}

	public void setOwner(Empire owner) {
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
	
	public String toString() {
		return getName();
	}
	
	public List<Action> returnActions(Empire empire){
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
		}
		return retval;
	}
	
	public void processActions(Action current) {
		Empire fetchedOwner = this.game.findMatchingEmpire(owner);		

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
		
	}
	
	public void getResourceProfile() {
		
	}
	
	public void endRound() {
		Empire fetchedOwner = this.game.findMatchingEmpire(owner);
		try{
			fetchedOwner.addMinerals(Math.max(Math.min(this.industry,this.power),0));
		}catch(IllegalActionException e){
			System.out.println("COLONY FOR "+fetchedOwner.getName()+" has negative industry!");
		}
	}
	
}
