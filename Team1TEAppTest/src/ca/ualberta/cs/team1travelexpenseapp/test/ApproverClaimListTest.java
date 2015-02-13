
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import junit.framework.TestCase;

public class ApproverClaimListTest extends ActivityInstrumentationTestCase2<ApproverClaimListActivity> {

	private Intent launch;
	final Button approverButton;
	
	
	protected void setUp() throws Exception {
		super.setUp(ClaimActivity.class);
        launch = new Intent(getInstrumentation()
                .getTargetContext(), ClaimActivity.class);
        startActivity(launch, null, null);
        approverButton = (Button) getActivity()
                .findViewById(R.id.approve_button);
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
	
	
	private ApproverClaimActivity startWithClaims(ClaimList claims){
		
		Intent intent = new Intent();
		
		intent.putExtra(claims);
		setActivityIntent(intent);		
		
		return (ApproverClaimActivity) getActivity();
	}
	
	//US08.01.01
	
	/*
	 * Tests that the Approver properly
	 * receives a submitted claim from online
	 * */
	public void testApproverProperReceive(){
		
		ApproverActivity activity = new ApproverActivity();
		ClaimListController list = new ClaimListController();

		// Add to the controller
		list.add(DummyClaim());
		list.saveOnline();
		ClaimList ApprovedClaims = activity.getSubmittedClaims();
		
		AssertEquals("Did approver get Claim?" ApprovedClaims.get(0).toString(), list.get(0).toString());
		
	}
	
	
	////US08.02.01
	/*testApproverSortedClaims()
	 * checks if ApproverActivity receives a sorted
	 * list of claims
	 */
	public void testApproverSortedClaims(){
		
		ApproverActivity activity = new ApproverActivity();
		ClaimListController list = new ClaimListController();
		
		Claim claim1 = (DummyClaim());
		claim1.setDate(200);
		list.add(claim1);
		Claim claim2 = (DummyClaim());
		claim2.setDate(300);
		list.add(claim2);
		Claim claim3 = (DummyClaim());
		claim3.setDate(400);
		list.add(claim3);
		Claim claim4 = (DummyClaim());
		claim4.setDate(500);
		list.add(claim4);
		Claim claim5 = (DummyClaim());
		claim5.setDate(600);
		list.add(claim5);
		
		list.saveOnline();
		ClaimList ApprovedClaims = activity.getSubmittedClaims();
		
		Claim claimToCheck;
		Claim claimCompared;
		for(int i= 0; i < (ApprovedClaims.length - 1); i++){
			
			claimToCheck = ApprovedClaims.get(i);
			claimCompared = ApprovedClaims.get(i+1);
			assertTrue("Approved Claims are sorted?", claimToCheck.getDate() < claimCompared.getDate());
			
		}
		claimToCheck = ApprovedClaims.get(ApprovedClaims.length -1);	
		claimCompared = ApprovedClaims.get(ApprovedClaims.length - 2);
		assertTrue("Last approved Claim in order?", claimToCheck.getDate() > claimCompared.getDate());
	}
	
	
	//US08.03.01
	/*
	 * in previous tests
	 * we test if a claim is 
	 * received properly by
	 * the approver
	 * so we only test if the list 
	 * of claims
	 * is visible
	 * 
	 * */
	public void testApproverClaimsVisible(){
		
		ApproverClaimActivity activity = new ApproverClaimActivity(); 
		ListView claimlist = (ListView) activity.findViewById(R.id.ApproverClaimList);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),view);
	}
	
	
	//US08.06.01
	/*
	*Tests if an approver comment
	*was successfully added to 
	*a claim
	*/
	public void testCommentAddable(){
			String testComment = "Test comment.";
			CommentFragment commentBox = new CommentFragment();
			
			commentBox.setArguments(DummyClaim().addExpense(DummyExpense()));
			commentBox.addComment(testComment);
			Claim claim = commentBox.getArguments();
			
			AssertEquals("Comment Written successfully?", testComment, claim.getComment());
		
	}
	
	
	//US08.07.01
	/*
	* tests if returned claims
	* show up for claimants
	*
	*/
	public void testReturnedVisible(){
		Claimlist list = new ClaimList();
		list.addClaim(DummyClaim());
		ApproverClaimActivity activity = startWithClaims(list);
		
		activity.ReturnClaim();
		
		ClaimantClaimsListActivity ClaimantActivity = new ClaimantClaimsListActivity();
		//loads the claimlist onCreate or onStart
		
		ClaimList claimlist = ClaimantActivity.LoadClaims();
		
		int statuscount;
		
		for(int i =0; i<claimlist.length; i++){
			
			if(claimlist.getClaimAtIndex(i).getStatus()=="returned"){
				statuscount++;
				
			}
			
		}
		
		assertTrue("Claim was returned?", statuscount > 0);
		
	}
	
	
	//US08.08.01
	//US08.08.02
	/*
	*Tests if the Appover's name
	*is attached to 
	* an approved claim
	*/
	public void testApproverNameAttached(){
		
		Claimlist list = new ClaimList();
		Claim claim = new DummyClaim("claimant","Jonh")
		list.addClaim(claim);
		
		
		ApproverClaimActivity activity = startWithClaims(list);
		
		User checkuser = New User("appover","John");
		
		//claim cannot be approved when claimant is the appover 
		assertFalse(activity.setApproverName(checkuser));//In this the name of the approver should be set only if not same name
		User checkuser = New User("appover","Kim");
		assertTrue(activity.setApproverName(checkuser));//In this the name of the approver should be set only if not same name
		
		ClaimantClaimsListActivity ClaimantActivity = new ClaimantClaimsListActivity();
		
		
		
		ClaimList claimlist = ClaimantActivity.LoadClaims();
		
		AssertEquals("Approver name comes through?","Timmy G", claimlist.getClaim(0).getApproverName());
		
		
	}
	
	
}
