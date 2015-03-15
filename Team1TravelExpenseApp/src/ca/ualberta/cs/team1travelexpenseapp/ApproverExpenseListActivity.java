package ca.ualberta.cs.team1travelexpenseapp;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
		finish();
	}
	
	public void onReturnClick(View v) {
		ClaimListController.onReturnClick();
		finish();
	}
	public void onInfoClick(View v) {
		Intent intent = new Intent(this,ApproverClaimInfo.class);
		startActivity(intent);
	}
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
		  ClaimListController.onCommentClick(value);
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

