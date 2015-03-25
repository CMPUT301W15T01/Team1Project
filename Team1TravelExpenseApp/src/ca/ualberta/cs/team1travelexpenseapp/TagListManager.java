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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;

/**
 * Will provide an interface to save the associated TagList to the disk and when possible to the web server (not yet implemented).
 *
 *
 */
public class TagListManager {
	private TagList tagList;
	private Context context;
	private String userName;
	
	/**
	 * Initialize with the tagList to be managed.
	 * @param tagList The TagList to saved from and loaded to
	 */
	TagListManager(TagList tagList, String userName){
		this.tagList=tagList;
		this.userName=userName;
	}
	
	/**
	 * save Tags to disk (and if possible to web server). (not yet implemented)
	 */
	public void saveTags(){
		Gson gson = new Gson();
		String saveFile=userName+"_tags.sav";
		try {
			FileOutputStream fos = context.openFileOutput(saveFile, 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(tagList.getTags(), osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * load Tags from disk (and if possible sync tags with web server). (not yet implemented)
	 * @return Loaded tag list
	 */
	public void loadTags(){
		Gson gson = new Gson();
		ArrayList<Tag> tags=new ArrayList <Tag>();
		String saveFile=userName+"_tags.sav";
		try {
			FileInputStream fis = context.openFileInput(saveFile);
			InputStreamReader in =new InputStreamReader(fis);
			//Taken from http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html on Jan 20 2015
			Type typeOfT = new TypeToken<ArrayList<Tag>>(){}.getType();
			tags = gson.fromJson(in, typeOfT);
			fis.close();

		} catch (FileNotFoundException e) {
			//if we can't find the save file create a new one and start the TagList fresh
			tagList.setTagList(new ArrayList<Tag>());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tagList.setTagList(tags);
		
	}
	
	/**
	 * Context must be set to save/load from file
	 * @param context
	 */
	public void setContext(Context context){
		this.context=context;
	}
}
