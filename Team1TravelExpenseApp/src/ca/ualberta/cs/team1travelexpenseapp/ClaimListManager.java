package ca.ualberta.cs.team1travelexpenseapp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClaimListManager {
	private ClaimList claimList;
	private Context context;
	
	/**
	 * Initialize with the claimList to be managed.
	 * @param claimList The ClaimList to saved from and loaded to
	 */
	ClaimListManager(ClaimList claimList){
		this.claimList=claimList;
	}
	
	/**
	 * save Claims to disk (and if possible to web server). (not yet implemented)
	 */
	public void saveClaims(){
		Gson gson = new Gson();
		String saveFile="claims.sav";
		try {
			FileOutputStream fos = context.openFileOutput(saveFile, 0);
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
	 * load Claims from disk (and if possible sync claims with web server). (not yet implemented)
	 * @return Loaded claim list
	 */
	public void loadClaims(){
		Gson gson = new Gson();
		ArrayList<Claim> claims=new ArrayList <Claim>();
		String saveFile="claims.sav";
		try {
			FileInputStream fis = context.openFileInput(saveFile);
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
}
