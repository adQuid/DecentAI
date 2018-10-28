package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import aibrain.HypotheticalResult;
import medciv.ui.MainUI;

public class EndGameAIListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		HypotheticalResult result = MainUI.brains.get(0).runAI(MainUI.liveGame);
		System.out.println(result.getImmediateActions());
		//MainUI.liveGame.setActionsForPlayer(result.getImmediateActions(), MainUI.liveGame.getSelectedPlayer());
		MainUI.endRound();
	}

}
