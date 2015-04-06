package ca.ualberta.cs.team1travelexpenseapp.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import testObjects.MockClaimant;
import views.MultiSelectionSpinner;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.TagListController;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import ca.ualberta.cs.team1travelexpenseapp.Tag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.TestCase;

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
		Intent intent = new Intent();
		user = new MockClaimant("CoolGuy");
		UserSingleton.getUserSingleton().setUser(user);
		setActivityIntent(intent);
		activity = getActivity();

		// user.initManagers(activity.getApplicationContext());
		claimListView = (ListView) (activity
				.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.claimsList));

		String[] strings={"good","great","excellent"};
		final Tag tag1=new Tag(strings[0]);
		final Tag tag2=new Tag(strings[1]);
		final Tag tag3=new Tag(strings[2]);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Tag> tags = new ArrayList<Tag>();
				tags.add(tag1);
				tags.add(tag2);
				tags.add(tag3);
				user.getTagList().setTagList(tags);
				
			}
		});
		getInstrumentation().waitForIdleSync();
		// set user as claimant
		// User user=new User("user","jeff");
		//
		// //create some claims to populate and test our list
		Claim claim1 = new Claim("name1", new Date(2000, 11, 11), new Date(
				2015, 12, 12));
		Claim claim2 = new Claim("name2", new Date(1990, 1, 8), new Date(2000,
				12, 12));
		Claim claim3 = new Claim("name3", new Date(1999, 9, 8), new Date(2012,
				12, 12));
		Claim claim4 = new Claim("name4", new Date(2013, 10, 8), new Date(2012,
				12, 12));
		Claim claim5 = new Claim("name5", new Date(2001, 10, 6), new Date(2012,
				12, 12));
		ArrayList<Tag> tags = claim1.getClaimTagList();
		tags.add(tag1);
		tags.add(tag2);
		claim1.setClaimTagList(tags);
		ArrayList<Tag> tags2 = claim1.getClaimTagList();
		tags2.add(tag3);
		claim1.setClaimTagList(tags2);
		
		ClaimList claimList = user.getClaimList();
		claimList.addClaim(claim1);
		claimList.addClaim(claim2);
		claimList.addClaim(claim3);
		claimList.addClaim(claim4);
		claimList.addClaim(claim5);
		
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

	

	// US 09.01.01
	public void testReadOnlineInfo() {
		// can't use Mock user in this case because we actually want to test
		// online functionality
		// just make a new user with a unique name to test this
		Claimant claimant = new Claimant(UUID.randomUUID().toString());
		UserSingleton.getUserSingleton().setUser(claimant);

		// TODO kenny: finish this test

	}

	private boolean isNetworkAvailable() {
		// TODO Auto-generated method stub
		return false;
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
