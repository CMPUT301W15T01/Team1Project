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
