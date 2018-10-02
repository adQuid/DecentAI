package medciv.ui;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import medciv.actionlisteners.VerticalListScrollListener;

public class VerticalList {

	private List<Component> components;
	private int position = 0;
	VerticalListScrollListener listener = new VerticalListScrollListener(this);
	
	public VerticalList(List<Component> components) {
		this.components = components;
	}
	
	public void updatePanel(JPanel panel, int size) {
		updatePanel(panel,components,size);
	}
	
	public void updatePanel(JPanel panel, List<Component> components, int size) {
		panel.removeAll();
		panel.removeMouseWheelListener(listener);
		
		this.components = components;
		
		panel.setLayout(new GridLayout(size,1));
		
		for(int index = position; index < position+size; index++) {
			if(index<components.size()) {
				panel.add(components.get(index));
			} else {
				panel.add(new JButton(""));
			}
		}
		
		panel.addMouseWheelListener(listener);
	}
	
	public void scroll(int scroll) {
		position += scroll;
		if(position < 0) {
			position = 0;
		}
		if(position >= components.size()) {
			position = Math.max(components.size()-1,0);
		}
	}
}
