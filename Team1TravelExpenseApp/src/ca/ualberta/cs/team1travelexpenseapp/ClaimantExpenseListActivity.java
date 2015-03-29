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
import java.util.Collection;

import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

/**
 * View for showing the expense items of a chosen claim
 * Allows navigation to add/edit expense items
 * Allows claimants to submit claims, as well as view comments from approvers
 */
public class ClaimantExpenseListActivity extends Activity {

	private Claim claim;
	private ArrayAdapter<Expense> expenselistAdapter ;
 	private ListView expenseListView ;
 	private Listener listener;
 	private ExpenseList expenseList;
 	private ExpenseListController expenseListController;
 	private TextView claimInfoHeader;
 	public AlertDialog editExpenseDialog;
 	public AlertDialog submitWarningDialog;
 	private User user;
 	private ClaimListController claimListController;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claimant_display_expenses);
		
		//As an approver, I want to list all the expense items for a submitted claim,
		//in order of entry, showing for each expense item: the date the expense was incurred,
		//the category, the textual description, amount spent, unit of currency, 
		//and whether there is a photographic receipt.
		
		user=UserSingleton.getUserSingleton().getUser();
		claimListController=new ClaimListController(user.getClaimList());
		claim=SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim();
		claimListController.setCurrentClaim(claim);
		expenseList=claim.getExpenseList();
		expenseListController= new ExpenseListController(expenseList);
		
		if( !claim.isSubmittalbe() ) {
			
			Button submitBT = (Button) findViewById(R.id.submitButton);
			submitBT.setClickable(false);
			submitBT.setFocusable(false);
			submitBT.setEnabled(false);
			
			
		}
        expenseListView = (ListView) findViewById(R.id.claimantExpensesList);

        Collection<Expense> expenses = claim.getExpenseList().getExpenses();
        final ArrayList<Expense> expensesList = new ArrayList<Expense>(expenses);
        expenselistAdapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, 
        		expensesList);
        expenseListView.setAdapter(expenselistAdapter);
        
        claimInfoHeader=(TextView) findViewById(R.id.claimInfoHeader);
        //taken from https://github.com/abramhindle/student-picker and modified
  		listener = new Listener() {			
			@Override
			public void update() {
				expensesList.clear();
				Collection<Expense> expenses = claim.getExpenseList().getExpenses();
				expensesList.addAll(expenses);
				expenselistAdapter.notifyDataSetChanged();
				claimInfoHeader.setText(claim.toString());	
			}
		};
		
		expenseList.addListener(listener);
		listener.update();
		user.getClaimList().addListener(listener);
		for (Listener i : user.getClaimList().getListeners()) {
			expenseList.addListener(i);
		}
			
		expenseListView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick( AdapterView<?> Parent, View v, int position, long id){
        		SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentExpense(expenselistAdapter.getItem(position));
    			Intent edit = new Intent(getBaseContext(), EditExpenseActivity.class);
    			startActivity(edit);
        	}
        });
        	
	    expenseListView.setOnItemLongClickListener(new OnItemLongClickListener(){
	        	
	    		public boolean onItemLongClick( AdapterView<?> Parent, View v, int position, long id){
	    			final Expense clickedExpense=expenselistAdapter.getItem(position);
	    			expenseListController.setCurrentExpense(clickedExpense);
	    			
	    			//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html (March 15, 2015)
					 AlertDialog.Builder editExpenseDialogBuilder = new AlertDialog.Builder(ClaimantExpenseListActivity.this);
					
					 editExpenseDialogBuilder.setPositiveButton("edit", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				    			//if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted && ClaimListController.getCurrentClaim().getStatus() != Status.approved){
				        	    SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentExpense(clickedExpense);
				    			Intent edit = new Intent(getBaseContext(), EditExpenseActivity.class);
				    			startActivity(edit);
				    				
				    			//}
				           }
				       });
					editExpenseDialogBuilder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   expenseListController.onRemoveExpenseClick();
				           }
				       });
					editExpenseDialogBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               //Do nothing
				           }
				       });
					editExpenseDialogBuilder.setTitle("Edit/Delete Expense?");
					editExpenseDialog=editExpenseDialogBuilder.create();
					editExpenseDialog.show();
					return true;//not too sure on return value look into this
	    		}
	    });	    

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claimant_expense_list, menu);
		return true;
	}
	/**
	 * The onClick method for the button that allows the claimant to view comments attached to a claim
	 * Navigates to the comment view activity
	 * @param v The view from the onClick
	 */
	public void onCommentClick(View v) {
		Intent intent = new Intent(this, ClaimantCommentActivity.class);
		startActivity(intent);

	}
	/**
	 * The onClick for the add expense button
	 * Redirects to the function within the ClaimListController
	 * @param v the view from the onClick
	 */
	public void onAddExpenseClick(View v) {
		expenseListController.onAddExpenseClick(this);
	}
	
	/**
	 * The onClick for the submit button
	 * Shows a warning dialog if the claim or expense is incomplete
	 * Redirects to the function within the ClaimListController
	 * @param v
	 */
	public void onSubmitClick(View v) {
		
		claimListController.onSubmitClick(this);
		
	}
	
	public void onDestroy(){
		super.onDestroy();
		expenseList.removeListener(listener);
	}
}
