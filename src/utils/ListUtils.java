package utils;

import java.util.ArrayList;
import java.util.List;

import aibrain.Action;

public class ListUtils {

	//there has to be a better way, but that's not the point of this
	public static List<Action> combine(List<Action> list1, List<Action> list2){
		List<Action> union = new ArrayList<Action>();
		union.addAll( list1 );
		union.addAll( list2 );
		
		return union;
	}
	
}
