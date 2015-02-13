package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.Calendar;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.ApproverExpenseListActivity;
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
	
	//US08.01.01
	/*As an approver, I want to view a list of all the expense claims that were submitted for approval, 
	* which have their claim status as submitted, showing for each claim: the claimant name, the starting date of travel,
	* the destination(s) of travel, the claim status, total currency amounts, and any approver name.
	*/
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
	//As an approver, I want the list of submitted expense claims to be sorted by starting date of travel,
	//in order from oldest to most recent, so that older claims are considered first.
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
			assertTrue("not sorted",list.get(i).getStartDate()>=list.get(i+1).getStartDate());
		}
	}
	
	//US08.03.01
	//As an approver, I want to view all the details of a submitted expense claim.
	//Displays details of submitted claims with their name, starting/ending date, destinations, status, amount and approver names
	public void testSubmittedClaimDetails(){
		//create a claim 
		claimListView = (ListView) (activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList));
		
		ClaimsListController.clearClaims();
		Calendar CalDate = Calendar.getInstance();
		CalDate.set(2014,10,10,0,0,0);
		Date startDate = CalDate.getTime();
		CalDate.set(2015,12,11,0,0,0);
		Date endDate = CalDate.getTime();
		
		Claim testclaim = new Claim("test 1", startDate, endDate);
		String d1 = "Destination 1";
		String d2 = "Destination 2";
		testclaim.addDestination(d1);
		testclaim.addDestination(d2);
		testclaim.setStatus("submitted");
		testclaim.addApprover("apperover 1");
		testclaim.addApprover("apperover 2");
		ClaimsListController.addClaim(testclaim);
		
		//get a claim info view, it will be text
		TextView claimInfo = (TextView) claimListView.getItemAtPosition(0);
		String viewtext = claimInfo.getText().toString();
		
		//check if all details viewed are same as claim's details 
		Claim claim = ClaimsListController.getClaim(0);
		assertTrue("View details not same as claim details", viewtext.equals(claim.toString()));
		
		
		
		
	}
	
<<<<<<< HEAD
	
	
	/* US05.01.01
	 * 
	 *  ExpenseInLists() checks if the expense has been added properly to a claim object
	 *  
	 *  
	 *  
	 *  */
	
	public Claim  CreateTestClaim();
	
	public void testExpenseInLists(){
		
		
		
		
	}
=======
>>>>>>> f927a50b14a9aa8f823521e0dd45924440ca8cc1

}
