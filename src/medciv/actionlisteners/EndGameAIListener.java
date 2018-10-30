package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import aibrain.HypotheticalResult;
import medciv.ui.MainUI;

public class EndGameAIListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		HypotheticalResult result = MainUI.getBrainForPlayer(MainUI.liveGame.getSelectedPlayer()).runAI(MainUI.liveGame);
		System.out.println(result.getImmediateActions());
		MainUI.liveGame.setActionsForPlayer(result.getImmediateActions(), MainUI.liveGame.getSelectedPlayer());
		MainUI.refresh();
	}

}
