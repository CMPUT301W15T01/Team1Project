package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collection;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ClaimantExpenseListActivity extends Activity {

	public Claim claim = ClaimListController.getCurrentClaim();
	private ArrayAdapter<Expense> expenselistAdapter ;
 	private ListView expenseListView ;
 	private Listener listener;
 	private ExpenseList expenseList = claim.getExpenseList();
 	
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
