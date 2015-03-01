// this is the starting claim menu 

package ca.ualberta.cs.team1travelexpenseapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ClaimantClaimsListActivity extends Activity {
	
	private ArrayAdapter<Claim> listAdapter ;
 	private ListView mainListView ;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claimant_activity_main);

		//As an approver, I want to view a list of all the expense claims that were submitted 
		//for approval, which have their claim status as submitted, showing for each claim:
		//the claimant name, the starting date of travel, the destination(s) of travel, the 
		//claim status, total currency amounts, and any approver name.
		
        mainListView = (ListView) findViewById(R.id.claimsList);
        listAdapter = new ArrayAdapter<Claim>(this, 
        		android.R.layout.simple_list_item_1,
        		ClaimsListController.getClaims());
        mainListView.setAdapter(listAdapter);
        
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
	
	public void onAddClaimClick(View v) {
		Intent intent = new Intent(this, EditClaimActivity.class);
		startActivity(intent);
	}
}
