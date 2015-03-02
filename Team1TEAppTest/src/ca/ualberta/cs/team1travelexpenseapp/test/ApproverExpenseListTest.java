package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.Calendar;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.ApproverExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
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
			super.setUp();
			Intent intent = new Intent();
			setActivityIntent(intent);
			activity = getActivity();
		}
		
		private Claim DummyClaim(){
			
			Claim claim = new Claim();
			//by default their status is submitted
			claim.setStartDate(new Date(100));
			claim.setEndDate(new Date(101));
			claim.setStatus("submitted");
			claim.setName("approver test");
			claim.addDestination("test dest");
			claim.setTotal(100,"EUR");
			
			return claim;
		}
	
		private Expense DummyExpense(){
			
			Expense expense = new Expense("Expense Name");
			
			expense.setDate(new Date(100));
			expense.setTotal(100, USD);
			
			return expense;
			
		}
		
		
		//US08.04.01
		/*
		 * Just checks if expenses 
		 * from claims can be seen
		 * 
		 * */
		public void testApproverClaimsVisible(){	
			
			ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimsActivity.class.getName(), null, false);
			UserSelectActivity  userSelect = new UserSelectActivity();
			
			final Button approverBT = (Button) userSelect.findViewById(R.id.BTApprover);
			userSelect.runOnUiThread(new Runnable(){
				
				public void run(){
					
					approverBT.performClick();// approver user type is selected
					//User type selected : precondition
				}
				
			});
			
			ApproverClaimListActivity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
			assertNotNull(nextActivity);
			
			//ApproverClaimSummaryActivity approverCSA = new ApproverClaimSummaryActivity(); 
			final ListView claimListLV = (ListView) nextActivity.findViewById(R.id.LVclaimList);
			approverCSA.runOnUiThread(new Runnable(){
				
				public void run(){
					
					claimListLV.performClick();//onClick would be overrided
				}
				
			});
		
			
			ApproverClaimSummary lastActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
			assertNotNull(lastActivity);
			
			final ListView expenseListLV = (ListView) lastActivity.findViewById(R.id.LVExpenseList);
			ViewAsserts.assertOnScreen(lastActivity.getWindow().getDecorView(),view);
			
			
			
		}
		
		
		//US08.05.01
		/*Checks if receipt is visible
		 * for the approver*/
		public void testReceiptVisible(){
			
			 ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimSummaryActivity.class.getName(), null, false);
				
			 ApproverClaimSummaryActivity approverCSA = new ApproverClaimSummaryActivity(); 
			 final ListView expenseListLV = (ListView) approverCSA.findViewById(R.id.LVExpenseList);
			 approverCSA.runOnUiThread(new Runnable(){
					
					public void run(){
						
						expenseListLV.performClick();//onClick would be overrided
					}
					
				});
			
				
				ApproverExpenseSummary lastActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
				assertNotNull(lastActivity);
				
			 
			  final ImageButton receipt = lastActivity.findViewById(R.id.receiptImage);
			  ViewAsserts.assertOnScreen(lastActivity.getWindow().getDecorView(),view); 
				
			

		}

	

}
