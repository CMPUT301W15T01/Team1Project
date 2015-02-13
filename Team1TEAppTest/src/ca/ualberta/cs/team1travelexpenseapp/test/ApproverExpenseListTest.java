package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.Calendar;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.ApproverExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
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
		public void testExpensesVisible(){
				
			  Expense expense = DummyExpense();
			  ApproverExpenseActivity ApproverActivity = new ApproverExpenseActivity();
			  ClaimlistController controller = new ClaimlistController();
			  
			  Claim claim = DummyClaim();
			  claim.addExpense(expense);
			  controller.add(claim);
			  controller.saveOnline();
			 
			  ApproverActivity.getSubmittedClaims();
			  ListView view = (ListView) activity.findViewById(R.id.claimlistview);
			  ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),view);
			  assertTrue("expense is not in claim",claim.contains(expense));
			
		}
		
		//US08.05.01
		/*Checks if receipt is visible
		 * for the approver*/
		public void testReceiptVisible(){
			ApproverExpenseSummaryActivity activity = new ApproverExpenseSummaryActivity();
			ImageButton receipt = (ImageButton) activity.findViewById(R.id.receipt);
			ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),receipt);
			
			
		}

	

}
