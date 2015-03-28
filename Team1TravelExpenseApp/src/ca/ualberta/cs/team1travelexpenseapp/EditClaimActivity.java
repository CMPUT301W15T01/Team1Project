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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
/**
 * View for adding/editing claims
 * Provides UI for entering claim information
 *
 */
public class EditClaimActivity extends Activity {

	private TextView destList;
	private Claim claim;
	private Claimant user;
	private ClaimListController claimListController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
		user=(Claimant) UserSingleton.getUserSingleton().getUser();
		claim = SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim();
		claimListController=new ClaimListController(user.getClaimList());
		claimListController.setCurrentClaim(claim);
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
		if(claim.getStatus()==Status.submitted
				|| claim.getStatus()==Status.approved){
			
			
			EditText nameET = (EditText) findViewById(R.id.claimNameBody);
			nameET.setFocusable(false);
			EditText destinationET = (EditText) findViewById(R.id.claimDestinationBody);
			destinationET.setFocusable(false);
			EditText reasonET = (EditText) findViewById(R.id.claimReasonBody);
			reasonET.setFocusable(false);
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
		
		TextView nameView  = (TextView) findViewById(R.id.claimNameBody);
		nameView.setText(claim.getClaimantName());
		
		MultiSelectionSpinner tagSpinner= (MultiSelectionSpinner) findViewById(R.id.claimTagSpinner);
		tagSpinner.setItems(user.getTagList().getTags());
		ArrayList<Tag> claimTags=claim.getClaimTagList();
		tagSpinner.setSelection(claimTags);
		
		destList = (TextView) findViewById(R.id.claimDestinationList);
		updateDestinationText();
		
		DatePicker startDate = (DatePicker) findViewById(R.id.claimFromDate);
		startDate.updateDate(claim.startDate.getYear()+1900, claim.startDate.getMonth(), claim.startDate.getDate());
		
		DatePicker endDate = (DatePicker) findViewById(R.id.claimEndDate);
		endDate.updateDate(claim.startDate.getYear()+1900, claim.startDate.getMonth(), claim.startDate.getDate());
		
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
		
		claimListController.onAddDestinationClick(this);
		updateDestinationText();
	}
	/**
	 * Allows the user to see the destination/reason pairs that they add to a claim
	 */
	public void updateDestinationText(){
		String destString = "";
		HashMap<String, String> destReasons = (HashMap<String, String>) claim.getDestinationReasonList();
		Iterator<String> destinations = claim.getDestinations().iterator();
		String dest;
		while(destinations.hasNext()){
			dest=destinations.next();
			destString+=dest;
			destString += ": "+destReasons.get(dest);
			destString+="\n";
		}
		destList.setText(destString);	
	}
	
	public void onDestroy(){
		super.onDestroy();
		claimListController.onSaveClick(this);
	}

}
