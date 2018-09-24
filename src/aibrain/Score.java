package aibrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score {

	private Map<String,Double> categories;

	//just used for debugging right now
	public List<Double> addAmounts = new ArrayList<Double>();
	
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
		//just for debugging
		other.addAmounts.add(this.totalScore());
		this.addAmounts = other.addAmounts;
		
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
		categories.entrySet().stream().forEach(entry -> entry.setValue(entry.getValue() * decayRate));
		return this;
	}
}
