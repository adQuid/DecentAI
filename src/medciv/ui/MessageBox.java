package medciv.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class MessageBox {

	public static void display(String message) {
		JFrame GUI = new JFrame("message");
		
		GUI.add(new JLabel(message));
		
		GUI.pack();
		GUI.setVisible(true);
	}
	
}
