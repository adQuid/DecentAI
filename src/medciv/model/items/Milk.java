package medciv.model.items;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import aibrain.Action;
import medciv.model.Item;
import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.ui.MainUI;

public class Milk extends Edible{
	
	int age = 0;
	
	public Milk(int size,  int ownerId) {
		this(size,0,ownerId);
	}
	
	public Milk(int size, int age,  int ownerId) {
		super(ownerId);
		this.stackSize = size;
		this.age = age;
	}
	
	public Milk(int size, int age, int id, int ownerId) {
		super(id, ownerId);
		this.stackSize = size;
		this.age = age;
	}
	
	@Override
	public void endRound(MedcivGame game, Villager owner) {
		age++;
	}

	@Override
	public Milk clone() {
		return new Milk(stackSize,age,this.getId(),ownerId);
	}

	public String toString() {
		return "Milk";
	}
	
	@Override
	public String description() {
		if(age < 2) {
			return stackSize+" units of milk";
		} else {
			return stackSize+" units of spoiled milk (age "+age+")";
		}
	}

	@Override
	public void focusOnItem() {
		MainUI.addItemOptions("Milk", new JButton("No Actions"));
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
		return stackSize == 0 || age > 4;
	}

	@Override
	public List<Action> getAssociatedActions(Villager villager) {
		return new ArrayList<Action>();
	}
}
