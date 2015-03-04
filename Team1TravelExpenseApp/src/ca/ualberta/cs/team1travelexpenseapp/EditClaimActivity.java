package ca.ualberta.cs.team1travelexpenseapp;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
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
		
		TextView   nameView   = (TextView) findViewById(R.id.claimNameBody);
		String     nameText   = nameView.getText().toString();
		
		DatePicker fDateView  = (DatePicker) findViewById(R.id.claimFromDate);
		Calendar   calendar   = Calendar.getInstance();
		calendar.set(fDateView.getYear(), fDateView.getMonth(), fDateView.getDayOfMonth());
		Date fromDate = calendar.getTime();
		
		DatePicker eDateView  = (DatePicker) findViewById(R.id.claimEndDate);
		calendar.set(eDateView.getYear(), eDateView.getMonth(), eDateView.getDayOfMonth());
		Date endDate = calendar.getTime();
		
		ClaimListController.updateCurrentClaim(new Claim(nameText, fromDate, endDate));

		Intent intent = new Intent(this, ClaimantClaimsListActivity.class);
		startActivity(intent);
	}
	
}
