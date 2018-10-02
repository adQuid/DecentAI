package spacegame.descriptionlisteners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import spacegame.Empire;
import spacegame.display.GridDisplay;
import spacegame.model.Colony;
import spacegame.model.Planet;
import spacegame.model.SpaceObject;
import spacegame.model.Tile;

public class TileDescriptionListener implements MouseListener{

	private Tile tile;
	
	public TileDescriptionListener(Tile tile){
		this.tile = tile;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		String retval = "<html>";
		if(tile.getObject() != null){
			SpaceObject object = tile.getObject();
			retval += object.getName();
			
			if(object instanceof Planet){
				Planet planet = (Planet)object;
				for(Colony current: planet.getActiveColonies()){
					retval += "<br>"+current.getName()+"<br>colony belonging to "+((Empire)current.getOwner()).getName()+
							"<br> industry:"+current.getIndustry()+"/"+current.getPower();
					
					if(current.getDefense() > 0) {
						retval += "<br> (defended)";
					}
				}
			}
		}
		
		GridDisplay.displayDescription(retval+"</html>");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
