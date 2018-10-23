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
	
	public Milk(MedcivGame game, int size,  int ownerId) {
		this(game,size,0,ownerId);
	}
	
	public Milk(MedcivGame game, int size, int age,  int ownerId) {
		super(game, ownerId);
		this.stackSize = size;
		this.age = age;
	}
	
	public Milk(MedcivGame game, int size, int age, int id, int ownerId) {
		super(game, id, ownerId);
		this.stackSize = size;
		this.age = age;
	}
	
	@Override
	public void endRound(Villager owner) {
		age++;		
	}

	@Override
	public Milk clone(MedcivGame game) {
		return new Milk(game,stackSize,age,this.getId(),ownerId);
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
