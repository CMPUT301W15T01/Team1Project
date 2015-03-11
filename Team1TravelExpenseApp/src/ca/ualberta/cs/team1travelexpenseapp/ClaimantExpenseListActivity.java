package ca.ualberta.cs.team1travelexpenseapp;

import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ClaimantExpenseListActivity extends Activity {

	public Claim claim;
	private ArrayAdapter<Expense> expenselistAdapter ;
 	private ListView expenseListView ;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claimant_display_expenses);
		
		//As an approver, I want to list all the expense items for a submitted claim,
		//in order of entry, showing for each expense item: the date the expense was incurred,
		//the category, the textual description, amount spent, unit of currency, 
		//and whether there is a photographic receipt.


		// TODO: need to set the claim, probably in onStart? 
		//commented out below because not yet functional
        /*expenseListView = (ListView) findViewById(R.id.expensesList);
        claim=ClaimListController.getCurrentClaim();
        expenselistAdapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, 
        		claim.getExpenses());
        expenseListView.setAdapter(expenselistAdapter);
        */
		
		TextView ClaimInfoDisplay  = (TextView) findViewById(R.id.destination);
		ClaimInfoDisplay.setText(ClaimListController.getCurrentClaim().toString());
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
	
	public void onSubmitClick(View v){
		if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted){
				if (ClaimListController.getCurrentClaim().getStatus() != Status.approved){
			
		
					ClaimListController.getCurrentClaim().setStatus(Status.submitted);
					Toast.makeText(getApplicationContext(),"Claim submitted", Toast.LENGTH_LONG).show();
					//push online here
					Intent intent =new Intent(this, ClaimantClaimsListActivity.class);
					startActivity(intent);
				}
		
		
		}
		else{
			Toast.makeText(getApplicationContext(),"Claim can not be submitted", Toast.LENGTH_SHORT).show();
			
		}
	
	
	}
}	
