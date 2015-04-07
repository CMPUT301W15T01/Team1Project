
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ProgressClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.sax.StartElementListener;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		  nextActivity.finish();
		  activity.finish();

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
		ArrayList <Tag> tagsList= new ArrayList <Tag>();
		tagsList.add(new Tag("rad"));
		tagsList.add(new Tag("hip"));
		claim.setClaimTagList(tagsList);
		claim.addDestination(new Destination("dest 1", "reason 1",new Location("")));
		claim.addDestination(new Destination("dest 2", "reason 2",new Location("")));
		user.getClaimList().addClaim(claim);
		
		
		
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
		  TextView claimInfo2 = (TextView) nextActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimDestinationHeader);
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
		final Claimant user = new Claimant(uniqueName);
		UserSingleton.getUserSingleton().setUser(user);
		
		activity = getActivity();
		
		//initManager so we can save
		user.initManagers(activity.getApplicationContext());
		
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
		final ArrayList<Claim> claims = user.getClaimList().getClaims();
		claims.add(claim);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				user.getClaimList().setClaimList(claims);
				
			}
		});
		getInstrumentation().waitForIdleSync();
		
		
		
	    // Clear the logged in user, claim info should be saved
		UserSingleton.getUserSingleton().setUser(new Claimant(""));
		
	    //get a new login activity and log in with the same name to test if the info is saved
		Intent intent = new Intent(activity, LoginActivity.class);
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(LoginActivity.class.getName(), null, false);
		activity.startActivity(intent);
		LoginActivity loginActivity = (LoginActivity) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		
		final EditText namefield = (EditText) loginActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.userNameField);
		final Button userLogin = (Button) loginActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.userButton);
		
		activityMonitor = getInstrumentation().addMonitor(ClaimantClaimsListActivity.class.getName(), null, false);
		loginActivity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		    	namefield.setText(uniqueName);
		    	userLogin.performClick();
		    	
		    }
 		});
		getInstrumentation().waitForIdleSync();
		
		ClaimantClaimsListActivity claimListActivity = (ClaimantClaimsListActivity)
				getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);

		
		final ListView claimListView = (ListView) claimListActivity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
		
		TextView claimInfo = (TextView) claimListView.getChildAt(0);
		
		
		assertNotNull("claim was not saved", claimInfo);
		assertEquals("claim info was not saved as expected", claimInfo.getText().toString(), claim.toString());
		
		activityMonitor = getInstrumentation().addMonitor(ClaimantExpenseListActivity.class.getName(), null, false);
		claimListActivity.runOnUiThread(new Runnable() {
			public void run(){
				claimListView.performItemClick(claimListView, 0, 0);
			}
		});
		getInstrumentation().waitForIdleSync();
		
		ClaimantExpenseListActivity expenseListActivity = (ClaimantExpenseListActivity)
				getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5000);
		
		final ListView expenseListView = (ListView) expenseListActivity.findViewById(
				ca.ualberta.cs.team1travelexpenseapp.R.id.claimantExpensesList);
		
		TextView expenseInfo = (TextView) expenseListView.getChildAt(0);
		
		assertNotNull("expense was not saved", expenseInfo);
		assertEquals("claim info was not saved as expected:\n"+expenseInfo.getText().toString()+"\nv.s.\n"+expense.toString(),
				expenseInfo.getText().toString(), expense.toString());
		expenseListActivity.finish();
		claimListActivity.finish();
		loginActivity.finish();
		activity.finish();
	}
	
	
	// US 09.01.01
	//requires rooted device to be an effective test of offline editing, should test online syncing regardless however
	public void testWorkOffline() {
		final String uniqueName = UUID.randomUUID().toString();
		//need to test saving so can't use MockClaimant, create a new user with unique name instead
		final Claimant user = new Claimant(uniqueName);
		UserSingleton.getUserSingleton().setUser(user);
		
		
		activity = getActivity();
		
		//init managers so we can save
		user.initManagers(activity.getApplicationContext());
		
		//disable network
		try {
			//from http://stackoverflow.com/a/28683889 april 6
			Runtime.getRuntime().exec("svc data disabled");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			fail("Exception disabling network connection.");
		}
		

		//add a claim and an expense
		ProgressClaim claim = new ProgressClaim();
		claim.setStartDate(new Date());
		claim.setEndDate(new Date());
		claim.setClaimantName(user.getName());
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		Expense expense = new Expense("car trip", new Date(), "vehicle rental", new BigDecimal(10), "CAD");
		expenses.add(expense);
		ExpenseList expenseList = claim.getExpenseList();
		expenseList.setExpenseList(expenses);
		final ArrayList<Claim> claims = user.getClaimList().getClaims();
		claims.add(claim);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				user.getClaimList().setClaimList(claims);
				
			}
		});
		getInstrumentation().waitForIdleSync();
		
		final ListView claimListView = (ListView) activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
		
		TextView claimInfo = (TextView) claimListView.getChildAt(0);
		
		
		assertNotNull("claim did not appear in list", claimInfo);
		assertEquals("claim info in list was not as expected", claimInfo.getText().toString(), claim.toString());
		
		//enable
		try {
			Runtime.getRuntime().exec("svc data enable");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			fail("Exception reenabling network connection.");
		}
		
		//wait a second for claim to sync
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ProgressClaim onlineClaim = (ProgressClaim) loadClaimByID(claim.getUniqueId());
		assertNotNull("Claim was not synced to online", onlineClaim);
		assertEquals("Online claim does not match local claim", onlineClaim.toString(), claim.toString());
		
		Expense onlineExpense = onlineClaim.getExpenseList().getExpenses().get(0);
		assertNotNull("Expense was not synced to online", onlineExpense);
		assertEquals("Online expense does not match local claim", onlineExpense.toString(), expense.toString());
		
		activity.finish();
		
	}

	//http://stackoverflow.com/a/11555457
	private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
	    final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    final Class conmanClass = Class.forName(conman.getClass().getName());
	    final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
	    connectivityManagerField.setAccessible(true);
	    final Object connectivityManager = connectivityManagerField.get(conman);
	    final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
	    final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	    setMobileDataEnabledMethod.setAccessible(true);

	    setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
	}
	
	/**
	 * From https://github.com/rayzhangcl/ESDemo March 28, 2015
	 * get the http response and return json string
	 */
	protected String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}
	
	//get claim at specified elastic search ID
	private Claim loadClaimByID(final UUID claimID){
		String RESOURCE_URL = "http://cmput301.softwareprocess.es:8080/cmput301w15t01/claim/";
		Claim claim = null;
    	HttpClient httpclient = new DefaultHttpClient();
    	try{
    		Gson gson = new Gson();
			HttpGet getRequest = new HttpGet(RESOURCE_URL+claimID);

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();

			String json = getEntityContent(response);

			// We have to tell GSON what type we expect
			Type elasticSearchResponseType = new TypeToken<ElasticSearchResponse<ProgressClaim>>(){}.getType();
			// Now we expect to get a tag response
			ElasticSearchResponse<Claim> esResponse = gson.fromJson(json, elasticSearchResponseType);
			//get the tags
			claim = esResponse.getSource();
		} catch (ClientProtocolException e) {

			Log.d("onlineTest", e.getCause()+":"+e.getMessage());

		} catch (IOException e) {

			Log.d("onlineTest", e.getCause()+":"+e.getMessage());
		}
    	return claim;
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

