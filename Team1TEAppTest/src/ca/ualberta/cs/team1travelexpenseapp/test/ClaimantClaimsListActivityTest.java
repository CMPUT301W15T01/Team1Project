package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;
import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
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
		Claimant user = new Claimant("CoolGuy");
		UserSingleton.getUserSingleton().setUser(user);
		activity = getActivity();
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
		final EditText   dest     = (EditText) activity.findViewById(R.id.claimDestinationBody);
		final EditText   reason   = (EditText) activity.findViewById(R.id.claimReasonBody);
		final Button     addDest  = (Button)   activity.findViewById(R.id.addDestinationButton);
		final DatePicker fromDate = 
				(DatePicker) activity.findViewById(R.id.claimFromDate);
		final DatePicker endDate  = 
				(DatePicker) activity.findViewById(R.id.claimEndDate);
		
		
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
		
		
		// @------------!!!NOTE!!!-------------@
		// @!!! ensure emulator is unlocked !!!@
		// @------------!!!NOTE!!!-------------@
		TouchUtils.clickView(this, addDest); 
		
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
		TouchUtils.dragViewToBottom(this, receiverActivity, fromDate, 5); 
		TouchUtils.dragViewToBottom(this, receiverActivity, endDate, 5); 
		
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
		TouchUtils.clickView(this, saveClaimButton); //check that ClaimantClaimsListActivity started


		
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
		Claim claim = new Claim("Cool Guy", sDate, eDate);
		claim.addDestination("Cool dest","Cool reason");
		assertTrue(claim.toString().equals(uiClaim.toString()) );
		
		// Remove the ActivityMonitor
		getInstrumentation().removeMonitor(receiverActivityMonitor);
		
		activity.finish();
		

	}
	
	
}

