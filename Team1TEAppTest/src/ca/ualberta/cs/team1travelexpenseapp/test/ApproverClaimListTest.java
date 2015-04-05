package ca.ualberta.cs.team1travelexpenseapp.test;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import testObjects.MockApprover;
import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ReturnedClaim;
import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimInfo;
import ca.ualberta.cs.team1travelexpenseapp.ApproverExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantCommentActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.LoginActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Approver;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import junit.framework.TestCase;

public class ApproverClaimListTest extends
		ActivityInstrumentationTestCase2<LoginActivity> {
	protected Instrumentation instrumentation;
	protected ApproverExpenseListActivity activity = null;
	protected ApproverClaimInfo claimInfoActivity = null;
	protected ApproverExpenseListActivity listActivity = null;
	protected ApproverClaimsListActivity claimlistActivity = null;
	protected LoginActivity login = null;
	// protected ActivityMonitor activityMonitor;


	protected Expense expense;
	protected Claim claim;

	protected ExpenseListController ExpenseListController;
	protected ClaimListController ClaimListController;
	protected Approver user;

	public ApproverClaimListTest() {
		super(LoginActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		Log.d("ApproverExpenseTest", "Setup Started");
		Approver user = new MockApprover("CoolGuy");
		Log.d("ApproverExpenseTest", "User Created");
		UserSingleton.getUserSingleton().setUser(user);
		// user.getClaimList().getClaims().clear();
		Log.d("ApproverExpenseTest", "User Singleton set");
		ClaimListController = new ClaimListController(user.getClaimList());

		user.getClaimList().getManager()
				.setContext(getActivity().getApplicationContext());

		Log.d("ApproverExpenseTest",
				"ClaimList has a manager? "
						+ String.valueOf(user.getClaimList().getManager() != null));
		instrumentation = getInstrumentation();
		login = getActivity();
		user.initManagers(activity.getApplicationContext());
		getApproverExpenseListactivity();

	}

	protected void cleanUp() {
		instrumentation.waitForIdleSync();
		if (activity != null) {
			activity.finish();
		}
		if (listActivity != null) {
			listActivity.finish();
		}

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				if (ExpenseListController.getExpenseList().getExpenses()
						.contains(expense)) {
					ExpenseListController.removeExpense(expense);
				}
				if (ClaimListController.getClaimList().getClaims()
						.contains(claim)) {
					ClaimListController.deleteClaim(claim);
				}
			}
		});
		instrumentation.waitForIdleSync();

	}

	protected void tearDown() throws Exception {
		cleanUp();
		super.tearDown();

	}

	private ApproverExpenseListActivity getApproverExpenseListactivity() {

		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				ApproverExpenseListActivity.class.getName(), null, false);
		final ListView listOfClaims = (ListView) claimlistActivity
				.findViewById(R.id.approverclaimList);
		// claimlistActivity.runOnUiThread(new Runnable() {
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claim = new Claim();
				ClaimListController.addClaim(claim);
				ClaimListController.updateCurrentClaim(claim);

				ExpenseListController = new ExpenseListController(claim
						.getExpenseList());
			}
		});
		instrumentation.waitForIdleSync();

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				listOfClaims.performItemClick(listOfClaims.getAdapter()
						.getView(0, null, null), 0, listOfClaims.getAdapter()
						.getItemId(0));
			}
		});
		instrumentation.waitForIdleSync();
		listActivity = (ApproverExpenseListActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(listActivity);
		return listActivity;
	}

	// US08.01.01

	/*
	 * Tests that the Approver can properly navigate to the screen with all the
	 * claims
	 */
	public void testgetSubmittedClaims() {
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
				ApproverClaimsListActivity.class.getName(), null, false);
		LoginActivity userSelect = (LoginActivity) getActivity();

		final Button approverBT = (Button) userSelect
				.findViewById(R.id.approverButton);
		userSelect.runOnUiThread(new Runnable() {

			public void run() {

				approverBT.performClick();// approver user type is selected
				// User type selected : precondition
			}

		});

		ApproverClaimsListActivity nextActivity = (ApproverClaimsListActivity) getInstrumentation()
				.waitForMonitorWithTimeout(activityMonitor, 5000);
		assertNotNull(nextActivity);

		ListView claimlistView = (ListView) nextActivity
				.findViewById(R.id.approverclaimList);
		ViewAsserts.assertOnScreen(nextActivity.getWindow().getDecorView(),
				claimlistView);

		nextActivity.finish();
	}

	// //US08.02.01
	/*
	 * testApproverSortedClaims() checks if ApproverActivity receives a sorted
	 * list of claims
	 */
	public void testApproverSortedClaims() {

		Claim claim = new Claim();
		ClaimListController.setCurrentClaim(claim);

		// claims loaded onStart of nextActivity
		ListView claimlistView = (ListView) getActivity().findViewById(
				R.id.approverclaimList);
		ViewAsserts.assertOnScreen(getActivity().getWindow().getDecorView(),
				claimlistView);

		// checks if claims are in order
		ClaimList submittedClaims = ClaimListController.getClaimList();
		Claim claimToCheck;
		Claim claimCompared;
		for (int i = 0; i < (submittedClaims.length() - 1); i++) {

			claimToCheck = submittedClaims.get(i);
			claimCompared = submittedClaims.get(i + 1);
			assertTrue("Approved Claims are sorted?", claimToCheck
					.getStartDate().compareTo(claimCompared.getStartDate()) < 0);

		}

	}

	// US08.07.01
	/*
	 * tests if returned claims show up for claimants
	 */
	public void testReturnedVisible() {


		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
				ApproverClaimsListActivity.class.getName(), null, false);

		LoginActivity userSelect = getActivity();

		final Button returnBT = (Button) userSelect
				.findViewById(R.id.approverButton);
		userSelect.runOnUiThread(new Runnable() {

			public void run() {

				returnBT.performClick();// approver user type is selected
				// User type selected : precondition
			}

		});

		ApproverClaimsListActivity nextActivity = (ApproverClaimsListActivity) getInstrumentation()
				.waitForMonitorWithTimeout(activityMonitor, 5000);
		assertNotNull(nextActivity);

		ClaimList claimList = new ClaimList(user);

		claimList = ClaimListController.getClaimList();

		int statuscount = 0;

		for (int i = 0; i < claimList.getClaims().size(); i++) {

			if (claimList.getClaim(i).getStatus().equals(ReturnedClaim.class)) {
				statuscount++;

			}

		}

		assertTrue("Claim was returned?", statuscount == 0);
		userSelect.finish();
		nextActivity.finish();
	}

}
