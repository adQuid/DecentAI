package spacegame;

import java.util.Map;

import aibrain.Action;
import model.ActionType;

public class SpaceGameAction implements Action{

	private ActionType type;
	private Map<String,Object> params;
	private boolean contingency = false;
	
	private int order = 5;
	
	public SpaceGameAction(ActionType type, Map<String,Object> params) {
		super();
		this.type = type;
		this.params = params;
		if(this.type == ActionType.defend) {
			this.order = 6;
		}
	}
	public SpaceGameAction(SpaceGameAction other){
		this.type = other.type;
		this.params = other.params;//somewhat dangerous
		this.order = other.order;
	}
	public ActionType getType() {
		return type;
	}
	public Map<String,Object> getParams() {
		return params;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpaceGameAction other = (SpaceGameAction) obj;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	public String toString() {
		if(params.size() > 0) {
			return type.name()+" "+params.toString();
		}else {
			return type.name();
		}
	}
	@Override
	public Object getParam(String param) {
		return params.get(param);
	}
	public void setParam(String param, Object value) {
		params.put(param, value);
	}
	
	
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public void setContingency(boolean contingency) {
		this.contingency = contingency;
	}
	@Override
	public boolean isContingency() {
		return contingency;
	}
}
