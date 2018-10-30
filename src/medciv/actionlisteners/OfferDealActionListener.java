package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.aiconstructs.MedcivPlayer;
import medciv.ui.DiplomacyUI;
import medciv.ui.MainUI;

public class OfferDealActionListener implements ActionListener{

	MedcivPlayer player;
	
	public OfferDealActionListener(MedcivPlayer player) {
		this.player = player;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Evaluation: "+MainUI.getBrainForPlayer(player).evaluateDeal(MainUI.liveGame, DiplomacyUI.getDeal()));
		
	}
}
