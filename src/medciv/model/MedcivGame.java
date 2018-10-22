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

		if(!cloning) {
		MedcivPlayer startPlayer = new MedcivPlayer();
		Town startTown = new Town("Townsburg");
		Villager startMan = new Villager(startTown,startPlayer,"bilbo");
		for(int i=0;i<7;i++) {
			startMan.addItems(new Cow());
		}
		
		people.add(startMan);		
		towns.add(startTown);
		players.add(startPlayer);
		
		selectedPlayer = players.get(0);
		selectedVillager = people.get(0);
		live = true;
		}
	}
	
	public MedcivGame clone() {
		MedcivGame retval = new MedcivGame(false);
		
		retval.live = false;
		for(Town current: towns) {
			retval.towns.add(current.clone());
		}
		
		for(Villager current: people) {
			retval.people.add(current.clone(this));
		}
		
		return retval;
	}
	
	public List<Villager> getPeople() {
		return people;
	}
	
	public MedcivPlayer getSelectedPlayer() {
		return selectedPlayer;
	}
	
	public Villager getSelectedVillager() {
		return selectedVillager;
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
	public void addActionForCurrentPlayer(MedcivAction action) {
		selectedPlayer.addActionThisTurn(action);
		MainUI.displayPlannedActions();
	}
	
	@Override
	public boolean isLive() {
		return live;
	}

	@Override
	public List<Player> getEmpires() {
		return new ArrayList<Player>(players);
	}

	@Override
	public void setLive(boolean live) {
		// TODO Auto-generated method stub
		
	}

	public List<Action> returnActions(Player empire) {
		List<Action> retval = new ArrayList<Action>();
		
		//this is going to get weird if there end up being multiple villagers per player. Not sure if I'm gonna do that though
		for(Villager current: people) {
			if(current.getOwner().equals(empire)) {
				retval.addAll(current.returnActions());
			}
		}
		
		return retval;
	}

	@Override
	public void setActionsForPlayer(List<Action> actions, Player empire) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendActionsForPlayer(List<Action> actions, Player empire) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound() {
		//this will have to change for any competitive actions
		for(MedcivPlayer player: players) {
			for(Action action: player.getActionsThisTurn()) {
				MedcivAction castAction = (MedcivAction)action;
				castAction.getType().doAction(this);
			}
		}
				
		for(Villager current: people) {
			current.endRound();
		}
		
		MainUI.refresh();
	}

	@Override
	public Game nextRound() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Game imageForPlayer(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
