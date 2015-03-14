
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.LoginActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.User;
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



//	private Intent launch;
//	final Button approverButton;
//	
	
	protected void setUp() throws Exception {
		super.setUp();

	}
	public ApproverClaimListTest() {
		super(LoginActivity.class);
		// TODO Auto-generated constructor stub
        //launch = new Intent(getInstrumentation()
        //        .getTargetContext(), ApproverClaimsListActivity.class);
//        approverButton = (Button) getActivity()
//                .findViewById(R.id.approveButton);
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
		//userSelect.finish();		
	}
//	
//	
//	////US08.02.01
//	/*testApproverSortedClaims()
//	 * checks if ApproverActivity receives a sorted
//	 * list of claims
//	 */
//	public void testApproverSortedClaims(){
//		
//		ClaimListController list = new ClaimListController();
//		Claim claim1 = (DummyClaim());
//		claim1.setDate(200);
//		list.add(claim1);
//		Claim claim2 = (DummyClaim());
//		claim2.setDate(300);
//		list.add(claim2);
//		Claim claim3 = (DummyClaim());
//		claim3.setDate(400);
//		list.add(claim3);
//		Claim claim4 = (DummyClaim());
//		claim4.setDate(500);
//		list.add(claim4);
//		
//		list.saveOnline();
//		
//		Claim claimToCheck;
//		Claim claimCompared;
//		
//		
//		
//		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimsActivity.class.getName(), null, false);
//		UserSelectActivity  userSelect = new UserSelectActivity();
//		
//		final Button approverBT = (Button) userSelect.findViewById(R.id.BTApprover);
//		userSelect.runOnUiThread(new Runnable(){
//			
//			public void run(){
//				
//				approverBT.performClick();// approver user type is selected
//				//User type selected : precondition
//			}
//			
//		});
//		
//		ApproverClaimListActicity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
//		assertNotNull(nextActivity);
//		//claims loaded onStart of nextActivity
//		
//		ListView claimlistView = (ListView) nextActivity.findViewById(R.id.ApproverClaimLV);
//		ViewAsserts.assertOnScreen(nextActivity.getWindow().getDecorView(),view);
//		
//		
//		
//		//checks if claims are in order
//		for(int i= 0; i < (ApprovedClaims.length - 1); i++){
//			
//			claimToCheck = ApprovedClaims.get(i);
//			claimCompared = ApprovedClaims.get(i+1);
//			assertTrue("Approved Claims are sorted?", claimToCheck.getDate() < claimCompared.getDate());
//			
//		}
//		claimToCheck = ApprovedClaims.get(ApprovedClaims.length -1);	
//		claimCompared = ApprovedClaims.get(ApprovedClaims.length - 2);
//		assertTrue("Last approved Claim in order?", claimToCheck.getDate() > claimCompared.getDate());
//	}
//	
//	

//	
//	
//	
//	//US08.06.01
//	/*
//	*Tests if an approver comment
//	*was successfully added to 
//	*a claim
//	*/
//	public void testCommentAddable(){
//			String testComment = "Test comment."; // pre condition. comment must exist
//			CommentFragment commentBox = new CommentFragment();
//			
//			commentBox.setArguments(DummyClaim().addExpense(DummyExpense()));
//			commentBox.addComment(testComment);
//			Claim claim = commentBox.getArguments();
//			
//			AssertEquals("Comment Written successfully?", testComment, claim.getComment());
//		
//	}
//	
//	
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
//	
//	
//	
//	
//	
}
