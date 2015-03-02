package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

public class TagListController {
	private static TagList tagList=null;

	public static TagList getTags() { 
		if (tagList == null) {
			tagList = new TagList();
		}
		
		return tagList;
	}
	
	public static void addTag(String name){
		
	}
	
	public static void removeTag(String name){
		
	}
	
	public static void updateTag(String newName, String oldName){
		
	}
	
	public static int tagCount() {
		return 0;
	}

}
