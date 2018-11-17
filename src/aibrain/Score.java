package aibrain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score {

	public final static int PRECISION = 5;
	
	private List<Map<String,BigDecimal>> layers = new ArrayList<Map<String,BigDecimal>>();	
	
	public Score() {
		
	}
		
	//probably going to make this into a seperate object soon enough
	public Score(Map<String,BigDecimal> layer) {
		addLayer(layer);
	}
	
	Score(Score other) {
		for(Map<String,BigDecimal> layer: other.layers) {
			layers.add(new HashMap(layer));//these only get made in this class, so I know it's always hashmap
		}
	}
	
	Map<String, BigDecimal> getFirstLayer() {
		return layers.get(0);
	}
	
	Map<String, BigDecimal> getLastLayer() {
		return layers.get(layers.size()-1);
	}

	Score addLayer(Map<String, BigDecimal> categories) {
		layers.add(categories);
		
		return this;
	}
	
	public BigDecimal totalScore() {
		BigDecimal retval = new BigDecimal(0);
		
		for(Map<String,BigDecimal> layer: layers) {
			for(BigDecimal value: layer.values()) {
			retval = retval.add(value);
			}
		}
		
		return retval.setScale(PRECISION, BigDecimal.ROUND_HALF_UP);
	}
	
	Score add(Score other) {
		
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
	
	Score withoutFirstRound() {
		Score retval = new Score(this);
		
		retval.layers.remove(0);
		
		return retval;
	}
	
	Score decay(double decayRate) {
		for(Map<String,BigDecimal> layer: layers) {
			layer.entrySet().stream().filter(entry -> !entry.getKey().equals("raids")).forEach(entry -> entry.setValue(entry.getValue().multiply(new BigDecimal(decayRate))));
		}
		return this;
	}
	
	public String toString() {
		return compressedCategories().toString();
	}
	
	Map<String,BigDecimal> compressedCategories() {
		Map<String,BigDecimal> retval = new HashMap<String,BigDecimal>();
		
		for(Map<String,BigDecimal> layer: layers) {
			for(String key: layer.keySet()) {
				if(retval.containsKey(key)) {
					retval.put(key, retval.get(key).add(layer.get(key).setScale(PRECISION, BigDecimal.ROUND_HALF_UP)));
				} else {
					retval.put(key, layer.get(key).setScale(PRECISION, BigDecimal.ROUND_HALF_UP));
				}
			}
		}
		
		return retval;
	}
}
