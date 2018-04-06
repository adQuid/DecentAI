package model;

import java.util.ArrayList;
import java.util.List;

import actions.Action;
import refdata.NameList;

public class Map {

	private Game game;
	private Tile[][] grid;
	public static final int SIZE = 11;
	
	public Map(){
		
	}
	
	public Map(Game game){
		this.game = game;
		grid = new Tile[SIZE][SIZE];
		
		for(int x=0; x<SIZE; x++){
			for(int y=0; y<SIZE; y++){
				grid[x][y] = new Tile(x,y, game);
			}
		}
	}
	
	public Map(Map other, Game game){
		grid = new Tile[SIZE][SIZE];
		
		for(int x=0; x<SIZE; x++){
			for(int y=0; y<SIZE; y++){
				grid[x][y]= new Tile(other.grid[x][y], game);
			}
		}
	}

	public Tile[][] getGrid() {
		return grid;
	}

	public void setGrid(Tile[][] grid) {
		this.grid = grid;
	}
	
	public void setGame(Game game){
		this.game = game;
		for(int x=0; x<SIZE; x++){
			for(int y=0; y<SIZE; y++){
				grid[x][y].setGame(game);
			}
		}
	}
	
	public void populateQuickRefrenceLists(List<Colony> colonies) {
		for(int x=0; x<SIZE; x++){
			for(int y=0; y<SIZE; y++){
				if(grid[x][y].getObject() instanceof Planet) {
					for(Colony current: ((Planet)grid[x][y].getObject()).getActiveColonies()) {
						colonies.add(current);
					}
				}
			}
		}
	}
	
	public List<Action> returnActions(){
		List<Action> retval = new ArrayList<Action>();
		for(int x=0; x<SIZE; x++){
			for(int y=0; y<SIZE; y++){
				retval.addAll(grid[x][y].returnActions());
			}
		}
		
		return retval;
	}
	
	public void processActions(Action action){
		for(int x=0; x<SIZE; x++){
			for(int y=0; y<SIZE; y++){
				grid[x][y].processActions(action);
			}
		}
	}
	
	public void getResourceProfile(){
		for(int x=0; x<SIZE; x++){
			for(int y=0; y<SIZE; y++){
				grid[x][y].getResourceProfile();;
			}
		}
	}
	
	public void endRound(){
		for(int x=0; x<SIZE; x++){
			for(int y=0; y<SIZE; y++){
				grid[x][y].endRound();
			}
		}
	}
}
