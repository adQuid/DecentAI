package medciv.model.items;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import aibrain.Action;
import medciv.actionlisteners.MilkAnimalListener;
import medciv.actionlisteners.TendAnimalListener;
import medciv.aiconstructs.MedcivAction;
import medciv.model.Item;
import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.model.actions.ActionType;
import medciv.model.actions.MilkAnimal;
import medciv.model.actions.TendAnimal;
import medciv.ui.MainUI;

public class Cow extends Item{
	
	private boolean wasMilkedThisTurn = false;
	private boolean tendedToThisTurn = false;
	private boolean dead = false;
	
	public Cow(MedcivGame game, int ownerId) {
		super(game, ownerId);
	}
	
	public Cow(MedcivGame game, int id, int ownerId) {
		super(game, id, ownerId);
	}
	
	public boolean willBeMilkedThisTurn() {
		for(Action current: MainUI.liveGame.getSelectedPlayer().getActionsThisTurn()) {
			MedcivAction castAction = (MedcivAction)current;
			if(castAction.getType().getClass() == MilkAnimal.class &&
					((MilkAnimal)castAction.getType()).getId() == this.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean willBeTendedThisTurn() {
		for(Action current: MainUI.liveGame.getSelectedPlayer().getActionsThisTurn()) {
			MedcivAction castAction = (MedcivAction)current;
			if(castAction.getType().getClass() == TendAnimal.class &&
					((TendAnimal)castAction.getType()).getTargetId() == this.getId()) {
				return true;
			}
		}
		return false;
	}

	public boolean isWasMilkedThisTurn() {
		return wasMilkedThisTurn;
	}

	public void setWasMilkedThisTurn(boolean wasMilkedThisTurn) {
		this.wasMilkedThisTurn = wasMilkedThisTurn;
	}
	
	public boolean isTendedToThisTurn() {
		return tendedToThisTurn;
	}

	public void setTendedToThisTurn(boolean tendedToThisTurn) {
		this.tendedToThisTurn = tendedToThisTurn;
	}

	@Override
	public void endRound(Villager owner) {
		wasMilkedThisTurn = false;		
		
		if(tendedToThisTurn) {
			tendedToThisTurn = false;
		} else {
			dead = true;
		}
	}

	@Override
	public Item clone(MedcivGame game) {
		Cow retval = new Cow(game, getId(), ownerId);
		retval.dead = this.dead;
		retval.tendedToThisTurn = this.tendedToThisTurn;
		retval.wasMilkedThisTurn = this.wasMilkedThisTurn;
		return retval;
	}
	
	public String toString() {
		return "Cow";
	}

	@Override
	public String description() {
		return "A Cow. Seems healthy.";
	}

	@Override
	public void focusOnItem() {
		
		String milkText = willBeMilkedThisTurn()?"Already milked this turn":"Milk";
		JButton milkButton = new JButton(milkText);
		milkButton.addActionListener(new MilkAnimalListener(this));
		
		String tendText = willBeTendedThisTurn()?"Already tended to this turn":"Tend to Animal";
		JButton tendButton = new JButton(tendText);
		tendButton.addActionListener(new TendAnimalListener(this));
		
		MainUI.setFocusItem(this);
		MainUI.addOptions("Cow", tendButton, milkButton);
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public List<Action> getAssociatedActions(Villager villager) {
		List<Action> retval = new ArrayList<Action>();
		
		retval.add(milkAction(villager));
		retval.add(tendAction(villager));
		
		return retval;
	}

	public MedcivAction milkAction(Villager villager) {
		ActionType milkType = new MilkAnimal(this.getId(), ownerId);			
		return new MedcivAction(milkType,villager.getId());
	}
	
	public MedcivAction tendAction(Villager villager) {
		ActionType tendType = new TendAnimal(this.getId(), ownerId);			
		return new MedcivAction(tendType,villager.getId());
	}
}
