package actions;

import java.util.List;

public class Action {

	private ActionType type;
	private List<Object> params;
	
	public Action(ActionType type, List<Object> params) {
		super();
		this.type = type;
		this.params = params;
	}
	public Action(Action other){
		this.type = other.type;
		this.params = other.params;//somewhat dangerous
	}
	public ActionType getType() {
		return type;
	}
	public void setType(ActionType type) {
		this.type = type;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
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
		Action other = (Action) obj;
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
		return type.name();
	}
}
