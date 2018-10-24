package medciv.model;

public class Town {

	private static int lastId=0;
	private int id;
	String name;
	
	public Town(String name) {
		this.name = name;
		this.id = lastId++;
	}
	
	public Town(String name, int id) {
		this.name = name;
		this.id = id;
	}
		
	public Town clone(MedcivGame game) {
		return new Town(name,this.id);
	}
	
	public int getId() {
		return id;
	}
}
