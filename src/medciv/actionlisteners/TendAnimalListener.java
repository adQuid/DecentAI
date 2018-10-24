package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.aiconstructs.MedcivAction;
import medciv.model.actions.ActionType;
import medciv.model.actions.MilkAnimal;
import medciv.model.actions.TendAnimal;
import medciv.model.items.Cow;
import medciv.model.items.Livestock;
import medciv.ui.MainUI;

public class TendAnimalListener implements ActionListener{

	private Livestock livestock;
	
	public TendAnimalListener(Livestock cow) {
		this.livestock = cow;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(!livestock.isTendedToThisTurn()) {
			MedcivAction tendAction = livestock.tendAction(MainUI.liveGame.getSelectedVillager());
			
			if(MainUI.liveGame.getSelectedVillager().canAffordAction(tendAction)) {
				MainUI.liveGame.addActionForCurrentPlayer(tendAction);
			
				livestock.focusOnItem();
			}
		}
	}		
}
