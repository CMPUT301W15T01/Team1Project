package ca.ualberta.cs.team1travelexpenseapp.test;

import android.test.ActivityInstrumentationTestCase2;
import junit.framework.TestCase;

public class ApproverClaimListTest extends ActivityInstrumentationTestCase2<ClaimActivity> {

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
	
	//US08.01.01 
	public void testgetSubmittedClaims() {
		ClaimList list = new ClaimList();
		Claim claim = new Claim();
		claim.setStartDate(new Date(100));
		claim.setEndDate(new Date(101));
		claim.setStatus("submitted");
		claim.setName("approver test");
		claim.addDestination("test dest");
		claim.setTotal(100,"EUR");
		list.add(claim);
		Claim result = list.get(0);
		
		assertEquals("start date not equal",claim.getStartDate(),result.getStartDate());
		assertEquals("end date not equal",claim.getEndDate(),result.getEndDate());
		assertEquals("status not equal",claim.getStatus,result.getStatus());
		assertEquals("name not equal",claim.getName,result.getName);
		assertEquals("destination not equal",claim.getDestination,result.getDestination());
		assertEquals("total not equal",claim.getTotal(),result.getTotal());
		
	}
	
	//US08.02.01
	public void testSortClaimDates() {
		Claim c1 = new Claim();
		Claim c2 = new Claim();
		Claim c3 = new Claim();
		Claim c4 = new Claim();

		c1.setStartDate(new Date(1));
		c2.setStartDate(new Date(2));
		c3.setStartDate(new Date(3));
		c4.setStartDate(new Date(4));

		ClaimList list = new ClaimList();
		
		list.add(c3);
		list.add(c1);
		list.add(c4);
		list.add(c2);
		
		assertEquals("length not equal",list.length(),4);
		
		for (int i = 1;i<list.length();i++) {
			assertTrue("not sorted",list.get(i).getStartDate()<=list.get(i+1).getStartDate());
			
		}
	}
	
	//US08.03.01
	public void testSubmittedExpenseClaimDetails() {
		ClaimActivity activity = getActivity();
		ListView view = (ListView) activity.findViewById(R.id.claimlistview);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),view);
    
	}
	
	//US08.04.01
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
	
	//US08.05.01
	public void testViewPhotographicReceipt() {
		ClaimActivity activity = getActivity();
		ImageButton view = (Button) activity.findViewById(R.id.photograph);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),view);
		assertNotNull("not empty",view.getDrawable());
		assertTrue(View.VISIBLE == view.getVisibility());
	}
	
	//US08.06.01
	public void testAddApproverClaimComment() {
		Claim claim = new Claim();
		claim.addComment("test comment");
		assertEquals("comment not equal","test comment",claim.getComment());
		
	}
	
	//US08.07.01
	public void testReturnUnapprovedClaim() {
		Claim claim = new Claim();
		claim.setStatus("returned");
		claim.addApprover("test approver");
		AssertTrue("approvers don't match",claim.getApprover(),"test approver");
		AssertTrue("claim is not returned",claim.getStatus(),"returned");
		
	}
	
	//US08.08.01
	public void testApproveClaim() {
		Claim claim = new Claim();
		claim.setStatus("approved");
		claim.addApprover("test approver");
		AssertTrue("approvers don't match",claim.getApprover(),"test approver");
		AssertTrue("claim is not returned",claim.getStatus(),"approved");
			
	}
}
