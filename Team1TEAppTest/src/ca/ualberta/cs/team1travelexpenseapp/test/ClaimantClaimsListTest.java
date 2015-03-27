package ca.ualberta.cs.team1travelexpenseapp.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.TagListController;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.TestCase;
import android.nfc.Tag;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.text.format.DateFormat;

public class ClaimantClaimsListTest extends ActivityInstrumentationTestCase2<ClaimantClaimsListActivity> {
	Activity activity;
	ListView claimListView;
	
	public ClaimantClaimsListTest() {
		super(ClaimantClaimsListActivity.class);
	}
	
	
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		setActivityIntent(intent);
		activity = getActivity();
		claimListView = (ListView) (activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList));
		
		//set user as claimant
		User user=new User("user","jeff");
		
		//create some claims to populate and test our list
		Claim claim1 = new Claim("name1",new Date(2000,11,11), new Date(2015,12,12));
		Claim claim2 = new Claim("name2",new Date(1990,1,8), new Date(2000,12,12));
		Claim claim3 = new Claim("name3",new Date(1999,9,8), new Date(2012,12,12));
		Claim claim4 = new Claim("name4",new Date(2013,10,8), new Date(2012,12,12));
		Claim claim5 = new Claim("name5",new Date(2001,10,6), new Date(2012,12,12));
		
	}

	
	//US02.01.01: As a claimant, I want to list all my expense claims, showing for each claim:
	//the starting date of travel, the destination(s) of travel, the claim status, tags, and 
	//total currency amounts.
	public void testListClaims(){
		int claimCount = claimListView.getCount();
		for(int i=0; i < claimCount; i++){
			//get text info from a claim at position of i of claimListView 
			TextView claimInfo = (TextView) claimListView.getItemAtPosition(i);
			
			//toString() method should be checked manually to verify it contains the correct info
			String viewtext = claimInfo.getText().toString();

			//get claim at position i of Claim list 
			Claim claim = ClaimListController.getCurrentClaim();
			
			String expectedText =claim.toString();
			assertEquals("Claim summary at list item "+i+" does not match expected value",expectedText, viewtext);	
		}
		//make sure the user can see this list
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),claimListView);
		
		
	}
	
	//US02.02.01: As a claimant, I want the list of expense claims to be sorted by 
	//starting date of travel, in order from most recent to oldest, so that ongoing 
	//or recent travel expenses are quickly accessible.
	public void testSorted(){
		int claimCount = ClaimListController.getClaimCount();
		Date currDate = ClaimListController.getCurrentClaim().getStartDate();
		Date prevDate;
		for(int i=1; i<claimCount; i++){
			prevDate = currDate;
			currDate=ClaimListController.getCurrentClaim().getStartDate();
			assertTrue("Claims are not sorted by start date",currDate.after(prevDate));
		}
		//Note:testListClaim() checks that claims in ClaimsListController show up in same order 
		//as claims in claimsListView, so it suffices to check here that the claims in
		//are sorted in the ClaimsListController
	}
	
	//US03.03.01: As a claimant, I want to filter the list of expense claims by tags, 
	//to show only those claims that have at least one tag matching any of a given set 
	//of one or more filter tags.
	public void testTagFilter(){		
		//add a tag to two of our claims so we can test the tag filtering functionality
		Claim claim = new Claim();
		ClaimListController.setCurrentClaim(claim);
		
		
		
		String claim2Info=claim.toString();
		
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claimListView.getCount()==2);
		String viewText1=claimListView.getItemAtPosition(0).toString();
		String viewText2=claimListView.getItemAtPosition(1).toString();
		
		//if our filter works the only two items in the listview should be 
		//claim2 and claim4, still in sorted oder so the following should hold
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claim2Info==viewText1);
		
		
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claimListView.getCount()==2);
		
		//we want claims to show up if they have AT LEAST on of the selected filter tags
		//so despite the addition of the extra tag the sam two claims should be displayed
		//claim2 and claim4, still in sorted oder so the following should hold
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claim2Info==viewText1);
		
		
		
		//now there should be no claims in the list since none have the single selected tag
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claimListView.getCount()==0);
	}
	
	
	//US 09.01.01
	public void testReadOnlineInfo(){
		ClaimList list = ClaimListController.getClaimList();
	
		ClaimList claimListFromOnline;
		
		
		assertTrue(isNetworkAvailable());
		
		
		ClaimListController.SaveToOnline();
		claimListFromOnline = ClaimListController.LoadFromOnline();
		
		
		assertEquals("was ClaimList succefully written to db?",list.toString(), claimListFromOnline.toString());
		//checks if it was written successfully
		//toString because it is very unlikely we need to overridde equals() for ClaimList
	}	
	
	private boolean isNetworkAvailable() {
		// TODO Auto-generated method stub
		return false;
	}


	//US04.08.01
	public void testNav() {
		
		ClaimListController.clearClaimList();
		Calendar CalDate = Calendar.getInstance();
		CalDate.set(2014,10,10,0,0,0);
		Date startDate = CalDate.getTime();
		CalDate.set(2015,12,11,0,0,0);
		Date endDate = CalDate.getTime();
		Claim testclaim = new Claim("test 1", startDate, endDate);
		ClaimListController.addClaim(testclaim);
		
		//calls on listeners 
		int counter = 0;
		//go to next view 
		activity.findViewById(R.id.claimsList).performLongClick();
		counter += 1;
		//add expense click button 
		activity.findViewById(R.id.addExpenseButton).performClick();
		counter += 1;
		//saves the expense 
		activity.findViewById(R.id.saveExpenseButton).performClick();
		counter += 1;
		assertTrue(counter <= 4);
		
	}
	
	
	
}
