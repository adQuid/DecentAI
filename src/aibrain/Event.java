package aibrain;

import model.Game;

public abstract class Event {

	protected double probability;
	
	public Event(double probability) {
		this.probability = probability;
	}
	
	/*have the event effect the game. If live is true, this will roll to decide if the event happens
	If it is false, it will apply that portion of the expected value*/
	public abstract Game applyTo(Game game, boolean live);

	public double getProbability() {
		return probability;
	}
	
}
