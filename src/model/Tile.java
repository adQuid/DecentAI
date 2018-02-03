package model;

import java.util.ArrayList;
import java.util.List;

import actions.Action;

public class Tile {

	private Game game;
	private SpaceObject object =  null;
	private int x;
	private int y;
	
	public Tile(){
		
	}
	
	public Tile(int x, int y, Game game){
		this.game = game;
		this.x = x;
		this.y = y;
		
		if(x==5&&y==5){
			//I am in the center of the solar system
			this.object = new Star("unnamed sun");
		}else if(x==5&&(y==8||y==9)){
			this.object = new Planet(this, game);
			((Planet)this.object).startColony(game.fetchCurrentEmpire());
		}else if(Math.random() < 0.20){
			//this.object = new Planet(this, game);
		}
	}
	
	public Tile(Tile other, Game game){
		this.x = other.x;
		this.y = other.y;
		if(other.object instanceof Planet){
			this.object = new Planet((Planet)other.object, game);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public SpaceObject getObject() {
		return object;
	}

	public void setObject(SpaceObject planet) {
		this.object = planet;
	}
	
	public void setGame(Game game){
		this.game = game;
		if(object != null){
			this.object.setGame(game);
		}
	}
	
	public List<Action> returnActions(){
		if(this.object != null){
			return this.object.returnActions();
		}else{
			return new ArrayList<Action>();
		}
	}
	
	public void processActions(Action action) {
		if(this.object != null){
			this.object.processActions(action);
		}
	}
	
	public void getResourceProfile() {
		if(this.object != null){
			this.object.getResourceProfile();
		}
	}
	
	public void endRound(){
		if(this.object != null){
			this.object.endRound();
		}
	}
}
