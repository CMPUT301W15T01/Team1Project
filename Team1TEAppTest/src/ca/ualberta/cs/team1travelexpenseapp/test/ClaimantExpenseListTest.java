package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import junit.framework.TestCase;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import ca.ualberta.cs.team1travelexpenseapp.R;


public class ClaimantExpenseListTest extends TestCase {
	Activity activity;
	ListView claimListView;
	
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		setActivityIntent(intent);
		activity = getActivity();
		claimListView = (ListView) (activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList));
		Claim claim1 = new Claim("name",new Date(2000,11,11), new Date(2015,12,12));
		Claim claim2 = new Claim("name",new Date(1990,1,8), new Date(2000,12,12));
		Claim claim3 = new Claim("name",new Date(1999,9,8), new Date(2012,12,12));
		Claim claim4 = new Claim("name",new Date(2013,10,8), new Date(2012,12,12));
		Claim claim5 = new Claim("name",new Date(2001,10,6), new Date(2012,12,12));
		ClaimsListController.addClaim(claim1);
		ClaimsListController.addClaim(claim2);
		ClaimsListController.addClaim(claim3);
		ClaimsListController.addClaim(claim4);
		ClaimsListController.addClaim(claim5);	
	}
	
	/*
	 *  US 7.01.01
	 *  As a claimant, I want to submit an expense claim for approval, denoting 
	 *  the claim status as submitted, with no further changes allowed by me to the 
	 *  claim information (except the tags).
	 */
	public void testSubmitExpense(){
		
	}
	
}
