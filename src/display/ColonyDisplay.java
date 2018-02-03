package display;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Colony;
import model.Empire;
import model.Planet;
import model.Tile;

public class ColonyDisplay {

	private static JFrame GUI = new JFrame("Colony");
	
	private static JLabel name = new JLabel();
	private static JLabel population = new JLabel();
	private static JLabel industry = new JLabel();
	
	static{
		GUI.setLayout(new GridLayout(3,1));
		GUI.add(name);
		GUI.add(population);
		GUI.add(industry);
		
		GUI.setLocation(600, 300);
	}
	
	public static void displayColony(Planet planet, Empire empire){
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
			name.setText(colony.getOwner().getName()+" colony on "+planet.getName());
			population.setText(colony.getPopulation()+" population");
			industry.setText(colony.getIndustry()+" industry");
		}
		
		GUI.pack();
		GUI.setVisible(true);
	}
}
