package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

public class TagListController {
	private static ArrayList<Tag> tagList=null;
	private static ArrayList <Listener> listeners;

	public static ArrayList<Tag> getTags() { 
		if (tagList == null) {
			tagList = new ArrayList<Tag>();
		}
		
		return tagList;
	}
	
	public static void addTag(String name){
		
	}
	
	public static void removeTag(String name){
		
	}
	
	public static void updateTag(String newName, String oldName){
		
	}
	
	public static void saveTagList(){
		
	}
	
	public static void addListener(Listener listener){
		
	}
	
	private static void notifyListeners(){
		
	}
	
	public static int tagCount() {
		return 0;
	}

}
