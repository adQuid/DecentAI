package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.aiconstructs.MedcivAction;
import medciv.aiconstructs.MedcivPlayer;
import medciv.model.Villager;
import medciv.ui.MainUI;

public class RemoveActionListener implements ActionListener{

	MedcivPlayer player;
	MedcivAction action;
	
	public RemoveActionListener(MedcivPlayer player, MedcivAction action) {
		this.player = player;
		this.action = action;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		player.getActionsThisTurn().remove(action);//not the most happy with this...		
		MainUI.refresh();
	}

}
