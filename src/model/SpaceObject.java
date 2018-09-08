package model;

import java.util.ArrayList;
import java.util.List;

import actions.Action;

public abstract class SpaceObject {

	public abstract String getName();
	public List<Action> returnActions(){
		return new ArrayList<Action>();
	}
	
	public void setGame(Game game){
		
	}
	
	public abstract void processActions(Action current);
	
	public abstract void getResourceProfile();
	
	public abstract void endRound();
}
