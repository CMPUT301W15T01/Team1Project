package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ClaimantExpenseListActivity extends Activity {

	public Claim claim = ClaimListController.getCurrentClaim();
	private ArrayAdapter<Expense> expenselistAdapter ;
 	private ListView expenseListView ;
 	private Listener listener;
 	private ExpenseList expenseList = claim.getExpenseList();
 	public AlertDialog editExpenseDialog;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claimant_display_expenses);
		
		//As an approver, I want to list all the expense items for a submitted claim,
		//in order of entry, showing for each expense item: the date the expense was incurred,
		//the category, the textual description, amount spent, unit of currency, 
		//and whether there is a photographic receipt.


        expenseListView = (ListView) findViewById(R.id.claimantExpensesList);

        Collection<Expense> expenses = claim.getExpenseList().getExpenses();
        final ArrayList<Expense> expensesList = new ArrayList<Expense>(expenses);
        expenselistAdapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, 
        		expensesList);
        expenseListView.setAdapter(expenselistAdapter);
        
        //taken from https://github.com/abramhindle/student-picker and modified
  		listener = new Listener() {			
			@Override
			public void update() {
				expensesList.clear();
				Collection<Expense> expenses = claim.getExpenseList().getExpenses();
				expensesList.addAll(expenses);
				expenselistAdapter.notifyDataSetChanged();
			}
		};
		
		expenseList.addListener(listener);
		for (Listener i : ClaimListController.getClaimList().getListeners()) {
			expenseList.addListener(i);
		}
		
	    expenseListView.setOnItemLongClickListener(new OnItemLongClickListener(){
	        	
	    		public boolean onItemLongClick( AdapterView Parent, View v, int position, long id){
	    			 ExpenseListController.setCurrentExpense(expenselistAdapter.getItem(position));
	    			
	    			//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html
					 AlertDialog.Builder editExpenseDialogBuilder = new AlertDialog.Builder(ClaimantExpenseListActivity.this);
					
					 editExpenseDialogBuilder.setPositiveButton("edit", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
				    			if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted && ClaimListController.getCurrentClaim().getStatus() != Status.approved){
				    				Intent edit = new Intent(getBaseContext(), EditExpenseActivity.class);
				    				startActivity(edit);
				    				
				    			}// if statement
				           }
				       });
					editExpenseDialogBuilder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   ExpenseListController.onRemoveExpenseClick();
				           }
				       });
					editExpenseDialogBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               //Do nothing
				           }
				       });
					editExpenseDialogBuilder.setTitle("Edit/Delete Claim?");
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
	
	public void onCommentClick(View v) {
		Intent intent = new Intent(this, ClaimantCommentActivity.class);
		startActivity(intent);

	}

	public void onAddExpenseClick(View v) {
		ExpenseListController.onAddExpenseClick(this);
	}
}
