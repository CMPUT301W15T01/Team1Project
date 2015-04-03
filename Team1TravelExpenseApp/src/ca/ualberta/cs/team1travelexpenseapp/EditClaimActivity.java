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

package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import views.MultiSelectionSpinner;
import ca.ualberta.cs.team1travelexpenseapp.claims.ApprovedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
/**
 * View for adding/editing claims
 * Provides UI for entering claim information
 *
 */
public class EditClaimActivity extends Activity {

	private static final int PICK_GEOLOCATION_REQUEST = 1;
	private ListView destList;
	private Claim claim;
	private Claimant user;
	private ClaimListController claimListController;
	private Location location=null;
	private Listener listener;
	public AlertDialog editDestDialog;
	public AlertDialog newDestDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		user=(Claimant) UserSingleton.getUserSingleton().getUser();
		claim = SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim();
		claimListController=new ClaimListController(user.getClaimList());
		claimListController.setCurrentClaim(claim);
		
		
		//taken from https://github.com/abramhindle/student-picker and modified
		final ListView destinationListView = (ListView) findViewById(R.id.claimDestinationList);
		Collection<Destination> dests = claim.getDestinationList();
		final ArrayList<Destination> destList = new ArrayList<Destination>(dests);
		final ArrayAdapter<Destination> destAdapter = new ArrayAdapter<Destination>(this, android.R.layout.simple_list_item_1, destList);
		destinationListView.setAdapter(destAdapter);
		
		listener=new Listener() {			
			@Override
			public void update() {
				destList.clear();
				Collection<Destination> dests = claim.getDestinationList();
				destList.addAll(dests);
				destAdapter.notifyDataSetChanged();
			}
		};
		
		user.getClaimList().addListener(listener);
		
		destinationListView.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int index,
					long id){
						 final Destination dest= (Destination) destAdapter.getItem(index);
						 
						//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html
						 AlertDialog.Builder editDestDialogBuilder = new AlertDialog.Builder(EditClaimActivity.this);
						
						 editDestDialogBuilder.setView(getLayoutInflater().inflate(R.layout.destination_alert, null));
						 editDestDialogBuilder.setPositiveButton("save", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   claimListController.onSetDestinationClick(dialog, dest, location);
					        	   location = null;
					           }
					       });
						editDestDialogBuilder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   claimListController.onRemoveDestinationClick(dialog, dest);
					        	   location = null;
					           }
					       });
						editDestDialogBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					               //Do nothing except set location back to null in case it was set
					        	   location = null;
					           }
					       });
						editDestDialogBuilder.setTitle("Enter Destination Info:");
						editDestDialog=editDestDialogBuilder.create();
						editDestDialog.show();
						EditText nameText = (EditText) editDestDialog.findViewById(R.id.destinationNameBody);
				        EditText reasonText = (EditText) editDestDialog.findViewById(R.id.destinationReasonBody);
						nameText.setText(dest.getName());
						reasonText.setText(dest.getReason());
						return true;//not too sure on return value look into this
					}
		});
		
		//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html
				AlertDialog.Builder newDestDialogBuilder = new AlertDialog.Builder(this);
				
				newDestDialogBuilder.setView(getLayoutInflater().inflate(R.layout.destination_alert, null));
				
				newDestDialogBuilder.setPositiveButton("save", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   claimListController.onAddDestinationClick(EditClaimActivity.this, dialog, location);
			        	   location=null;
			           }
			       });
				newDestDialogBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               //Do nothing except set location back to null in case it was set
			        	   location = null;
			           }
			       });
				newDestDialogBuilder.setTitle("Enter Destination Info:");
				newDestDialog=newDestDialogBuilder.create();
		
	}

	//on start method that loads all of the CurrentClaim values into the editTexts
	
	protected void onStart(){
		super.onStart();
			
		final AlertDialog.Builder StatusAlert =  new AlertDialog.Builder(EditClaimActivity.this);
		StatusAlert.setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		if(claim.getStatus()==SubmittedClaim.class
				|| claim.getStatus()==ApprovedClaim.class){
			
			ListView destinationList = (ListView) findViewById(R.id.claimDestinationList);
			destinationList.setFocusable(false);
			DatePicker dateStartPick = (DatePicker) findViewById(R.id.claimFromDate);
			dateStartPick.setFocusable(false);
			dateStartPick.setClickable(false);
			DatePicker dateEndPick = (DatePicker) findViewById(R.id.claimEndDate);
			dateEndPick.setFocusable(false);
			dateEndPick.setClickable(false);
			Button addDestBT = (Button) findViewById(R.id.addDestinationButton);
			addDestBT.setFocusable(false);
			
			StatusAlert.setMessage("ONLY TAGS may be edited in this claim as it is already"
			+ claim.getStatus().toString());
			
			StatusAlert.show();
		}
		
		MultiSelectionSpinner tagSpinner= (MultiSelectionSpinner) findViewById(R.id.claimTagSpinner);
		tagSpinner.setItems(user.getTagList().getTags());
		ArrayList<Tag> claimTags=claim.getClaimTagList();
		tagSpinner.setSelection(claimTags);
		
		destList = (ListView) findViewById(R.id.claimDestinationList);
		
		DatePicker startDate = (DatePicker) findViewById(R.id.claimFromDate);
		startDate.updateDate(claim.getStartDate().getYear()+1900, claim.getStartDate().getMonth(), claim.getStartDate().getDate());
		
		DatePicker endDate = (DatePicker) findViewById(R.id.claimEndDate);
		endDate.updateDate(claim.getEndDate().getYear()+1900, claim.getEndDate().getMonth(), claim.getEndDate().getDate());
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_claim, menu);
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
	 * The onClick method for the save button, allowing the user to save the data they hav entered
	 * @param v The view passed when the save button is clicked
	 */
	public void onSaveClick(View v) {

		//editing model happens in controller 
	
		claimListController.onSaveClick(this);
	}
	/**
	 * The onClick method for the button allowing the user to add a destination/reason pair to a claim
	 * @param v The view passed when the add destination/reason button is clicked
	 */
	public void onAddDestinationClick(View v) {
		newDestDialog.show();
		EditText nameText = (EditText) newDestDialog.findViewById(R.id.destinationNameBody);
        EditText reasonText = (EditText) newDestDialog.findViewById(R.id.destinationReasonBody);
        nameText.setText("");
        reasonText.setText("");
        
	}
	
	public void onAddDestinationLocationClick(View v) {
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
	        	location = new Location("");
	        	location.setLongitude(lon);
	        	location.setLatitude(lat);
	        	// TODO add destination to location
	        	//Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();
	        
	            // Do something with the contact here (bigger example below)
	        }
	    }
	}	

	
	public void onDestroy(){
		super.onDestroy();
		claimListController.onSaveClick(this);
	}

}
