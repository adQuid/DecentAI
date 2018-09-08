package display;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import model.Game;
import model.Planet;
import model.Tile;
import ui.Mainmenu;

public class TileDetailWindow {

	static JFrame display;
	
	static JLabel planetName = new JLabel();
	static JButton colonyListButton = new JButton("Colonies");
	static JButton myColonyButton = new JButton("My Colony");
	
	static Tile focus = null;
	
	static boolean open = false;
	
	static{
		display = new JFrame("Tile Detail");
		
		display.setLayout(new GridLayout(3,1));
		
		display.setLocation(600, 100);
		
		myColonyButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(getFocus() != null && getFocus().getObject() instanceof Planet){
					ColonyDisplay.displayColony((Planet)focus.getObject(), Mainmenu.liveGame.fetchCurrentEmpire());
				}
			}
		});
		
		display.add(planetName);
		display.add(colonyListButton);
		display.add(myColonyButton);
	}
	
	public static void displayTile(Game game, int x, int y){
		
		Tile tile = game.getMap().getGrid()[x][y];
		focus = tile;
		if(tile.getObject() != null){
			planetName.setText(tile.getObject().getName());
		}else{
			planetName.setText("empty space");
		}
		
		
		display.pack();
		display.addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowClosed(WindowEvent e) {
				open = false;
			}
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}			
		});
		display.setVisible(true);
		open = true;
	}
	
	private static Tile getFocus(){
		return focus;
	}
	
}
