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
import aibrain.BrainThread;
import model.Colony;
import model.Empire;
import model.Game;
import testers.GameRunTestser;

public class TopMenuButtons {

	static JButton endTurn = new JButton("Turn "+Mainmenu.currentTurn+" (End Turn)");
	static boolean processing = false;
	
	public static JPanel generateButtons(Game game){
		JPanel buttonsDisplay;
				
		endTurn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!processing){
					processing = true;
					endTurn.setText("Ending turn "+Mainmenu.currentTurn);
					
					for(Empire current: Mainmenu.liveGame.getEmpires()){
						Thread brain = new Thread(new BrainThread(Mainmenu.liveGame,current,8,8));
						brain.start();
					}
				}
			}
		});
		
		buttonsDisplay = new JPanel();
		buttonsDisplay.setLayout(new GridLayout(1,3));
		buttonsDisplay.add(new JButton("Some button"));
		buttonsDisplay.add(new JButton("Some other button"));
		buttonsDisplay.add(endTurn);
		
		return buttonsDisplay;
	}
	
	public static void updateTurn(){
		endTurn.setText("Turn "+Mainmenu.currentTurn+" (End Turn)");
		processing = false;
	}
	
}
