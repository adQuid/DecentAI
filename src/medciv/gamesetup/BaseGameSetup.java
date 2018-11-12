package medciv.gamesetup;

import medciv.aiconstructs.MedcivPlayer;
import medciv.model.MedcivGame;
import medciv.model.Town;
import medciv.model.Villager;
import medciv.model.items.Chicken;
import medciv.model.items.Cow;
import medciv.model.items.Eggs;
import medciv.model.items.Milk;
import medciv.ui.MainUI;

public class BaseGameSetup {

	public static MedcivGame newBasicGame() {
		
		MedcivGame retval = new MedcivGame(false);
		
		MedcivPlayer startPlayer = new MedcivPlayer(retval);
		MedcivPlayer startPlayer2 = new MedcivPlayer(retval);
		Town startTown = new Town("Townsburg");
		
		MainUI.setFocusTown(startTown);
		
		Villager playerGuy = new Villager(retval,startTown.getId(),startPlayer,"bilbo");
		Villager botMan = new Villager(retval,startTown.getId(),startPlayer2,"bobbo");
		
		playerGuy.addItem(new Cow(playerGuy.getId()));
		playerGuy.addItem(new Cow(playerGuy.getId()));
		playerGuy.addItem(new Cow(playerGuy.getId()));
		playerGuy.addItem(new Chicken(playerGuy.getId()));
		playerGuy.addItem(new Eggs(4, 1, playerGuy.getId()));
		playerGuy.addItem(new Eggs(4, 3, playerGuy.getId()));
		playerGuy.addItem(new Milk(3, 1, playerGuy.getId()));
		
		botMan.addItem(new Chicken(botMan.getId()));
		botMan.addItem(new Chicken(botMan.getId()));
		botMan.addItem(new Chicken(botMan.getId()));
		botMan.addItem(new Chicken(botMan.getId()));
		botMan.addItem(new Eggs(2, 1, botMan.getId()));
		//botMan.addItem(new Eggs(4, 3, botMan.getId()));
				
		retval.getPeople().add(playerGuy);	
		retval.getPeople().add(botMan);
		retval.getTowns().add(startTown);
		retval.getPlayers().add(startPlayer);
		retval.getPlayers().add(startPlayer2);
		
		retval.setSelectedPlayer(retval.getPlayers().get(0));
		retval.setSelectedVillager(retval.getPeople().get(0));
		retval.setLive(true);
		
		return retval;
	}
	
}
