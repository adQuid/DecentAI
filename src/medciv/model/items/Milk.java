package medciv.model.items;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import aibrain.Action;
import medciv.aiconstructs.MedcivAction;
import medciv.model.Item;
import medciv.model.Villager;
import medciv.ui.MainUI;

public class Milk extends Stackable implements Item,Edible{
	
	int age = 0;
	
	public Milk(int size) {
		this(size,0);
	}
	
	public Milk(int size, int age) {
		this.stackSize = size;
		this.age = age;
	}
	
	@Override
	public void endRound(Villager owner) {
		age++;
		
	}

	@Override
	public Milk clone() {
		return new Milk(stackSize,age);
	}

	public String toString() {
		return "Milk";
	}
	
	@Override
	public String description() {
		if(age < 2) {
			return stackSize+" units of milk";
		} else {
			return stackSize+" units of spoiled milk";
		}
	}

	@Override
	public void focusOnItem() {
		MainUI.addOptions("Milk", new JButton("Consume"));
	}

	@Override
	public int foodPriority() {
		return 10-(age*3);
	}

	@Override
	public boolean isEquivelent(Stackable other) {
		if(other instanceof Milk) {
		return this.age == ((Milk)other).age;
		} else {
			return false;
		}
	}

	@Override
	public boolean isDead() {
		return stackSize == 0;
	}

	@Override
	public List<Action> getAssociatedActions(Villager villager) {
		return new ArrayList<Action>();
	}

}
