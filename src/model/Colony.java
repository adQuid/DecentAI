package model;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import actions.ActionType;
import exceptions.IllegalActionException;
import refdata.NameList;

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
	
	public List<Action> returnActions(){
		List<Action> retval = new ArrayList<Action>();
		
		Empire fetchedEmpire = this.game.fetchCurrentEmpire();
		
		if(fetchedEmpire.equals(this.getOwner())){
			List<Object> params = new ArrayList<Object>();
			params.add(this);
			if(fetchedEmpire.getMinerals() >= 3 + (1*industry)){
				retval.add(new Action(ActionType.develop,params));
			}
			if(fetchedEmpire.getMinerals() >= 12 + (1*industry)){
				retval.add(new Action(ActionType.develop2,params));
			}
			if(fetchedEmpire.getMinerals() >= 3){
				retval.add(new Action(ActionType.developPower,params));
			}
			if(fetchedEmpire.getMinerals() >= 5){
				retval.add(new Action(ActionType.cashNow,params));
			}
		}
		return retval;
	}
	
	public void processActions(Action current) {
		Empire fetchedOwner = this.game.findMatchingEmpire(owner);		

		if(current.getType() == ActionType.develop&&
				((Colony)(current.getParams().get(0))).equals(this)){
			try{
				fetchedOwner.addMinerals(-3 - (1*industry));
				this.industry++;
			}catch(IllegalActionException e){
				//Do nothing, you just can't afford this
			}
		}
		if(current.getType() == ActionType.develop2&&
				((Colony)(current.getParams().get(0))).equals(this)){
			try{
				fetchedOwner.addMinerals(-12 - (1*industry));
				this.industry+=7;
			}catch(IllegalActionException e){
				//Do nothing, you just can't afford this
			}
		}
		if(current.getType() == ActionType.developPower&&
				((Colony)(current.getParams().get(0))).equals(this)){
			try{
				fetchedOwner.addMinerals(-3);
				this.power+=1;
			}catch(IllegalActionException e){
				//Do nothing, you just can't afford this
			}
		}
		if(current.getType() == ActionType.cashNow&&
				((Colony)(current.getParams().get(0))).equals(this)){
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
