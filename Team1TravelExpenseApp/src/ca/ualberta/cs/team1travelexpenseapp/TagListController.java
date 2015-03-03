package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

public class TagListController {
	private static TagList tagList=null;

	public static TagList getTagList() { 
		if (tagList == null) {
			tagList = new TagList();
		}
		
		return tagList;
	}
	
	public static void addTag(Tag tag){
		ArrayList<Tag> tagArray=TagListController.getTagList().getTags();
		tagArray.add(tag);
		tagList.setTagList(tagArray);
	}
	
	public static void removeTag(Tag tag){
		ArrayList<Tag> tagArray=TagListController.getTagList().getTags();
		tagArray.remove(tag);
		tagList.setTagList(tagArray);
	}
	
	public static void updateTag(Tag tag, String newName){
		tag.setName(newName);
	}
	
	public static int tagCount() {
		ArrayList<Tag> tagArray=TagListController.getTagList().getTags();
		return tagArray.size();
	}

}
