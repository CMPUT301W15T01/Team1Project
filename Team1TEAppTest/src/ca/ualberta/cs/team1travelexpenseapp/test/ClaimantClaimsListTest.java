package ca.ualberta.cs.team1travelexpenseapp.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.User;
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
		User user=new User("claimant");
		
		//create some claims to populate and test our list
		Claim claim1 = new Claim("name1",new Date(2000,11,11), new Date(2015,12,12));
		Claim claim2 = new Claim("name2",new Date(1990,1,8), new Date(2000,12,12));
		Claim claim3 = new Claim("name3",new Date(1999,9,8), new Date(2012,12,12));
		Claim claim4 = new Claim("name4",new Date(2013,10,8), new Date(2012,12,12));
		Claim claim5 = new Claim("name5",new Date(2001,10,6), new Date(2012,12,12));
		
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
		int claimCount = ClaimsListController.getClaimCount();
		Date currDate = ClaimsListController.getClaim(0).getStartDate();
		Date prevDate;
		for(int i=1; i<claimCount; i++){
			prevDate = currDate;
			currDate=ClaimsListController.getClaim(i).getStartDate();
			assertTrue("Claims are not sorted by start date",currDate.after(prevDate));
		}
		//Note:testListClaim() checks that claims in ClaimsListController show up in same order 
		//as claims in claimsListView, so it suffices to check here that the claims in
		//are sorted in the ClaimsListController
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
		
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claimListView.getCount()==2);
		String viewText1=claimListView.getItemAtPosition(0).toString();
		String viewText2=claimListView.getItemAtPosition(1).toString();
		
		//if our filter works the only two items in the listview should be 
		//claim2 and claim4, still in sorted oder so the following should hold
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claim2Info==viewText1);
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claim4Info==viewText2);	
		
		selections={0,1};//should choose both tags from tag selection spinner
		tagSelector.setSelection(selections);
		
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claimListView.getCount()==2);
		
		//we want claims to show up if they have AT LEAST on of the selected filter tags
		//so despite the addition of the extra tag the sam two claims should be displayed
		//claim2 and claim4, still in sorted oder so the following should hold
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claim2Info==viewText1);
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claim4Info==viewText2);
		
		selections={1};//should choose exactly one tag from tag selection spinner
		tagSelector.setSelection(selections);
		
		//now there should be no claims in the list since none have the single selected tag
		assertTrue("Incorrect items displayed by tag filter,claim2Info",claimListView.getCount()==0);
	}
	
	
	//US 09.01.01
	public void testReadOnlineInfo(){
		ClaimList claimlist = new ClaimsListController.getClaims();
	
		ClaimList claimListFromOnline;
		
		
		assertTrue(isNetworkAvailable());
		
		
		ClaimListController.SaveToOnline();
		claimListFromOnline = ClaimsListController.LoadFromOnline();
		
		
		assertEquals("was ClaimList succefully written to db?",claimlist.toString(), claimListFromOnline.toString());
		//checks if it was written successfully
		//toString because it is very unlikely we need to overridde equals() for ClaimList
	}
	
	
	//http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectionManager
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectionManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	/* US07.05.01
	* As a claimant, I want to see the name of the approver and any comment(s) 
	* from the approver on a returned or approved claim.
	*/
	public void testApproverNameComments(){
		
		//get user status logged on 
		User claimant = new User("claimant");
		claimant.logOn();
		assertTrue(claimant.onlineCheck());
		
		
		claimListView = (ListView) (activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList));
		
		ClaimsListController.clearClaims();
		Calendar CalDate = Calendar.getInstance();
		CalDate.set(2014,10,10,0,0,0);
		Date startDate = CalDate.getTime();
		CalDate.set(2015,12,11,0,0,0);
		Date endDate = CalDate.getTime();
		
		Claim testclaim = new Claim("test 1", startDate, endDate);
		testclaim.setApproved();
		
		//check approved/returned claim has been downloaded from server
		ClaimsListController.loadClaim(testclaim);
		ClaimsListController.downloadClaims();
		assertTrue(ClaimsListController.checkdownloadstatus());
		assertTrue(ClaimsListController.has(testclaim));
		
		Approver testa = new Approver("John");
		testclaim.addApprover(testa);
		testclaim.addComment("nice!!!");
		//test user 
		User testuser = new User("Claimant");
		String approverU = "Claimant";
		assertEquals("Is not a claimant", approverU.equals(testuser.type()) );
		
		//test get claim
		Claim claimantClaim = User.getClaim(0);
		assertEquals("Not approver", "John", claimantClaim.getApprover());
		assertEquals("Not comment", "nice!!!", claimantClaim.getComments());
		ArrayList<Approver> alist = {testa};
		//test User sees complete list of approvers for a given returned/approved claim claim
		assertEquals(alist == claimantClaim.getApproversList());
	}
	
	//US03.01.01:As a claimant, I want to give an expense claim one or more alphanumeric 
	//tags, so that claims can be organized by me into groups.
	public void testAddTags(){
		final View item=claimListView.getAdapter().getView(0, null, null);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// click button, should produce dialog to choose edit or delete claim
				item.performLongClick();
				AlertDialog dialog=activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimDialog);
				
				//this should take us to the edit claims menu for the chosen claim
				Button editButton=(Button)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.editClaimButton);
				editButton.preformClick();
			}
		});
		
		activity=getActivity();
		
		//select the first item in the tagSelector as the sole tag for the claim
		CheckboxSpinner tagSelector=(CheckboxSpinner)activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.tagSelector);
		tagSelector.setSelection({0});
		
		Button saveButton=(Button)findViewById(ca.ualberta.cs.team1travelexpenseapp.test);
		activity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				//click the save button
				saveButton.preformClick();
			}
		});
		
		ArrayList<Tag> tags=ClaimsListController.getClaim(0).getTags();
		
		assertTrue("Tag was not set correctly",tags.size()==1);
		assertTrue("Tag was not set correctly",tags.get(0)==TagListManager.getTag(0));
	}
	
	
	//US04.08.01
	public void testNav() {
		
		ClaimsListController.clearClaims();
		Calendar CalDate = Calendar.getInstance();
		CalDate.set(2014,10,10,0,0,0);
		Date startDate = CalDate.getTime();
		CalDate.set(2015,12,11,0,0,0);
		Date endDate = CalDate.getTime();
		Claim testclaim = new Claim("test 1", startDate, endDate);
		ClaimsListController.addClaim(testclaim);
		
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
