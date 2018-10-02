package spacegame.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import aibrain.AIBrain;
import aibrain.Player;
import spacegame.SpaceGameCloner;
import spacegame.SpaceGameContingencyGenerator;
import spacegame.SpaceGameEvaluator;
import spacegame.SpaceGameIdeaGenerator;
import spacegame.display.GridDisplay;
import spacegame.model.Game;

public class Mainmenu {

	public static Game liveGame = null;
	public static List<AIBrain> brains = new ArrayList<AIBrain>();
	public static int currentTurn = 1;
	public static HashSet<String> playersReady = new HashSet<String>();
	
	public static void main(String[] args){
		
		JFrame GUI = new JFrame("Decent AI");		
		
		JButton newGame = new JButton("Create new Game");
				
		newGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				liveGame = new Game();
				
				for(Player current: liveGame.getEmpires()) {
					brains.add(new AIBrain(current, 8, 0, 12, SpaceGameIdeaGenerator.instance(),SpaceGameContingencyGenerator.instance(),SpaceGameEvaluator.getInstance(),SpaceGameCloner.getInstance()));
				}
								
				try {
					GridDisplay.display(liveGame);
				} catch (IOException e1) {
					JFrame errorMessage = new JFrame("Error");
					errorMessage.add(new JLabel("Failed to display game"));
					errorMessage.pack();
					errorMessage.setVisible(true);
				}
			}
		});
				
		GUI.setLayout(new GridLayout(2,1));
		
		GUI.add(newGame);
		
		GUI.pack();
		GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		GUI.setVisible(true);
	}
	
}
