
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.EditExpenseActivity;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.MultiSelectionSpinner;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.UserSingleton;
import android.app.Activity;
import android.app.AlertDialog;
import ca.ualberta.cs.team1travelexpenseapp.TagListController;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ClaimTest extends ActivityInstrumentationTestCase2<ClaimantClaimsListActivity> {

	protected Instrumentation instrumentation;
	protected ClaimantClaimsListActivity activity = null;
	protected Claimant user;

	public ClaimTest() {
		super(ClaimantClaimsListActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		Claimant user = new Claimant("CoolGuy");
		UserSingleton.getUserSingleton().setUser(user);
		instrumentation = getInstrumentation();
	}
	
	protected void tearDown() throws Exception{
		cleanUp();
		super.tearDown();
		
	}
	
	protected void cleanUp(){
		if(activity != null){
			activity.finish();
		}

		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run() {
				ClaimListController.clearClaimList();
			}
		});
		instrumentation.waitForIdleSync();

	}

	
	
	//US01.01.01 is in ClaimClaimsListActivityTest.java 

	

	//US01.02.01 is in ClaimClaimsListActivityTest.java
	
	
	//US01.03.01
	public void testViewClaim() {
		// Creating a claim with info filled in
		Claim claim = new Claim();
		Date startDate=new Date();
		claim.setStartDate(startDate);
		Date endDate=new Date();
		endDate.setTime(startDate.getTime()+8*10^8);
		claim.setEndDate(endDate);
		claim.setClaimantName("Jimmy");
		ArrayList <Tag> tagsList= new ArrayList <Tag>();
		tagsList.add(new Tag("rad"));
		tagsList.add(new Tag("hip"));
		claim.setClaimTagList(tagsList);
		claim.addDestination("dest 1", "reason 1");
		claim.addDestination("dest 2", "reason 2" );	
		ClaimListController.addClaim(claim);
		
		
		
		//get activity
		activity = getActivity();
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ClaimantExpenseListActivity.class.getName(), null, false);
		 // get list view 
 		final ListView view = (ListView) activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
 		TextView claimInfo1= (TextView) view.getChildAt(0);
 		assertTrue("Claim info in claim list does not match expected claim info", claim.toString().equals(claimInfo1.getText().toString()));
		// click the claim
		  activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open the add claim activity.
	              view.performItemClick(view.getChildAt(0), 0, view.getAdapter().getItemId(0));
		    }
		  });
		  
		  Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		  assertNotNull("Expense list for claim failed to open",nextActivity);
		  TextView claimInfo2 = (TextView) nextActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimInfoHeader);
		  ViewAsserts.assertOnScreen(nextActivity.getWindow().getDecorView(), claimInfo2);
		  assertTrue("Claim info on in expense list does not match expected claim info", claim.toString().equals(claimInfo2.getText().toString()));

	}
//	
	//US01.04.01
	public void testEditClaim() {
		// Creating a claim with info filled in
		Claim claim = new Claim();
		Date startDate=new Date();
		claim.setStartDate(startDate);
		Date endDate=new Date();
		endDate.setTime(startDate.getTime()+8*10^8);
		claim.setEndDate(endDate);
		claim.setClaimantName("Jimmy");
		ArrayList <Tag> tagsList= new ArrayList <Tag>();
		tagsList.add(new Tag("rad"));
		tagsList.add(new Tag("hip"));
		claim.setClaimTagList(tagsList);
		claim.addDestination("dest 1", "reason 1");
		claim.addDestination("dest 2", "reason 2" );	
		ClaimListController.addClaim(claim);
		
		
		
		//get activity
		final ClaimantClaimsListActivity activity = getActivity();
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(EditClaimActivity.class.getName(), null, false);
		 // get list view 
 		final ListView view = (ListView) activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
 		TextView claimInfo1= (TextView) view.getChildAt(0);
 		assertTrue("Claim info in claim list does not match expected claim info", claim.toString().equals(claimInfo1.getText().toString()));
		// click the claim
		  activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open the add claim activity.
			      // long click button and open the add claim activity.
	              view.getChildAt(0).performLongClick();
	              // I  getLastDialog  in claimactivity class. 
	              AlertDialog dialog = activity.editClaimDialog; 
        		 dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
		    }
		  });
		  
		  Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		  assertNotNull("claim edit list for claim failed to open",nextActivity);
		  TextView claimInfo2 = (TextView) nextActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimNameBody);
		  ViewAsserts.assertOnScreen(nextActivity.getWindow().getDecorView(), claimInfo2);
		  //assertTrue("Claim info on in expense list does not match expected claim info", claim.getClaimantName().equals(claimInfo2.getText().toString()));
		  activity.finish();
		  nextActivity.finish();
	}
//	//US01.05.01
//	public void testDeleteClaim() {
//		// Creating a claim and adding test destination values
//		Claim claim = new Claim();
//		claim.addDestination("dest 1");
//		claim.addDestination("dest 2");	
//		ClaimListController list = new ClaimListController();
//		list.add(claim);
//		// Add the claim and assert it's not empty
//		assertTrue("list is empty",list.length()==1);
//		//get activity and assert user has logged in
//		ClaimActivity Activity = getActivity();
//		User.login("bob");
//		assertTrue("not logged in",User.loggedin());
//		
//		 // get list view 
// 		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimlistview);
//		// longclick the claim
//		  Activity.runOnUiThread(new Runnable() {
//		    @Override
//		    public void run() {
//		      // long click and remove claim.
//	              view.getAdapter().getView(0, null, null).performLongClick();
//	              // I create getLastDialog method in claimactivity class. Its return last created AlertDialog
//		    AlertDialog dialog = Activity.getLastDialog(); 
//        		 performClick(dialog.getButton(DialogInterface.DELETE_BUTTON));
//		    }
//		  });
//		// Create a claim and add it to the controller
//		ClaimListController list = new ClaimListController();
//		// Remove the claim and assert it's empty
//		assertTrue("empty list",list.length()==0);
//
//	}
	//US01.06.01
	public void testSaveClaims() {
		// Start the main activity of the application under test
		Activity activity = getActivity();
		// user has Created and fill the claim with values
		ClaimListController list = new ClaimListController();
		Claim claim = new Claim();
		ClaimListController.setCurrentClaim(claim);
	    // Stop the activity - The onDestroy() method should save the state of the claim
		activity.finish();
	    // Re-start the Activity - the onResume() method should restore the state of the Spinner
		activity = getActivity();
		// Get current claim from the controller
		Claim claim2 = ClaimListController.getCurrentClaim();
		final String actual = claim.getClaimantName();
	    // Assert that the current claim is the same as the starting claim
		assertEquals("same?", claim, claim2);
		
	}
//	
//	//US03.01.01:As a claimant, I want to give an expense claim one or more alphanumeric 
//	//tags, so that claims can be organized by me into groups.
	public void testAddTags(){
		activity= getActivity();
		 // get list view 
 		final ListView claimListView = (ListView) activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
 		final Claim claim=new Claim();
 		final Tag tag1=new Tag("cool");
 		final Tag tag2=new Tag("hip");
 		final Tag tag3=new Tag("next level");
 		
 		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(EditClaimActivity.class.getName(), null, false);
 		
 		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		    	ClaimListController.addClaim(claim);
		    	TagListController.addTag(tag1);
		    	TagListController.addTag(tag2);
		    	TagListController.addTag(tag3);
		    }
 		});
 		getInstrumentation().waitForIdleSync();
 		
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // long click the claim
	             claimListView.getChildAt(0).performLongClick();
	             AlertDialog dialog = activity.editClaimDialog;
	             //click the edit claim button
	             Button editClaimButton=(Button)dialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE);
				 editClaimButton.performClick();
		    }
		 });
		  
		EditClaimActivity nextActivity = (EditClaimActivity)
				getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		
		assertNotNull("EditClaimAcivity did not open on click as expected", nextActivity);
		  
		  
		final Button saveClaimButton = (Button) nextActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.saveClaimButton);
	 	final MultiSelectionSpinner tagSpinner = (MultiSelectionSpinner) 
	 			nextActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimTagSpinner);
	 	
	 	nextActivity.runOnUiThread(new Runnable() {
	 		 public void run() {
	 			 tagSpinner.setSelection(new String[] {"cool","next level"});
	 			 saveClaimButton.performClick();
	 		 }	
	 	});
	 	getInstrumentation().waitForIdleSync();
	 	
	 	assertTrue("Tags were not correctly added to claim", claim.getClaimTagList().contains(tag1) && 
	 			claim.getClaimTagList().contains(tag1) && claim.getClaimTagList().size()==2);
	 	 
	}
//	
	

}

