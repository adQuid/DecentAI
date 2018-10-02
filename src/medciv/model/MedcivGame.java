package medciv.model;

import java.util.ArrayList;
import java.util.List;

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
	
	public MedcivGame(Boolean cloning) {

		if(!cloning) {
		Town startTown = new Town("Townsburg");
		Villager startMan = new Villager(startTown,"bilbo");
		startMan.addItems(new Cow());
		MedcivPlayer startPlayer = new MedcivPlayer();
		
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

	@Override
	public List<Action> returnActions(Player empire) {
		// TODO Auto-generated method stub
		return null;
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
		for(Villager current: people) {
			current.endRound();
		}
		//this will have to change for any competitive actions
		for(MedcivPlayer player: players) {
			for(Action action: player.getActionsThisTurn()) {
				MedcivAction castAction = (MedcivAction)action;
				castAction.getType().doAction();
			}
		}
				
		MainUI.displayPlannedActions();
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
