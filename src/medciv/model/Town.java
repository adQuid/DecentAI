package medciv.model;

public class Town {

	String name;
	
	public Town(String name) {
		this.name = name;
	}
	
	public Town clone(MedcivGame game) {
		return new Town(name);
	}
	
}
