package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import aibrain.Action;
import aibrain.Player;
import medciv.aiconstructs.MedcivAction;
import medciv.aiconstructs.MedcivPlayer;
import medciv.ui.DiplomacyUI;
import medciv.ui.MainUI;
import medciv.ui.MessageBox;

public class OfferDealActionListener implements ActionListener{

	MedcivPlayer player;
	
	public OfferDealActionListener(MedcivPlayer player) {
		this.player = player;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		BigDecimal dealValue = MainUI.getBrainForPlayer(player).evaluateDeal(MainUI.liveGame, DiplomacyUI.getDeal());
		
		System.out.println(dealValue);
		if(dealValue.compareTo(BigDecimal.ZERO) > 0) {
			MessageBox.display("I approve of this plan at value "+dealValue.toString());
			
			//very much prototype code. Won't work for more complex deals
			for(Player player: DiplomacyUI.getDeal().offers.keySet()) {
				for(Action action: DiplomacyUI.getDeal().offers.get(player).getLayer(0)) {
					//from the AI's point of view, there is no difference between doing this now vs at the start of the end of the turn
					((MedcivAction)action).getType().doAction(MainUI.liveGame);
				}
			}
			
			DiplomacyUI.clearAllActions();
			MainUI.refresh();
		} else {
			MessageBox.display("I decline this plan at value: "+dealValue.toString());
		}
	}
}
