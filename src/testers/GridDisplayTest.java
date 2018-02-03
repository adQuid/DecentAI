package testers;

import java.io.IOException;

import display.GridDisplay;
import model.Game;

public class GridDisplayTest {

	public static void main(String[] args){
		Game testGame = new Game();
		
		try {
			GridDisplay.display(testGame);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
