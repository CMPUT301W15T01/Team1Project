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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

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

/**
 * The {@link ApproverClaimListManager} is the base class for all elastic web
 * server connections which allow an application to draw claims from the
 * webserver onto the approver's claims list
 * 
 * @since 1.0
 */
public class ApproverClaimListManager extends ClaimListManager {

	public ApproverClaimListManager(ClaimList claimList) {
		super();
		this.claimList = claimList;
	}

	/**
	 * Saves the claim to the elastic web server
	 * 
	 * @param claim
	 *            The claim to be saved
	 */
	private void saveClaimToWeb(final Claim claim) {
		if (NetworkAvailable()) {
			Log.d("approvalTest", "network available");
			Thread t = new Thread(new Runnable() {
				public void run() {
					HttpPost httpPost = new HttpPost(RESOURCE_URL
							+ claim.getUniqueId());
					StringEntity stringentity = null;
					Gson gson = GsonUtils.getGson();
					HttpClient httpclient = new DefaultHttpClient();
					try {
						stringentity = new StringEntity(gson.toJson(claim,
								Claim.class));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					httpPost.setHeader("Accept", "application/json");

					httpPost.setEntity(stringentity);
					HttpResponse response = null;
					try {
						response = httpclient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
						return;
					}
					int statusCode = response.getStatusLine().getStatusCode();
					Log.d("approvalTest", Integer.toString(statusCode));
					if (statusCode == 200) {
						// claim is synced if it is successfully saved to web
						claim.setSynced(true);
						Log.d("approvalTest", "Claim saved!");

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
	 * Saves a set of claims to the elastic web server
	 * 
	 * @param claims
	 *            the arraylist of claims
	 */
	private void saveClaimsToWeb(ArrayList<Claim> claims) {
		Log.d("approvalTest", "Approver saved");
		for (Claim claim : claims) {
			Log.d("approvalTest", claim.getUniqueId().toString());
			Log.d("approvalTest", claim.toString());
			saveClaimToWeb(claim);
		}
	}

	/**
	 * Load and return all submitted claims from elastic search web server
	 * 
	 * @return ArrayList<Claim> containing all submitted claims
	 */
	private ArrayList<Claim> loadClaimsFromWeb() {
		final ArrayList<Claim> claims = new ArrayList<Claim>();
		if (NetworkAvailable()) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					Gson gson = GsonUtils.getGson();
					HttpClient httpclient = new DefaultHttpClient();
					try {
						HttpGet searchRequest = new HttpGet(SEARCH_URL
								+ "?q=type:SubmittedClaim");
						searchRequest.setHeader("Accept", "application/json");
						HttpResponse response = httpclient
								.execute(searchRequest);
						String json = getEntityContent(response);
						Log.d("onlineTest", json);
						Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Claim>>() {
						}.getType();
						ElasticSearchSearchResponse<Claim> esResponse = gson
								.fromJson(json, elasticSearchSearchResponseType);
						Collection<ElasticSearchResponse<Claim>> esResponseList = esResponse
								.getHits();
						for (ElasticSearchResponse<Claim> result : esResponseList) {
							Log.d("onlineTest", "in web load function:"
									+ result.getSource().toString());
							claims.add(result.getSource());
						}
					} catch (ClientProtocolException e) {
						Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
					} catch (IOException e) {
						Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
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

	/**
	 * Automatically saves current claims to the elastic web server
	 */
	@Override
	public void saveClaims() {
		ArrayList<Claim> claims = claimList.getClaims();
		saveClaimsToWeb(claims);

	}

	/**
	 * Would remove a claims but approvers cannot currently do this
	 * 
	 * @param claim The claim to be removed
	 */
	public void removeClaim(Claim claim) {
		// do nothing approvers cannot remove claims at this time
	}

	/**
	 * Loads claims from the elastic web server
	 */
	@Override
	public void loadClaims() {
		claimList.setClaimList(loadClaimsFromWeb());

	}

	/**
	 * Loads claims when connectivity is established
	 */
	public void onConnect() {
		loadClaims();
	}
}
