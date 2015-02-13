package ca.ualberta.cs.team1travelexpenseapp.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import ca.ualberta.cs.team1travelexpenseapp.R;
import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.TestCase;
import android.test.ActivityInstrumentationTestCase2;
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
		
		//create some claims to populate and test our list
		Claim claim1 = new Claim("name",new Date(2000,11,11), new Date(2015,12,12));
		Claim claim2 = new Claim("name",new Date(1990,1,8), new Date(2000,12,12));
		Claim claim3 = new Claim("name",new Date(1999,9,8), new Date(2012,12,12));
		Claim claim4 = new Claim("name",new Date(2013,10,8), new Date(2012,12,12));
		Claim claim5 = new Claim("name",new Date(2001,10,6), new Date(2012,12,12));
		
		//create tags to populate the tags list in order to test the filtering
		TagsListController.addTag(new Tag("business"));
		TagsListController.addTag(new Tag("pleasure"));
		
		//add claims to ClaimsListController so we can test it
		ClaimsListController.addClaim(claim1);
		ClaimsListController.addClaim(claim2);
		ClaimsListController.addClaim(claim3);
		ClaimsListController.addClaim(claim4);
		ClaimsListController.addClaim(claim5);	
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
			Claim claim = ClaimsListController.getClaim(i);
			
			//this is what the text in the listview at position i should look like to match
			//the corresponding claim in the ClaimsListController
			
			String expectedText =claim.toString();
			assertEquals("Claim summary at list item"+i+"does not match expected value",expectedText, viewtext);	
		}
		
		
	}
	
	//US02.02.01: As a claimant, I want the list of expense claims to be sorted by 
	//starting date of travel, in order from most recent to oldest, so that ongoing 
	//or recent travel expenses are quickly accessible.
	public void testSorted(){
		int claimCount = ClaimsListController.getClaimCount();
		Date currDate = ClaimsListController.getClaim(0).getStartDate();
		Date prevDate;
		for(int i=1; i<claimCount; i++){
			prevDate = currDate;
			currDate=ClaimsListController.getClaim(i).getStartDate();
			assertTrue("Claims are not sorted by start date",currDate.after(prevDate));
			
		}
	}
	
	//US03.03.01: As a claimant, I want to filter the list of expense claims by tags, 
	//to show only those claims that have at least one tag matching any of a given set 
	//of one or more filter tags.
	public void testTagFilter{
		//We will implement this in order to select multiple tags at once from a spinner
		CheckboxSpinner tagSelector= (CheckBoxSpinner)findViewById(
				ca.ualberta.cs.team1travelexpenseapp.R.id.tagSelector));
		
		ArrayList<Tag> tags=TagsListController.getTags();
		
		//add a tag to two of our claims so we can test the tag filtering functionality
		Claim claim2=ClaimsListController.getClaim(2);
		Claim claim4=ClaimsListController.getClaim(4);
		claim2.addTag(tags.get(0));
		claim4.addTag(tags.get(0));
		
		int[] selections={0};//should choose exactly 1 tag (tag[0]) from tag selection spinner
		tagSelector.setSelection(selections);//selections on CheckboxSpinner can be set with an int array
		
		String claim2Info=claim2.toString();
		String claim4Info=claim4.toString();
		
		String viewText1=claimListView.getItemAtPosition(0).toString();
		String viewText2=claimListView.getItemAtPosition(1).toString();
		
		//if our filter works the only two items in the listview should be 
		//claim2 and claim4, still in sorted oder so the following should hold
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claim2Info==viewText1);
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claim4Info==viewText2);	
	}
	

}
