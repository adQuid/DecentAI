package medciv.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aibrain.Action;
import aibrain.Game;
import aibrain.Player;
import medciv.aiconstructs.MedcivAction;
import medciv.aiconstructs.MedcivPlayer;
import medciv.model.items.Cow;
import medciv.ui.MainUI;

public class MedcivGame implements Game{

	private boolean live;
	private List<Town> towns = new ArrayList<Town>();
	private List<Villager> people = new ArrayList<Villager>();
	private List<MedcivPlayer> players = new ArrayList<MedcivPlayer>();

	//maybe delete one of these...
	private MedcivPlayer selectedPlayer;
	private Villager selectedVillager;	
	
	private Random random = new Random();
	
	public MedcivGame(Boolean cloning) {


	}
	
	public MedcivGame clone() {
		MedcivGame retval = new MedcivGame(false);
		
		retval.live = false;
		for(Town current: towns) {
			retval.towns.add(current.clone(this));
		}
		
		for(Villager current: people) {
			retval.people.add(current.clone(this));
		}
		
		for(MedcivPlayer current: players) {
			retval.players.add(current.clone(this));
		}
		
		return retval;
	}
	
	public List<Town> getTowns() {
		return towns;
	}
	
	public List<Villager> getPeople() {
		return people;
	}
	
	public List<MedcivPlayer> getMedcivPlayers() {
		return players;
	}
	
	public MedcivPlayer getSelectedPlayer() {
		return selectedPlayer;
	}
	
	public void setSelectedPlayer(MedcivPlayer player) {
		this.selectedPlayer = player;
	}
	
	public Villager getSelectedVillager() {
		return selectedVillager;
	}
	
	public void setSelectedVillager(Villager villager) {
		this.selectedVillager = villager;
	}
	
	//I'm assuming a one-to-one relationship
	public Villager firstVillagerOwnedByPlayer(MedcivPlayer player) {
		for(Villager current: people) {
			if(current.getOwner().equals(player)) {
				return current;
			}
		}
		
		return null;
	}
	
	public Villager matchingVillager(Villager other) {
		for(Villager current: people) {
			if(current.equals(other)) {
				return current;
			}
		}
		return null;
	}
	
	public Villager matchingVillager(int id) {
		for(Villager current: people) {
			if(current.getId() == id) {
				return current;
			}
		}
		return null;
	}
	
	public Random getRandom() {
		return random;
	}
	
	public MedcivPlayer findMatchingPlayer(MedcivPlayer player) {
		for(MedcivPlayer current: players) {
			if(current.matches(player)) {
				return current;
			}
		}
		return null;
	}
	
	//used by UI
	public boolean addActionForCurrentPlayer(MedcivAction action) {
		return addActionForPlayer(action, selectedPlayer);
	}
	
	public boolean addActionForPlayer(MedcivAction action, MedcivPlayer player) {
		Villager villager = matchingVillager(action.getVillagerId());
		if(villager.getOwner().equals(player) && villager.canAffordAction(action)) {
			player.addActionThisTurn(action);
			MainUI.displayPlannedActions();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isLive() {
		return live;
	}

	public List<Player> getPlayers() {
		return new ArrayList<Player>(players);
	}

	public void addPlayer(MedcivPlayer player) {
		this.players.add(player);
	}
	
	@Override
	public void setLive(boolean live) {
		this.live = live;
	}

	@Override
	public void setActionsForPlayer(List<Action> actions, Player empire) {
		for(MedcivPlayer current: players) {
			if(empire.equals(current)) {
				current.setActionsThisTurn(actions);
			}
		}
	}

	@Override
	public void appendActionsForPlayer(List<Action> actions, Player empire) {
		for(MedcivPlayer current: players) {
			if(empire.equals(current)) {
				current.addActionThisTurn(actions);
			}
		}
	}

	@Override
	public void endRound() {
		//this will have to change for any competitive actions
		for(int init = 100; init >= 0; init--) {
			for(MedcivPlayer player: players) {
				for(Action action: player.getActionsThisTurn()) {
					MedcivAction castAction = (MedcivAction)action;
					if(castAction.getInitiative() == init &&
							matchingVillager(castAction.getVillagerId()).timeLeft(this) > 0) {
						castAction.getType().doAction(this);
					}else {
						//do nothing: you just can't afford this
					}
				}
			}
		}
		
		for(Villager current: people) {
			current.endRound(this);
		}
		//clear actions
		for(MedcivPlayer player: players) {
			player.setActionsThisTurn(new ArrayList<Action>());
		}
	}

	@Override
	public Game nextRound() {
		MedcivGame retval = this.clone();
		retval.endRound();
		return retval;
	}

	@Override
	public Game imageForPlayer(Player player) {
		return this.clone();
	}

	
	
}
