package medciv.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import aibrain.Action;
import medciv.actionlisteners.EndGameHumanActionListener;
import medciv.actionlisteners.ItemActionListener;
import medciv.aiconstructs.MedcivAction;
import medciv.model.MedcivGame;
import medciv.model.Villager;

public class MainUI {

	public static MedcivGame liveGame;
	
	private static JFrame GUI = new JFrame("Medciv");
	
	private static JPanel mainWindow = new JPanel();
	private static JPanel descriptionWindow = new JPanel();
	private static JPanel detailWindow = new JPanel();
	private static JPanel sideOptionsWindow = new JPanel();
	private static JPanel topOptionsWindow = new JPanel();
	
	private static JLabel description = new JLabel("Hover over things for details");
	
	private static JButton quit = new JButton("Quit");
	private static JButton save = new JButton("Save");
	private static JButton endTurn1 = new JButton("End Turn as Human");	
	private static JButton endTurn2 = new JButton("End Turn as AI");	
	
	private static Villager focusVillager;
	
	public static void setup(MedcivGame game) {
		
		liveGame = game;
		
		endTurn1.addActionListener(new EndGameHumanActionListener(liveGame));
		
		mainWindow.setMinimumSize(new Dimension(200,200));
		descriptionWindow.setMinimumSize(new Dimension(200,200));
		detailWindow.setMinimumSize(new Dimension(400,400));
		sideOptionsWindow.setMinimumSize(new Dimension(200,200));
		
		displayPlannedActions();
		
		topOptionsWindow.setLayout(new GridLayout(1,4));
		topOptionsWindow.add(quit);
		topOptionsWindow.add(save);
		topOptionsWindow.add(endTurn1);
		topOptionsWindow.add(endTurn2);
		
		descriptionWindow.add(description);
				
		GUI.add(mainWindow);
		GUI.add(topOptionsWindow,BorderLayout.NORTH);
		GUI.add(sideOptionsWindow,BorderLayout.WEST);
		GUI.add(detailWindow,BorderLayout.EAST);
		GUI.add(descriptionWindow,BorderLayout.SOUTH);
		
		GUI.setSize(800, 800);
		GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	
	public static void setDescription(String text) {
		description.setText(text);
	}
	
	public static JPanel getMainFocus() {
		return mainWindow;
	}
	
	public static void focusOnVillager(Villager focus) {
		focusVillager = focus;
		
		mainWindow.removeAll();
		detailWindow.removeAll();
		
		mainWindow.add(new JLabel(focus.getName()));
		
		detailWindow.setLayout(new GridLayout(7,1));
		if(focus.getOwnedItems().size() < 7) {
			for(int index = 0; index < focus.getOwnedItems().size(); index++) {
				JButton toAdd = new JButton(focus.getOwnedItems().get(index).toString());
				toAdd.addMouseListener(new ItemActionListener(focus.getOwnedItems().get(index)));
				detailWindow.add(toAdd);
			}
			for(int index = focus.getOwnedItems().size(); index < 7; index++) {
				detailWindow.add(new JButton(""));
			}
		} else {
			for(int index = 0; index < 6; index++) {
				JButton toAdd = new JButton(focus.getOwnedItems().get(index).toString());
				detailWindow.add(toAdd);
			}
			detailWindow.add(new JButton("down"));
		}
		GUI.revalidate();
		GUI.repaint();
	}
	
	public static void displayPlannedActions() {
		sideOptionsWindow.removeAll();
		
		List<Action> actionList = liveGame.getSelectedPlayer().getActionsThisTurn();
		
		sideOptionsWindow.setLayout(new GridLayout(Math.max(actionList.size(),12),1));
		
		for(Action current: actionList) {
			sideOptionsWindow.add(new JButton(((MedcivAction)current).getType().toString()));
		}
		
		for(int index = actionList.size(); index < 12; index++) {
			sideOptionsWindow.add(new JLabel("Empty Time"));
		}
		
		GUI.revalidate();
		GUI.repaint();
	}
	
	public static void addOptions(String title, Component... components) {
		mainWindow.removeAll();
		
		mainWindow.setLayout(new GridLayout(components.length+2,1));
		
		mainWindow.add(new JLabel(title));
		for(Component current: components) {
			mainWindow.add(current);
		}
		
		JButton cancel = new JButton("cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				MainUI.cancelItemFocus();
			}			
		});
		mainWindow.add(cancel);
		GUI.revalidate();
		GUI.repaint();
	}
	
	public static void cancelItemFocus() {
		focusOnVillager(focusVillager);
	}
	
	public static void main(String[] args) {
		setup(new MedcivGame(false));
		
		focusOnVillager(liveGame.getPeople().get(0));
		
		GUI.setVisible(true);
	}
}
