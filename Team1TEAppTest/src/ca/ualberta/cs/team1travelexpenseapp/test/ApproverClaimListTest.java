
package ca.ualberta.cs.team1travelexpenseapp.test;

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
	
	//US08.06.01
	public void testCommentAddable(){
			String testComment = "Test comment.";
			CommentFragment commentBox = new CommentFragment();
			
			commentBox.setArguments(DummyClaim().addExpense(DummyExpense()));
			commentBox.addComment(testComment);
			Claim claim = commentBox.getArguments();
			
			AssertEquals("Comment Written successfully?", testComment, claim.getComment());
		
	}
	
	
	//US08.07.01
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
	public void testApproverNameAttached(){
		
		Claimlist list = new ClaimList();
		list.addClaim(DummyClaim());
		ApproverClaimActivity activity = startWithClaims(list);
		
		activity.setApproverName("Timmy G");
		activity.approveClaim();//In this the name of the approver should be set
		
		ClaimantClaimsListActivity ClaimantActivity = new ClaimantClaimsListActivity();
		
		ClaimList claimlist = ClaimantActivity.LoadClaims();
		
		AssertEquals("Approver name comes through?","Timmy G", claimlist.getClaim(0).getApproverName());
		
		
	}
	
	
	//US08.01.01
	public void testgetSubmittedClaims() {
		// Populate the claim controller
		ClaimListController list = new ClaimListController();
		// Populate claim values
		Claim claim = new Claim();
		claim.setStartDate(new Date(100));
		claim.setEndDate(new Date(101));
		claim.setStatus("submitted");
		claim.setName("approver test");
		claim.addDestination("test dest");
		claim.setTotal(100,"EUR");
		// Add to the controller
		list.add(claim);
		// Retrieve the current claim that should match the starting claim
		Claim result = list.get(0);
		// Assert that the values match
		assertEquals("start date not equal",claim.getStartDate(),result.getStartDate());
		assertEquals("end date not equal",claim.getEndDate(),result.getEndDate());
		assertEquals("status not equal",claim.getStatus,result.getStatus());
		assertEquals("name not equal",claim.getName,result.getName);
		assertEquals("destination not equal",claim.getDestination,result.getDestination());
		assertEquals("total not equal",claim.getTotal(),result.getTotal());
		
	}
	
	//US08.02.01
	public void testSortClaimDates() {
		// Populate the claim controller
		Claim c1 = new Claim();
		Claim c2 = new Claim();
		Claim c3 = new Claim();
		Claim c4 = new Claim();
		// Set the dates
		c1.setStartDate(new Date(1));
		c2.setStartDate(new Date(2));
		c3.setStartDate(new Date(3));
		c4.setStartDate(new Date(4));
		// Create the controller
		ClaimListController list = new ClaimListController();
		// Add the claims to the controller
		list.add(c3);
		list.add(c1);
		list.add(c4);
		list.add(c2);
		// Check for equal length
		assertEquals("length not equal",list.length(),4);
		// Assert that the values are sorted
		for (int i = 1;i<list.length();i++) {
			assertTrue("not sorted",list.get(i).getStartDate()<=list.get(i+1).getStartDate());
		}
	}
	
	//US08.03.02
	public void testSubmittedExpenseClaimDetails() {
		ClaimActivity activity = getActivity();
		ListView view = (ListView) activity.findViewById(R.id.claimlistview);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),view);
    
	}
	
	//US08.04.02
	public void testListSubmittedClaimExpenses() {
	  Expense expense = new Expense("name",Date(1),"taxi","description",100,"EUR");
	  ClaimList list = new ClaimList();
	  Claim claim = new Claim(expense);
	  list.add(claim);
	  ClaimActivity activity = getActivity();
	  ListView view = (ListView) activity.findViewById(R.id.claimlistview);
	  ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),view);
	  assertTrue("expense is not in claim",claim.contains(expense));
  	
	}
	
	//US08.05.02
	public void testViewPhotographicReceipt() {
		ClaimActivity activity = getActivity();
		ImageButton view = (Button) activity.findViewById(R.id.photograph);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),view);
		assertNotNull("not empty",view.getDrawable());
		assertTrue(View.VISIBLE == view.getVisibility());
	}
	
	//US08.06.02
	public void testAddApproverClaimComment() {
		Claim claim = new Claim();
		claim.addComment("test comment");
		assertEquals("comment not equal","test comment",claim.getComment());
		
	}
	
	//US08.07.02
	public void testReturnUnapprovedClaim() {
		Claim claim = new Claim();
		claim.setStatus("returned");
		claim.addApprover("test approver");
		AssertTrue("approvers don't match",claim.getApprover(),"test approver");
		AssertTrue("claim is not returned",claim.getStatus(),"returned");
		
	}
	
	//US08.08.02
	public void testApproveClaim() {
		Claim claim = new Claim();
		claim.setStatus("approved");
		claim.addApprover("test approver");
		AssertTrue("approvers don't match",claim.getApprover(),"test approver");
		AssertTrue("claim is not returned",claim.getStatus(),"approved");
			
	}
	
	
}
