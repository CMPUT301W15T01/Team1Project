// this is the starting claim menu 

package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

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

        ArrayList<Claim> claims = ClaimListController.getClaimList().getClaims();
        listAdapter = new ArrayAdapter<Claim>(this, android.R.layout.simple_list_item_1,
        	claims);
        mainListView.setAdapter(listAdapter);
        
        mainListView.setOnItemClickListener(new OnItemClickListener(){
        	
    		
        	
    		public void onItemClick( AdapterView Parent, View v, int position, long id){
    			
    			
    			ClaimListController.setCurrentClaim(ClaimListController.getClaimList().getClaim(position));
    			
    			if(ClaimListController.getCurrentClaim().getStatus()!= 1 || ClaimListController.getCurrentClaim().getStatus() != 3){
    				
    				//toast for debugging
    				Toast.makeText(getApplicationContext(), ClaimListController.getCurrentClaim().toString(), Toast.LENGTH_SHORT).show();
    				
    				Intent edit = new Intent(getBaseContext(), EditClaimActivity.class);
    				startActivity(edit);
    				
    			}// if statement
    			
    		}
    	      	
    });
        
        
        
        

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
		ClaimListController.addClaim(new Claim());
		
		Intent intent = new Intent(this, EditClaimActivity.class);
		startActivity(intent);
	}
	
	public void onManageTagsClick(View v){
		Intent intent= new Intent(this, TagManagerActivity.class);
		startActivity(intent);
	}
	
	
}
