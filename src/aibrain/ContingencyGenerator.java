package aibrain;

import java.util.List;

import model.Empire;

public interface ContingencyGenerator {

	public List<Contingency> generateContingencies(Game game, Empire empire, List<Action> possibilities, int iteration);
	
}
