package model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import actions.Action;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
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
