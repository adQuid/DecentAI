package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.aiconstructs.MedcivAction;
import medciv.ui.DiplomacyUI;
import medciv.ui.MainUI;

public class RemoveDiplomacyActionListener implements ActionListener{

	MedcivAction action;
	boolean forSelf;
	
	public RemoveDiplomacyActionListener(MedcivAction action, boolean forSelf) {
		this.action = action;
		this.forSelf = forSelf;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(forSelf) {
			DiplomacyUI.removeActionFromSelf(action);
		}else {
			DiplomacyUI.removeActionFromOther(action);
		}
		MainUI.refresh();
	}

}