package medciv.model.items;

import java.util.List;

import aibrain.Action;
import medciv.aiconstructs.MedcivAction;
import medciv.model.Item;
import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.model.actions.ActionType;
import medciv.model.actions.TendAnimal;
import medciv.ui.MainUI;

public abstract class Livestock extends Item{

	private boolean tendedToThisTurn = false;
	private boolean dead = false;
	
	public Livestock(int ownerId) {
		super(ownerId);
	}
	
	public Livestock(int id, int ownerId) {
		super(id, ownerId);
	}

	public boolean willBeTendedToThisTurn(MedcivGame game) {
		for(Action current: game.getSelectedPlayer().getActionsThisTurn()) {
			MedcivAction castAction = (MedcivAction)current;
			if(castAction.getType().getClass() == TendAnimal.class &&
					((TendAnimal)castAction.getType()).getTargetId() == this.getId()) {
				return true;
			}
		}
		return false;
	}

	public void setTendedToThisTurn(boolean tendedToThisTurn) {
		this.tendedToThisTurn = tendedToThisTurn;
	}
	
	@Override
	public void endRound(MedcivGame game, Villager owner) {		
		if(tendedToThisTurn) {
			tendedToThisTurn = false;
		} else {
			dead = true;
		}
	}
	
	//kind of a cheat; needs to be manually called by all subclasses
	public void cloneHelper(Livestock cloned) {
		cloned.dead = this.dead;
		cloned.tendedToThisTurn = this.tendedToThisTurn;
	}
	
	@Override
	public boolean isDead() {
		return dead;
	}
	
	public MedcivAction tendAction(Villager villager) {
		ActionType tendType = new TendAnimal(this.getId(), villager.getId(), this.toString());			
		return new MedcivAction(tendType,villager.getId());
	}
	
}
