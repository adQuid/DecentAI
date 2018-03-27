package spacegame;

import aibrain.Event;
import exceptions.IllegalActionException;
import model.Empire;
import model.Game;

public class PowerOverload extends Event{

	public PowerOverload(double probability) {
		super(probability);
	}

	@Override
	public Game applyTo(Game game, boolean live) {
		for(Empire current: game.getEmpires()) {
			if(live) {
				if(Math.random() < probability && current.getMinerals() >= 1) {
					try {
						current.addMinerals(-1);
						if(current.getName().contains("0")) {
							System.out.println("Power overload!");
						}
					} catch (IllegalActionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else {
				if(current.getMinerals() >= 1) {
					try {
						current.addMinerals(-1 * probability);
					} catch (IllegalActionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return game;
	}

}
