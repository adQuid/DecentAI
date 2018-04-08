package aibrain;

public class Modifier {

	private double probability;
	private double scoreImpact;
	
	public Modifier(double probability, double scoreImpact) {
		super();
		this.probability = probability;
		this.scoreImpact = scoreImpact;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public double getScoreImpact() {
		return scoreImpact;
	}

	public void setScoreImpact(double scoreImpact) {
		this.scoreImpact = scoreImpact;
	}
}
