package medciv.aiconstructs;

import java.util.ArrayList;
import java.util.List;

import aibrain.Contingency;
import aibrain.ContingencyGenerator;
import aibrain.Game;
import aibrain.Player;

public class MedcivContingencyGenerator implements ContingencyGenerator{

	@Override
	public List<Contingency> generateContingencies(Game game, Player empire, int iteration) {
		// right now nothing bad can happen
		return new ArrayList<Contingency>();
	}

}
