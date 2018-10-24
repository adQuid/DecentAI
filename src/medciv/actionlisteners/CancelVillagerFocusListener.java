package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.ui.MainUI;

public class CancelVillagerFocusListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		MainUI.focusOnTown(MainUI.getFocusTown());
		
	}

}
