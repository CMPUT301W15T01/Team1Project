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

package dataManagers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.TagList;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity.Header;
import android.util.Log;

/**
 * Will provide an interface to save the associated TagList to the disk and when possible to the web server (not yet implemented).
 *
 *
 */
public class TagListManager {
	private static final String BASE_URL="http://cmput301.softwareprocess.es:8080/cmput301w15t01/";
	private String RESOURCE_URL;
	private TagList tagList;
	private Context context;
	private String claimantName;
	
	/**
	 * Initialize with the tagList to be managed.
	 * @param tagList The TagList to saved from and loaded to
	 */
	public TagListManager(TagList tagList){
		this.tagList=tagList;
		this.claimantName="Guest";
		this.RESOURCE_URL=BASE_URL+claimantName+"_tags/1";
	}

	public void setClaimantName(String claimantName) {
		this.claimantName=claimantName;
		this.RESOURCE_URL=BASE_URL+claimantName+"_tags/1";
	}

	/**
	 * save Tags to disk (and if possible to web server). (not yet implemented)
	 */
	private void saveTagsToDisk(ArrayList<Tag> tags){
		Gson gson = new Gson();
		try {
			FileOutputStream fos = context.openFileOutput(claimantName+"_tags.sav", 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(tags, osw);
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
	
	private void saveTagsToWeb(final ArrayList<Tag> tags){
		if(NetworkAvailable()){
			Thread t=new Thread(new Runnable() {
		        public void run() {
		        	Log.d("onlineTest", RESOURCE_URL);
					HttpPost httpPost = new HttpPost(RESOURCE_URL);
					StringEntity stringentity = null;
					Gson gson= new Gson();
					HttpClient httpclient = new DefaultHttpClient();
					try {
						TagListWrapper wrappedTags=new TagListWrapper(tags);
						stringentity = new StringEntity(gson.toJson(wrappedTags));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					httpPost.setHeader("Accept","application/json");
			
					httpPost.setEntity(stringentity);
					HttpResponse response = null;
					try {
						Log.d("onlineTest", "Executing query");
						response = httpclient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						Log.d("onlineTest", e.getCause()+":"+e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("onlineTest", e.getCause()+":"+e.getMessage());
					}
					Log.d("onlineTest", "TagList saved to web");
//					String status = response.getStatusLine().toString();
//					Log.d("onlineTest", status);
//					//do something with this response if necessary
//					HttpEntity entity = response.getEntity();
		        }
			});
			t.start();
		}
	}
	
	public void saveTags(){
		saveTagsToDisk(tagList.getTags());
		saveTagsToWeb(tagList.getTags());
	}
	
	
	
	/**
	 * load Tags from disk (and if possible sync tags with web server). (not yet implemented)
	 * @return Loaded tag list
	 */
	private ArrayList<Tag> loadTagsFromDisk(){
		Gson gson = new Gson();
		ArrayList<Tag> tags=new ArrayList <Tag>();
		try {
			FileInputStream fis = context.openFileInput(claimantName+"_tags.sav");
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
		return tags;
	}
	
	private ArrayList<Tag> loadTagsFromWeb(){
		final ArrayList<Tag> tags=new ArrayList<Tag>();
		if(NetworkAvailable()){
			Thread t=new Thread(new Runnable() {
		        public void run() {
		        	HttpClient httpclient = new DefaultHttpClient();
		        	try{
		        		Gson gson = new Gson();
		        		TagListWrapper loadedTags;;
		    			HttpGet getRequest = new HttpGet(RESOURCE_URL);

		    			getRequest.addHeader("Accept","application/json");

		    			HttpResponse response = httpclient.execute(getRequest);

		    			String status = response.getStatusLine().toString();
		    			System.out.println(status);

		    			String json = getEntityContent(response);

		    			// We have to tell GSON what type we expect
		    			Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<TagListWrapper>>(){}.getType();
		    			// Now we expect to get a tag response
		    			ElasticSearchResponse<TagListWrapper> esResponse = gson.fromJson(json, elasticSearchResponseType);
		    			//get the tags
		    			loadedTags = esResponse.getSource();
		    			if(loadedTags!=null){
			    			for(Tag tag: loadedTags.tags){
			    				tags.add(tag);
			    			}
		    			}

		    		} catch (ClientProtocolException e) {

		    			Log.d("onlineTest", e.getCause()+":"+e.getMessage());

		    		} catch (IOException e) {

		    			Log.d("onlineTest", e.getCause()+":"+e.getMessage());
		    		}
		        }
			});
			t.start();
			
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.d("onlineTest", e.getCause()+":"+e.getMessage());
			}
		}
		return tags;
	}
	
	
	public void loadTags(){
		//much simpler than equivalent for ClaimList, here we simply load the list from web if possible and use the local one
		//otherwise
		ArrayList<Tag> tags=loadTagsFromWeb();
		if(tags.isEmpty()){	
			tags=loadTagsFromDisk();
		}
		tagList.setTagList(tags);
	}
	
	
	/**
	 * From https://github.com/rayzhangcl/ESDemo March 28, 2015
	 * get the http response and return json string
	 */
	protected String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}
	
	/**
	 * Context must be set to save/load from file
	 * @param context
	 */
	public void setContext(Context context){
		this.context=context;
	}
	
	private boolean NetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	//Elastic search doesn't like saving arrayLists so I made this to save instead
	private static class TagListWrapper{
		public ArrayList<Tag> tags;
		TagListWrapper(ArrayList<Tag> tags){
			this.tags=tags;
		}
	}
}
