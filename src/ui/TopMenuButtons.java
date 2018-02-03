package ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import actions.Action;
import actions.ActionType;
import aibrain.AIBrain;
import model.Game;
import testers.GameRunTestser;

public class TopMenuButtons {

	public static JPanel generateButtons(Game game){
		JPanel buttonsDisplay;
		
		JButton endTurn = new JButton("Turn "+Mainmenu.currentTurn+" (End Turn)");
		
		endTurn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
								
				AIBrain brain = new AIBrain();
				
				List<Action> actions = brain.runAI(game, game.fetchCurrentEmpire(), 15);
				
				game.setActionsForEmpire(actions, game.fetchCurrentEmpire());
				
				game.endRound();
				
				Mainmenu.currentTurn++;
				endTurn.setText("Turn "+Mainmenu.currentTurn+" (End Turn)");
			}
		});
		
		buttonsDisplay = new JPanel();
		buttonsDisplay.setLayout(new GridLayout(1,3));
		buttonsDisplay.add(new JButton("Some button"));
		buttonsDisplay.add(new JButton("Some other button"));
		buttonsDisplay.add(endTurn);
		
		return buttonsDisplay;
	}
	
}
