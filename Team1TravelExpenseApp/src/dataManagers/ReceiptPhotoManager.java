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
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ReceiptPhoto;
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

	//private ConnectionChangeReceiver reciever;
	protected static final String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/receipts/";
	protected static final String SEARCH_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/receipts/_search";

	/**
	 * Initialize the ReceiptPhotoManager.
	 * 
	 */
	public ReceiptPhotoManager() {
		super();
	}
	
	
	// Code for taking a photo Heavily influenced/copied from https://developer.android.com/training/camera/photobasics.html April 2015
	// and https://github.com/dfserrano/BogoPicLab
	/**
	 * Initializes a new photo file on the SD card
	 * @return The File related to the created file
	 */
	public File initNewPhoto(){
		// Create a folder to store pictures
		String folder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Receipts";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}
		
		// Create an URI for the picture file
		String imageFilePath = folder + "/"
				+ UserSingleton.getUserSingleton().getUser().getName() + "_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		return(new File(imageFilePath));
	}

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
		is.close();
		return bytes;
	}

	/**
	 * Attempts to save the ReceiptPhoto's image file to the elastic search server.
	 * @param receipt 
	 * The ReceiptPhoto containing the photo to be pushed.
	 */
	public void savePhotoToWeb(final ReceiptPhoto receipt){
		if(NetworkAvailable()){
		Thread t=new Thread(new Runnable() {
	        public void run() {
			HttpPost httpPost = new HttpPost(RESOURCE_URL+receipt.getUniquePhotoId());
			StringEntity stringentity = null;
			Gson gson= GsonUtils.getGson();
			HttpClient httpclient = new DefaultHttpClient();
			
			try {
				String encodedString = gson.toJson(encodeFileToBase64Binary(receipt.getReceiptFile()).toString()).toString();
				
				//stringentity = new StringEntity(gson.toJson(expense, Expense.class));
				stringentity = new StringEntity(gson.toJson(new PhotoWrapper(encodedString), PhotoWrapper.class));
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
			
			if(statusCode==200 || statusCode==201){
				//claim is synced if it is successfully saved to web
				//claim.setSynced(true);
				receipt.setPhotoSavedToWeb(true);
				Log.d("ReceiptPhotoManager", "Photo successfully saved to web");
			}
			Log.d("ReceiptPhotoManager", "PhotoToWeb Status: " + statusCode);
        }
		});
		t.start();
		}
	}

	/**
	 * Attempts to load the ReceiptPhoto's image file from the elastic search server.
	 * @param receipt 
	 * The ReceiptPhoto containing the photo to be loaded.
	 */
	public void loadPhotoFromWeb(final ReceiptPhoto receipt) {
		final PhotoWrapper photoWrapper = new PhotoWrapper();

		if (NetworkAvailable()) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					HttpClient httpclient = new DefaultHttpClient();
					try {
						Log.d("ReceiptPhotoManager", "PhotoFromWeb Starting");
						Gson gson = new Gson();
						PhotoWrapper loadedPhoto;
						
						HttpGet getRequest = new HttpGet(RESOURCE_URL + receipt.getUniquePhotoId());

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
				fos = new FileOutputStream(receipt.getReceiptFile());
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
	 * Remove the passed ReceiptPhoto's photo from the web server.
	 */
	public void removePhoto(final ReceiptPhoto receipt){
		if(NetworkAvailable()){
			Thread t=new Thread(new Runnable() {
		        public void run() {
		        	HttpClient httpclient = new DefaultHttpClient();
					HttpDelete httpDelete = new HttpDelete(RESOURCE_URL + receipt.getUniquePhotoId());
					Log.d("onlineTest", RESOURCE_URL + receipt.getUniquePhotoId());
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

	/**
	 * Sets the android application context to the given context.
	 */
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
			this.photoString = null;
		}
	}

}
