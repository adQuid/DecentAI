package medciv.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import medciv.model.Villager;
import medciv.ui.MainUI;

public class FocusVillagerListener implements ActionListener{

	Villager villager;
	
	public FocusVillagerListener(Villager villager) {
		this.villager = villager;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		MainUI.focusOnVillager(villager);
	}

}
