package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import display.TileDetailWindow;

public class SelectTileListener implements MouseListener{

	int x,y;
	
	public SelectTileListener(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		TileDetailWindow.displayTile(Mainmenu.liveGame, x, y);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
