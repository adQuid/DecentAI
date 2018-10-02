package spacegame.display;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import spacegame.descriptionlisteners.TileDescriptionListener;
import spacegame.model.Game;
import spacegame.model.Map;
import spacegame.model.Star;
import spacegame.model.Tile;
import spacegame.ui.SelectTileListener;
import spacegame.ui.TopMenuButtons;

public class GridDisplay {

	private static JFrame GUI;
	private static JPanel buttonsDisplay;
	private static JPanel mapDisplay;
	private static JPanel descriptionDisplay;
	private static JLabel description;
		
	
	public static void display(Game game) throws IOException{
		GUI = new JFrame("Decent AI");
		
		buttonsDisplay = TopMenuButtons.generateButtons();
		
		mapDisplay = new JPanel();
		mapDisplay.setLayout(new GridLayout(Map.SIZE,Map.SIZE));
		for(int x=0; x<game.getMap().getGrid().length; x++){
			for(int y=0; y<game.getMap().getGrid().length; y++){				
				JLabel toAdd = new JLabel(new ImageIcon(ImageIO.read(new File(imagePath(game.getMap().getGrid()[x][y])))));
				toAdd.addMouseListener(new SelectTileListener(x,y));
				toAdd.addMouseListener(new TileDescriptionListener(game.getMap().getGrid()[x][y]));
				mapDisplay.add(toAdd);
			}
		}
		
		descriptionDisplay = new JPanel();
		description = new JLabel();
		description.setText("This is the map of the only solar system that exists right now");
		descriptionDisplay.add(description);
		
		JPanel topSection = new JPanel();
		topSection.setLayout(new BorderLayout());
		topSection.add(buttonsDisplay, BorderLayout.NORTH);
		topSection.add(mapDisplay,BorderLayout.SOUTH);

		GUI.add(topSection,BorderLayout.NORTH);
		GUI.add(descriptionDisplay,BorderLayout.SOUTH);
		GUI.pack();
		GUI.setVisible(true);
	}
	
	private static String imagePath(Tile tile){
		if(tile.getObject() != null){
			if(tile.getObject() instanceof Star){
				return "images/sun.png";
			}else{
				return "images/basic planet.png";
			}
		}else{
			return "images/starfield.png";
		}
	}
	
	public static void displayDescription(String text){
		description.setText(text);
		GUI.pack();
	}
	
}
