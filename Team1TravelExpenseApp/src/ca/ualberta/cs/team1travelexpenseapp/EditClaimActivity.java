package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
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

public class EditClaimActivity extends Activity {

	private TextView destList;
	private Claim claim;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
	}

	//TODO 
	//on start method that loads all of the CurrentClaim values into the editTexts
	
	protected void onStart(){
		super.onStart();
			
		final AlertDialog.Builder StatusAlert =  new AlertDialog.Builder(EditClaimActivity.this);
		StatusAlert.setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		if(ClaimListController.getCurrentClaim().getStatus()==Status.submitted
				|| ClaimListController.getCurrentClaim().getStatus()==Status.approved){
			
			
			EditText nameET = (EditText) findViewById(R.id.claimNameBody);
			nameET.setFocusable(false);
			EditText destinationET = (EditText) findViewById(R.id.claimDestinationBody);
			destinationET.setFocusable(false);
			EditText reasonET = (EditText) findViewById(R.id.claimReasonBody);
			reasonET.setFocusable(false);
			DatePicker dateStartPick = (DatePicker) findViewById(R.id.claimFromDate);
			dateStartPick.setFocusable(false);
			DatePicker dateEndPick = (DatePicker) findViewById(R.id.claimEndDate);
			dateEndPick.setFocusable(false);
			Button addDestBT = (Button) findViewById(R.id.addDestinationButton);
			addDestBT.setFocusable(false);
			
			StatusAlert.setMessage("ONLY TAGS may be edited in this claim as it is already"
			+ ClaimListController.getCurrentClaim().getStatus().toString());
			
			StatusAlert.show();
		}
		claim = ClaimListController.getCurrentClaim();
		
		TextView nameView  = (TextView) findViewById(R.id.claimNameBody);
		nameView.setText(claim.getClaimantName());
		
		MultiSelectionSpinner tagSpinner= (MultiSelectionSpinner) findViewById(R.id.claimTagSpinner);
		tagSpinner.setItems(TagListController.getTagList().getTags());
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
	
	
	public void onSaveClick(View v) {

		//editing model happens in controller 
	
		ClaimListController.onSaveClick(this);
	}
	
	public void onAddDestinationClick(View v) {
		
		ClaimListController.onAddDestinationClick(this);
		updateDestinationText();
	}
	
	public void updateDestinationText(){
		String destString = "";
		Map<String, String> destReasons = claim.getDestinationReasonList();
		Iterator<String> destinations = claim.getDestinations().iterator();
		String dest;
		while(destinations.hasNext()){
			dest=destinations.next();
			destString+=dest;
			if(!destReasons.get(dest).equals("")) destString += ": "+destReasons.get(dest)+"\n";
		}
		destList.setText(destString);	
	}
	
	public void onDestroy(){
		super.onDestroy();
		ClaimListController.onSaveClick(this);
	}

}
