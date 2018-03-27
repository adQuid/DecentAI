package gameload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.Game;

public class GameLoader {

	public static Game loadWorld(String name){
		try{
			ObjectMapper mapper = new ObjectMapper();
			
			Game retval = mapper.reader(Game.class).readValue(new File("saves/"+name+".gam"));
			retval.setLive(true);
			
			return retval;
		}catch(Exception e){
			e.printStackTrace();
			JFrame errorMessage = new JFrame("Error");
			errorMessage.add(new JLabel("Failed to load game file : "+name+".gam"));
			errorMessage.pack();
			errorMessage.setVisible(true);
			return null;
		}
	}
	
	public static void saveWorld(Game world, String name){
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			mapper.writer().writeValue(new BufferedWriter(new FileWriter(new File("saves/"+name+".gam"))), world);
		} catch (Exception e) {
			JFrame errorMessage = new JFrame("Error");
			errorMessage.add(new JLabel("Failed to save game file : "+name+".gam"));
			errorMessage.pack();
			errorMessage.setVisible(true);
			e.printStackTrace();
		}
	}
	
	
}
