package aibrain;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic DTO for deals. Offers represent the set of actions that each player is offering to do (in addition to any other 
 * actions they might do outside of the deal) should the deal be accepted. Threats are not currently implemented, but 
 * will represent what each player will do unilaterally if the deal is "broken". Not all players in the game need to have 
 * entries in the deal, but any player who does appear must have an offer and a threat, and all plans must be of the same 
 * length.
 */
public class Deal {

	public Map<Player,Plan> offers = new HashMap<Player,Plan>();
	public Map<Player,Plan> threats = new HashMap<Player,Plan>();
	
}
