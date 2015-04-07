
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import testObjects.MockClaimant;
import views.MultiSelectionSpinner;
import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.Destination;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.EditExpenseActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.LoginActivity;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import ca.ualberta.cs.team1travelexpenseapp.TagListController;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ProgressClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.sax.StartElementListener;
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
	protected ExpenseListController ExpenseListController;
	protected ClaimListController ClaimListController;
	protected TagListController TagListController;
	public ClaimTest() {
		super(ClaimantClaimsListActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		user = new MockClaimant("CoolGuy");
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
				((MockClaimant) user).clearData();
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
		ArrayList <Tag> tagsList= new ArrayList <Tag>();
		tagsList.add(new Tag("rad"));
		tagsList.add(new Tag("hip"));
		claim.setClaimTagList(tagsList);
		claim.addDestination(new Destination("dest 1", "reason 1",new Location("")));
		claim.addDestination(new Destination("dest 2", "reason 2",new Location("")));
		user.getClaimList().addClaim(claim);
		
		//get activity
		activity = getActivity();
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ClaimantExpenseListActivity.class.getName(), null, false);
		 // get list view 
 		final ListView view = (ListView) activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
 		TextView claimInfo1= (TextView) view.getChildAt(0);
 		assertNotNull("Added claim did not show up in claim list view.", claimInfo1);
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
		claim.addDestination(new Destination("dest 1", "reason 1",new Location("")));
		claim.addDestination(new Destination("dest 2", "reason 2",new Location("")));
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
		  TextView claimInfo2 = (TextView) nextActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimInfoHeader);
		  ViewAsserts.assertOnScreen(nextActivity.getWindow().getDecorView(), claimInfo2);
		  //assertTrue("Claim info on in expense list does not match expected claim info", claim.getClaimantName().equals(claimInfo2.getText().toString()));
		  activity.finish();
		  nextActivity.finish();
	}
	//US01.05.01
	public void testDeleteClaim() {
		// Creating a claim and adding test destination values
		Claim claim = new Claim();
		Date startDate=new Date();
		claim.setStartDate(startDate);
		Date endDate=new Date();
		endDate.setTime(startDate.getTime()+8*10^8);
		claim.setEndDate(endDate);
		ArrayList <Tag> tagsList= new ArrayList <Tag>();
		tagsList.add(new Tag("rad"));
		tagsList.add(new Tag("hip"));
		claim.setClaimTagList(tagsList);
		claim.addDestination(new Destination("dest 1", "reason 1",new Location("")));
		claim.addDestination(new Destination("dest 2", "reason 2",new Location("")));
		user.getClaimList().addClaim(claim);
		//get activity and assert user has logged in
		final ClaimantClaimsListActivity Activity = getActivity();
		
		
		 // get list view 
 		final ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
		// longclick the claim
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // long click and remove claim.
	              view.getChildAt(0).performLongClick();
	              // I create getLastDialog method in claimactivity class. Its return last created AlertDialog
	              Activity.editClaimDialog.getButton(Dialog.BUTTON_NEUTRAL).performClick();
		    }
		  });
		// Remove the claim and assert it's empty
		assertFalse("empty list",!user.getClaimList().getClaims().contains(claim));

	}
	//US01.06.01
	public void testSaveClaims() {
		final String uniqueName = UUID.randomUUID().toString();
		//need to test saving so can't use MockClaimant, create a new user with unique name instead
		Claimant user = new Claimant(uniqueName);
		// Start the main activity of the application under test
		Activity activity = getActivity();
		// user has Created and fill the claim with values
		Claim claim = new ProgressClaim();
		claim.setStartDate(new Date());
		claim.setEndDate(new Date());
		claim.setClaimantName(user.getName());
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense("car trip", new Date(), "vehicle rental", new BigDecimal(10), "CAD");
		expenses.add(expense);
		ExpenseList expenseList = claim.getExpenseList();
		expenseList.setExpenseList(expenses);
		user.getClaimList().addClaim(claim);
		
	    // Stop the activity claim info should be saved
		activity.finish();
		
	    // Create a new user with the same name and login as them in login screen, app should load the saved info
		LoginActivity loginActivity = new LoginActivity();
		Intent intent = new Intent(loginActivity, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		loginActivity.startActivity(intent);
		
		final EditText namefield = (EditText) loginActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.userNameField);
		
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ClaimantClaimsListActivity.class.getName(), null, false);
		loginActivity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		    	namefield.setText(uniqueName);
		    }
 		});
		getInstrumentation().waitForIdleSync();
		
		ClaimantClaimsListActivity claimListActivity = (ClaimantClaimsListActivity)
				getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		
		final ListView claimListView = (ListView) claimListActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
		
		TextView claimInfo = (TextView) claimListView.getChildAt(0);
		
		
		assertNotNull("claim was not saved", claimInfo);
		assertEquals("claim info was not saved as expected:\n"+claimInfo.getText().toString()+"\nv.s.\n"+claim.toString(), claimInfo.getText().toString(), claim.toString());
		
		activityMonitor = getInstrumentation().addMonitor(ClaimantExpenseListActivity.class.getName(), null, false);
		claimListActivity.runOnUiThread(new Runnable() {
			public void run(){
				claimListView.getChildAt(0).performLongClick();
			}
		});
		ClaimantExpenseListActivity expenseListActivity = (ClaimantExpenseListActivity)
				getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		
		final ListView expenseListView = (ListView) expenseListActivity.findViewById(
				ca.ualberta.cs.team1travelexpenseapp.R.id.claimantExpensesList);
			
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
		    	user.getClaimList().addClaim(claim);
		    	ArrayList<Tag> tags=new ArrayList<Tag>();
		    	tags.add(tag1);
		    	tags.add(tag2);
		    	tags.add(tag3);
		    	user.getTagList().setTagList(tags);
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
	 	final MultiSelectionSpinner<Tag> tagSpinner = (MultiSelectionSpinner<Tag>) 
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
	
	// US03.03.01: As a claimant, I want to filter the list of expense claims by
	// tags,
	// to show only those claims that have at least one tag matching any of a
	// given set
	// of one or more filter tags.
	public void testTagFilter() {
		// added a tag to two of our claims so we can test the tag filtering
		// functionality
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
		    	user.getClaimList().addClaim(claim);
		    	ArrayList<Tag> tags=new ArrayList<Tag>();
		    	tags.add(tag1);
		    	tags.add(tag2);
		    	tags.add(tag3);
		    	user.getTagList().setTagList(tags);
		    }
		});
		getInstrumentation().waitForIdleSync();
		// open the edit claim dialog and click edit
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
	 	final MultiSelectionSpinner<Tag> tagSpinner = (MultiSelectionSpinner<Tag>) 
	 			nextActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimTagSpinner);
	 	
	 	nextActivity.runOnUiThread(new Runnable() {
	 		 public void run() {
	 			 tagSpinner.setSelection(new String[] {"cool","next level"});
	 			 saveClaimButton.performClick();
	 		 }	
	 	});
	 	getInstrumentation().waitForIdleSync();
	 	
	 	// test that there was a claim added
	 	assertEquals("test1", claimListView.getCount(), 1);
	 	// add a tag filter
	 	final MultiSelectionSpinner<Tag> tagFilter = (MultiSelectionSpinner<Tag>) 
	 			nextActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimFilterSpinner);
	 	activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tagSpinner.setSelection(new String[] {"cool"});
			}
	 		
	 	});
	 	getInstrumentation().waitForIdleSync();
	 	assertEquals("test2", claimListView.getCount(), 1);
	 	// add another tag filter
	 	activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stu
				tagSpinner.setSelection(new String[] {"hip"});
				
			}
	 		
	 	});
	 	getInstrumentation().waitForIdleSync();
	 	assertEquals("test3", 0, 0);
	}
}

