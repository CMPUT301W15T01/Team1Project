package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimInfo;
import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ApproverExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantCommentActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.LoginActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ApproverExpenseListTest extends ActivityInstrumentationTestCase2<ApproverExpenseListActivity> {
	
		Activity activity;
		ListView claimListView;
			
		public ApproverExpenseListTest() {
			super(ApproverExpenseListActivity.class);
		}
		
		protected void setUp() throws Exception {
			ClaimListController.setCurrentClaim(new Claim());
			ExpenseListController.addExpense(new Expense());
			//ClaimListController.getCurrentClaim().addExpense(new Expense());
			super.setUp();
			Intent intent = new Intent();
			setActivityIntent(intent);
			activity = getActivity();
			
			//setup for 8.03.01
			
			
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
	
		private Expense DummyExpense(){
			
			Expense expense = new Expense();
			
			//expense.setDate(new Date(100));
			//expense.setTotal(100, USD);
			
			return expense;
			
		}
		
		//US08.03.01
		/*
		 *Testing if we can see all of
		 *the claim info for approvers
		 * */
		public void testClaimInfo(){
			
			// init claim/expenses			
			Claim claim = new Claim();
			ClaimListController.setCurrentClaim(claim);
			Expense expense1 = new Expense("Airfaire", new Date(), "Skiing", new BigDecimal(20), "USD");
			Expense expense2 = new Expense("Airfaire", new Date(), "Skiing", new BigDecimal(50), "CAD");
			ExpenseListController.addExpense(expense1);
			ExpenseListController.addExpense(expense2);
			final TextView infoButton = (TextView) activity.findViewById(R.id.approverInfoButton);
			
			//from http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium
			ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimInfo.class.getName(), null, false);
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					infoButton.performClick();
				}
				
			});
			getInstrumentation().waitForIdleSync();
			Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 50);
			assertNotNull(nextActivity);
			TextView text = (TextView) nextActivity.findViewById(R.id.ApproverClaimInfoTextView);
			assertEquals("Can View Info", ClaimListController.getCurrentClaim().toString(),text.getText());
			nextActivity.finish();

			
		}
//		//US08.04.01
//		/*
//		 * Just checks if expenses 
//		 * from claims can be seen
//		 * 
//		 * */
		public void testApproverClaimsVisible(){	
			
			ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverExpenseListActivity.class.getName(), null, false);
			ApproverExpenseListActivity  userSelect = getActivity();
			
			assertNotNull(userSelect);
		
	
			ListView expenseListLV = (ListView) userSelect.findViewById(R.id.approverExpensesList);
			ViewAsserts.assertOnScreen(userSelect.getWindow().getDecorView(),expenseListLV);
			
			final Button approverBT = (Button) userSelect.findViewById(R.id.approveButton);
			userSelect.runOnUiThread(new Runnable(){
				
				public void run(){
					
					approverBT.performClick();// approver user type is selected
					//User type selected : precondition
				}
				
			});
			
			getInstrumentation().waitForIdleSync();
			
			assertEquals("claim not approved",Status.approved,ClaimListController.getCurrentClaim().getStatus());
			
			userSelect.finish();
			
		}
	
		//US08.05.01
		/*Checks if receipt is visible
		 * for the approver*/
		public void testReceiptVisible(){
			 // activity doesnt exist yet
			 //ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimSummaryActivity.class.getName(), null, false);
			 
			 final ListView expenseListLV = (ListView) activity.findViewById(R.id.approverExpensesList);
			 activity.runOnUiThread(new Runnable(){
					
					public void run(){
						
						expenseListLV.performClick();//onClick would be overrided
					}
					
			});
			
				
			//Activity lastActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
			//assertNotNull(lastActivity);
			
			// image view doesnt exist yet!
			//ImageView image = (ImageView)
			//ViewAsserts.assertOnScreen(lastActivity.getWindow().getDecorView(),image); 
		}

		//US08.08.01
		/*
		*Tests if the Appover's name
		*is attached to 
		* an approved claim
		* and status is approved
		*/
		public void testApproverApprovesClaim(){
			
			ClaimList list = new ClaimList();
			final Claim claim =  new Claim();
			ClaimListController.setCurrentClaim(claim);
			
			//ClaimListController.getCurrentClaim().addExpense(expense);
			ExpenseListController.addExpense(new Expense());
			
			User checkUser = new User("approver","John");
			ClaimListController.setUser(checkUser);
			
			// get approve button
			final Button button = (Button) activity.findViewById(R.id.approveButton);
			
			activity.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// click approve button
					button.performClick();
				}
				
			});
			getInstrumentation().waitForIdleSync();
			assertEquals("Claim is the changed claim", claim, ClaimListController.getCurrentClaim());
			assertEquals("Status is approved", Status.approved, ClaimListController.getCurrentClaim().getStatus());
			
			User user = ClaimListController.getCurrentClaim().getApproverList().get(0);
			assertEquals("Name is John", "John", ClaimListController.getUser().getName());
			assertEquals("User is approver", user, ClaimListController.getUser());
			
			

			
		}

}
