package medciv.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import aibrain.AIBrain;
import aibrain.Action;
import medciv.actionlisteners.EndGameAIListener;
import medciv.actionlisteners.EndGameHumanActionListener;
import medciv.actionlisteners.ItemActionListener;
import medciv.actionlisteners.RemoveActionListener;
import medciv.aiconstructs.MedcivAction;
import medciv.aiconstructs.MedcivBaseIdeaGen;
import medciv.aiconstructs.MedcivCloner;
import medciv.aiconstructs.MedcivContingencyGenerator;
import medciv.aiconstructs.MedcivEvaluator;
import medciv.aiconstructs.MedcivPlayer;
import medciv.gamesetup.BaseGameSetup;
import medciv.model.FoodGrouping;
import medciv.model.Item;
import medciv.model.MedcivGame;
import medciv.model.Villager;
import medciv.model.items.Edible;

public class MainUI {

	public static MedcivGame liveGame;
	public static List<AIBrain> brains = new ArrayList<AIBrain>();
	
	private static JFrame GUI = new JFrame("Medciv");
	
	private static JPanel mainWindow = new JPanel();
	private static JPanel descriptionWindow = new JPanel();
	private static JPanel detailWindow = new JPanel();
	private static JPanel sideOptionsWindow = new JPanel();
	private static JPanel topOptionsWindow = new JPanel();
	
	private static JLabel description = new JLabel("Hover over things for details");
	
	private static VerticalList itemList = new VerticalList(new ArrayList<Component>());
	private static VerticalList actionList = new VerticalList(new ArrayList<Component>());
	
	private static JButton quit = new JButton("Quit");
	private static JButton save = new JButton("Save");
	private static JButton endTurn1 = new JButton("End Turn as Human");	
	private static JButton endTurn2 = new JButton("End Turn as AI");	
	
	private static Villager focusVillager;
	private static Item focusItem;
		
	public static Villager getFocusVillager() {
		return focusVillager;
	}

	public static void setFocusVillager(Villager focusVillager) {
		MainUI.focusVillager = focusVillager;
	}

	public static Item getFocusItem() {
		return focusItem;
	}

	public static void setFocusItem(Item focusItem) {
		MainUI.focusItem = focusItem;
	}

	public static void setup(MedcivGame game) {
		
		liveGame = game;
		
		endTurn1.addActionListener(new EndGameHumanActionListener());
		endTurn2.addActionListener(new EndGameAIListener());
		
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
		
		String foodString = "Eating: ";
		FoodGrouping food = focus.getPlannedFood();
		for(Edible current: food.getFood()) {
			foodString += current.description();
		}
		
		String timeString = "Avalible time: "+focusVillager.timeLeft();
		
		JLabel nameLabel = new JLabel(focus.getName());
		JLabel foodLabel = new JLabel(foodString);
		JLabel timeLabel = new JLabel(timeString);
		
		displayItems();
		
		mainWindow.setLayout(new GridLayout(3,1));
		mainWindow.add(nameLabel);
		mainWindow.add(foodLabel);
		mainWindow.add(timeLabel);
		
		GUI.revalidate();
		GUI.repaint();
	}
	
	public static void displayItems() {
		List<Component> itemButtons = new ArrayList<Component>();
		for(Item current: focusVillager.getOwnedItems()) {
			JButton toAdd = new JButton(current.toString());
			toAdd.addMouseListener(new ItemActionListener(current));
			itemButtons.add(toAdd);
		}
		itemList.updatePanel(detailWindow, itemButtons, 7);
	}
	
	public static void displayPlannedActions() {
		sideOptionsWindow.removeAll();
		
		List<Action> actionsThisTurn = liveGame.getSelectedPlayer().getActionsThisTurn();
		
		List<Component> buttonList = new ArrayList<Component>();
		for(Action current: actionsThisTurn) {
			JButton toAdd = new JButton(((MedcivAction)current).getType().toString());
			toAdd.addActionListener(new RemoveActionListener(liveGame.getSelectedPlayer(),(MedcivAction)current));
			buttonList.add(toAdd);
		}
		actionList.updatePanel(sideOptionsWindow, buttonList, 12);
		
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
		focusItem = null;
		focusOnVillager(focusVillager);
	}
	
	public static void refresh() {
		if(focusItem != null) {
			focusItem.focusOnItem();
		} else {
			focusOnVillager(focusVillager);
		}
		displayItems();
		displayPlannedActions();
	}
	
	public static void endRound() {
		liveGame.endRound();
		MainUI.cancelItemFocus();
		MainUI.refresh();
	}
	
	public static void main(String[] args) {
		setup(BaseGameSetup.newBasicGame());
		
		for(MedcivPlayer current: liveGame.getPlayers()) {
			brains.add(new AIBrain(current, 4, 0, 1, new MedcivBaseIdeaGen(), new MedcivContingencyGenerator(), new MedcivEvaluator(), new MedcivCloner()));
		}
		
		focusOnVillager(liveGame.getPeople().get(0));
		
		GUI.setVisible(true);
	}
}
