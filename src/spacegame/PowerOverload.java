package spacegame;

import aibrain.AIBrain;
import spacegame.model.Colony;
import spacegame.model.Game;
import ui.Mainmenu;

public class PowerOverload{

	private double probability;
	
	public PowerOverload(double probability) {
		this.probability = probability;
	}

	public Game applyTo(Game game, boolean live) {
		for(Colony current: game.fetchAllColonies()) {
			if(live) {
				if(Math.random() < probability && current.getPower() > 0) {
					if(current.getPower() < 3) {
					current.setPower(current.getPower()-1);
					
					//super dangerous and only for debug
					if(Mainmenu.liveGame != null) {
						for(AIBrain brain: Mainmenu.brains) {
							brain.addLog("Power overload!");
						}
						System.out.println("power overload for "+((Empire)current.getOwner()).getName());
					}
						
					}else {
						//super dangerous and only for debug
						if(Mainmenu.liveGame != null) {
							for(AIBrain brain: Mainmenu.brains) {
								brain.addLog("Power overload blocked!");
							}
						}
					}
				}
			}else {
				if(current.getPower() > 0 && current.getPower() < 3) {
					current.setPower(current.getPower()-(1 * probability));
				}
			}
		}
		
		return game;
	}

}
