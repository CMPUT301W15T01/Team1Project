package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.content.Context;

/**
 * Will provide an interface to save the associated TagList to the disk and when possible to the web server (not yet implemented).
 *
 *
 */
public class TagListManager {
	private TagList tagList;
	
	/**
	 * Initialize with the tagList to be managed.
	 * @param tagList The TagList to saved from and loaded to
	 */
	TagListManager(TagList tagList){
		this.tagList=tagList;
	}
	
	/**
	 * saveTags to disk (and if possible to web server). (not yet implemented)
	 */
	public void saveTags(){
		
	}
	
	/**
	 * load Tags from disk (and if possible sync tags with web server). (not yet implemented)
	 * @return Loaded tag list
	 */
	public ArrayList<Tag> loadTags(){
		return null;
		
	}
}
