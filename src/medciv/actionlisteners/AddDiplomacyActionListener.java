package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.aiconstructs.MedcivAction;
import medciv.ui.DiplomacyUI;
import medciv.ui.MainUI;

public class AddDiplomacyActionListener implements ActionListener{

	MedcivAction action;
	boolean forSelf;
	
	public AddDiplomacyActionListener(MedcivAction action, boolean forSelf) {
		this.action = action;
		this.forSelf = forSelf;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(forSelf) {
			DiplomacyUI.addActionToSelf(action);
		}else {
			DiplomacyUI.addActionToOther(action);
		}
		MainUI.refresh();
	}

}