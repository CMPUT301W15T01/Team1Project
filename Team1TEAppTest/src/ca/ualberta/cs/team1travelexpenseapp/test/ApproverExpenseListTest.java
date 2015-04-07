package ca.ualberta.cs.team1travelexpenseapp.test;

import java.io.File;
import testObjects.MockApprover;
import testObjects.MockClaimant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimInfo;
import ca.ualberta.cs.team1travelexpenseapp.ApproverClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ApproverExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.EditExpenseActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.claims.ApprovedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Approver;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;


public class ApproverExpenseListTest extends
		ActivityInstrumentationTestCase2<ApproverClaimsListActivity> {

	protected Instrumentation instrumentation;
	protected Activity activity = null;
	protected ApproverClaimInfo claimInfoActivity = null;
	protected ApproverExpenseListActivity expenselistActivity = null;
	protected ApproverClaimsListActivity claimlistActivity = null;
	// protected ActivityMonitor activityMonitor;

	protected Expense expense;
	protected SubmittedClaim claim;

	protected ExpenseListController ExpenseListController;
	protected ClaimListController ClaimListController;
	protected Approver user;

	public ApproverExpenseListTest() {
		super(ApproverClaimsListActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		Log.d("ApproverExpenseTest", "Setup Started");
		user = new MockApprover("CoolGuy");
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
		claimlistActivity = getActivity();

	}

	protected void tearDown() throws Exception {
		if (activity != null) {
			activity.finish();
		}
		if (expenselistActivity != null) {
			expenselistActivity.finish();
		}
		if (claimlistActivity != null) {
			claimlistActivity.finish();
		}
		if (claimInfoActivity != null) {
			claimInfoActivity.finish();
		}
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
				claim = new SubmittedClaim();
				
				ClaimListController.addClaim(claim);

				ExpenseListController = new ExpenseListController(claim
						.getExpenseList());
				claim.getExpenseList().getExpenses()
						.add(expense = new Expense());
			}
		});

		instrumentation.waitForIdleSync();

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				listOfClaims.performItemClick(listOfClaims.getChildAt(0), 0,
						listOfClaims.getAdapter().getItemId(0));
				// listOfClaims.performItemClick(listOfClaims, 0,
				// listOfClaims.getId());
			}
		});
		instrumentation.waitForIdleSync();
		expenselistActivity = (ApproverExpenseListActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(expenselistActivity);
		return expenselistActivity;
	}

	// US08.03.01
	/*
	 * Testing if we can see all of the claim info for approvers
	 */
	public void testClaimInfo() {
		activity = getApproverExpenseListactivity();
		// init claim/expenses
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
				ApproverClaimInfo.class.getName(), null, false);
		final ListView listOfexpenses = (ListView) expenselistActivity
				.findViewById(R.id.approverExpensesList);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				listOfexpenses.performItemClick(listOfexpenses.getChildAt(0),
						0, listOfexpenses.getAdapter().getItemId(0));
			}
		});
		instrumentation.waitForIdleSync();
		claimInfoActivity = (ApproverClaimInfo) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 1000);
		assertNotNull(claimInfoActivity);
		TextView text = (TextView) claimInfoActivity
				.findViewById(R.id.ApproverClaimInfoTextView);
		assertEquals("Can View Info", expense.toString(), text.getText()
				.toString());

	}

	// //US08.04.01
	// /*
	// * Just checks if expenses
	// * from claims can be seen
	// *
	// * */
	public void testApproverClaimsVisible() {
		activity = getApproverExpenseListactivity();

		assertNotNull(activity);

		ListView expenseListLV = (ListView) activity
				.findViewById(R.id.approverExpensesList);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),
				expenseListLV);

	}

	// US08.05.01
	/*
	 * Checks if receipt is visible for the approver
	 */
	public void testReceiptVisible() {
		// activity doesnt exist yet
		// ActivityMonitor activityMonitor =
		// getInstrumentation().addMonitor(ApproverClaimSummaryActivity.class.getName(),
		// null, false);

		activity = getApproverExpenseListactivity();

		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				ApproverClaimInfo.class.getName(), null, false);

		final ListView expenseListLV = (ListView) activity
				.findViewById(R.id.approverExpensesList);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(),
				expenseListLV);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				expenseListLV.performItemClick(expenseListLV.getAdapter()
						.getView(0, null, null), 0, expenseListLV.getAdapter()
						.getItemId(0));
			}
		});

		claimInfoActivity = (ApproverClaimInfo) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(claimInfoActivity);
		TextView text = (TextView) claimInfoActivity
				.findViewById(R.id.ApproverClaimInfoTextView);
		assertEquals("Can View Info", expense.toString(), text.getText()
				.toString());
		ImageView receipt = (ImageView) claimInfoActivity
				.findViewById(R.id.imageViewApproverReceipt);
		ViewAsserts.assertOnScreen(
				claimInfoActivity.getWindow().getDecorView(), receipt);

	}

	// US08.08.01
	/*
	 * Tests if the Approver's name is attached to an approved claim and status
	 * is approved
	 */
	public void testApproverApprovesClaim() {

		activity = getApproverExpenseListactivity();
		MockClaimant mc = new MockClaimant("hi");
		UserSingleton.getUserSingleton().setUser(mc);

		// get approve button
		final Button button = (Button) activity
				.findViewById(R.id.approveButton);

		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// click approve button
				button.performClick();
			}

		});
		getInstrumentation().waitForIdleSync();

		assertNotNull("Approver not added", ClaimListController
				.getCurrentClaim().getApproverList());
		assertFalse("The current approver is not the user", user==
				UserSingleton.getUserSingleton().getUser());

	}

	/*
	 * US08.09.01 added 2015-02-12 As an approver, I want to ensure I cannot
	 * return or approve an expense claim for which I am the claimant.
	 */
	public void ApproverNotClaimant() {
		getApproverExpenseListactivity();
		claim.setClaimantName("CoolGuy");
		final Button button = (Button) expenselistActivity
				.findViewById(R.id.approveButton);
		expenselistActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// click approve button
				button.performClick();
			}

		});
		getInstrumentation().waitForIdleSync();

		assertTrue("Status is approved",
				ApprovedClaim.class != ClaimListController.getCurrentClaim()
						.getStatus());

	

	}
}
