package medciv.actionlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import medciv.ui.MainUI;
import medciv.ui.VerticalList;

public class VerticalListScrollListener implements MouseWheelListener{

	VerticalList parentList;
	
	public VerticalListScrollListener(VerticalList parentList) {
		this.parentList = parentList;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent scroll) {
		if(scroll.getWheelRotation() > 0) {
			parentList.scroll(1);
		}else if(scroll.getWheelRotation() < 0){
			parentList.scroll(-1);
		}
		MainUI.refresh();
	}


}
