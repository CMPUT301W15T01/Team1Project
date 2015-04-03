package ca.ualberta.cs.team1travelexpenseapp.test;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.EditClaimActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import junit.framework.TestCase;

public class EditClaimActivityTest extends ActivityInstrumentationTestCase2<EditClaimActivity> {
	Activity activity;
	protected Claimant user;
	
	public EditClaimActivityTest() {
		super(EditClaimActivity.class);
		// TODO Auto-generated constructor stub
		
	}

	protected void setUp() throws Exception {
		super.setUp();
		Claimant user= new Claimant("CoolGuy");
		UserSingleton.getUserSingleton().setUser(user);
		Claim claim1 = new Claim("name",new Date(2000,11,11), new Date(2015,12,12));
		user.getClaimList().getClaims().add(claim1);	
		SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentClaim(claim1);
		
		Intent intent = new Intent();
		intent.putExtra("Index", 0);
		setActivityIntent(intent);
		activity = getActivity();

	}
	
	private Claim DummyClaim(){
		
		Claim claim = new Claim();
		Date startDate = new Date(1994,12,10);
		Date endDate = new Date(2030,10,10);
		claim.setClaimantName("BILL");
		claim.setStartDate(new Date(100));
		claim.setEndDate(new Date(101));
		claim.setStatus(Status.inProgress);
		claim.setClaimantName("approver test");
		claim.addDestination("test dest", null);
		claim.setStartDate(startDate);
		claim.setEndDate(endDate);
		
		return claim;
	}
	
	//us07.01.01
	//Makes sure you can not edit an already submitted claim
	public void testEditSubmittedClaim(){
		
		Claim claim = DummyClaim();
		claim.setStatus(Status.submitted);
		ClaimListController.updateCurrentClaim(claim);
		Set<String> dest = claim.getDestinations();
		
		final Button saveBT = (Button) activity.findViewById(R.id.saveClaimButton);
		final EditText claimNameET  = (EditText) activity.findViewById(R.id.claimNameBody);
		final DatePicker fromDate = (DatePicker) activity.findViewById(R.id.claimFromDate);
		final DatePicker endDate  = (DatePicker) activity.findViewById(R.id.claimEndDate);
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				claimNameET.setText("JOE");
				fromDate.updateDate(2015, 0, 3);
				endDate.updateDate(2015, 0, 15);
				saveBT.performClick();
			}
		});
		
		getInstrumentation().waitForIdleSync();
		
		assertFalse("Name not changed?",ClaimListController.getCurrentClaim().getClaimantName() == "JOE");
		assertFalse("Start date not changed?", ClaimListController.getCurrentClaim().getStartDate() == new Date(2015,0,3) );
		assertFalse("End date not changed?" , ClaimListController.getCurrentClaim().getEndDate() == new Date(2015,0,15));
		
	}
	
	
	/*
	 *  US 7.03.01
	 *  As a claimant, I want a submitted expense claim that was 
	 *  not approved to be denoted by a claim status of returned, with further 
	 *  changes allowed by me to the claim information.
	 */
	
	public void testReturned() {
		// preconditions
		Claim claim = DummyClaim();
		claim.setStatus(Status.returned);
		ClaimListController.updateCurrentClaim(claim);
	//	ClaimListController.addClaim(claim);
		assertEquals("Claim status returned", Status.returned, ClaimListController.getCurrentClaim().getStatus());

		final EditText editName = (EditText) activity.findViewById(R.id.claimNameBody);
		final DatePicker startDatePick = (DatePicker) activity.findViewById(R.id.claimFromDate);
		final DatePicker endDatePick = (DatePicker) activity.findViewById(R.id.claimEndDate);
		
		final Button button = (Button) activity.findViewById(R.id.saveClaimButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		    editName.setText("Joe");
		    startDatePick.updateDate(2015, 0, 10);
		    endDatePick.updateDate(2015, 0, 21);
		      // click button and open next activity.
		      button.performClick();
		    }
		});
		
		getInstrumentation().waitForIdleSync();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String startdate = format.format(new Date(2015-1900,0,10));
		String enddate = format.format(new Date(2015-1900,0,21));
		
		String  startDateFromClaim = format.format(ClaimListController.getCurrentClaim().getStartDate());
		String  endDateFromClaim = format.format(ClaimListController.getCurrentClaim().getEndDate());
		
		Log.d("help", ClaimListController.getCurrentClaim().getStartDate().toString());
		assertEquals("Claim name editable", ClaimListController.getCurrentClaim().getClaimantName(), "Joe");
		assertEquals("claim start date editable",startDateFromClaim, startdate);
		assertEquals("claim end date editable",endDateFromClaim,enddate);
	}
	
	/*
	 *  US 7.04.01
	 *  As a claimant, I want a submitted expense claim that was 
	 *  approved to be denoted by a claim status of approved, with no
	 *   further changes allowed by me to the claim information (except the tags).
	 */
	
	public void testApproved(){
		// precondition - claimant has an approved claim
		Claim claim = DummyClaim();
		claim.setStatus(Status.approved);
		ClaimListController.updateCurrentClaim(claim);
		
		
		final EditText editName = (EditText) activity.findViewById(R.id.claimNameBody);
		final DatePicker startDatePick = (DatePicker) activity.findViewById(R.id.claimFromDate);
		final DatePicker endDatePick = (DatePicker) activity.findViewById(R.id.claimEndDate);

		
		//editDestination.setText("Hawaii");
		//editReason.setText("Business");
//		claim.addTag("Holiday");
//
		final Button button = (Button) activity.findViewById(R.id.saveClaimButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		    startDatePick.updateDate(2015, 0, 10);
			   endDatePick.updateDate(2015, 0, 21);
		      editName.setText("Joe");
		      button.performClick();
		    }
		});
		
		getInstrumentation().waitForIdleSync();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String startdate = format.format(new Date(1993-1900,10,20));
		String enddate = format.format(new Date(1994-1900,9,21));
		
		String  startDateFromClaim = format.format(ClaimListController.getCurrentClaim().getStartDate());
		String  endDateFromClaim = format.format(ClaimListController.getCurrentClaim().getEndDate());
		
		
		assertNotSame("Claim name not editable", ClaimListController.getCurrentClaim().getClaimantName(), "Joe");
		assertFalse("Claim start date not editable",startDateFromClaim == startdate);
		assertFalse("Claim end date not editable", endDateFromClaim == enddate);
	}
	
	

}
