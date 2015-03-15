package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
/**
 * Controller for TagList, uses lazy singleton design pattern. 
 * The contained TagList will be automatically created when getTagList is called for the first time.
 * Provides a set of methods which allow the TagListManagerActivity view to modify the underlying TagList data.
 * @author Team1
 */
public class TagListController {
	private static TagList tagList=null;

	/**
	 * Return the controlled TagList, will be created on the first call.
	 * @return The controlled TagList
	 */
	public static TagList getTagList() { 
		if (tagList == null) {
			tagList = new TagList();
		}
		
		return tagList;
	}
	
	/**
	 * Add a Tag to the TagList.
	 * @param tag The Tag to be added
	 */
	public static void addTag(Tag tag){
		ArrayList<Tag> tagArray=TagListController.getTagList().getTags();
		tagArray.add(tag);
		tagList.setTagList(tagArray);
	}
	
	/**
	 * Remove the passed tag from the TagList.
	 * @param tag The Tag to be removed
	 */
	public static void removeTag(Tag tag){
		ArrayList<Tag> tagArray=TagListController.getTagList().getTags();
		tagArray.remove(tag);
		tagList.setTagList(tagArray);
	}
	
	/**
	 * Change the name associated with tag to newName.
	 * @param tag The Tag to be modified
	 * @param newName A String to be used as the new name for tag
	 */
	public static void updateTag(Tag tag, String newName){
		ArrayList<Tag> tagArray=TagListController.getTagList().getTags();
		tagArray.set(tagArray.indexOf(tag), new Tag(newName));
		tagList.setTagList(tagArray);
	}
	
	/**
	 * Called by TagManagerActivity when the add tag button in the newTagDialog is clicked.
	 * Grabs the string entered by the user and creates a new tag by that name in the TagList.
	 * @param dialog The Dialog in which the add tag button resides
	 */
    public static void onAddTagClick(DialogInterface dialog) {
        EditText nameField=((EditText) ((AlertDialog) dialog).findViewById(R.id.simpleEditText));
        String name=nameField.getText().toString();
        TagListController.addTag(new Tag(name));
    }
    
    /**
     * Called by TagManagerActivity when the set tag button in the editTagDialog is clicked.
     * Grabs the string entered by the user and sets the name of the selected tag to that name.
     * @param dialog The Dialog in which the set tag button resides
     * @param tag The Tag to be modified
     */
    public static void onSetTagClick(DialogInterface dialog, Tag tag) {
 	   	EditText nameField=((EditText) ((AlertDialog) dialog).findViewById(R.id.simpleEditText));
        String name=nameField.getText().toString();
        TagListController.updateTag(tag, name);;
    }

    /**
     * Called by TagManagerActivity when the remove tag button in the editTagDialog is clicked.
     * Removes the selected tag from the TagList
     * @param dialog The Dialog in which remove tag button resides
     * @param tag A Tag to be removed
     */
    public static void onRemoveTagClick(DialogInterface dialog, Tag tag) {
    	TagListController.removeTag(tag);
    }
}
