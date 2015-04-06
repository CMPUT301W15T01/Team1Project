package dataManagers;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.gsonUtils.GsonUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClaimantClaimListManager extends ClaimListManager {
	private String claimantName;
	private ConnectionChangeReceiver reciever;
	
	/**
	 * Initialize with the claimList to be managed.
	 * @param claimList The ClaimList to saved from and loaded to
	 */
	public ClaimantClaimListManager(ClaimList claimList){
		super();
		this.claimList=claimList;
		this.claimantName="Guest";
		this.reciever=new ConnectionChangeReceiver(this);
		
	}
	
	/**
	 * save Claims to disk (and if possible to web server). (not yet implemented)
	 */
	public void saveClaims(){
		Log.d("approvalTest", "Claimant saved");
		final ArrayList<Claim> claims=claimList.getClaims();
		final ArrayList<Claim> unsyncedClaims=new ArrayList<Claim>();
		for(Claim claim: claimList.getClaims()){
			if(!claim.isSynced()){
				unsyncedClaims.add(claim);
			}
		}
		Thread t=new Thread(new Runnable() {
	        public void run() {
				saveClaimsToWeb(unsyncedClaims);
				saveClaimsToDisk(claims); 
				}
		});
		t.start();
	}
	
	
	private void saveClaimToWeb(final Claim claim){
		if(NetworkAvailable()){
			HttpPost httpPost = new HttpPost(RESOURCE_URL+claim.getUniqueId());
			StringEntity stringentity = null;
			Gson gson= GsonUtils.getGson();
			HttpClient httpclient = new DefaultHttpClient();
			try {
				stringentity = new StringEntity(gson.toJson(claim,Claim.class));
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
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("onlineTest", e.getCause()+":"+e.getMessage());
				return;
			}
			int statusCode=response.getStatusLine().getStatusCode();
			
			if(statusCode==200){
				//claim is synced if it is successfully saved to web
				claim.setSynced(true);
			}
        }
	}
	
	private void saveClaimsToDisk(ArrayList<Claim> claims){
		Gson gson= GsonUtils.getGson();
		if(context==null){
			//we can't save to disk without context so if it isn't set just don't
			return;
		}
		try {
			FileOutputStream fos = context.openFileOutput(claimantName+"_claims.sav", 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			Type typeOfT = new TypeToken<ArrayList<Claim>>(){}.getType();
			gson.toJson(claims, typeOfT, osw);
			osw.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveClaimsToWeb(ArrayList<Claim> claims){
		for(Claim claim: claims){
			saveClaimToWeb(claim);
		}
	}
	
	/**
	 * Load and return all claims corresponding to the claimantName for this manager from the web server
	 * @return ArrayList<Claim> containing all claims made by the claimant
	 */
	private ArrayList<Claim> loadClaimsFromWeb(){
		final ArrayList<Claim> claims=new ArrayList<Claim>();
		if(NetworkAvailable()){
			Thread t=new Thread(new Runnable() {
		        public void run() {
		        	Gson gson= GsonUtils.getGson();
		        	HttpClient httpclient = new DefaultHttpClient();
					try {
						HttpGet searchRequest = new HttpGet(SEARCH_URL+"?q=claim.claimantName:"+claimantName);
						searchRequest.setHeader("Accept","application/json");
						HttpResponse response = httpclient.execute(searchRequest);
						String json = getEntityContent(response);
						Log.d("onlineTest", json);
						Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Claim>>(){}.getType();
						ElasticSearchSearchResponse<Claim> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
						Collection <ElasticSearchResponse<Claim>> esResponseList;
						if(esResponse!=null){
							esResponseList = esResponse.getHits();
						}
						else{
							//if response is null just exit thread and return the empty claimsList
							return;
						}
						for(ElasticSearchResponse<Claim> result: esResponseList){
							Log.d("onlineTest", "in web load function:"+result.getSource().toString());
							claims.add(result.getSource());
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
				e.printStackTrace();
			}
		}
		for(Claim claim: claims){
			claim.setSynced(true);
		}
		return claims;
	}
	
	
	private ArrayList<Claim> loadClaimsFromDisk(){
		Gson gson= GsonUtils.getGson();
		ArrayList<Claim> claims = null;
		if(context==null){
			//we can't load from disk without context so if it isn't set just don't
			return claims;
		}
		try {
			FileInputStream fis = context.openFileInput(claimantName+"_claims.sav");
			InputStreamReader in =new InputStreamReader(fis);
			//Taken from http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html on Jan 20 2015
			Type typeOfT = new TypeToken<ArrayList<Claim>>(){}.getType();
			claims = gson.fromJson(in, typeOfT);
			fis.close();

		} catch (FileNotFoundException e) {
			//if we can't find the save file create a new one and start the ClaimList fresh
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(claims==null){
			claims = new ArrayList<Claim>();
		}
		return claims;
		
	}
	
	/**
	 * Remove the passed Claim from the web server.
	 */
	public void removeClaim(final Claim claim){
		if(NetworkAvailable()){
			Thread t=new Thread(new Runnable() {
		        public void run() {
		        	HttpClient httpclient = new DefaultHttpClient();
					HttpDelete httpDelete = new HttpDelete(RESOURCE_URL+claim.getUniqueId().toString());
					Log.d("onlineTest", RESOURCE_URL+claim.getUniqueId().toString());
					httpDelete.addHeader("Accept","application/json");
					
					HttpResponse response=null;
					try {
						//do something with this response if nessesary
						response = httpclient.execute(httpDelete);
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
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * load Claims from disk (and if possible sync claims with web server). (not yet implemented)
	 * @return Loaded claim list
	 */
	public void loadClaims(){
		//no loading from disk for now it breaks things, also loading from online is not working atm working on this
		ArrayList<Claim> localClaims = loadClaimsFromDisk();
		ArrayList<Claim> webClaims = loadClaimsFromWeb();
		
		
		//if webClaims is empty, either this user has no saved claims online or the connection failed, either way just use the locally save list
		if(webClaims.isEmpty()){
			claimList.setClaimList(localClaims);
		}
		
		//if webClaims is not empty then we want to use the web version of all claims except those that have been locally modified and not synced, any claims which are synced
		//but are not on the web server are assumed to have been deleted elsewhere
		else{
			ArrayList<Claim> claims = new ArrayList <Claim>();
			
			for(Claim claim: localClaims){
				Log.d("onlineTest", "From Local:"+claim.toString());
				if(!claim.isSynced()){
					Log.d("approvalTest", "Claim not synced");
					claims.add(claim);
				}
			}
			
			for(Claim claim: webClaims){
				boolean exists=false;
				Log.d("onlineTest", "From web:"+claim.toString());
				for(Claim existingClaim: claims){
					if (claim.getUniqueId().equals(existingClaim.getUniqueId())){
						exists=true;
					}
				}
				if(!exists){
					claims.add(claim);
				}
			}
			claimList.setClaimList(claims);
		}
		//after loading from both sources attempt to saveClaims in order to sync online with local
		saveClaims();
		
		//claim of expense list is transient to avoid circular reference, set it again on load
		for(Claim claim: claimList.getClaims()){
			claim.getExpenseList().setClaim(claim);
		}
		
	}
	
	public void setClaimantName(String claimantName) {
		this.claimantName = claimantName;
	}
	
	public void onConnect(){
		ArrayList<Claim> unsyncedClaims=new ArrayList<Claim>();
		for(Claim claim: claimList.getClaims()){
			if(!claim.isSynced()){
				unsyncedClaims.add(claim);
			}
		}
		saveClaimsToWeb(unsyncedClaims);
	}
	
	@Override
	public void setContext(Context context){
		super.setContext(context);
		IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		context.registerReceiver(reciever, intentFilter);
	}
	
}
