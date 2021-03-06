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

public class Cow extends Livestock{
	
	private boolean wasMilkedThisTurn = false;
	
	public Cow(int ownerId) {
		super(ownerId);
	}
	
	public Cow(int id, int ownerId) {
		super(id, ownerId);
	}
	
	public boolean willBeMilkedThisTurn(MedcivGame game) {
		for(Action current: game.getSelectedPlayer().getActionsThisTurn()) {
			MedcivAction castAction = (MedcivAction)current;
			if(castAction.getType().getClass() == MilkAnimal.class &&
					((MilkAnimal)castAction.getType()).getId() == this.getId()) {
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
	}

	@Override
	public Item clone() {
		Cow retval = new Cow(getId(), ownerId);
		retval.wasMilkedThisTurn = this.wasMilkedThisTurn;
		super.cloneHelper(retval);
		return retval;
	}
	
	public String toString() {
		return "Cow";
	}

	@Override
	public String description() {
		return "A cow. Seems healthy.";
	}

	@Override
	public void focusOnItem() {
		
		String milkText = willBeMilkedThisTurn(MainUI.liveGame)?"Already milked this turn":"Milk";
		JButton milkButton = new JButton(milkText);
		milkButton.addActionListener(new MilkAnimalListener(this));
		
		String tendText = willBeTendedToThisTurn(MainUI.liveGame)?"Already tended to this turn":"Tend to Animal";
		JButton tendButton = new JButton(tendText);
		tendButton.addActionListener(new TendAnimalListener(this));
		
		MainUI.setFocusItem(this);
		MainUI.addItemOptions("Cow", tendButton, milkButton);
	}

	@Override
	public List<Action> getAssociatedActions(Villager villager) {
		List<Action> retval = new ArrayList<Action>();
		
		retval.add(milkAction(villager));
		retval.add(tendAction(villager));
		
		return retval;
	}

	public MedcivAction milkAction(Villager villager) {
		//we don't just use owner here since the cow might be being given this round
		ActionType milkType = new MilkAnimal(this.getId(), villager.getId());			
		return new MedcivAction(milkType,villager.getId());
	}
	
}
