package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
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


public class ClaimantExpenseListActivity extends Activity {

	public Claim claim;
	private ArrayAdapter<Expense> expenselistAdapter ;
 	private ListView expenseListView ;
 	private Listener listener;
 	private ExpenseList expenseList;
 	private TextView claimInfoHeader;
 	public AlertDialog editExpenseDialog;
 	public AlertDialog submitWarningDialog;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claimant_display_expenses);
		
		//As an approver, I want to list all the expense items for a submitted claim,
		//in order of entry, showing for each expense item: the date the expense was incurred,
		//the category, the textual description, amount spent, unit of currency, 
		//and whether there is a photographic receipt.

		claim=ClaimListController.getCurrentClaim();
		if(claim.getStatus() == Status.submitted || claim.getStatus() ==Status.approved){
			
			Button submitBT = (Button) findViewById(R.id.submitButton);
			submitBT.setClickable(false);
			submitBT.setFocusable(false);
			submitBT.setEnabled(false);
			
			
		}
		expenseList=ExpenseListController.getCurrentExpenseList();
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
		ClaimListController.getClaimList().addListener(listener);
		for (Listener i : ClaimListController.getClaimList().getListeners()) {
			expenseList.addListener(i);
		}
			
		expenseListView.setOnItemClickListener(new OnItemClickListener(){
        	public void onItemClick( AdapterView<?> Parent, View v, int position, long id){
        		ExpenseListController.setCurrentExpense(expenselistAdapter.getItem(position));
    			Intent edit = new Intent(getBaseContext(), EditExpenseActivity.class);
    			startActivity(edit);
        	}
        });
        	
	    expenseListView.setOnItemLongClickListener(new OnItemLongClickListener(){
	        	
	    		public boolean onItemLongClick( AdapterView<?> Parent, View v, int position, long id){
	    			 ExpenseListController.setCurrentExpense(expenselistAdapter.getItem(position));
	    			
	    			//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html
					 AlertDialog.Builder editExpenseDialogBuilder = new AlertDialog.Builder(ClaimantExpenseListActivity.this);
					
					 editExpenseDialogBuilder.setPositiveButton("edit", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				    			//if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted && ClaimListController.getCurrentClaim().getStatus() != Status.approved){
				    			Intent edit = new Intent(getBaseContext(), EditExpenseActivity.class);
				    			startActivity(edit);
				    				
				    			//}
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
	
	public void onCommentClick(View v) {
		Intent intent = new Intent(this, ClaimantCommentActivity.class);
		startActivity(intent);

	}

	public void onAddExpenseClick(View v) {
		ExpenseListController.onAddExpenseClick(this);
	}
	
	public void onSubmitClick(View v) {
		
		boolean expensesFlag = false;
		for(Expense expense: ClaimListController.getCurrentClaim().getExpenseList().getExpenses()){
			if(expense.isFlagged() == true){
				expensesFlag = true;
			}
		}
		
		if(ClaimListController.getCurrentClaim().isComplete() == false || expensesFlag == true){
			AlertDialog.Builder submitBuilder = new AlertDialog.Builder(this);
			submitBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               //Do nothing
		        	   ClaimListController.getCurrentClaim().setStatus(Status.submitted);
		        	   Toast.makeText(getApplicationContext(),"Claim submitted", Toast.LENGTH_LONG).show();
		        	   //push online here
		           }
		    });
			submitBuilder.setNegativeButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Claim was not Submitted", Toast.LENGTH_SHORT).show();
				}
			});
			submitBuilder.setTitle("Claim may be incomplete");
			submitWarningDialog=submitBuilder.create();
			submitWarningDialog.show();

		}else{
			
			if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted && ClaimListController.getCurrentClaim().getStatus() != Status.approved){
				
				//ClaimListController.getCurrentClaim().setStatus(Status.submitted);
				Claim submittedClaim = ClaimListController.getCurrentClaim();
				submittedClaim.setStatus(Status.submitted);
				ClaimListController.updateCurrentClaim(submittedClaim);
				
				Toast.makeText(getApplicationContext(),"Claim submitted", Toast.LENGTH_LONG).show();
				//push online here
				Intent intent =new Intent(this, ClaimantClaimsListActivity.class);
				startActivity(intent);
			}
			else{
				Toast.makeText(getApplicationContext(),"Claim can not be submitted", Toast.LENGTH_SHORT).show();

			}
			
		}
		
	}
	
	public void onDestroy(){
		super.onDestroy();
		expenseList.removeListener(listener);
	}
}
