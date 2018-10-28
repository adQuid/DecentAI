package aibrain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score {

	private List<Map<String,BigDecimal>> layers = new ArrayList<Map<String,BigDecimal>>();

	
	public Score() {
		
	}
		
	//probably going to make this into a seperate object soon enough
	public Score(Map<String,BigDecimal> layer) {
		addLayer(layer);
	}
	
	public Score(Score other) {
		for(Map<String,BigDecimal> layer: other.layers) {
			layers.add(new HashMap(layer));//these only get made in this class, so I know it's always hashmap
		}
	}
	
	public Map<String, BigDecimal> getFirstLayer() {
		return layers.get(0);
	}
	
	public Map<String, BigDecimal> getLastLayer() {
		return layers.get(layers.size()-1);
	}

	public Score addLayer(Map<String, BigDecimal> categories) {
		layers.add(categories);
		
		return this;
	}
	
	public double totalScore() {
		double retval = 0;
		
		for(Map<String,BigDecimal> layer: layers) {
			retval += layer.values().stream().mapToDouble(Number::doubleValue).sum();
		}
		
		return retval;
	}
	
	public Score add(Score other) {
		
		for(int index=0; index<Math.max(this.layers.size(), other.layers.size()); index++) {
			if(index < this.layers.size()) {
				for(String key: other.layers.get(index).keySet()) {
					if(this.layers.get(index).containsKey(key)) {
						this.layers.get(index).put(key, this.layers.get(index).get(key).add(other.layers.get(index).get(key)));
					} else {
						this.layers.get(index).put(key, other.layers.get(index).get(key));
					}
				}
			}
		}
		
		return this;
	}
	
	public Score withoutFirstRound() {
		Score retval = new Score(this);
		
		retval.layers.remove(0);
		
		return retval;
	}
	
	public Score decay(double decayRate) {
		for(Map<String,BigDecimal> layer: layers) {
			layer.entrySet().stream().filter(entry -> !entry.getKey().equals("raids")).forEach(entry -> entry.setValue(entry.getValue().multiply(new BigDecimal(decayRate))));
		}
		return this;
	}
	
	public String toString() {
		return Double.toString(totalScore());
	}
}
