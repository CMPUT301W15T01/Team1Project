package ca.ualberta.cs.team1travelexpenseapp;

import java.util.Calendar;
import java.util.Date;

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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class EditClaimActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_claim);
	}

	//TODO 
	//on start method that loads all of the CurrentClaim values into the editTexts
	
	protected void onStart(){
		super.onStart();
		TextView nameView   = (TextView) findViewById(R.id.claimNameBody);
		nameView.setText(ClaimListController.getCurrentClaim().getClaimantName());	
		final AlertDialog.Builder StatusAlert =  new AlertDialog.Builder(EditClaimActivity.this);
		StatusAlert.setPositiveButton("OK",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		if(ClaimListController.getCurrentClaim().getStatus()==Status.submitted){
			
			
			
			StatusAlert.setMessage("ONLY TAGS may be edited in this claim as it is already submitted.");
			StatusAlert.show();
		}
		if(ClaimListController.getCurrentClaim().getStatus()==Status.approved){
			
			StatusAlert.setMessage("ONLY TAGS may be edited in this claim as it is approved.");
			StatusAlert.show();
			
		}
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
	}

}
