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

public class Chicken extends Livestock{
	
	private boolean wasMilkedThisTurn = false;
	
	public Chicken(int ownerId) {
		super(ownerId);
	}
	
	public Chicken(int id, int ownerId) {
		super(id, ownerId);
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
	

	@Override
	public void endRound(MedcivGame game, Villager owner) {
		super.endRound(game, owner);
		wasMilkedThisTurn = false;		
		
		owner.addItems(new Eggs(1, owner.getId()));
	}

	@Override
	public Item clone() {
		Chicken retval = new Chicken(getId(), ownerId);
		retval.wasMilkedThisTurn = this.wasMilkedThisTurn;
		super.cloneHelper(retval);
		return retval;
	}
	
	public String toString() {
		return "Chicken";
	}

	@Override
	public String description() {
		return "A chicken. Seems healthy.";
	}

	@Override
	public void focusOnItem() {
				
		String tendText = willBeTendedThisTurn()?"Already tended to this turn":"Tend to Animal";
		JButton tendButton = new JButton(tendText);
		tendButton.addActionListener(new TendAnimalListener(this));
		
		MainUI.setFocusItem(this);
		MainUI.addItemOptions("Chicken", tendButton);
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
	
}
