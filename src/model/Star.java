package model;

import actions.Action;

public class Star extends SpaceObject{

	String name;
	
	public Star(){
		
	}
	
	public Star(String name){
		this.name = name;
	}
	
	public Star(Star other){
		this.name = other.name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void processActions(Action action){
		
	}
	
	public void getResourceProfile(){
		
	}
	
	public void endRound(){
		
	}
}
