
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;

import android.R;
import android.nfc.Tag;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import junit.framework.TestCase;

public class ClaimTest extends ActivityInstrumentationTestCase2<ClaimActivity> {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	//US01.01.01
	public void testAddClaim() {
		//get activity and assert user has logged in
		ClaimActivity Activity = getActivity();
		AssertTrue("not logged in",User.loggedin());
		//get the button and press it
		 final Button button = (Button) Activity.findViewById(ca.ualberta.cs.R.id.addclaim);
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open the add claim activity.
		      button.performClick();
		    }
		  });
		
		EditText name = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.name);
		EditText start = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.start);
		EditText end = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.end);
		
		name.setText("name");
		start.setText("2012");
		end.setText("2013");
		
		 final Button saveButton = (Button) Activity.findViewById(ca.ualberta.cs.R.id.saveclaim);
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and save and finish the activity.
		      saveButton.performClick();
		    }
		  });
		// get the listview and assert that the user can see it on the screen
		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		ViewAsserts.assertOnScreen(Activity.getWindow().getDecorView(), view);
		//Assert values match after retreiving the claim
		ClaimListController list = new ClaimListController();
		Claim claim = list.get(0);
		assertEquals("name?",claim.getName(),"name");
		assertEquals("start date?",new Date(2012),claim.getStartDate());
		assertEquals("end date?",new Date(2013),claim.getEndDate());
		
		
		// model creating a claim and adding test values
		claim = new Claim();
		claim.setName("name");
		claim.setStartDate(new Date(2000,11,11));
		claim.setEndDate(new Date(2015,12,12));
		final String expected = "name";
		final String actual = claim.getName();
		// Asserting that the values match
		assertEquals("name?",expected,actual);
		assertEquals("start date?",new Date(2000,11,11),claim.getStartDate());
		assertEquals("end date?",new Date(2015,12,12),claim.getEndDate());
	}

	

	//US01.02.01
	public void testEnterDestination() {
		// Creating a claim and adding test destination values
		Claim claim = new Claim();
		claim.addDestination("dest 1");
		claim.addDestination("dest 2");	
		ClaimListController list = new ClaimListController();
		list.add(claim);
		//get activity and assert user has logged in
		ClaimActivity Activity = getActivity();
		AssertTrue("not logged in",User.loggedin());
		 // get list view 
 		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		// longclick the claim
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open the add claim activity.
	              view.getAdapter().getView(0, null, null).performLongClick();
	              // I create getLastDialog method in claimactivity class. Its return last created AlertDialog
		    AlertDialog dialog = Activity.getLastDialog(); 
        		 performClick(dialog.getButton(DialogInterface.EDIT_BUTTON));
		    }
		  });
				
		EditText name = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.name);
		EditText start = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.start);
		EditText end = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.end);
		EditText dest = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.destination);

				
		name.setText("test name");
		start.setText("2012-11-11");
		end.setText("2013-11-11");
		dest.setText("orlando");
		//get button and save the edits
		final Button saveButton = (Button) Activity.findViewById(ca.ualberta.cs.R.id.saveclaim);
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and save and finish the activity.
		      saveButton.performClick();
		    }
		  });
		// get the listview and assert that the user can see it on the screen
		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		ViewAsserts.assertOnScreen(Activity.getWindow().getDecorView(), view);
		//Assert values match after retreiving the claim
		ClaimListController list = new ClaimListController();
		Claim claim = list.get(0);
		assertEquals("name?",claim.getName(),"test name");
		assertEquals("start date?",new Date(2012,11,11),claim.getStartDate());
		assertEquals("end date?",new Date(2013,11,11),claim.getEndDate());
	
		// Assert values match
		assertEquals("Destination","dest 1",claim.getDestination(0));
		// Edit first value and assert they match
		claim.editDestination(0,"dest 3");
		assertEquals("Destination","dest 3",claim.getDestination(0));
		assertTrue("contains claim", claim.contains("dest 3"));
		assertEquals("length of destination doesn't match",3,claim.destinationCount());
		
	}
	
	//US01.03.01
	public void testViewClaim() {
		// Creating a claim and adding test destination values
		Claim claim = new Claim();
		claim.addDestination("dest 1");
		claim.addDestination("dest 2");	
		ClaimListController list = new ClaimListController();
		list.add(claim);
		//get activity and assert user has logged in
		ClaimActivity Activity = getActivity();
		AssertTrue("not logged in",User.loggedin());
		 // get list view 
 		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		// longclick the claim
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open the add claim activity.
	              view.getAdapter().getView(0, null, null).performLongClick();
	              // I create getLastDialog method in claimactivity class. Its return last created AlertDialog
		    AlertDialog dialog = Activity.getLastDialog(); 
        		 performClick(dialog.getButton(DialogInterface.DETAILS));
		    }
		  });
		  
		// assert that it's on the screen
		TextView view = (TextView) activity.findViewByID(R.id.claimtext);
		assertNotNull("activity",activity);
		assertNotNull("textview",view);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), view);
		//user clicks back
		activity.finish();
		

	}
	
	//US01.04.01
	public void testEditClaim() {
		// Creating a claim and adding test destination values
		Claim claim = new Claim();
		claim.addDestination("dest 1");
		claim.addDestination("dest 2");	
		ClaimListController list = new ClaimListController();
		list.add(claim);
		//get activity and assert user has logged in
		ClaimActivity Activity = getActivity();
		User.login("bob");
		AssertTrue("not logged in",User.loggedin());
		
		 // get list view 
 		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		// longclick the claim
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open the add claim activity.
	              view.getAdapter().getView(0, null, null).performLongClick();
	              // I create getLastDialog method in claimactivity class. Its return last created AlertDialog
		    AlertDialog dialog = Activity.getLastDialog(); 
        		 performClick(dialog.getButton(DialogInterface.EDIT_BUTTON));
		    }
		  });
				
		EditText name = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.name);
		EditText start = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.start);
		EditText end = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.end);
		EditText dest = (EditText) Activity.findViewById(ca.ualberta.cs.R.id.destination);

		// Status is in progress by default		
		name.setText("test name");
		start.setText("2012-11-11");
		end.setText("2013-11-11");
		dest.setText("orlando");
		//get button and save the edits
		final Button saveButton = (Button) Activity.findViewById(ca.ualberta.cs.R.id.saveclaim);
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and save and finish the activity.
		      saveButton.performClick();
		    }
		  });
		// get the listview and assert that the user can see it on the screen
		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		ViewAsserts.assertOnScreen(Activity.getWindow().getDecorView(), view);
		//Assert values match after retreiving the claim
		ClaimListController list = new ClaimListController();
		Claim result = list.get(0);
		
		
		// Create a claim with same test values
		Claim claim = new Claim();
		claim.setName("test name");
		claim.setStartDate(new Date(2012,11,11));
		claim.setEndDate(new Date(2013,11,11));
		claim.setDest("orlando");
		final String expected = "test name";
		final String actual = result.getName();
		// Assert the new values match
		assertEquals("name does not match",expected,actual);
		assertEquals("start date does not match",new Date(2012,11,11),result.getStartDate());
		assertEquals("end date does not match",new Date(2013,11,11),result.getEndDate());
		// Attempt to edit non editable claim
		claim.setStatus("submitted");
		claim.setName("NURBS");
		// Assert that the edit failed
		AssertEquals("edit made to non editable claim",claim.getName(),expected);
	}
	//US01.05.01
	public void testDeleteClaim() {
		// Creating a claim and adding test destination values
		Claim claim = new Claim();
		claim.addDestination("dest 1");
		claim.addDestination("dest 2");	
		ClaimListController list = new ClaimListController();
		list.add(claim);
		// Add the claim and assert it's not empty
		assertTrue("list is empty",list.length()==1);
		//get activity and assert user has logged in
		ClaimActivity Activity = getActivity();
		User.login("bob");
		AssertTrue("not logged in",User.loggedin());
		
		 // get list view 
 		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		// longclick the claim
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // long click and remove claim.
	              view.getAdapter().getView(0, null, null).performLongClick();
	              // I create getLastDialog method in claimactivity class. Its return last created AlertDialog
		    AlertDialog dialog = Activity.getLastDialog(); 
        		 performClick(dialog.getButton(DialogInterface.DELETE_BUTTON));
		    }
		  });
		// Create a claim and add it to the controller
		ClaimsListController list = new ClaimsListController();
		// Remove the claim and assert it's empty
		assertTrue("empty list",list.length()==0);

	}
	//US01.06.01
	public void testSaveClaims() {
		// Start the main activity of the application under test
		ClaimActivity activity = getActivity();
		// user has Created and fill the claim with values
		ClaimsListController list = new ClaimsListController();
		Claim claim = new Claim();
		final String expected = "name";
		claim.setName(expected);
		claim.setStartDate(new Date(2000,11,11));
		claim.setEndDate(new Date(2015,12,12));
		list.add(claim);
	    // Stop the activity - The onDestroy() method should save the state of the claim
		activity.finish();
	    // Re-start the Activity - the onResume() method should restore the state of the Spinner
		ClaimActivity activity = getActivity();
		// Get current claim from the controller
		claim = list.get(0);
		final String actual = claim.getName();
	    // Assert that the current claim is the same as the starting claim
		assertEquals("name?",expected,actual);
		assertEquals("start date?",new Date(2000,11,11),claim.getStartDate());
		assertEquals("end date?",new Date(2015,12,12),claim.getEndDate());
		
	}
	
	//US03.01.01:As a claimant, I want to give an expense claim one or more alphanumeric 
	//tags, so that claims can be organized by me into groups.
	public void addTags(){
		//User has logged on
		User.login("bob");
		AssertTrue("not logged on",User.loggedin());
		Tag tag1=new Tag("buisness");
		Tag tag2=new Tag("pleasure");
		//create a claim and a tag
		Claim claim= new Claim();
		claim.addTag(tag1);
		ArrayList<Tag> tags=claim.getTags();
		ArrayList<String> tagStrings=new ArrayList<String>();
		assertEquals("Tags were not added properly, length of list incorrect",tags.size(),2);
		for(int i=0; i<tags.size(); i++){
			tagStrings.add(tags.get(i).toString());
		}
		assertTrue("Tags were not added properly, strings do not match those added",tags.contains("buisness") && tags.contains("pleasure"));
		 // get list view 
 		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
  		Button saveClaim = (Button) Activity.findViewById(ca.ualberta.cs.R.id.saveclaimbutton);
 		Button tagbutton = (Button) Activity.findviewById(ca.ualberta.cs.R.id.tagaddbutton);
   		ListView tagview = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.taglistview);
		// longclick the claim
		  Activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // long click and remove claim.
	              view.getAdapter().getView(0, null, null).performLongClick();
	              // I create getLastDialog method in claimactivity class. Its return last created AlertDialog
		    AlertDialog dialog = Activity.getLastDialog(); 
        		 performClick(dialog.getButton(DialogInterface.EDIT_BUTTON));
        		 tagbutton.performClick(); //user selects add new tag
        		 tagview.setcheckbox(0); //selects desired tag
        		 saveClaim.performClick(); //saves claim 
        		 
		    }
		  });
		  //get the claim and check if the claim contains the tag
  		ClaimsListController list = new ClaimsListController();
		AssertTrue(list.get(0).tagcontains("buisness"));
		
		// get the listview and assert that the user can see it on the screen
		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		ViewAsserts.assertOnScreen(Activity.getWindow().getDecorView(), view);
		 
	}
	
	

	
}

