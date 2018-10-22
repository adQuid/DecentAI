package aibrain;

import java.util.List;

public interface ContingencyGenerator {

	public List<Contingency> generateContingencies(Game game, Player empire, int iteration);
	
}
