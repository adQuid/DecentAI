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
	private JPanel parentPanel;
	private int size;
	
	public VerticalList(JPanel parentPanel, List<Component> components, int size) {
		this.parentPanel = parentPanel;
		this.components = components;
		this.size = size;
	}
	
	public void updatePanel() {
		updatePanel(components);
	}
	
	public void updatePanel(List<Component> components) {
		parentPanel.removeAll();
		parentPanel.removeMouseWheelListener(listener);
		
		this.components = components;
		
		parentPanel.setLayout(new GridLayout(size,1));
		
		for(int index = position; index < position+size; index++) {
			if(index<components.size()) {
				parentPanel.add(components.get(index));
			} else {
				parentPanel.add(new JButton(""));
			}
		}
		
		parentPanel.addMouseWheelListener(listener);
		parentPanel.repaint();
	}
	
	public void scroll(int scroll) {
		position += scroll;
		if(position < 0) {
			position = 0;
		}
		if(position >= components.size()) {
			position = Math.max(components.size()-1,0);
		}
		updatePanel();
	}
}
