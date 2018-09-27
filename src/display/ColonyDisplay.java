package display;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import aibrain.Player;
import spacegame.Empire;
import spacegame.model.Colony;
import spacegame.model.Planet;

public class ColonyDisplay {

	private static JFrame GUI = new JFrame("Colony");
	
	private static JLabel name = new JLabel();
	private static JLabel population = new JLabel();
	private static JLabel industry = new JLabel();
	private static JLabel power = new JLabel();
	
	static{
		GUI.setLayout(new GridLayout(4,1));
		GUI.add(name);
		GUI.add(population);
		GUI.add(industry);
		GUI.add(power);
		
		GUI.setLocation(600, 300);
	}
	
	public static void displayColony(Planet planet, Player empire){
		List<Colony> colList = planet.getActiveColonies();
		Colony colony = null;
		for(Colony current: colList){
			if(current.getOwner().equals(empire)){
				colony = current;
			}
		}
		
		if(colony == null){
			name.setText("no colony for this empire");
		}else{
			name.setText(((Empire)colony.getOwner()).getName()+" colony on "+planet.getName());
			population.setText(colony.getPopulation()+" population");
			industry.setText(colony.getIndustry()+" industry");
			power.setText(colony.getIndustry()+" power");
		}
		
		GUI.pack();
		GUI.setVisible(true);
	}
}
