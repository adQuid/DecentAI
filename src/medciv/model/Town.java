package medciv.model;

public class Town {

	String name;
	
	public Town(String name) {
		this.name = name;
	}
	
	public Town clone() {
		return new Town(name);
	}
	
}
