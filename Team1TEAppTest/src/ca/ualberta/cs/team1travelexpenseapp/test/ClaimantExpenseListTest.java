package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import junit.framework.TestCase;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.User;


public class ClaimantExpenseListTest extends ActivityInstrumentationTestCase2<ClaimantExpenseListActivity> {
	Activity activity;
	ListView expenseListView;
	Context context;
	
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
		
		//add some expense to the claim to test on
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
	
	public void testSubmit() {
		//preconditions - User has a claim made that they are viewing 
		User user = new User("Claimant");
		Activity activity = getActivity();
		Claim claim = ClaimsListController.getClaim(0);
		
		// from http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html#DetermineConnection
		ConnectivityManager cm =
		        (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		
		assertTrue("Connected", isConnected);
		
		//Trigger
		// from http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium
		
		final Button button = (Button) activity.findViewById(R.id.submitClaimButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button.performClick();
		    }
		});
		
		assertEquals("Status submitted", "Submitted", claim.getStatus());
		assertFalse("Claim name not editable", claim.setName());
		assertFalse("Claim destination not editable", claim.addDestination(null));
		assertFalse("Claim reason not editable", claim.addReason());
		assertFalse("Claim from date not editable", claim.setFromDate());
		assertFalse("Claim to date not editable", claim.setToDate());
		assertTrue("Claim tags editable", claim.addTag());

		 
		
	}
	// old test
	public void testSubmitClaim(){
		final Claim claim = ClaimsListController.getClaim(0);
		Button button = (Button) activity.findViewById(R.id.submitClaimButton);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				claim.submit();
				
			}
		});
		Claim claimSubmitted = ClaimsListController.getSubmittedClaim(0);
		assertEquals("Claim Submitted", claim, claimSubmitted);
		assertEquals("Claim status submitted", "Submitted", claim.getStatus());
		assertFalse("Claim name not editable", claim.setName());
		assertFalse("Claim destination not editable", claim.addDestination(null));
		assertFalse("Claim reason not editable", claim.addReason());
		assertFalse("Claim from date not editable", claim.setFromDate());
		assertFalse("Claim to date not editable", claim.setToDate());
		assertTrue("Claim tags editable", claim.addTag());

	}
	/*
	 * US 7.02.01
	 * As a claimant, I want a visual warning when trying to 
	 * submit an expense claim when there are fields with missing values or 
	 * there are any incompleteness indicators on the expense items.
	 */
	
	public void testSubmitWarning() {
		//preconditions
		Claim claim = ClaimsListController.getClaim(0);
		Expense expense = claim.getExpense(0);
		expense.setIncomplete(true);
		//trigger
		final Button button = (Button) activity.findViewById(R.id.submitClaimButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button.performClick();
		    }
		});
		// Test toast is shown (Check if this is proper way)
		View v = activity.getWindow().getDecorView().findViewById(android.R.id.content);
		assertTrue("Toast is shown", v.isShown());
	}

	/*
	 *  US 7.03.01
	 *  As a claimant, I want a submitted expense claim that was 
	 *  not approved to be denoted by a claim status of returned, with further 
	 *  changes allowed by me to the claim information.
	 */
	
	public void testReturned() {
		// preconditions
		Claim claim = ClaimsListController.getReturnedClaim(1);
		
		assertEquals("Claim status returned", "Returned", claim.getStatus());

		EditText editName = (EditText) activity.findViewById(R.id.claimNameBody);
		editName.setText("Joe");
		
		EditText editDestination = (EditText) activity.findViewById(R.id.claimDestinationBody);
		editDestination.setText("Hawaii");
		
		EditText editReason = (EditText) activity.findViewById(R.id.claimReasonBody);
		editReason.setText("Business");
		
		final Button button = (Button) activity.findViewById(R.id.saveClaimButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button.performClick();
		    }
		});
		
		assertEquals("Claim name editable", claim.getName(), "Joe");
		assertEquals("Claim destination editable", claim.getDestination(0), "Joe");
		assertEquals("Claim reason editable", claim.getReason(), "Business");
	}
	
	/*
	 *  US 7.04.01
	 *  As a claimant, I want a submitted expense claim that was 
	 *  approved to be denoted by a claim status of approved, with no
	 *   further changes allowed by me to the claim information (except the tags).
	 */
	
	public void testApproved(){
		// precondition - claimant has an approved claim
		Claim claim = ClaimsListController.getApprovedClaim(0);
		
		EditText editName = (EditText) activity.findViewById(R.id.claimNameBody);
		EditText editDestination = (EditText) activity.findViewById(R.id.claimDestinationBody);
		EditText editReason = (EditText) activity.findViewById(R.id.claimReasonBody);

		editName.setText("Joe");
		editDestination.setText("Hawaii");
		editReason.setText("Business");
		claim.addTag("Holiday");

		final Button button = (Button) activity.findViewById(R.id.saveClaimButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button.performClick();
		    }
		});
		assertEquals("Claim status Approved", "Approved", claim.getStatus());
		assertEquals("Claim tags editable", claim.getTag(0), "Holiday");
		assertNotSame("Claim name not editable", claim.getName(), "Joe");
		assertNotSame("Claim destination not editable", claim.getDestination(0), "Hawaii");
		assertNotSame("Claim reason not editable", claim.getReason(), "Business");

		
	}
	
	//US05.01.01: As a claimant, I want to list all the expense items for a claim, 
	//in order of entry, showing for each expense item: the date the expense was 
	//incurred, the category, the textual description, amount spent, unit of currency, 
	//whether there is a photographic receipt, and incompleteness indicator.
	public void testListExpense(){
		//precondition
		Claim claim = ClaimsListController.getClaim(0);
		
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
