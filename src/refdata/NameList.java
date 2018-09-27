package refdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import spacegame.model.Tile;

public class NameList {

	static Random die = new Random();
	
	static int index = 1;
	
	static List<String> humanColonyNames = new ArrayList<String>();
	
	static{
		try {
			Scanner planetNameReader;

			planetNameReader = new Scanner(new File("names/human_colonies.txt"));
			
			while(planetNameReader.hasNextLine()){
				humanColonyNames.add(planetNameReader.nextLine());
			}
			
			planetNameReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String generatePlanetName(Tile tile){
		String retval = "Sollis ";//name of system
		
		retval += Math.max(Math.abs(tile.getX()-5), Math.abs(tile.getY()-5));
		
		return retval;
	}
	
	public static String randomColonyName(){
		return humanColonyNames.get(index++);
		/*if(humanColonyNames.size() == 0){
			return "Planet";
		}else{
			int index = die.nextInt(humanColonyNames.size());
			return humanColonyNames.remove(index);
		}*/
	}
	
}
