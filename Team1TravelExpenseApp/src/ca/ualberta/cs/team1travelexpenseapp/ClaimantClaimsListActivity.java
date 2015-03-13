// this is the starting claim menu 

package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class ClaimantClaimsListActivity extends Activity {
	
	private ClaimList claimList;

 	private ListView mainListView;
 	public  AlertDialog editClaimDialog;
 	private Listener listener;

 	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claimant_activity_main);

		//As an approver, I want to view a list of all the expense claims that were submitted 
		//for approval, which have their claim status as submitted, showing for each claim:
		//the claimant name, the starting date of travel, the destination(s) of travel, the 
		//claim status, total currency amounts, and any approver name.

        mainListView = (ListView) findViewById(R.id.claimsList);
        
        //taken from https://github.com/abramhindle/student-picker and modified
  		claimList=ClaimListController.getClaimList();
  		Collection<Claim> claims = claimList.getClaims();
		final ArrayList<Claim> claimsList = new ArrayList<Claim>(claims);
  		final ArrayAdapter<Claim> claimsAdapter = new ArrayAdapter<Claim>(this, android.R.layout.simple_list_item_1, claimsList);
  		mainListView.setAdapter(claimsAdapter);
  		
  		listener=new Listener() {			
			@Override
			public void update() {
				claimsList.clear();
				Collection<Claim> claims = ClaimListController.getClaimList().getClaims();
				claimsList.addAll(claims);
				claimsAdapter.notifyDataSetChanged();
			}
		};
        
        claimList.addListener(listener);
        
        
        mainListView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick( AdapterView Parent, View v, int position, long id){
        		ClaimListController.setCurrentClaim(ClaimListController.getClaimList().getClaim(position));
        		Intent intent= new Intent(getBaseContext(),ClaimantExpenseListActivity.class);	
        		startActivity(intent);
        	}
        });
        	
       mainListView.setOnItemLongClickListener(new OnItemLongClickListener(){
        	
    		public boolean onItemLongClick( AdapterView Parent, View v, int position, long id){
    			 ClaimListController.setCurrentClaim(claimsAdapter.getItem(position));
    			
    			//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html
				 AlertDialog.Builder editClaimDialogBuilder = new AlertDialog.Builder(ClaimantClaimsListActivity.this);
				
				 editClaimDialogBuilder.setPositiveButton("edit", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   
			    			if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted || ClaimListController.getCurrentClaim().getStatus() != Status.approved){
			    				
			    				//toast for debugging
			    				Toast.makeText(getApplicationContext(), ClaimListController.getCurrentClaim().toString(), Toast.LENGTH_SHORT).show();
			    				
			    				Intent edit = new Intent(getBaseContext(), EditClaimActivity.class);
			    				startActivity(edit);
			    				
			    			}// if statement
			           }
			       });
				editClaimDialogBuilder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   ClaimListController.onRemoveClaimClick();
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
		ClaimListController.onAddClaimClick(this);
	}
	
	public void onManageTagsClick(View v){
		Intent intent= new Intent(this, TagManagerActivity.class);
		startActivity(intent);
	}
	
	public void onDestroy(){
		super.onDestroy();
		claimList.removeListener(listener);
	}
	
	
}
