package medciv.actionlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import medciv.model.Item;
import medciv.ui.MainUI;

public class ItemActionListener implements MouseListener{

	Item item;
	
	public ItemActionListener(Item item) {
		this.item = item;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		MainUI.setDescription(item.description());
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		MainUI.setDescription("");		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		item.focusOnItem();	
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	
	}

}
