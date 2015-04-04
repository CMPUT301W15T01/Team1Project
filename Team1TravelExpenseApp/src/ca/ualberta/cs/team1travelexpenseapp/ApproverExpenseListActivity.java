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

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Allows the approver to view the expenses contained in a clicked claim and provides the option
 * to approve, return or comment on the claim via buttons on the bottom.
 *
 */
public class ApproverExpenseListActivity extends Activity {

	private Claim claim;
	private ClaimListController claimListController;
	private ArrayAdapter<Expense> expenselistAdapter;
 	private ListView expenseListView ;
 	private User user;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user=UserSingleton.getUserSingleton().getUser();
		claimListController=new ClaimListController(user.getClaimList());
		setContentView(R.layout.approver_display_expenses);
		
		// display current claims expense items 
		claim = SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim();
		claimListController.setCurrentClaim(claim);
		
		expenseListView = (ListView) findViewById(R.id.approverExpensesList);
        expenselistAdapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, 
        		claim.getExpenseList().getExpenses());
        expenseListView.setAdapter(expenselistAdapter);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		TextView info = (TextView) findViewById(R.id.approverClaimInfoTextView);
		info.setText(claim.toString());
		
		if (claim.getClaimantName().equals(user.getName())) {
			findViewById(R.id.approveButton).setEnabled(false);
			findViewById(R.id.returnButton).setEnabled(false);
			findViewById(R.id.approverComment).setEnabled(false);
		}
		expenseListView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick( AdapterView<?> Parent, View v, int position, long id){
        		SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentExpense(expenselistAdapter.getItem(position));
    			Intent edit = new Intent(getBaseContext(), ApproverClaimInfo.class);
    			startActivity(edit);
        	}
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.approver_expense_list, menu);
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
	 * Call the onApproveClick function in ClaimListController
	 * @param v The button clicked by the user.
	 */
	public void onApproveClick(View v) {
		claimListController.onApproveClick();
		finish();
	}
	
	/**
	 * Call the onReturnClick function in ClaimListController.
	 * @param v The button clicked by the user.
	 */
	public void onReturnClick(View v) {
		claimListController.onReturnClick();
		finish();
	}
	
	/**
	 * On info click open the ApproverClaimInfo Activity.
	 * @param v The button clicked by the user.
	 */
	public void onInfoClick(View v) {
		Intent intent = new Intent(this,ApproverClaimInfo.class);
		startActivity(intent);
	}
	
	/**
	 * On comment click show a dialog to allow the user to enter a comment.
	 * @param v The button clicked by the user.
	 */
	public void onCommentClick(View v) {
		
		//Retrieved from http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog (March 15, 2015)
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Comments");
		alert.setMessage("Enter your comment.");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  // save the comment
		  claimListController.onCommentClick(value);
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();
	}
	
	
	
}

