package medciv.model.items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import medciv.actionlisteners.MilkAnimalListener;
import medciv.aiconstructs.ActionType;
import medciv.aiconstructs.MedcivAction;
import medciv.model.Item;
import medciv.model.Villager;
import medciv.model.actions.MilkAnimal;
import medciv.ui.MainUI;

public class Cow implements Item{

	private boolean willBeMilkedThisTurn = false;
	private boolean wasMilkedThisTurn = false;
	
	
	
	public boolean isWillBeMilkedThisTurn() {
		return willBeMilkedThisTurn;
	}

	public void setWillBeMilkedThisTurn(boolean willBeMilkedThisTurn) {
		this.willBeMilkedThisTurn = willBeMilkedThisTurn;
	}

	public boolean isWasMilkedThisTurn() {
		return wasMilkedThisTurn;
	}

	public void setWasMilkedThisTurn(boolean wasMilkedThisTurn) {
		this.wasMilkedThisTurn = wasMilkedThisTurn;
	}

	@Override
	public void applyAction(MedcivAction action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound(Villager owner) {
		willBeMilkedThisTurn = false;
		wasMilkedThisTurn = false;		
	}

	@Override
	public Item clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		return "Cow";
	}

	@Override
	public String description() {
		return "A Cow. Seems healthy.";
	}

	@Override
	public void focusOnItem() {
		
		String milkText = willBeMilkedThisTurn?"Already milked this turn":"Milk";
		
		JButton milkButton = new JButton(milkText);
		
		milkButton.addActionListener(new MilkAnimalListener(this));
		
		MainUI.addOptions("Cow", new JButton("Feed"), milkButton);
	}

}
