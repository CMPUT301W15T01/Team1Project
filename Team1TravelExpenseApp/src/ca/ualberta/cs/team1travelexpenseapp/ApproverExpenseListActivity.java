package ca.ualberta.cs.team1travelexpenseapp;

import java.math.BigDecimal;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ApproverExpenseListActivity extends Activity {

	public Claim claim;
	private ArrayAdapter<Expense> expenselistAdapter ;
 	private ListView expenseListView ;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.approver_display_expenses);
		
		// display current claims expense items 
		claim = ClaimListController.getCurrentClaim();
		
		expenseListView = (ListView) findViewById(R.id.expensesList);
        expenselistAdapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, 
        		claim.getExpenseList().getExpenses());
        expenseListView.setAdapter(expenselistAdapter);
        
	}
	
	@Override
	protected void onStart() {
		super.onStart();
        // display general claim info at top of screen
      	Claim myClaim = ClaimListController.getCurrentClaim();
        TextView claimInfo = (TextView) findViewById(R.id.approverClaimInfoTextView);
        String infoString = myClaim.toString();
        claimInfo.setText(infoString);
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
	
	public void onApproveClick(View v) {
		ClaimListController.onApproveClick();
		
	}
}
