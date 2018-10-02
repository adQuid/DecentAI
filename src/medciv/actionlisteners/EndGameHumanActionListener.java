package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.model.MedcivGame;
import medciv.ui.MainUI;

public class EndGameHumanActionListener implements ActionListener{

	MedcivGame game;
	
	public EndGameHumanActionListener(MedcivGame game) {
		this.game = game;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		game.endRound();
		MainUI.cancelItemFocus();
		MainUI.refresh();
	}

}
