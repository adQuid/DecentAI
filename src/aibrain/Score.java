package aibrain;

import java.util.HashMap;
import java.util.Map;

public class Score {

	private Map<String,Double> categories;

	public Score() {
		this(new HashMap<String,Double>());
	}
	public Score(Map<String, Double> categories) {
		super();
		this.categories = categories;
	}

	public Map<String, Double> getCategories() {
		return categories;
	}

	public void setCategories(Map<String, Double> categories) {
		this.categories = categories;
	}
	
	public double totalScore() {
		return categories.values().stream().mapToInt(Number::intValue).sum();
	}
	
	public Score addTo(Score other) {
		for(String current: categories.keySet()) {
			if(other.categories.containsKey(current)) {
				categories.put(current, categories.get(current) + other.categories.get(current));
			}
		}
		return this;
	}
	
	public Score decay(double decayRate) {
		categories.entrySet().stream().forEach(entry -> entry.setValue(entry.getValue() * decayRate));
		return this;
	}
}
