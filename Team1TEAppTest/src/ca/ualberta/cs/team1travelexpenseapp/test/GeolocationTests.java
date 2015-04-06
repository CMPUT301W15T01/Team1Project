package ca.ualberta.cs.team1travelexpenseapp.test;

import testObjects.MockClaimant;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.location.Address;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import ca.ualberta.cs.team1travelexpenseapp.ApproverExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.Destination;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.EditExpenseActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.OSMDroidMapActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;

public class GeolocationTests extends
		ActivityInstrumentationTestCase2<ClaimantClaimsListActivity> {
	protected Instrumentation instrumentation;
	protected ExpenseListController ExpenseListController;
	protected ClaimListController ClaimListController;
	protected ClaimantClaimsListActivity claimantClaimsListActivity;
	protected ClaimantExpenseListActivity claimantExpenseListActivity;
	protected EditClaimActivity editClaimActivity;
	protected EditExpenseActivity editExpenseActivity;
	protected OSMDroidMapActivity osmDroidMapActivity;
	
	protected Claimant user;
	protected Expense expense;
	protected Claim claim;
	
	public GeolocationTests() {
		super(ClaimantClaimsListActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		Log.d("Geolocationtest", "Setup Started");
		user = new MockClaimant("CoolGuy");
		user.setLocation(new Location(""));
		Log.d("Geolocationtest", "User Created");
		UserSingleton.getUserSingleton().setUser(user);
		// user.getClaimList().getClaims().clear();
		Log.d("Geolocationtest", "User Singleton set");
		ClaimListController = new ClaimListController(user.getClaimList());

		user.getClaimList().getManager()
				.setContext(getActivity().getApplicationContext());

		Log.d("Geolocationtest",
				"ClaimList has a manager? "
						+ String.valueOf(user.getClaimList().getManager() != null));
		instrumentation = getInstrumentation();
		
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claim = new Claim();
				claim.addDestination(new Destination("name", "reason", new Location("")));
				ClaimListController.addClaim(claim);
				ClaimListController.updateCurrentClaim(claim);

				ExpenseListController = new ExpenseListController(claim
						.getExpenseList());
				expense = new Expense();
				expense.setLocation(new Location(""));
				claim.getExpenseList().getExpenses()
						.add(expense);
			}
		});

		instrumentation.waitForIdleSync();
		
		claimantClaimsListActivity = getActivity();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		 if (claimantClaimsListActivity != null) {
		 claimantClaimsListActivity.finish();
		 }
		 if (claimantExpenseListActivity != null) {
		 claimantExpenseListActivity.finish();
		 }
		 if (osmDroidMapActivity != null) {
		 osmDroidMapActivity.finish();
		 }			 
		 if (editClaimActivity!= null) {
			 editClaimActivity.finish();
		 }
		 if (editExpenseActivity!= null) {
			 editExpenseActivity.finish();
		 }
	}

	public void getAddDestination() {
		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				EditClaimActivity.class.getName(), null, false);
		final Button button = (Button) claimantClaimsListActivity
				.findViewById(R.id.addClaimButton);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				button.performClick();
			}
		});
		instrumentation.waitForIdleSync();
		editClaimActivity = (EditClaimActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(editClaimActivity);
		
	}

	public void getExpenseListActivity() {
		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				ClaimantExpenseListActivity.class.getName(), null, false);
		final ListView listOfClaims = (ListView) claimantClaimsListActivity
				.findViewById(R.id.claimsList);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				listOfClaims.performItemClick(listOfClaims.getChildAt(0), 0,
						listOfClaims.getAdapter().getItemId(0));

			}
		});
		instrumentation.waitForIdleSync();
		claimantExpenseListActivity = (ClaimantExpenseListActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(claimantExpenseListActivity);
	}

	public void getAddExpenseActivity() {
		getExpenseListActivity();
		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				EditExpenseActivity.class.getName(), null, false);
		final Button button = (Button) claimantExpenseListActivity
				.findViewById(R.id.addExpenseButton);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				button.performClick();
			}
		});
		instrumentation.waitForIdleSync();
		editExpenseActivity = (EditExpenseActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(editExpenseActivity);
		
	}

	/*
	 * US01.07.01 As a claimant, I want to attach a geolocation to a
	 * destination.
	 */
	public void testAddGeolocationToDestination() {
		getAddDestination();
		
	}

	/*
	 * US02.03.01 added 2015-03-23 () As a claimant, I want the list
	 * of expense claims to have each claim color code by the distance of its
	 * first destination geolocation to my home geolocation, so that claims for
	 * distant travel can be distinguished from claims for nearby travel.
	 */
	public void testClaimsColorCodedByGeolocations() {
		
	}

	/*
	 * US04.09.01 added 2015-03-23 () As a claimant, I want to
	 * optionally attach a geolocation to an editable expense item, so I can
	 * record where an expense was incurred.
	 */
	public void testGeolocationAttachedToExpenseItem() {

	}

	/*
	 * US10.01.01 added 2015-03-23 () As a claimant, I want to set my
	 * home geolocation.
	 */
	public void testSetHomeGeolocation() {
		
	}

	/*
	 * US10.02.01 added 2015-03-23 () As a claimant, I want to specify
	 * a geolocation assisted by my mobile device (e.g., GPS) or manually using
	 * a map.
	 */
	public void testSpecifyGeolocationByMapGPS() {

	}

	/*
	 * US10.03.01 added 2015-03-23 () As a claimant, I want to view
	 * any set or attached geolocation using a map.
	 */
	public void testViewGeolocationByMap() {

	}
}
