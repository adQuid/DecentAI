package medciv.model.items;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import medciv.aiconstructs.MedcivAction;
import medciv.model.Item;
import medciv.model.Villager;
import medciv.ui.MainUI;

public class Milk extends Stackable implements Item{
	
	@Override
	public void applyAction(MedcivAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound(Villager owner) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Item clone() {
		return new Milk();
	}

	public String toString() {
		return "Milk";
	}
	
	@Override
	public String description() {
		return stackSize+" units of milk";
	}

	@Override
	public void focusOnItem() {
		MainUI.addOptions("Milk", new JButton("Consume"));
	}

}
