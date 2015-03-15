package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
/**
 * Stores a list of tags along with a set of listeners to be updated whenever the list is modified.
 * Will eventually provide an interface to a TagListManager which will save the list to disk and the web when connected.
 *
 */
public class TagList {
	private ArrayList<Tag> tagList;
	private ArrayList<Listener> listeners;
	private static TagListManager manager;
	
	TagList(){
		manager=new TagListManager(this);
		tagList=new ArrayList<Tag>();
		listeners=new ArrayList<Listener>();
	}
	
	/**
	 * Save the stored tagList to disk/the web server (not yet implemented).
	 */
	public void saveTagList(){
		manager.saveTags();
	}
	/**
	 * Add a new listener to be updated whenever the tagList is changed.
	 * @param listener
	 */
	public void addListener(Listener listener){
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener so that it will no longer be updated when the tagList is changed.
	 * @param listener
	 */
	public void removeListener(Listener listener){
		listeners.remove(listener);
	}
	
	/**
	 * Call update method on all listeners (called on tagList changes).
	 */
	private void notifyListeners(){
		for(int i=0; i<listeners.size();i++){
			listeners.get(i).update();
		}
	}

	/**
	 * Returns the stored tagList.
	 * @return
	 */
	public ArrayList<Tag> getTags() {
		return tagList;
	}

	/**
	 * Set the stored tagList, will automatically update listeners and save the list once saving is implemented.
	 * @param tagList
	 */
	public void setTagList(ArrayList<Tag> tagList) {
		this.tagList = tagList;
		notifyListeners();
		saveTagList();
	}
}
