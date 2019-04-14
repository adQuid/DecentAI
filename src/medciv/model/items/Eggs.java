package medciv.model.items;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import aibrain.Action;
import medciv.model.Item;
import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.ui.MainUI;

public class Eggs extends Edible{
	
	int age = 0;
	
	public Eggs(int size,  int ownerId) {
		this(size,0,ownerId);
	}
	
	public Eggs(int size, int age,  int ownerId) {
		super(ownerId);
		this.stackSize = size;
		this.age = age;
	}
	
	public Eggs(int size, int age, int id, int ownerId) {
		super(id, ownerId);
		this.stackSize = size;
		this.age = age;
	}
	
	@Override
	public void endRound(MedcivGame game, Villager owner) {
		//removing food aging for ease
		//age++;
	}

	@Override
	public Eggs clone() {
		return new Eggs(stackSize,age,this.getId(),ownerId);
	}

	public String toString() {
		if(age < 3) {
			return "Eggs";
		} else {
			return "Old Eggs";
		}
	}
	
	@Override
	public String description() {
		if(age < 3) {
			return stackSize+" units of eggs";
		} else {
			return stackSize+" units of rotten eggs (age "+age+")";
		}
	}

	@Override
	public void focusOnItem() {
		MainUI.addItemOptions("Milk", new JButton("No Actions"));
	}

	@Override
	public int foodPriority() {
		return 9-(age);
	}

	@Override
	public boolean isEquivelent(Stackable other) {
		if(other instanceof Eggs) {
		return this.age == ((Eggs)other).age;
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
