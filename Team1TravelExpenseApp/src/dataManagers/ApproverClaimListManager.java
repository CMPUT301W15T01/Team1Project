package dataManagers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.gsonUtils.GsonUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApproverClaimListManager extends ClaimListManager{
	
	public ApproverClaimListManager(ClaimList claimList){
		super();
		this.claimList=claimList;
	}
	
	private void saveClaimToWeb(final Claim claim){
		if(NetworkAvailable()){
			
			Thread t=new Thread(new Runnable() {
		        public void run() {
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
						claim.setSynced(true);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						Log.d("onlineTest", e.getCause()+":"+e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("onlineTest", e.getCause()+":"+e.getMessage());
					}
			
					String status = response.getStatusLine().toString();
					Log.d("onlineTest", status);
					//do something with this response if necessary
					HttpEntity entity = response.getEntity();
		        }
			});
			t.start();
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
						HttpGet searchRequest = new HttpGet(SEARCH_URL+"?q=type:SubmittedClaim");
						searchRequest.setHeader("Accept","application/json");
						HttpResponse response = httpclient.execute(searchRequest);
						String json = getEntityContent(response);
						Log.d("onlineTest", json);
						Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Claim>>(){}.getType();
						ElasticSearchSearchResponse<Claim> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
						Collection <ElasticSearchResponse<Claim>> esResponseList = esResponse.getHits();
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
		return claims;
	}

	@Override
	public void saveClaims() {
		ArrayList<Claim> claims=claimList.getClaims();
		saveClaimsToWeb(claims);	
		
	}
	
	public void removeClaim(Claim claim){
		//do nothing approvers cannot remove claims at this time
	}

	@Override
	public void loadClaims() {
		claimList.setClaimList(loadClaimsFromWeb());
		
	}
	
	public void onConnect(){
		loadClaims();
	}
}
