package actions;

import java.util.List;

public interface Action {
		
	public boolean equals(Object obj);
	
	public Object getParam(String param);
	
	public String toString();
	
}
