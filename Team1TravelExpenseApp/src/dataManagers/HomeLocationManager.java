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

import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceActivity.Header;
import android.util.Log;

/**
 * Will provide an interface to save the user's home location to the disk and to the web.
 *
 *
 */
public class HomeLocationManager {
	private static final String BASE_URL="http://cmput301.softwareprocess.es:8080/cmput301w15t01/";
	private String RESOURCE_URL;
	private Context context;
	private String claimantName;
	
	/**
	 * Initialize with the location to be managed.
	 * @param location The Location to saved from and loaded to
	 */
	public HomeLocationManager(String claimantName){
		this.claimantName=claimantName;
		this.RESOURCE_URL=BASE_URL+claimantName+"_location/1";
	}

	/**
	 * save passed location to disk in a directory specified by the claimantName attribute.
	 */
	private void saveLocationToDisk(Location location){
		Gson gson = new Gson();
		try {
			FileOutputStream fos = context.openFileOutput(claimantName+"_location.sav", 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(location, osw);
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
	 * Save the passes location to the web server at a location specified by the claimantName attribute.
	 * @param location
	 */
	private void saveLocationToWeb(final Location location){
		if(NetworkAvailable()){
			Thread t=new Thread(new Runnable() {
		        public void run() {
					HttpPost httpPost = new HttpPost(RESOURCE_URL);
					StringEntity stringentity = null;
					Gson gson= new Gson();
					HttpClient httpclient = new DefaultHttpClient();
					try {
						stringentity = new StringEntity(gson.toJson(location));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					httpPost.setHeader("Accept","application/json");
			
					httpPost.setEntity(stringentity);
					HttpResponse response = null;
					try {
						response = httpclient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						Log.d("onlineTest", e.getCause()+":"+e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("onlineTest", e.getCause()+":"+e.getMessage());
					}
		        }
			});
			t.start();
		}
	}
	
	/**
	 * Save the passed location to both the disk and webserver (if possible).
	 * @param location The location to be saved.
	 */
	public void saveLocation(Location location){
		saveLocationToDisk(location);
		saveLocationToWeb(location);
	}
	
	
	
	/**
	 * Load the claimants home location from disk.
	 * @return The loaded location
	 */
	private Location loadLocationFromDisk(){
		Gson gson = new Gson();
		Location location = new Location("");
		try {
			FileInputStream fis = context.openFileInput(claimantName+"_location.sav");
			InputStreamReader in =new InputStreamReader(fis);
			//Taken from http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html on Jan 20 2015
			Type typeOfT = new TypeToken<Location>(){}.getType();
			location = gson.fromJson(in, typeOfT);
			fis.close();

		} catch (FileNotFoundException e) {
			//if we can't find the save file create a new one and start fresh
			location = new Location("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
	}
	
	/**
	 * Load the claimants home location from the web.
	 * @return The loaded location
	 */
	private Location loadLocationFromWeb(){
		final Location location = new Location("");
		if(NetworkAvailable()){
			Thread t=new Thread(new Runnable() {
		        public void run() {
		        	HttpClient httpclient = new DefaultHttpClient();
		        	try{
		        		Gson gson = new Gson();
		    			HttpGet getRequest = new HttpGet(RESOURCE_URL);

		    			getRequest.addHeader("Accept","application/json");

		    			HttpResponse response = httpclient.execute(getRequest);

		    			String status = response.getStatusLine().toString();
		    			System.out.println(status);

		    			String json = getEntityContent(response);

		    			// We have to tell GSON what type we expect
		    			Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<Location>>(){}.getType();
		    			// Now we expect to get a location response
		    			ElasticSearchResponse<Location> esResponse = gson.fromJson(json, elasticSearchResponseType);
		    			//get the location
		    			Location loadedLocation = esResponse.getSource();
		    			
		    			
		    			if(loadedLocation != null){
			    			//set our location to match the loaded one
			    			location.setLatitude(loadedLocation.getLatitude());
			    			location.setLongitude(loadedLocation.getLongitude());
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
		return location;
	}
	
	
	/**
	 * Load the claimants home location from the web (or from the disk as a backup).
	 * @return
	 */
	public Location loadLocation(){
		//much simpler than equivalent for ClaimList, here we simply load the Location from web if possible and use the local one
		//otherwise
		Location location = loadLocationFromWeb();
		if(location.equals(new Location(""))){	
			location = loadLocationFromDisk();
		}
		return location;
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
	
	/**
	 * Do we have an internet connection?
	 * @return boolean Indicating whether or not we have a connection to the internet.
	 */
	private boolean NetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
