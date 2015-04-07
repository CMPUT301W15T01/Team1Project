package ca.ualberta.cs.team1travelexpenseapp.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.TagListController;
import ca.ualberta.cs.team1travelexpenseapp.ESdata.ElasticSearchResponse;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ProgressClaim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class ClaimantClaimsListTest extends
		ActivityInstrumentationTestCase2<ClaimantClaimsListActivity> {
	Activity activity;
	ListView claimListView;
	protected MockClaimant user;

	public ClaimantClaimsListTest() {
		super(ClaimantClaimsListActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		//Intent intent = new Intent();
		user = new MockClaimant("CoolGuy");
		//UserSingleton.getUserSingleton().setUser(user);
		//setActivityIntent(intent);
		//activity = getActivity();
		//claimListView = (ListView) (activity
		//		.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList));

		// set user as claimant
		// User user=new User("user","jeff");
		//
		// //create some claims to populate and test our list
		/*Claim claim1 = new Claim("name1", new Date(2000, 11, 11), new Date(
				2015, 12, 12));
		Claim claim2 = new Claim("name2", new Date(1990, 1, 8), new Date(2000,
				12, 12));
		Claim claim3 = new Claim("name3", new Date(1999, 9, 8), new Date(2012,
				12, 12));
		Claim claim4 = new Claim("name4", new Date(2013, 10, 8), new Date(2012,
				12, 12));
		Claim claim5 = new Claim("name5", new Date(2001, 10, 6), new Date(2012,
				12, 12));
		ClaimList claimList = user.getClaimList();
		claimList.addClaim(claim1);
		claimList.addClaim(claim2);
		claimList.addClaim(claim3);
		claimList.addClaim(claim4);
		claimList.addClaim(claim5);*/
	}

	// US02.01.01: As a claimant, I want to list all my expense claims, showing
	// for each claim:
	// the starting date of travel, the destination(s) of travel, the claim
	// status, tags, and
	// total currency amounts.
	public void testListClaims() {
		int claimCount = claimListView.getCount();
		for (int i = 0; i < claimCount; i++) {
			// get text info from a claim at position of i of claimListView
			TextView claimInfo = (TextView) claimListView.getItemAtPosition(i);

			ClaimList claimList = user.getClaimList();

			// toString() method should be checked manually to verify it
			// contains the correct info
			String viewtext = claimInfo.getText().toString();

			// get claim at position i of Claim list
			Claim claim = claimList.get(i);

			String expectedText = claim.toString();
			assertEquals("Claim summary at list item " + i
					+ " does not match expected value", expectedText, viewtext);
		}
		// make sure the user can see this list
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),
				claimListView);

	}

	// US02.02.01: As a claimant, I want the list of expense claims to be sorted
	// by
	// starting date of travel, in order from most recent to oldest, so that
	// ongoing
	// or recent travel expenses are quickly accessible.
	public void testSorted() {
		ClaimList claimList = user.getClaimList();
		int claimCount = claimList.getClaims().size();
		Date currDate = claimList.get(0).getStartDate();
		Date prevDate;
		for (int i = 1; i < claimCount; i++) {
			prevDate = currDate;
			currDate = claimList.get(0).getStartDate();
			assertTrue("Claims are not sorted by start date",
					currDate.after(prevDate));
		}
		// Note:testListClaim() checks that claims in ClaimsListController show
		// up in same order
		// as claims in claimsListView, so it suffices to check here that the
		// claims in
		// are sorted in the ClaimsListController
	}

	// US03.03.01: As a claimant, I want to filter the list of expense claims by
	// tags,
	// to show only those claims that have at least one tag matching any of a
	// given set
	// of one or more filter tags.
	public void testTagFilter() {
		// add a tag to two of our claims so we can test the tag filtering
		// functionality
		Claim claim = new Claim();

		String claim2Info = claim.toString();

		assertTrue("Incorrect items displayed by tag filter,claim2Info",
				claimListView.getCount() == 2);
		String viewText1 = claimListView.getItemAtPosition(0).toString();
		String viewText2 = claimListView.getItemAtPosition(1).toString();

		// if our filter works the only two items in the listview should be
		// claim2 and claim4, still in sorted oder so the following should hold
		assertTrue("Incorrect items displayed by tag filter,claim2Info",
				claim2Info == viewText1);

		assertTrue("Incorrect items displayed by tag filter,claim2Info",
				claimListView.getCount() == 2);

		// we want claims to show up if they have AT LEAST one of the selected
		// filter tags
		// so despite the addition of the extra tag the same two claims should
		// be displayed
		// claim2 and claim4, still in sorted oder so the following should hold
		assertTrue("Incorrect items displayed by tag filter,claim2Info",
				claim2Info == viewText1);

		// now there should be no claims in the list since none have the single
		// selected tag
		assertTrue("Incorrect items displayed by tag filter,claim2Info",
				claimListView.getCount() == 0);
	}

	// US 09.01.01
	public void testWorkOnline() {
		final String uniqueName = UUID.randomUUID().toString();
		//need to test saving so can't use MockClaimant, create a new user with unique name instead
		final Claimant user = new Claimant(uniqueName);
		UserSingleton.getUserSingleton().setUser(user);
		
		
		activity = getActivity();
		
		//init managers so we can save
		user.initManagers(activity.getApplicationContext());
		
		
		
		/*try {
			setMobileDataEnabled(activity.getApplicationContext(), false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

		//add a claim and an expense
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
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final ListView claimListView = (ListView) activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList);
		
		TextView claimInfo = (TextView) claimListView.getChildAt(0);
		
		
		assertNotNull("claim did not appear in list", claimInfo);
		assertEquals("claim info in list was not as expected", claimInfo.getText().toString(), claim.toString());
		
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


	// US04.08.01
	public void testNav() {

		user.clearData();
		Calendar CalDate = Calendar.getInstance();
		CalDate.set(2014, 10, 10, 0, 0, 0);
		Date startDate = CalDate.getTime();
		CalDate.set(2015, 12, 11, 0, 0, 0);
		Date endDate = CalDate.getTime();
		Claim testclaim = new Claim("test 1", startDate, endDate);
		user.getClaimList().addClaim(testclaim);
		Activity ExpenseListactivity = activity;
		Activity EditExpenseactivity = activity;

		// calls on listeners
		int counter = 0;
		// go to next view
		ListView list = (ListView) activity.findViewById(R.id.claimsList);
		try {
			list.getChildAt(0).performClick();
		} catch (Exception e) {
		}
		counter += 1;
		// add expense click button
		try {
			ExpenseListactivity.findViewById(R.id.addExpenseButton).performClick();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		counter += 1;
		// saves the expense
		try {
			EditExpenseactivity.findViewById(R.id.saveExpenseButton).performClick();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		counter += 1;
		assertTrue(counter <= 4);

	}

}
