package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

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
		ArrayList<Tag> tagArray=TagListController.getTagList().getTags();
		tagArray.set(tagArray.indexOf(tag), new Tag(newName));
		tagList.setTagList(tagArray);
	}
	
    public static void onAddTagClick(DialogInterface dialog) {
        EditText nameField=((EditText) ((AlertDialog) dialog).findViewById(R.id.simpleEditText));
        String name=nameField.getText().toString();
        TagListController.addTag(new Tag(name));
    }
    
    public static void onSetTagClick(DialogInterface dialog, Tag tag) {
 	   	EditText nameField=((EditText) ((AlertDialog) dialog).findViewById(R.id.simpleEditText));
        String name=nameField.getText().toString();
        TagListController.updateTag(tag, name);;
    }

    public static void onRemoveTagClick(DialogInterface dialog, Tag tag) {
    	TagListController.removeTag(tag);
    }
}
