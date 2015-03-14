package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ApproverExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.LoginActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.User;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.Button;
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
		
        // COMMENTED OUT BECAUSE THEY DONT WORK YET  
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
			final TextView info = (TextView) activity.findViewById(R.id.approverClaimInfoTextView);
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.d("TEST", ClaimListController.getCurrentClaim().toString());
					info.setText(ClaimListController.getCurrentClaim().toString());
				}
				
			});
			
			assertEquals("Claim info visible", ClaimListController.getCurrentClaim().toString(), info.getText().toString());

			
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
			
//			final Button approverBT = (Button) userSelect.findViewById(R.id.approverButton);
//			userSelect.runOnUiThread(new Runnable(){
//				
//				public void run(){
//					
//					approverBT.performClick();// approver user type is selected
//					//User type selected : precondition
//				}
//				
//			});
			
			//ApproverExpenseListActivity nextActivity = (ApproverExpenseListActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
			//assertNotNull(nextActivity);
			
			//ApproverClaimSummaryActivity approverCSA = new ApproverClaimSummaryActivity(); 
//			final ListView claimListLV = (ListView) nextActivity.findViewById(R.id.expensesList);
//			approverCSA.runOnUiThread(new Runnable(){
//				
//				public void run(){
//					
//					claimListLV.performClick();//onClick would be overrided
//				}
//				
//			});
		
			
//			ApproverClaimsListActivity lastActivity = (ApproverClaimsListActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
//			assertNotNull(lastActivity);
			
			ListView expenseListLV = (ListView) userSelect.findViewById(R.id.approverExpensesList);
			ViewAsserts.assertOnScreen(userSelect.getWindow().getDecorView(),expenseListLV);
			
			
			
		}
//		
//		
//		//US08.05.01
//		/*Checks if receipt is visible
//		 * for the approver*/
//		public void testReceiptVisible(){
//			
//			 ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimSummaryActivity.class.getName(), null, false);
//				
//			 ApproverClaimSummaryActivity approverCSA = new ApproverClaimSummaryActivity(); 
//			 final ListView expenseListLV = (ListView) approverCSA.findViewById(R.id.LVExpenseList);
//			 approverCSA.runOnUiThread(new Runnable(){
//					
//					public void run(){
//						
//						expenseListLV.performClick();//onClick would be overrided
//					}
//					
//				});
//			
//				
//				ApproverExpenseSummary lastActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
//				assertNotNull(lastActivity);
//				
//			 
//			  final ImageButton receipt = lastActivity.findViewById(R.id.receiptImage);
//			  ViewAsserts.assertOnScreen(lastActivity.getWindow().getDecorView(),view); 
//				
//			
//
//		}

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
			list.addClaim(claim);
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
					assertEquals("Claim is the changed claim", claim, ClaimListController.getCurrentClaim());
					assertEquals("Status is approved", Status.approved, ClaimListController.getCurrentClaim().getStatus());
					ArrayList<User> approvers = claim.getApproverList();
					User user = approvers.get(0);
					assertEquals("Name is John", "John", ClaimListController.getUser().getName());
					assertEquals("User is approver", user, ClaimListController.getUser());
				}
				
			});
			
			

			
		}

}
