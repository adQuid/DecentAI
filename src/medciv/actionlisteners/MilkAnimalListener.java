package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import aibrain.Action;
import medciv.aiconstructs.ActionType;
import medciv.aiconstructs.MedcivAction;
import medciv.model.actions.MilkAnimal;
import medciv.model.items.Cow;
import medciv.ui.MainUI;

public class MilkAnimalListener implements ActionListener{

	private Cow cow;
	
	public MilkAnimalListener(Cow cow) {
		this.cow = cow;
	}
	
	public void actionPerformed(ActionEvent arg0) {
			ActionType milkType = new MilkAnimal(cow, MainUI.liveGame.getSelectedVillager());
			
			MedcivAction milkAction = new MedcivAction(milkType);
			
			MainUI.liveGame.addActionForCurrentPlayer(milkAction);
		
			cow.setWillBeMilkedThisTurn(true);
	}		
}
