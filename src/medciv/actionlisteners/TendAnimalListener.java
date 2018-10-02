package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.aiconstructs.ActionType;
import medciv.aiconstructs.MedcivAction;
import medciv.model.actions.MilkAnimal;
import medciv.model.actions.TendAnimal;
import medciv.model.items.Cow;
import medciv.ui.MainUI;

public class TendAnimalListener implements ActionListener{

	private Cow cow;
	
	public TendAnimalListener(Cow cow) {
		this.cow = cow;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(!cow.isWasMilkedThisTurn()) {
			ActionType tendType = new TendAnimal(cow, MainUI.liveGame.getSelectedVillager());			
			MedcivAction tendAction = new MedcivAction(tendType);
			
			if(MainUI.liveGame.getSelectedVillager().canAffordAction(tendAction)) {
				MainUI.liveGame.addActionForCurrentPlayer(tendAction);
			
				cow.focusOnItem();
			}
		}
	}		
}
