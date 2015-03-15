package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.content.Context;

/**
 * Will provide an interface to save the associated TagList to the disk and when possible to the web server (not yet implemented).
 * @author kenny_789
 *
 */
public class TagListManager {
	private TagList tagList;
	
	/**
	 * Initialize with the tagList to be managed.
	 * @param tagList
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
	 * @return
	 */
	public ArrayList<Tag> loadTags(){
		return null;
		
	}
}
