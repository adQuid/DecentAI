package spacegame;

import exceptions.IllegalActionException;
import model.Colony;
import model.Empire;
import model.Game;

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
					if(current.getOwner().getName().contains("0")) {
						System.out.println("Power overload!");
					}
					}else {
						if(current.getOwner().getName().contains("0")) {
							System.out.println("Power overload blocked!");
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
