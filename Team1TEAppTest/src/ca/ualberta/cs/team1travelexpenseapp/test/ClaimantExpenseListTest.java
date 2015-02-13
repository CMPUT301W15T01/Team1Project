package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.TestCase;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.R;


public class ClaimantExpenseListTest extends ActivityInstrumentationTestCase2<ClaimantExpenseListActivity> {
	Activity activity;
	ListView expenseListView;
	
	public ClaimantExpenseListTest() {
		super(ClaimantExpenseListActivity.class);
	}
	
	
	protected void setUp() throws Exception {
		super.setUp();
		//add a claim to test on
		Claim claim1 = new Claim("name",new Date(2000,11,11), new Date(2015,12,12));
		ClaimsListController.addClaim(claim1);	
		
		Intent intent = new Intent();
		intent.putExtra("Index", 0);
		setActivityIntent(intent);
		activity = getActivity();
		expenseListView = (ListView) (activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.expenseList));
		
		//add some claims to the expense to test on
		Expense expense1=new Expense("Expense1",new Date(2000,11,11),"category1",
				new BigDecimal(10.00), "CURRENCY1");
		expense1.setIncomplete(true);//this will default to false
		Expense expense2=new Expense("Expense2",new Date(200,11,13),"category2",
				new BigDecimal(20.00), "CURRENCY2");
		Expense expense3=new Expense("Expense3",new Date(200,11,12),"category3",
				new BigDecimal(25.00), "CURRENCY3");
		
		claim1.addExpense(expense1);
		claim1.addExpense(expense2);
		claim1.addExpense(expense3);
		
	}
	
	/*
	 *  US 7.01.01
	 *  As a claimant, I want to submit an expense claim for approval, denoting 
	 *  the claim status as submitted, with no further changes allowed by me to the 
	 *  claim information (except the tags).
	 */
	public void testSubmitClaim(){
		final Claim claim = ClaimsListController.getClaim(0);
		Button button = (Button) activity.findViewById(R.id.submitClaimButton);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				claim.submit();
				claim.setStatus("Submitted");
				
			}
		});
		Claim claimSubmitted = ClaimsListController.getSubmittedClaim(1);
		assertEquals("Claim Submitted", claim, claimSubmitted);
		assertEquals("Claim status submitted", "Submitted", claim.getStatus());
		assertFalse("Claim name not editable", claim.setName());
		assertFalse("Claim destination not editable", claim.addDestination());
		assertFalse("Claim reason not editable", claim.addReason());
		assertFalse("Claim from date not editable", claim.setFromDate());
		assertFalse("Claim to date not editable", claim.setToDate());
	}
	
	

	//US05.01.01: As a claimant, I want to list all the expense items for a claim, 
	//in order of entry, showing for each expense item: the date the expense was 
	//incurred, the category, the textual description, amount spent, unit of currency, 
	//whether there is a photographic receipt, and incompleteness indicator.
	public void testListExpense(){
		int claimCount = expenseListView.getCount();
		for(int i=0; i < claimCount; i++){
			//get text info from a claim at position of i of expenseListView 
			TextView expenseInfo = (TextView) expenseListView.getItemAtPosition(i);
			
			//toString() method should be checked manually to verify it contains the correct info
			String viewText = expenseInfo.getText().toString();

			//get expense at position i of expenseList of claim for the activity
			Expense expense=((ClaimantExpenseListActivity) activity).claim.getExpense(i);
			String expenseText=expense.toString();
			
			String expectedText =((ClaimantExpenseListActivity) activity).claim.toString();
			assertEquals("Expense summary at list item "+i+" does not match expected value",expectedText, viewtext);	
		}
	}
	
	
}
