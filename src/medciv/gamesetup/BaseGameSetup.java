package medciv.gamesetup;

import medciv.aiconstructs.MedcivPlayer;
import medciv.model.MedcivGame;
import medciv.model.Town;
import medciv.model.Villager;
import medciv.model.items.Cow;

public class BaseGameSetup {

	public static MedcivGame newBasicGame() {
		
		MedcivGame retval = new MedcivGame(false);
		
		MedcivPlayer startPlayer = new MedcivPlayer(retval);
		Town startTown = new Town("Townsburg");
		Villager startMan = new Villager(retval,startTown,startPlayer,"bilbo");
		for(int i=0;i<1;i++) {
			startMan.addItems(new Cow(retval,startMan.getId()));
		}
		
		retval.getPeople().add(startMan);		
		retval.getTowns().add(startTown);
		retval.getPlayers().add(startPlayer);
		
		retval.setSelectedPlayer(retval.getPlayers().get(0));
		retval.setSelectedVillager(retval.getPeople().get(0));
		retval.setLive(true);
		
		return retval;
	}
	
}
