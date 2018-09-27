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
		this.categories = new HashMap<String, Double>();
		for(String current: categories.keySet()) {
			if(!this.categories.containsKey(current)) {
				this.categories.put(current, categories.get(current));
			}
		}
	}
	public Score(Score other) {
		this(other.categories);		
	}
	
	public Map<String, Double> getCategories() {
		return categories;
	}

	public void setCategories(Map<String, Double> categories) {
		this.categories = categories;
	}
	
	public double totalScore() {
		return categories.values().stream().mapToDouble(Number::doubleValue).sum();
	}
	
	public Score add(Score other) {
		
		for(String current: categories.keySet()) {
			if(other.categories.containsKey(current)) {
				categories.put(current, categories.get(current) + other.categories.get(current));
			}
		}
		for(String current: other.categories.keySet()) {
			if(!this.categories.containsKey(current)) {
				categories.put(current, other.categories.get(current));
			}
		}
		
		return this;
	}
	
	public Score decay(double decayRate) {
		categories.entrySet().stream().filter(entry -> !entry.getKey().equals("raids")).forEach(entry -> entry.setValue(entry.getValue() * decayRate));
		return this;
	}
}
