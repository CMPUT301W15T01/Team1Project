package ca.ualberta.cs.team1travelexpenseapp.test;

import org.osmdroid.views.MapView;

import testObjects.MockClaimant;
import android.app.Dialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ListView;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.Destination;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.EditExpenseActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.LoginActivity;
import ca.ualberta.cs.team1travelexpenseapp.OSMDroidMapActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;

public class GeolocationTests extends
		ActivityInstrumentationTestCase2<LoginActivity> {
	protected Instrumentation instrumentation;
	protected ExpenseListController ExpenseListController;
	protected ClaimListController ClaimListController;
	protected ClaimantClaimsListActivity claimantClaimsListActivity;
	protected ClaimantExpenseListActivity claimantExpenseListActivity;
	protected EditClaimActivity editClaimActivity;
	protected EditExpenseActivity editExpenseActivity;
	protected OSMDroidMapActivity osmDroidMapActivity;
	protected LoginActivity loginActivity;

	protected Claimant user;
	protected Expense expense;
	protected Claim claim;

	public GeolocationTests() {
		super(LoginActivity.class);
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

		loginActivity = getActivity();
		user.getClaimList().getManager()
				.setContext(loginActivity.getApplicationContext());

		Log.d("Geolocationtest",
				"ClaimList has a manager? "
						+ String.valueOf(user.getClaimList().getManager() != null));
		instrumentation = getInstrumentation();

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				claim = new Claim();
				claim.addDestination(new Destination("name", "reason",
						new Location("")));
				ClaimListController.addClaim(claim);
				ClaimListController.updateCurrentClaim(claim);
				ClaimListController.changeClaim(claim);

				ExpenseListController = new ExpenseListController(claim
						.getExpenseList());
				expense = new Expense();
				expense.setLocation(new Location(""));
				claim.getExpenseList().getExpenses().add(expense);
				ExpenseListController.setCurrentExpense(expense);

			}
		});

		instrumentation.waitForIdleSync();

	}

	protected void tearDown() throws Exception {

		if (osmDroidMapActivity != null) {
			osmDroidMapActivity.finish();
		}
		if (editClaimActivity != null) {
			editClaimActivity.finish();
		}
		if (editExpenseActivity != null) {
			editExpenseActivity.finish();
		}
		if (claimantExpenseListActivity != null) {
			claimantExpenseListActivity.finish();
		}
		if (claimantClaimsListActivity != null) {
			claimantClaimsListActivity.finish();
		}
		if (loginActivity != null) {
			loginActivity.finish();
		}
		super.tearDown();

	}

	public void getLoginActivity() {
		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				ClaimantClaimsListActivity.class.getName(), null, false);
		final Button button = (Button) loginActivity
				.findViewById(R.id.userButton);
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				button.performClick();
			}
		});
		instrumentation.waitForIdleSync();
		claimantClaimsListActivity = (ClaimantClaimsListActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(claimantClaimsListActivity);

	}

	public void getAddDestination() {
		getLoginActivity();
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
		getLoginActivity();
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
		assertNotNull(button);
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

		final Button button = (Button) editClaimActivity
				.findViewById(R.id.addDestinationButton);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				button.performClick();
			}
		});
		instrumentation.waitForIdleSync();

		ActivityMonitor osm = instrumentation.addMonitor(
				OSMDroidMapActivity.class.getName(), null, false);

		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				EditClaimActivity.class.getName(), null, false);
		Dialog dest = editClaimActivity.newDestDialog;
		assertNotNull(dest);
		assertTrue(dest.isShowing());

		final Button buttonlocation = (Button) dest
				.findViewById(R.id.selectLocationButton);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				buttonlocation.performClick();
			}
		});
		instrumentation.waitForIdleSync();

		osmDroidMapActivity = (OSMDroidMapActivity) instrumentation
				.waitForMonitorWithTimeout(osm, 10000);

		assertNotNull(osmDroidMapActivity);

		final Button buttonSetlocation = (Button) osmDroidMapActivity
				.findViewById(R.id.gps);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				buttonSetlocation.performClick();
			}
		});
		instrumentation.waitForIdleSync();

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				editClaimActivity.newDestDialog.getButton(
						Dialog.BUTTON_POSITIVE).performClick();

			}
		});
		instrumentation.waitForIdleSync();

		editClaimActivity = (EditClaimActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(editClaimActivity);

		final Button saveClaimButton = (Button) editClaimActivity
				.findViewById(R.id.saveClaimButton);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				saveClaimButton.performClick();

			}
		});

		assertNotNull(claim.getDestinationList());
	}

	/*
	 * US02.03.01 added 2015-03-23 () As a claimant, I want the list of expense
	 * claims to have each claim color code by the distance of its first
	 * destination geolocation to my home geolocation, so that claims for
	 * distant travel can be distinguished from claims for nearby travel.
	 */
	public void testClaimsColorCodedByGeolocations() {
		getLoginActivity();
		final ListView listOfClaims = (ListView) claimantClaimsListActivity
				.findViewById(R.id.claimsList);
		ColorDrawable listviewColor = (ColorDrawable) listOfClaims
				.getChildAt(0).getBackground();
		int colorId = listviewColor.getColor();
		assertNotNull(colorId);

	}

	/*
	 * US04.09.01 added 2015-03-23 () As a claimant, I want to optionally attach
	 * a geolocation to an editable expense item, so I can record where an
	 * expense was incurred.
	 */
	public void testGeolocationAttachedToExpenseItem() {
		getAddExpenseActivity();
		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				OSMDroidMapActivity.class.getName(), null, false);
		final Button selectGeolocationButton = (Button) editExpenseActivity
				.findViewById(R.id.GeolocationButtonExpense);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				selectGeolocationButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();
		osmDroidMapActivity = (OSMDroidMapActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(osmDroidMapActivity);

		final Button buttonSetlocation = (Button) osmDroidMapActivity
				.findViewById(R.id.gps);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				buttonSetlocation.performClick();
			}
		});
		instrumentation.waitForIdleSync();

		assertNotNull(expense.getLocation());

	}

	/*
	 * US10.01.01 added 2015-03-23 () As a claimant, I want to set my home
	 * geolocation.
	 */
	public void testSetHomeGeolocation() {
		getLoginActivity();

		final Button homeGeolocationButton = (Button) claimantClaimsListActivity
				.findViewById(R.id.HomeLocationButton);
		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				OSMDroidMapActivity.class.getName(), null, false);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				homeGeolocationButton.performClick();
			}
		});

		osmDroidMapActivity = (OSMDroidMapActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(osmDroidMapActivity);
		final Button buttonSetlocation = (Button) osmDroidMapActivity
				.findViewById(R.id.gps);
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				buttonSetlocation.performClick();
			}
		});
		instrumentation.waitForIdleSync();

		assertNotNull(user.getLocation());
	}

	/*
	 * US10.02.01 added 2015-03-23 () As a claimant, I want to specify a
	 * geolocation assisted by my mobile device (e.g., GPS) or manually using a
	 * map.
	 */
	public void testSpecifyGeolocationByMapGPS() {
		getLoginActivity();

		final Button GeolocationButton = (Button) claimantClaimsListActivity
				.findViewById(R.id.HomeLocationButton);
		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				OSMDroidMapActivity.class.getName(), null, false);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				GeolocationButton.performClick();
			}
		});

		osmDroidMapActivity = (OSMDroidMapActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(osmDroidMapActivity);

		final MapView map = (MapView) osmDroidMapActivity
				.findViewById(R.id.map);

		ViewAsserts.assertOnScreen(osmDroidMapActivity.getWindow()
				.getDecorView(), map);

		final Button buttonSetlocation = (Button) osmDroidMapActivity
				.findViewById(R.id.gps);
		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				buttonSetlocation.performClick();
			}
		});
		instrumentation.waitForIdleSync();

		assertNotNull(user.getLocation());
	}

	/*
	 * US10.03.01 added 2015-03-23 () As a claimant, I want to view any set or
	 * attached geolocation using a map.
	 */
	public void testViewGeolocationByMap() {
		getLoginActivity();

		final Button GeolocationButton = (Button) claimantClaimsListActivity
				.findViewById(R.id.HomeLocationButton);
		ActivityMonitor activityMonitor = instrumentation.addMonitor(
				OSMDroidMapActivity.class.getName(), null, false);

		instrumentation.runOnMainSync(new Runnable() {
			@Override
			public void run() {
				GeolocationButton.performClick();
			}
		});

		osmDroidMapActivity = (OSMDroidMapActivity) instrumentation
				.waitForMonitorWithTimeout(activityMonitor, 10000);
		assertNotNull(osmDroidMapActivity);

		final MapView map = (MapView) osmDroidMapActivity
				.findViewById(R.id.map);

		ViewAsserts.assertOnScreen(osmDroidMapActivity.getWindow()
				.getDecorView(), map);
	}
}
