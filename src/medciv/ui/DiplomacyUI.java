package medciv.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import aibrain.Action;
import medciv.actionlisteners.AddDiplomacyActionListener;
import medciv.actionlisteners.RemoveDiplomacyActionListener;
import medciv.aiconstructs.MedcivAction;
import medciv.model.Item;
import medciv.model.Villager;
import medciv.model.actions.GiveItem;

public class DiplomacyUI {
	
	private static Villager you;
	private static Villager target;
	
	private static JPanel myPossibleOffersPanel = new JPanel();
	private static JPanel myOffersPanel = new JPanel();
	private static JPanel targetOffersPanel = new JPanel();
	private static JPanel targetPossibleOffersPanel = new JPanel();
	
	private static VerticalList myPossibleOffersList = new VerticalList(myPossibleOffersPanel, new ArrayList<Component>(), 12);
	private static VerticalList myOffersList = new VerticalList(myOffersPanel, new ArrayList<Component>(), 12);
	private static VerticalList targetOffersList = new VerticalList(targetOffersPanel, new ArrayList<Component>(), 12);
	private static VerticalList targetPossibleOffersList = new VerticalList(targetPossibleOffersPanel, new ArrayList<Component>(), 12);
		
	private static List<MedcivAction> myOffers = new ArrayList<MedcivAction>();
	private static List<MedcivAction> targetOffers = new ArrayList<MedcivAction>();
	
	public static void setupDiplomacyScreen(Villager you, Villager target) {
		DiplomacyUI.you = you;
		DiplomacyUI.target = target;
		
		myOffers = new ArrayList<MedcivAction>();
		targetOffers = new ArrayList<MedcivAction>();
	}
	
	public static JPanel showDiplomacyScreen() {
		JPanel retval = new JPanel();
		retval.setLayout(new BorderLayout());
		
		JPanel bigButtons = new JPanel();
		
		JButton offer = new JButton("Offer");
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				MainUI.cancelDiplomacyMenu();
			}			
		});
		
		bigButtons.setLayout(new GridLayout(2,1));
		bigButtons.add(offer);
		bigButtons.add(cancel);
		
		JPanel diploPanel = new JPanel();
		diploPanel.setLayout(new GridLayout(1,4));
		
		List<List<Component>> possibleOffers = getPossibleOffers(you,target);
		
		myPossibleOffersList.updatePanel(possibleOffers.get(0));
		myOffersList.updatePanel(getStandingOffers(myOffers,true));
		targetOffersList.updatePanel(getStandingOffers(targetOffers,false));
		targetPossibleOffersList.updatePanel(possibleOffers.get(1));
				
		diploPanel.add(myPossibleOffersPanel);
		diploPanel.add(myOffersPanel);
		diploPanel.add(targetOffersPanel);
		diploPanel.add(targetPossibleOffersPanel);
		
		retval.add(diploPanel,BorderLayout.CENTER);
		retval.add(bigButtons,BorderLayout.SOUTH);
		
		retval.validate();
		
		return retval;
	}

	private static List<Component> getStandingOffers(List<MedcivAction> offers, boolean self){
		List<Component> retval = new ArrayList<Component>();
		
		for(MedcivAction current: offers) {
			JButton removeButton = new JButton(current.toString());
			removeButton.addActionListener(new RemoveDiplomacyActionListener(current, self));
			retval.add(removeButton);
		}
		
		return retval;
	}
	
	private static List<List<Component>> getPossibleOffers(Villager you, Villager target) {
		List<List<Component>> retval = new ArrayList<List<Component>>();
		
		//the following two chunks are obvious canidates for DRY
		List<Component> selfOffers = new ArrayList<Component>();
		//giving items 
		for(Item current: you.getOwnedItems()) {
			MedcivAction giveAction = new MedcivAction(new GiveItem(you.getId(),target.getId(),current.getId(),current.toString()),you.getId());
			if(!myOffers.contains(giveAction)) {
				JButton giveItem = new JButton("give "+current.toString());
				giveItem.addActionListener(new AddDiplomacyActionListener(giveAction,true));
				selfOffers.add(giveItem);
			}
		}
		
		List<Component> otherOffers = new ArrayList<Component>();
		//getting items 
		for(Item current: you.getOwnedItems()) {
			MedcivAction giveAction = new MedcivAction(new GiveItem(target.getId(),you.getId(),current.getId(),current.toString()),target.getId());
			if(!targetOffers.contains(giveAction)) {
				JButton giveItem = new JButton("give "+current.toString());
				giveItem.addActionListener(new AddDiplomacyActionListener(giveAction,false));
				otherOffers.add(giveItem);
			}
		}
		
		retval.add(selfOffers);		
		retval.add(otherOffers);
		return retval;
	}
	
	public static void addActionToSelf(MedcivAction action) {
		myOffers.add(action);
	}
	
	public static void removeActionFromSelf(MedcivAction action) {
		myOffers.remove(action);
	}

	public static void addActionToOther(MedcivAction action) {
		targetOffers.add(action);
	}

	public static void removeActionFromOther(MedcivAction action) {
		targetOffers.remove(action);
	}
}