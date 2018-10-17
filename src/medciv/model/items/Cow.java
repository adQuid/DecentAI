package medciv.model.items;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import aibrain.Action;
import medciv.actionlisteners.MilkAnimalListener;
import medciv.actionlisteners.TendAnimalListener;
import medciv.aiconstructs.ActionType;
import medciv.aiconstructs.MedcivAction;
import medciv.model.Item;
import medciv.model.Villager;
import medciv.model.actions.MilkAnimal;
import medciv.model.actions.TendAnimal;
import medciv.ui.MainUI;

public class Cow implements Item{
	
	private boolean wasMilkedThisTurn = false;
	private boolean tendedToThisTurn = false;
	private boolean dead = false;
	
	public boolean willBeMilkedThisTurn() {
		for(Action current: MainUI.liveGame.getSelectedPlayer().getActionsThisTurn()) {
			MedcivAction castAction = (MedcivAction)current;
			if(castAction.getType().getClass() == MilkAnimal.class &&
					((MilkAnimal)castAction.getType()).getTarget() == this) {
				return true;
			}
		}
		return false;
	}
	
	public boolean willBeTendedThisTurn() {
		for(Action current: MainUI.liveGame.getSelectedPlayer().getActionsThisTurn()) {
			MedcivAction castAction = (MedcivAction)current;
			if(castAction.getType().getClass() == TendAnimal.class &&
					((TendAnimal)castAction.getType()).getTarget() == this) {
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
	public Item clone() {
		// TODO Auto-generated method stub
		return null;
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
		
		retval.add(milkAction());
		retval.add(tendAction());
		
		return retval;
	}

	public MedcivAction milkAction() {
		ActionType milkType = new MilkAnimal(this, MainUI.liveGame.getSelectedVillager());			
		return new MedcivAction(milkType);
	}
	
	public MedcivAction tendAction() {
		ActionType tendType = new TendAnimal(this, MainUI.liveGame.getSelectedVillager());			
		return new MedcivAction(tendType);
	}
}
