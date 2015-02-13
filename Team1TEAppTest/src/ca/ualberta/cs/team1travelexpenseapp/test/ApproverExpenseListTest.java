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
	

}
