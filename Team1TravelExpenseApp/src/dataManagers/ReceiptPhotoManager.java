package dataManagers;

import java.io.BufferedReader;
import java.io.File;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.gsonUtils.GsonUtils;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class ReceiptPhotoManager {
	protected Context context = UserSingleton.getUserSingleton().getContext();
	private String claimantName;
	private ConnectionChangeReceiver reciever;

	//protected static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/receipts/";
	//protected static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/receipts/_search";
	protected static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/receipt/";
	protected static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/receipt/_search";

	/**
	 * Initialize with the claimList to be managed.
	 * 
	 * @param claimList
	 *            The ClaimList to saved from and loaded to
	 */
	public ReceiptPhotoManager() {
		super();
		// this.claimList=claimList;
		this.claimantName = "Guest";
		// this.reciever=new ConnectionChangeReceiver(this);

	}

	/**
	 * save Photos to disk (and if possible to web server). (not yet
	 * implemented)
	 */
	// public void savePhoto(){
	// Log.d("WebPhotos", "Photo being saved");
	// final ArrayList<Claim> Photos=claimList.getPhotos();
	// final ArrayList<Claim> unsyncedPhotos=new ArrayList<Claim>();
	// for(Claim claim: claimList.getPhotos()){
	// if(!claim.isSynced()){
	// unsyncedPhotos.add(claim);
	// }
	// }
	// Thread t=new Thread(new Runnable() {
	// public void run() {
	// savePhotosToWeb(unsyncedPhotos);
	// savePhotosToDisk(Photos);
	// }
	// });
	// t.start();
	// try {
	// t.join();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	
	//Adapted from https://gist.github.com/utkarsh2012/1276960 April 2015
	private String encodeFileToBase64Binary(File file) throws IOException {

		//File file = new File(fileName);
		byte[] bytes = loadFile(file);
		String encodedString = Base64.encodeToString(bytes, Base64.NO_WRAP);
		return encodedString;
	}
	
	//Adapted from https://gist.github.com/utkarsh2012/1276960 April 2015
	private static byte[] loadFile(File file) throws IOException {
		FileInputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}

	/*
	 * Checks if the network is available and Saves a claim the the web server
	 * 
	 * @param claim the claim to be saved
	 */
//	public void savePhotoToWeb(final Expense expense){
//		//if(NetworkAvailable()){
//			Log.d("ReceiptPhotoManager", "PhotoToWeb started. Context is null? " + String.valueOf(this.context == null));
//			Thread t=new Thread(new Runnable() {
//		        public void run() {
//					HttpPost httpPost = new HttpPost(RESOURCE_URL);//+expense.getUniquePhotoId());
//					StringEntity stringentity = null;
//					Gson gson= GsonUtils.getGson();
//					HttpClient httpclient = new DefaultHttpClient();
//					try {
//						//stringentity = new StringEntity(Base64.encodeToString(expense.getReceiptFile()., flags)gson.toJson(claim,Claim.class));
//						//FileInputStream fis = new FileInputStream(expense.getReceiptFile());
//						//String encodedString = gson.toJson(encodeFileToBase64Binary(expense.getReceiptFile()).toString()).toString();
//						//encodedString = encodedString.split("\"")[1];
//						//encodedString = encodedString.replace("/", "s");
//						//encodedString = encodedString.replace("+", "s");
//						
//						stringentity = new StringEntity("hello");
//						//Log.d("ReceiptPhotoManager", "PhotoToWeb String: " + encodedString);
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
////					} catch (FileNotFoundException e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
////					} catch (IOException e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
////					}
//					}
//					httpPost.setHeader("Accept","application/json");
//			
//					httpPost.setEntity(stringentity);
//					HttpResponse response = null;
//					try {
//						response = httpclient.execute(httpPost);
//					} catch (ClientProtocolException e) {
//						// TODO Auto-generated catch block
//						Log.d("onlineTest", "PhotoToWeb error: " + e.getCause()+":"+e.getMessage());
//						return;
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						Log.d("onlineTest", "PhotoToWeb error: " + e.getCause()+":"+e.getMessage());
//						return;
//					}
//					int statusCode=response.getStatusLine().getStatusCode();
//					
//					if(statusCode==200){
//						//photo is set to saved if it is successfully saved to web
//						expense.setPhotoSaved(true);
//						Log.d("ReceiptPhotoManager", "Photo successfully saved to web");
//					}
//					Log.d("ReceiptPhotoManager", "PhotoToWeb Status: " + statusCode);
//				}
//			});
//			t.start();
//		}
//	
	public void savePhotoToWeb(final Expense expense){
		if(NetworkAvailable()){
		Thread t=new Thread(new Runnable() {
	        public void run() {
			HttpPost httpPost = new HttpPost(RESOURCE_URL+expense.getUniquePhotoId());
			StringEntity stringentity = null;
			Gson gson= GsonUtils.getGson();
			HttpClient httpclient = new DefaultHttpClient();
			
			try {
				String encodedString = gson.toJson(encodeFileToBase64Binary(expense.getReceiptFile()).toString()).toString();
				
				//stringentity = new StringEntity(gson.toJson(expense, Expense.class));
				stringentity = new StringEntity(gson.toJson(new PhotoWrapper(encodedString), PhotoWrapper.class));//gson.toJson(encodedString, Base64.class));
				Log.d("ReceiptPhotoManager", "PhotoToWeb String: " + encodedString);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
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
				//claim.setSynced(true);
				expense.setPhotoSaved(true);
				Log.d("ReceiptPhotoManager", "Photo successfully saved to web");
			}
			Log.d("ReceiptPhotoManager", "PhotoToWeb Status: " + statusCode);
        }
		});
		t.start();
		}
	}
	//}

//	/*
//	 * Serializes and saves a list of Photos the disk
//	 * 
//	 * @param Photos the list of Photos
//	 */
//	private void savePhotosToDisk(ArrayList<Claim> Photos) {
//		Gson gson = GsonUtils.getGson();
//		if (context == null) {
//			// we can't save to disk without context so if it isn't set just
//			// don't
//			return;
//		}
//		try {
//			FileOutputStream fos = context.openFileOutput(claimantName
//					+ "_Photos.sav", 0);
//			OutputStreamWriter osw = new OutputStreamWriter(fos);
//			Type typeOfT = new TypeToken<ArrayList<Claim>>() {
//			}.getType();
//			gson.toJson(Photos, typeOfT, osw);
//			osw.flush();
//			fos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/*
//	 * Saves individual Photos to the web server
//	 * 
//	 * @param Photos the arraylist of Photos
//	 */
//	private void savePhotosToWeb(ArrayList<Claim> Photos) {
//		for (Claim claim : Photos) {
//			saveClaimToWeb(claim);
//		}
//	}
//
//	/**
//	 * Load and return all Photos corresponding to the claimantName for this
//	 * manager from the web server
//	 * 
//	 * @return ArrayList<Claim> containing all Photos made by the claimant
//	 */
//	private ArrayList<Claim> loadPhotosFromWeb() {
//		final ArrayList<Claim> Photos = new ArrayList<Claim>();
//		if (NetworkAvailable()) {
//			Thread t = new Thread(new Runnable() {
//				public void run() {
//					Gson gson = GsonUtils.getGson();
//					HttpClient httpclient = new DefaultHttpClient();
//					try {
//						HttpGet searchRequest = new HttpGet(SEARCH_URL
//								+ "?q=claim.claimantName:" + claimantName);
//						searchRequest.setHeader("Accept", "application/json");
//						HttpResponse response = httpclient
//								.execute(searchRequest);
//						String json = getEntityContent(response);
//						Log.d("onlineTest", json);
//						Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Claim>>() {
//						}.getType();
//						ElasticSearchSearchResponse<Claim> esResponse = gson
//								.fromJson(json, elasticSearchSearchResponseType);
//						Collection<ElasticSearchResponse<Claim>> esResponseList;
//						if (esResponse != null) {
//							esResponseList = esResponse.getHits();
//						} else {
//							// if response is null just exit thread and return
//							// the empty PhotosList
//							return;
//						}
//						for (ElasticSearchResponse<Claim> result : esResponseList) {
//							Log.d("onlineTest", "in web load function:"
//									+ result.getSource().toString());
//							Photos.add(result.getSource());
//						}
//					} catch (ClientProtocolException e) {
//						Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
//					} catch (IOException e) {
//						Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
//					}
//				}
//			});
//			t.start();
//
//			try {
//				t.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		for (Claim claim : Photos) {
//			claim.setSynced(true);
//		}
//		return Photos;
//	}
	
//	private ArrayList<Claim> loadPhotosFromWeb() {
//	final ArrayList<Claim> Photos = new ArrayList<Claim>();
//	if (NetworkAvailable()) {
//		Thread t = new Thread(new Runnable() {
//			public void run() {
//				Gson gson = GsonUtils.getGson();
//				HttpClient httpclient = new DefaultHttpClient();
//				try {
//					HttpGet searchRequest = new HttpGet(SEARCH_URL
//							+ "?q=claim.claimantName:" + claimantName);
//					searchRequest.setHeader("Accept", "application/json");
//					HttpResponse response = httpclient
//							.execute(searchRequest);
//					String json = getEntityContent(response);
//					Log.d("onlineTest", json);
//					Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Claim>>() {
//					}.getType();
//					ElasticSearchSearchResponse<Claim> esResponse = gson
//							.fromJson(json, elasticSearchSearchResponseType);
//					Collection<ElasticSearchResponse<Claim>> esResponseList;
//					if (esResponse != null) {
//						esResponseList = esResponse.getHits();
//					} else {
//						// if response is null just exit thread and return
//						// the empty PhotosList
//						return;
//					}
//					for (ElasticSearchResponse<Claim> result : esResponseList) {
//						Log.d("onlineTest", "in web load function:"
//								+ result.getSource().toString());
//						Photos.add(result.getSource());
//					}
//				} catch (ClientProtocolException e) {
//					Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
//				} catch (IOException e) {
//					Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
//				}
//			}
//		});
//		t.start();
//
//		try {
//			t.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	for (Claim claim : Photos) {
//		claim.setSynced(true);
//	}
//	return Photos;
//}
//	public void loadPhotoFromWeb(Expense expense) {
//		File photoFile = expense.getReceiptFile();
//	if (NetworkAvailable()) {
//		Thread t = new Thread(new Runnable() {
//			public void run() {
//				Gson gson = GsonUtils.getGson();
//				HttpClient httpclient = new DefaultHttpClient();
//				try {
//					HttpGet searchRequest = new HttpGet(SEARCH_URL
//							+ "?q=claim.claimantName:" + claimantName);
//					searchRequest.setHeader("Accept", "application/json");
//					HttpResponse response = httpclient
//							.execute(searchRequest);
//					String json = getEntityContent(response);
//					Log.d("onlineTest", json);
//					Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchSearchResponse<Claim>>() {
//					}.getType();
//					ElasticSearchSearchResponse<Claim> esResponse = gson
//							.fromJson(json, elasticSearchSearchResponseType);
//					Collection<ElasticSearchResponse<Claim>> esResponseList;
//					if (esResponse != null) {
//						esResponseList = esResponse.getHits();
//					} else {
//						// if response is null just exit thread and return
//						// the empty PhotosList
//						return;
//					}
//					for (ElasticSearchResponse<Claim> result : esResponseList) {
//						Log.d("onlineTest", "in web load function:"
//								+ result.getSource().toString());
//						//Photos.add(result.getSource());
//					}
//				} catch (ClientProtocolException e) {
//					Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
//				} catch (IOException e) {
//					Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
//				}
//			}
//		});
//		t.start();
//
//		try {
//			t.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	//for (Claim claim : Photos) {
//	//	claim.setSynced(true);
//	}
//	//return photo;
	
	public void loadPhotoFromWeb(final Expense expense) {
		final PhotoWrapper photoWrapper = new PhotoWrapper();

		if (NetworkAvailable()) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					HttpClient httpclient = new DefaultHttpClient();
					try {
						Log.d("ReceiptPhotoManager", "PhotoFromWeb Starting");
						Gson gson = new Gson();
						PhotoWrapper loadedPhoto;
						
						HttpGet getRequest = new HttpGet(RESOURCE_URL + expense.getUniquePhotoId());

						getRequest.addHeader("Accept", "application/json");

						HttpResponse response = httpclient.execute(getRequest);

						String status = response.getStatusLine().toString();
						System.out.println(status);

						String json = getEntityContent(response);

						// We have to tell GSON what type we expect
						Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<PhotoWrapper>>() {
						}.getType();
						// Now we expect to get a tag response
						ElasticSearchResponse<PhotoWrapper> esResponse = gson
								.fromJson(json, elasticSearchResponseType);
						// get the tags
						loadedPhoto = esResponse.getSource();
						if (loadedPhoto != null) {
							//for (Tag tag : loadedTags.tags) {
							//	tags.add(tag);
							//}
							Log.d("ReceiptPhotoManager", "PhotoFromWeb is not null");
							//encodedPhoto = loadedPhoto.photoString;
							photoWrapper.photoString = loadedPhoto.photoString;
							Log.d("ReceiptPhotoManager", "PhotoFromWeb as a String: " + photoWrapper.photoString);
							
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
				Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
			}
		}
		
		if(photoWrapper.photoString != null){
			Log.d("ReceiptPhotoManager", "PhotoFromWeb String successfully pulled");
			//public String loadPhotoFromWeb(final Expense expense) {
			String encodedPhoto = photoWrapper.photoString;
			byte[] photoBytes = Base64.decode(encodedPhoto, Base64.NO_WRAP);
			
			//save the photofile to disk
			//File photoFile = new File(expense.getReceiptFile().getAbsolutePath());
			Log.d("ReceiptPhotoManager", "PhotoFromWeb About to create FileOutPutStream");
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(expense.getReceiptFile());
				fos.write(photoBytes);
				Log.d("ReceiptPhotoManager", "PhotoFromWeb Written");
				fos.flush();
				fos.close();
				Log.d("ReceiptPhotoManager", "PhotoFromWeb Saved");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * Remove the passed expense's photo from the web server.
	 */
	public void removePhoto(final Expense expense){
		if(NetworkAvailable()){
			Thread t=new Thread(new Runnable() {
		        public void run() {
		        	HttpClient httpclient = new DefaultHttpClient();
					HttpDelete httpDelete = new HttpDelete(RESOURCE_URL + expense.getUniquePhotoId());
					Log.d("onlineTest", RESOURCE_URL + expense.getUniquePhotoId());
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
	 * From https://github.com/rayzhangcl/ESDemo March 28, 2015 get the http
	 * response and return json string
	 */
	protected String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:" + json);
		return json;
	}
//
//	/*
//	 * Deserializes the Photos from the disk and outputs the list of Photos
//	 * 
//	 * @return arraylist of Photos
//	 */
//	private ArrayList<Claim> loadPhotosFromDisk() {
//		Gson gson = GsonUtils.getGson();
//		ArrayList<Claim> Photos = null;
//		if (context == null) {
//			// we can't load from disk without context so if it isn't set just
//			// don't
//			return Photos;
//		}
//		try {
//			FileInputStream fis = context.openFileInput(claimantName
//					+ "_Photos.sav");
//			InputStreamReader in = new InputStreamReader(fis);
//			// Taken from
//			// http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/index.html
//			// on Jan 20 2015
//			Type typeOfT = new TypeToken<ArrayList<Claim>>() {
//			}.getType();
//			Photos = gson.fromJson(in, typeOfT);
//			fis.close();
//
//		} catch (FileNotFoundException e) {
//			// if we can't find the save file create a new one and start the
//			// ClaimList fresh
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (Photos == null) {
//			Photos = new ArrayList<Claim>();
//		}
//		return Photos;
//
//	}
//
//	/**
//	 * Remove the passed Claim from the web server.
//	 */
//	public void removeClaim(final Claim claim) {
//		if (NetworkAvailable()) {
//			Thread t = new Thread(new Runnable() {
//				public void run() {
//					HttpClient httpclient = new DefaultHttpClient();
//					HttpDelete httpDelete = new HttpDelete(RESOURCE_URL
//							+ claim.getUniqueId().toString());
//					Log.d("onlineTest", RESOURCE_URL
//							+ claim.getUniqueId().toString());
//					httpDelete.addHeader("Accept", "application/json");
//
//					HttpResponse response = null;
//					try {
//						// do something with this response if nessesary
//						response = httpclient.execute(httpDelete);
//					} catch (ClientProtocolException e) {
//						// TODO Auto-generated catch block
//						Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						Log.d("onlineTest", e.getCause() + ":" + e.getMessage());
//					}
//				}
//			});
//			t.start();
//			try {
//				t.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * load Photos from disk (and if possible sync Photos with web server). (not
//	 * yet implemented)
//	 * 
//	 * @return Loaded claim list
//	 */
//	public void loadPhotos() {
//		// no loading from disk for now it breaks things, also loading from
//		// online is not working atm working on this
//		ArrayList<Claim> localPhotos = loadPhotosFromDisk();
//		ArrayList<Claim> webPhotos = loadPhotosFromWeb();
//
//		// if webPhotos is empty, either this user has no saved Photos online or
//		// the connection failed, either way just use the locally save list
//		if (webPhotos.isEmpty()) {
//			claimList.setClaimList(localPhotos);
//		}
//
//		// if webPhotos is not empty then we want to use the web version of all
//		// Photos except those that have been locally modified and not synced,
//		// any Photos which are synced
//		// but are not on the web server are assumed to have been deleted
//		// elsewhere
//		else {
//			ArrayList<Claim> Photos = new ArrayList<Claim>();
//
//			for (Claim claim : localPhotos) {
//				Log.d("onlineTest", "From Local:" + claim.toString());
//				if (!claim.isSynced()) {
//					Log.d("approvalTest", "Claim not synced");
//					Photos.add(claim);
//				}
//			}
//
//			for (Claim claim : webPhotos) {
//				boolean exists = false;
//				Log.d("onlineTest", "From web:" + claim.toString());
//				for (Claim existingClaim : Photos) {
//					if (claim.getUniqueId().equals(existingClaim.getUniqueId())) {
//						exists = true;
//					}
//				}
//				if (!exists) {
//					Photos.add(claim);
//				}
//			}
//			claimList.setClaimList(Photos);
//		}
//		// after loading from both sources attempt to savePhotos in order to
//		// sync online with local
//		savePhotos();
//
//		// claim of expense list is transient to avoid circular reference, set
//		// it again on load
//		for (Claim claim : claimList.getPhotos()) {
//			claim.getExpenseList().setClaim(claim);
//		}
//
//	}
//
//	/*
//	 * Sets the current claimaint's name
//	 * 
//	 * @param claimantName name of the claimant
//	 */
//	public void setClaimantName(String claimantName) {
//		this.claimantName = claimantName;
//	}
//
//	/*
//	 * Saves the claimant's unsynced Photos to the web server
//	 */
//	public void onConnect() {
//		final ArrayList<Claim> unsyncedPhotos = new ArrayList<Claim>();
//		for (Claim claim : claimList.getPhotos()) {
//			if (!claim.isSynced()) {
//				unsyncedPhotos.add(claim);
//			}
//		}
//		Thread t = new Thread(new Runnable() {
//			public void run() {
//				savePhotosToWeb(unsyncedPhotos);
//			}
//		});
//		t.start();
//		try {
//			t.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void setContext(Context context) {
//		super.setContext(context);
//		IntentFilter intentFilter = new IntentFilter(
//				"android.net.conn.CONNECTIVITY_CHANGE");
//		context.registerReceiver(reciever, intentFilter);
//	}
//
	public void setContext(Context context) {
		this.context = context;
	}
	
	private boolean NetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private static class PhotoWrapper {
		public String photoString;

		PhotoWrapper(String photoString) {
			this.photoString = photoString;
		}

		public PhotoWrapper() {
			// TODO Auto-generated constructor stub
		}
	}

}
