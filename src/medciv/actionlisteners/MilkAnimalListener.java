package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import aibrain.Action;
import medciv.aiconstructs.MedcivAction;
import medciv.model.actions.ActionType;
import medciv.model.actions.MilkAnimal;
import medciv.model.items.Cow;
import medciv.ui.MainUI;

public class MilkAnimalListener implements ActionListener{

	private Cow cow;
	
	public MilkAnimalListener(Cow cow) {
		this.cow = cow;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(!cow.willBeMilkedThisTurn(MainUI.liveGame)) {
			MedcivAction milkAction = cow.milkAction(MainUI.liveGame.getSelectedVillager());
			
			if(MainUI.liveGame.getSelectedVillager().canAffordAction(milkAction)) {
				MainUI.liveGame.addActionForCurrentPlayer(milkAction);
			
				cow.focusOnItem();
			}
		}
	}		
}
