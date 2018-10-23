package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.model.MedcivGame;
import medciv.ui.MainUI;

public class EndGameHumanActionListener implements ActionListener{
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		MainUI.endRound();
	}

}
