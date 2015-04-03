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

// this is the starting claim menu 

package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import views.MultiSelectionSpinner;
import ca.ualberta.cs.team1travelexpenseapp.claims.ApprovedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Displays the claimant's list of claims and allows them to be clicked to view the underlying 
 * expenses. Items in the list can also be long clicked to open a dialog to choose to delete or 
 * edit the info of the claim.
 *
 */
public class ClaimantClaimsListActivity extends Activity {
	
	private static final int PICK_GEOLOCATION_REQUEST = 1;
	private ClaimList claimList;
 	private ListView mainListView;
 	public  AlertDialog editClaimDialog;
 	private Listener listener;
 	private ArrayList<Claim> displayList;
 	private ArrayAdapter<Claim> claimsAdapter;
 	private Claimant user;
 	private ClaimListController claimListController;

 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claimant_activity_main);
		user=(Claimant) UserSingleton.getUserSingleton().getUser();
		//user.toString();
		claimListController= new ClaimListController(user.getClaimList());
		
		final MultiSelectionSpinner tagSpinner= (MultiSelectionSpinner) findViewById(R.id.claimFilterSpinner);
		tagSpinner.setItems(user.getTagList().getTags());	
		//As an approver, I want to view a list of all the expense claims that were submitted 
		//for approval, which have their claim status as submitted, showing for each claim:
		//the claimant name, the starting date of travel, the destination(s) of travel, the 
		//claim status, total currency amounts, and any approver name.

		mainListView = (ListView) findViewById(R.id.claimsList);
        
        //taken from https://github.com/abramhindle/student-picker and modified
  		claimList=user.getClaimList();
  		Collection<Claim> claims = claimList.getClaims();
  		displayList = new ArrayList<Claim>(claims);
  		
  		claimsAdapter = new ArrayAdapter<Claim>(this, android.R.layout.simple_list_item_1, displayList);

  		mainListView.setAdapter(claimsAdapter);
  		
  		listener=new Listener() {			
			@Override
			public void update() {
				List<String> tags = tagSpinner.getSelectedStrings();
				
				if(tags.size() == 0){
					displayList.clear();
					Collection<Claim> claims = user.getClaimList().getClaims();
			  		displayList.addAll(claims);
					claimsAdapter.notifyDataSetChanged();
				} else {
					displayList.clear();
					Collection<Claim> claims = user.getClaimList().getClaims();
			  		displayList.addAll(claims);
					ArrayList<Claim> list = new ArrayList<Claim>();
					for(Claim claim: displayList){
						for(String tag: claim.getClaimTagNameList()) {
							if(tags.contains(tag)){
								list.add(claim);
							}
						}
					}
					displayList.clear();
			  		displayList.addAll(list);
					claimsAdapter.notifyDataSetChanged();
				}
			}
		};
        
        claimList.addListener(listener);
        
        
        mainListView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick( AdapterView<?> Parent, View v, int position, long id){
        		SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentClaim(claimsAdapter.getItem(position));
        		Intent intent= new Intent(getBaseContext(),ClaimantExpenseListActivity.class);	
        		startActivity(intent);
        	}
        });
        	
       mainListView.setOnItemLongClickListener(new OnItemLongClickListener(){
        	
    		public boolean onItemLongClick( AdapterView<?> Parent, View v, int position, long id){
    			final Claim clickedClaim=claimsAdapter.getItem(position);
    			claimListController.setCurrentClaim(clickedClaim);
    			
    			//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html (March 15, 2015)
				 AlertDialog.Builder editClaimDialogBuilder = new AlertDialog.Builder(ClaimantClaimsListActivity.this);
				
				 editClaimDialogBuilder.setPositiveButton("edit", new DialogInterface.OnClickListener() {

			           public void onClick(DialogInterface dialog, int id) {		
			        	   SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentClaim(clickedClaim);
			        	   Intent edit = new Intent(getBaseContext(), EditClaimActivity.class);
			        	   startActivity(edit);
			    					

			           }
			       });
				editClaimDialogBuilder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   
			        	   if(claimListController.getCurrentClaim().getStatus()!= SubmittedClaim.class){
			        			   if (claimListController.getCurrentClaim().getStatus() != ApprovedClaim.class){
			        				   claimListController.onRemoveClaimClick();
			        			   }
			        			   else{
			        				   Toast.makeText(getApplicationContext(),"This claim can not be deleted", Toast.LENGTH_SHORT).show();
			        			   }
			        		  
			        	   }
			        	   else{
			        		   
			        		   Toast.makeText(getApplicationContext(),"This claim can not be deleted", Toast.LENGTH_SHORT).show();
			        		   
			        	   }
			           }
			       });
				editClaimDialogBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               //Do nothing
			           }
			       });
				editClaimDialogBuilder.setTitle("Edit/Delete Claim?");
				editClaimDialog=editClaimDialogBuilder.create();
				editClaimDialog.show();
				return true;//not too sure on return value look into this
    		}
    	      	
    });

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//set up the tag filter spinner
		final MultiSelectionSpinner tagSpinner= (MultiSelectionSpinner) findViewById(R.id.claimFilterSpinner);
		tagSpinner.setItems(user.getTagList().getTags());		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Call the onAddClaimClick function in the ClaimListController
	 * @param v The button clicked by the user.
	 */
	public void onAddClaimClick(View v) {
		claimListController.onAddClaimClick(this);
	}
	
	/**
	 * Open the TagManagerActivity to allow the user to add, edit and delete available Tags.
	 * @param v The button clicked by the user.
	 */
	public void onManageTagsClick(View v){
		Intent intent= new Intent(this, TagManagerActivity.class);
		startActivity(intent);
	}
	
	/**
	 * On destroy remove the listener from the claimList so it does not continue notifying it.
	 */
	public void onDestroy(){
		super.onDestroy();
		claimList.removeListener(listener);
	}

	public void HomeGeolocationSelect(View v) {
		Intent intent = new Intent(this,OSMDroidMapActivity.class);
		startActivityForResult(intent, PICK_GEOLOCATION_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == PICK_GEOLOCATION_REQUEST ) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // The user picked a contact.
	            // The Intent's data Uri identifies which contact was selected.
	        	Double lon = data.getExtras().getDouble("longitude");
	        	Double lat = data.getExtras().getDouble("latitude");
	        	Location location = new Location("");
	        	location.setLongitude(lon);
	        	location.setLatitude(lat);
	        	user.setLocation(location);
	        	//Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();
	        
	            // Do something with the contact here (bigger example below)
	        }
	    }
	}	

	public ArrayAdapter<Claim> getArrayAdapter() {
		// TODO Auto-generated method stub
		return claimsAdapter;
	}
	
	public void setAdapter(ArrayAdapter<Claim> a) {
		this.claimsAdapter = a;
	}
	
	public ArrayList<Claim> getDisplayList() {
		return displayList;
	}
	
	public void setDisplayList(ArrayList<Claim> list) {
		this.displayList = list;
	}
	
}
