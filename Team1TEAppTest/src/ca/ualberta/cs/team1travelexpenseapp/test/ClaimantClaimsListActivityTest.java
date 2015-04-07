package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.Calendar;
import java.util.Date;

import testObjects.MockClaimant;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation.ActivityMonitor;
import android.content.DialogInterface;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.Destination;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ProgressClaim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;


// @------------!!!NOTE!!!-------------@
// @!!! ensure emulator is unlocked !!!@
// @------------!!!NOTE!!!-------------@
public class ClaimantClaimsListActivityTest extends ActivityInstrumentationTestCase2<ClaimantClaimsListActivity> {

	Activity activity;
	protected Claimant user;
	
	public ClaimantClaimsListActivityTest() {
		super(ClaimantClaimsListActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(true);
		Claimant user = new MockClaimant("CoolGuy");
		UserSingleton.getUserSingleton().setUser(user);
		activity = getActivity();
		//user.initManagers(activity.getApplicationContext());
	}
	

	/*
	 * http://developer.android.com/training/activity-testing/activity-functional-testing.html
	 * 03/06/2015
	 * -test activity navigation
	 * -test US01.01.01
	 * -test US01.02.01
	 */
	public void testAddClaimUI() {
		
		//change activity 
		final Button addClaimButton =
				(Button) activity.findViewById(R.id.addClaimButton);
		//monitor EditClaimActivity 
		ActivityMonitor receiverActivityMonitor = 
				getInstrumentation().addMonitor(EditClaimActivity.class.getName(),
						null, false);
		 
		
		// @------------!!!NOTE!!!-------------@
		// @!!! ensure emulator is unlocked !!!@
		// @------------!!!NOTE!!!-------------@
		TouchUtils.clickView(this, addClaimButton); //check that EditClaimActivity started
		
		
		EditClaimActivity receiverActivity = (EditClaimActivity) 
		        receiverActivityMonitor.waitForActivityWithTimeout(720); //time out in 12s
		assertNotNull("ReceiverActivity is null", receiverActivity);
		assertEquals("Monitor for ReceiverActivity has not been called",
		        1, receiverActivityMonitor.getHits());
		assertEquals("Activity is of wrong type",
				EditClaimActivity.class, receiverActivity.getClass());

		// Remove the ActivityMonitor
		//getInstrumentation().removeMonitor(receiverActivityMonitor);
		
		// add claim 
		activity = receiverActivity;
		
		//find addClaimButton
		final Button saveClaimButton =
				(Button) activity.findViewById(R.id.saveClaimButton);
		final Button     addDest  = (Button)   activity.findViewById(R.id.addDestinationButton);
		final DatePicker fromDate = 
				(DatePicker) activity.findViewById(R.id.claimFromDate);
		final DatePicker endDate  = 
				(DatePicker) activity.findViewById(R.id.claimEndDate);
		
		getInstrumentation().runOnMainSync(new Runnable() {
		    @Override
		    public void run() {
		        addDest.requestFocus();
		    }
		});
		getInstrumentation().waitForIdleSync();
		TouchUtils.clickView(this,addDest);
		
		AlertDialog dialog=((EditClaimActivity) activity).newDestDialog;
		final EditText dest = (EditText) dialog.findViewById(R.id.destinationNameBody);
		final EditText reason = (EditText) dialog.findViewById(R.id.destinationReasonBody);
		final Button setDestButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		
		
		// Send destination
		getInstrumentation().runOnMainSync(new Runnable() {
		    @Override
		    public void run() {
		        dest.requestFocus();
		    }
		});
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync("Cool dest");
		getInstrumentation().waitForIdleSync();
		assertTrue("destination text not set" , dest.getText().toString().equals("Cool dest"));
		
		
		// Send reason
		getInstrumentation().runOnMainSync(new Runnable() {
		    @Override
		    public void run() {
		        reason.requestFocus();
		    }
		});
		getInstrumentation().waitForIdleSync();
		getInstrumentation().sendStringSync("Cool reason");
		getInstrumentation().waitForIdleSync();
		assertTrue("reason text not set" , reason.getText().toString().equals("Cool reason"));

		getInstrumentation().runOnMainSync(new Runnable() {
		    @Override
		    public void run() {
		        setDestButton.requestFocus();
				setDestButton.performClick();
		    }
		});
		
		//not 100% sure this will work but we'll see
		//TouchUtils.clickView(this, setDestButton);

		Calendar beforeUIcalF = Calendar.getInstance();
		beforeUIcalF.set(fromDate.getYear(), fromDate.getMonth(), 
						fromDate.getDayOfMonth());
		Calendar beforeUIcalE = Calendar.getInstance();
		beforeUIcalE.set(endDate.getYear(), endDate.getMonth(), 
						endDate.getDayOfMonth());
		//set fromDate 
		// @------------!!!NOTE!!!-------------@
		// @!!! ensure emulator is unlocked !!!@
		// @------------!!!NOTE!!!-------------@
		//TouchUtils.dragViewToBottom(this, receiverActivity, fromDate, 5); 
		//TouchUtils.dragViewToBottom(this, receiverActivity, endDate, 5); 
		getInstrumentation().runOnMainSync(new Runnable() {
		    @Override
		    public void run() {
				endDate.updateDate(1,2,3);
				fromDate.updateDate(3,2,1);
		    }
		});
		
		//check that user can change date of from and end 
		Calendar testDate = Calendar.getInstance();
		testDate.set(fromDate.getYear(), fromDate.getMonth(), 
				fromDate.getDayOfMonth());
		assertFalse("SAME" , beforeUIcalF.getTime().toString()
				.equals(testDate.getTime().toString()));
		Date sDate = testDate.getTime();
		testDate.set(endDate.getYear(), endDate.getMonth(), 
				endDate.getDayOfMonth());
		assertFalse("SAME" , beforeUIcalE.getTime().toString()
				.equals(testDate.getTime().toString()));
		Date eDate = testDate.getTime();
		
		getInstrumentation().removeMonitor(receiverActivityMonitor);
		//monitor ClaimClaimsListActivity
		receiverActivityMonitor = 
				getInstrumentation().addMonitor(ClaimantClaimsListActivity.class.getName(),
						null, false);
		 
		final View decorView = activity.getWindow().getDecorView();
		ViewAsserts.assertOnScreen(decorView, saveClaimButton);
		// @------------!!!NOTE!!!-------------@
		// @!!! ensure emulator is unlocked !!!@
		// @------------!!!NOTE!!!-------------@
		//TouchUtils.clickView(this, saveClaimButton); //check that ClaimantClaimsListActivity started
		getInstrumentation().runOnMainSync(new Runnable() {
		    @Override
		    public void run() {
				saveClaimButton.performClick();
		    }
		});
		
		ClaimantClaimsListActivity newReceiverActivity = (ClaimantClaimsListActivity) 
		receiverActivityMonitor.waitForActivityWithTimeout(720); //time out in 12s
		assertNotNull("ReceiverActivity is null", newReceiverActivity);
		assertEquals("Started new activity",
		        0, receiverActivityMonitor.getHits());
		assertEquals("Activity is of wrong type",
				ClaimantClaimsListActivity.class, newReceiverActivity.getClass());
		
		//checks one ClaimantClaimsListActivity 
		ViewAsserts.assertOnScreen(newReceiverActivity.getWindow().getDecorView(),newReceiverActivity.findViewById(R.id.claimsList) );


		Claim uiClaim = UserSingleton.getUserSingleton().getUser().getClaimList().getClaims().get(0);
		Claim claim = new ProgressClaim("CoolGuy", sDate, eDate);
		claim.addDestination(new Destination("Cool dest", "Cool reason",new Location("")));
		assertTrue(claim.toString().equals(uiClaim.toString()) );
		
		// Remove the ActivityMonitor
		getInstrumentation().removeMonitor(receiverActivityMonitor);
		
		activity.finish();
		

	}
	
	
}

