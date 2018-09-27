package ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import spacegame.Empire;

public class TopMenuButtons {

	static JButton endTurn = new JButton("Turn "+Mainmenu.currentTurn+" (End Turn)");
	static JButton emp1 = new JButton("Empire 0 log");
	static JButton emp2 = new JButton("Empire 1 log");
	static boolean processing = false;
	
	public static JPanel generateButtons(){
		JPanel buttonsDisplay;
				
		endTurn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!processing){
					processing = true;
					endTurn.setText("Ending turn "+Mainmenu.currentTurn);
										
					Thread brainThread = new Thread(new BrainThread());
					
					brainThread.start();
				}
			}
		});
		
		emp1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Empire 0:");
				System.out.println("Minerals: "+((Empire)Mainmenu.liveGame.getEmpires().get(0)).getMinerals());
				System.out.println("Currency: "+((Empire)Mainmenu.liveGame.getEmpires().get(0)).getCurrency());
				System.out.println("logs:");
				for(String current: Mainmenu.brains.get(0).getLogs()) {
					System.out.println(current);
				}
			}
		});
		
		emp2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Empire 1:");
				System.out.println("Minerals: "+((Empire)Mainmenu.liveGame.getEmpires().get(1)).getMinerals());
				System.out.println("Currency: "+((Empire)Mainmenu.liveGame.getEmpires().get(1)).getCurrency());
				System.out.println("logs:");
				for(String current: Mainmenu.brains.get(1).getLogs()) {
					System.out.println(current);
				}
			}
		});
		
		buttonsDisplay = new JPanel();
		buttonsDisplay.setLayout(new GridLayout(1,3));
		buttonsDisplay.add(emp1);
		buttonsDisplay.add(emp2);
		buttonsDisplay.add(endTurn);
		
		return buttonsDisplay;
	}
	
	public static void updateTurn(){
		endTurn.setText("Turn "+Mainmenu.currentTurn+" (End Turn)");
		processing = false;
	}
	
}
