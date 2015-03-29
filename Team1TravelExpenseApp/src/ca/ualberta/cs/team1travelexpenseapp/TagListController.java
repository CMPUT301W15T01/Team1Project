/*
Copyright 2015 Jeffrey Oduro, Cody Ingram, Boyan Peychoff, Kenny Young, Dennis Truong, Victor Olivares 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
/**
 * Controller for TagList, uses lazy singleton design pattern. 
 * The contained TagList will be automatically created when getTagList is called for the first time.
 * Provides a set of methods which allow the TagListManagerActivity view to modify the underlying TagList data.
 */
public class TagListController {
	private TagList tagList;

	/**
	 * Return the controlled TagList, will be created on the first call.
	 * @return The controlled TagList
	 */
	TagListController(TagList tagList){
		this.tagList=tagList;
	}
	
	/**
	 * Add a Tag to the TagList.
	 * @param tag The Tag to be added
	 */
	public void addTag(Tag tag){
		ArrayList<Tag> tagArray=tagList.getTags();
		tagArray.add(tag);
		tagList.setTagList(tagArray);
	}
	
	/**
	 * Remove the passed tag from the TagList.
	 * @param tag The Tag to be removed
	 */
	public void removeTag(Tag tag){
		ArrayList<Tag> tagArray=tagList.getTags();
		tagArray.remove(tag);
		tagList.setTagList(tagArray);
	}
	
	/**
	 * Change the name associated with tag to newName.
	 * @param tag The Tag to be modified
	 * @param newName A String to be used as the new name for tag
	 */
	public void updateTag(Tag tag, String newName){
		ArrayList<Tag> tagArray=tagList.getTags();
		tagArray.set(tagArray.indexOf(tag), new Tag(newName));
		tagList.setTagList(tagArray);
	}
	
	/**
	 * Called by TagManagerActivity when the add tag button in the newTagDialog is clicked.
	 * Grabs the string entered by the user and creates a new tag by that name in the TagList.
	 * @param dialog The Dialog in which the add tag button resides
	 */
    public boolean onAddTagClick(DialogInterface dialog) {
        EditText nameField=((EditText) ((AlertDialog) dialog).findViewById(R.id.simpleEditText));
        String name=nameField.getText().toString();
        if(tagList.hasTagNamed(name)){
        	return false;
        }
        else{
        addTag(new Tag(name));
        return true;
        }
    }
    
    /**
     * Called by TagManagerActivity when the set tag button in the editTagDialog is clicked.
     * Grabs the string entered by the user and sets the name of the selected tag to that name.
     * @param dialog The Dialog in which the set tag button resides
     * @param tag The Tag to be modified
     */
    public boolean onSetTagClick(DialogInterface dialog, Tag tag) {
 	   	EditText nameField=((EditText) ((AlertDialog) dialog).findViewById(R.id.simpleEditText));
        String name=nameField.getText().toString();
        if(tagList.hasTagNamed(name)){
        	return false;
        }
        else{
        updateTag(tag, name);
        return true;
        }
    }

    /**
     * Called by TagManagerActivity when the remove tag button in the editTagDialog is clicked.
     * Removes the selected tag from the TagList
     * @param dialog The Dialog in which remove tag button resides
     * @param tag A Tag to be removed
     */
    public void onRemoveTagClick(DialogInterface dialog, Tag tag) {
    	removeTag(tag);
    }
    
    /**
     * Reset the TagList to a new TagList removing all it's old contents.
     */
    public void clearTagList(){
    	tagList=new TagList();
    }
    
}
