package ca.ualberta.cs.team1travelexpenseapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClaimantClaimListManager {
	private ClaimList claimList;
	private Context context;
	private String claimantName;
	private static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/";
	private static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/claim/_search";
	private HttpClient httpclient = new DefaultHttpClient();
	
	

	/**
	 * Initialize with the claimList to be managed.
	 * @param claimList The ClaimList to saved from and loaded to
	 */
	ClaimantClaimListManager(ClaimList claimList){
		this.claimList=claimList;
		this.claimantName="Guest";
	}
	
	/**
	 * save Claims to disk (and if possible to web server). (not yet implemented)
	 */
	public void saveClaims(){
		Gson gson = new Gson();
		try {
			FileOutputStream fos = context.openFileOutput(claimantName+"_claims.sav", 0);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			gson.toJson(claimList.getClaims(), osw);
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
	 * get the http response and return json string
	 */
	String getEntityContent(HttpResponse response) throws IOException {
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
	
	private void saveClaimsToWeb(ArrayList<Claim> claims){
		
	}
	
	
	private ArrayList<Claim> loadClaimsFromWeb(){
		ArrayList<Claim> claims =new ArrayList<Claim>();
		try {
			HttpGet searchRequest = new HttpGet(SEARCH_URL+"?q=claimantName:"+claimantName);
			Gson gson = new Gson();
			searchRequest.setHeader("Accept","application/json");
			HttpResponse response = httpclient.execute(searchRequest);
			String json = getEntityContent(response);
			Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Claim>>(){}.getType();
			ElasticSearchSearchResponse<Claim> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
			Collection <ElasticSearchResponse<Claim>> esResponseList = esResponse.getHits();
			for(ElasticSearchResponse<Claim> result: esResponseList){
				claims.add(result.getSource());
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return claims;
	}
	
	
	/**
	 * load Claims from disk (and if possible sync claims with web server). (not yet implemented)
	 * @return Loaded claim list
	 */
	public void loadClaims(){
		Gson gson = new Gson();
		ArrayList<Claim> claims=new ArrayList <Claim>();
		try {
			FileInputStream fis = context.openFileInput(claimantName+"_claims.sav");
			InputStreamReader in =new InputStreamReader(fis);
			//Taken from http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html on Jan 20 2015
			Type typeOfT = new TypeToken<ArrayList<Claim>>(){}.getType();
			claims = gson.fromJson(in, typeOfT);
			fis.close();

		} catch (FileNotFoundException e) {
			//if we can't find the save file create a new one and start the ClaimList fresh
			claimList.setClaimList(new ArrayList<Claim>());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		claimList.setClaimList(claims);
		
	}
	
	/**
	 * Context must be set to save/load from file
	 * @param context
	 */
	public void setContext(Context context){
		this.context=context;
	}
	
	public void setClaimantName(String claimantName) {
		this.claimantName = claimantName;
	}
}
