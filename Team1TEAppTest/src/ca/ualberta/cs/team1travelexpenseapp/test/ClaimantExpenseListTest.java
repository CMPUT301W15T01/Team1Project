package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import junit.framework.TestCase;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantCommentActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.SelectedItemsSingleton;
import ca.ualberta.cs.team1travelexpenseapp.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;


public class ClaimantExpenseListTest extends ActivityInstrumentationTestCase2<ClaimantExpenseListActivity> {
	Activity activity;
	ListView expenseListView;
	Context context;
	
	public ClaimantExpenseListTest() {
		super(ClaimantExpenseListActivity.class);
	}

	private Claim DummyClaim(){
		
		Claim claim = new Claim();
		//by default their status is submitted
		claim.setStartDate(new Date(100));
		claim.setEndDate(new Date(101));
		claim.setStatus(Status.submitted);
		claim.setClaimantName("approver test");
		claim.addDestination("test dest", null);
		//claim.setTotal(100,"EUR");
		
		return claim;
	}
	
	
	protected void setUp() throws Exception {
		super.setUp();
		
		Claimant user = new Claimant("CoolGuy");
		UserSingleton.getUserSingleton().setUser(user);
		
		//add a claim to test on

		Claim claim1 = new Claim("name",new Date(2000,11,11), new Date(2015,12,12));
		//ClaimListController.addClaim(claim1);	
		SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentClaim(claim1);
		
		Intent intent = new Intent();
		intent.putExtra("Index", 0);
		setActivityIntent(intent);
		activity = getActivity();
		expenseListView = (ListView) (activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimantExpensesList));
		
		//add some expense to the claim to test on
		Expense expense1=new Expense("Expense1",new Date(2000,11,11),"category1",
				new BigDecimal(10.00), "CURRENCY1");
		expense1.setComplete(false);//this will default to false
		Expense expense2=new Expense("Expense2",new Date(200,11,13),"category2",
				new BigDecimal(20.00), "CURRENCY2");
		Expense expense3=new Expense("Expense3",new Date(200,11,12),"category3",
				new BigDecimal(25.00), "CURRENCY3");
		
//		claim1.addExpense(expense1);
//		claim1.addExpense(expense2);
//		claim1.addExpense(expense3);
		
	}
	

	
	/*
//	 *  US 7.01.01
//	 *  As a claimant, I want to submit an expense claim for approval, denoting 
//	 *  the claim status as submitted, with no further changes allowed by me to the 
//	 *  claim information (except the tags).
//	 */
//	

	public void testSubmitButton(){
		
		
		//claim
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ClaimantClaimsListActivity.class.getName(), null, false);
		
		final Button submitButton = (Button) activity.findViewById(R.id.submitButton);
		activity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Claim claim = DummyClaim();
				claim.setStatus(Status.inProgress);
				claim.setComplete(true);
				
				ClaimListController.setCurrentClaim(claim);
				ClaimListController.updateCurrentClaim(claim);
				
				submitButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		
		assertTrue("claim submittied success?",
				Status.submitted == ClaimListController.getCurrentClaim().getStatus());
		
		Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 500);
		nextActivity.finish();
		
		
	}
	

	/*
	 * US 7.02.01
	 * As a claimant, I want a visual warning when trying to 
	 * submit an expense claim when there are fields with missing values or 
	 * there are any incompleteness indicators on the expense items.
	 */
	
	public void testSubmitWarning() {
		
		
		Claim claim = new Claim();
		claim.setComplete(false);
		ClaimListController.clearClaimList();
		ClaimListController.updateCurrentClaim(claim);
		

		
		final Button submitButton = (Button) activity.findViewById(R.id.submitButton);
		activity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				submitButton.performClick();
				AlertDialog dia = getActivity().submitWarningDialog;
				assertTrue("Not null", dia != null);
				
			}
		});
		getInstrumentation().waitForIdleSync();
		AlertDialog dia = ClaimListController.submitWarningDialog;
		assertTrue("Not null", dia != null);

		assertTrue("Dialog shows1", dia.isShowing());
		
		claim.setComplete(true);
		Expense expense = new Expense();
		expense.setFlagged(true);
		ExpenseListController.addExpense(expense);
		activity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				submitButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();

		//dia = ClaimListController.WarningDialog;
		//assertTrue("Dialog shows2", dia.isShowing());
	}



	
	//US05.01.01: As a claimant, I want to list all the expense items for a claim, 
	//in order of entry, showing for each expense item: the date the expense was 
	//incurred, the category, the textual description, amount spent, unit of currency, 
	//whether there is a photographic receipt, and incompleteness indicator.
	public void testListExpense(){
		//precondition
		ClaimListController.getClaimList().addClaim(new Claim());
		Claim claim = ClaimListController.getClaimList().get(0);
		
		int claimCount = expenseListView.getCount();
		for(int i=0; i < claimCount; i++){
			//get text info from a claim at position of i of expenseListView 
			TextView expenseInfo = (TextView) expenseListView.getItemAtPosition(i);
			
			//toString() method should be checked manually to verify it contains the correct info
			String viewText = expenseInfo.getText().toString();

			//get expense at position i of expenseList of claim for the activity
			Expense expense=((ClaimantExpenseListActivity) activity).claim.getExpenseList().getExpenses().get(i);
			String expenseText=expense.toString();
			
			String expectedText =((ClaimantExpenseListActivity) activity).claim.toString();
			assertEquals("Expense summary at list item "+i+" does not match expected value",expectedText, viewText);	
		}
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),expenseListView);
	}

	
	/* US07.05.01
	* As a claimant, I want to see the name of the approver and any comment(s) 
	* from the approver on a returned or approved claim.
	*/

	public void testApproverNameComments(){
	
		ClaimList list = new ClaimList();
		final Claim claim =  new Claim();
		list.addClaim(claim);

		ClaimListController.setCurrentClaim(claim);
		
		Expense expense = new Expense();
		ExpenseListController.addExpense(expense);
					
		User checkUser = new User("approver","John");
		ClaimListController.setUser(checkUser);
		
		ClaimListController.getCurrentClaim().addComment("HI it looks good");
		
		// get approve button
		final Button button = (Button) activity.findViewById(R.id.viewCommentsButton);
		//from http://stackoverflow.com/a/9406087 (March 15, 2015)
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ClaimantCommentActivity.class.getName(), null, false);
		activity.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// click approve button
				button.performClick(); 	
				
			}
			
		});
		getInstrumentation().waitForIdleSync();

		Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		// next activity is opened and captured.
		TextView text = (TextView) nextActivity.findViewById(R.id.claimantCommentString);
		assertEquals("Can View Comments","John\nHI it looks good", text.getText().toString());
		assertNotNull(nextActivity);
		nextActivity.finish();
	}
	
	//US08.06.01
	/*
	*Tests if an approver comment
	*was successfully added to 
	*a claim
	*/
	
	//this test is for PART 5
	
	public void testCommentAddable(){
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ClaimantCommentActivity.class.getName(), null, false);
		Claim claim = new Claim();
		ClaimListController.setCurrentClaim(claim);
		User user = new User("Approver", "Geoff");
		ClaimListController.setUser(user);
		
		claim.addComment("comment");
		
		final Button commentButton = (Button) activity.findViewById(R.id.viewCommentsButton);
		
//		activity.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				commentButton.performClick();
//			}
//		});
//		getInstrumentation().waitForIdleSync();

		

		Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);

		//TextView text = (TextView) nextActivity.findViewById(R.id.claimantCommentString);
		//assertEquals("Can View Comments","comment", text.getText().toString());
		assertNotNull(nextActivity);
		nextActivity.finish();
		

		
	}
}
