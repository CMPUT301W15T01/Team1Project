
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantCommentActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.LoginActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.TestCase;

public class ApproverClaimListTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	protected Claimant user;
	
	
	public ApproverClaimListTest() {
		super(LoginActivity.class);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception {
		super.setUp();

	}
	
	private Claim DummyClaim(){
		
		Claim claim = new Claim();
		//by default their status is submitted
		claim.setStartDate(new Date(100));
		claim.setEndDate(new Date(101));
		claim.setStatus(Claim.Status.returned);
		claim.getApproverList().add(new User("Approver","bobby"));
		claim.setApproverList(claim.getApproverList());
		return claim;
	}
	
	private Expense DummyExpense(){
			
			Expense expense = new Expense();
			
			expense.setDate(new Date(100));
			expense.setAmount(new BigDecimal(900));
			return expense;
			
		}
	
	
	private LoginActivity startWithClaims(Intent claims){
		
		Intent intent = new Intent();
		
		intent.putExtras(claims);
		setActivityIntent(intent);		
		
		return getActivity();
	}
	
	//US08.01.01
	
	/*
	 * Tests that the Approver can properly
	 * navigate to the screen
	 * with all the claims
	 * 
	 * */
	public void testgetSubmittedClaims(){
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimsListActivity.class.getName(), null, false);
		LoginActivity  userSelect = (LoginActivity) getActivity();
		
		final Button approverBT = (Button) userSelect.findViewById(R.id.approverButton);
		userSelect.runOnUiThread(new Runnable(){
			
			public void run(){
				
				approverBT.performClick();// approver user type is selected
				//User type selected : precondition
			}
			
		});
		
		ApproverClaimsListActivity nextActivity = (ApproverClaimsListActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		assertNotNull(nextActivity);
		
		ListView claimlistView = (ListView) nextActivity.findViewById(R.id.approverclaimList);
		ViewAsserts.assertOnScreen(nextActivity.getWindow().getDecorView(),claimlistView);
		
		nextActivity.finish();
	}
	
	
	////US08.02.01
	/*testApproverSortedClaims()
	 * checks if ApproverActivity receives a sorted
	 * list of claims
	 */
	public void testApproverSortedClaims(){
		

		Claim claim= new Claim();
		ClaimListController.setCurrentClaim(claim);
		
		//claims loaded onStart of nextActivity
		ListView claimlistView = (ListView) getActivity().findViewById(R.id.approverclaimList);
		ViewAsserts.assertOnScreen(getActivity().getWindow().getDecorView(),claimlistView);
			
		//checks if claims are in order
		ClaimList submittedClaims = ClaimListController.getSubmittedClaims();
		Claim claimToCheck;
		Claim claimCompared;
		for(int i= 0; i < (submittedClaims.length() - 1); i++){
			
			claimToCheck = submittedClaims.get(i);
			claimCompared = submittedClaims.get(i+1);
			assertTrue("Approved Claims are sorted?", claimToCheck.getStartDate().compareTo(claimCompared.getStartDate())<0);
			
		}
	
	}
	
	
	//US08.07.01
	/*
	* tests if returned claims
	* show up for claimants
	*
	*/
	public void testReturnedVisible(){
		ClaimList list = new ClaimList();
		list.addClaim(DummyClaim());
		
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimsListActivity.class.getName(), null, false);
		
		LoginActivity  userSelect = getActivity();
		
		final Button returnBT = (Button) userSelect.findViewById(R.id.approverButton);
		userSelect.runOnUiThread(new Runnable(){
			
			public void run(){
				
				returnBT.performClick();// approver user type is selected
				//User type selected : precondition
			}
			
		});
		
		ApproverClaimsListActivity nextActivity = (ApproverClaimsListActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		assertNotNull(nextActivity);
		
		
		
		ClaimList claimList = new ClaimList();
		
		claimList = ClaimListController.getClaimList();
		
		int statuscount = 0;
		
		for(int i =0; i<claimList.getClaims().size(); i++){
			
			if(claimList.getClaim(i).getStatus().equals(Claim.Status.returned)){
				statuscount++;
				
			}
			
		}
		
		assertTrue("Claim was returned?", statuscount == 0);
		userSelect.finish();
		nextActivity.finish();
	}

}
