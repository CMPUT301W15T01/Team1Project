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
/**
 * Stores a list of tags (as an ArrayList) along with a set of listeners to be updated whenever the list is modified.
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
	 * @param listener The listener to be added
	 */
	public void addListener(Listener listener){
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener so that it will no longer be updated when the tagList is changed.
	 * @param listener The listener to be removed
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
	 * @return ArrayList of available tags
	 */
	public ArrayList<Tag> getTags() {
		return tagList;
	}

	/**
	 * Set the stored tagList, will automatically update listeners and save the list once saving is implemented.
	 * @param tagList The ArrayList of Tags to be set
	 */
	public void setTagList(ArrayList<Tag> tagList) {
		this.tagList = tagList;
		notifyListeners();
		saveTagList();
	}
}
