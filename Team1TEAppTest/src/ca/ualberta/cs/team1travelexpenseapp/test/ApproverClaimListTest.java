
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.R;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.TestCase;

public class ApproverClaimListTest extends ActivityInstrumentationTestCase2<ApproverClaimListActivity> {

	private Intent launch;
	final Button approverButton;
	
	
	protected void setUp() throws Exception {
		super.setUp(ClaimActivity.class);
        launch = new Intent(getInstrumentation()
                .getTargetContext(), ClaimActivity.class);
        startActivity(launch, null, null);
        approverButton = (Button) getActivity()
                .findViewById(R.id.approve_button);
	}
	
	
	
	private Claim DummyClaim(){
		
		Claim claim = new Claim();
		//by default their status is submitted
		claim.setStartDate(new Date(100));
		claim.setEndDate(new Date(101));
		claim.setStatus("submitted");
		claim.setName("approver test");
		claim.addDestination("test dest");
		claim.setTotal(100,"EUR");
		
		return claim;
	}
	
	private Expense DummyExpense(){
			
			Expense expense = new Expense("Expense Name");
			
			expense.setDate(new Date(100));
			expense.setTotal(100, USD);
			
			return expense;
			
		}
	
	
	private ApproverClaimActivity startWithClaims(ClaimList claims){
		
		Intent intent = new Intent();
		
		intent.putExtra(claims);
		setActivityIntent(intent);		
		
		return (ApproverClaimActivity) getActivity();
	}
	
	//US08.01.01
	
	/*
	 * Tests that the Approver can properly
	 * navigate to the screen
	 * with all the claims
	 * 
	 * */
	public void testgetSubmittedClaims(){
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimsActivity.class.getName(), null, false);
		UserSelectActivity  userSelect = new UserSelectActivity();
		
		final Button approverBT = (Button) userSelect.findViewById(R.id.BTApprover);
		userSelect.runOnUiThread(new Runnable(){
			
			public void run(){
				
				approverBT.performClick();// approver user type is selected
				//User type selected : precondition
			}
			
		});
		
		ApproverClaimListActivity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
		assertNotNull(nextActivity);
		
		ListView claimlistView = (ListView) nextActivity.findViewById(R.id.ApproverClaimLV);
		ViewAsserts.assertOnScreen(nextActivity.getWindow().getDecorView(),view);
		
		nextActivity .finish();
		
	}
	
	
	////US08.02.01
	/*testApproverSortedClaims()
	 * checks if ApproverActivity receives a sorted
	 * list of claims
	 */
	public void testApproverSortedClaims(){
		
		ClaimListController list = new ClaimListController();
		Claim claim1 = (DummyClaim());
		claim1.setDate(200);
		list.add(claim1);
		Claim claim2 = (DummyClaim());
		claim2.setDate(300);
		list.add(claim2);
		Claim claim3 = (DummyClaim());
		claim3.setDate(400);
		list.add(claim3);
		Claim claim4 = (DummyClaim());
		claim4.setDate(500);
		list.add(claim4);
		
		list.saveOnline();
		
		Claim claimToCheck;
		Claim claimCompared;
		
		
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimsActivity.class.getName(), null, false);
		UserSelectActivity  userSelect = new UserSelectActivity();
		
		final Button approverBT = (Button) userSelect.findViewById(R.id.BTApprover);
		userSelect.runOnUiThread(new Runnable(){
			
			public void run(){
				
				approverBT.performClick();// approver user type is selected
				//User type selected : precondition
			}
			
		});
		
		ApproverClaimListActicity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
		assertNotNull(nextActivity);
		//claims loaded onStart of nextActivity
		
		ListView claimlistView = (ListView) nextActivity.findViewById(R.id.ApproverClaimLV);
		ViewAsserts.assertOnScreen(nextActivity.getWindow().getDecorView(),view);
		
		
		
		//checks if claims are in order
		for(int i= 0; i < (ApprovedClaims.length - 1); i++){
			
			claimToCheck = ApprovedClaims.get(i);
			claimCompared = ApprovedClaims.get(i+1);
			assertTrue("Approved Claims are sorted?", claimToCheck.getDate() < claimCompared.getDate());
			
		}
		claimToCheck = ApprovedClaims.get(ApprovedClaims.length -1);	
		claimCompared = ApprovedClaims.get(ApprovedClaims.length - 2);
		assertTrue("Last approved Claim in order?", claimToCheck.getDate() > claimCompared.getDate());
	}
	
	
	//US08.03.01
	/*
	 *Testing if we can see all of
	 *the expenses of a claim
	 * */
	public void testExpensesVisible(){
		
		  Expense expense = DummyExpense();
		  ApproverExpenseActivity ApproverActivity = new ApproverExpenseActivity();
		  ClaimlistController controller = new ClaimlistController();
		  
		  Claim claim = DummyClaim();
		  claim.addExpense(expense);
		  controller.add(claim);
		  controller.save();
		  AssertFalse("claim list not empty?" , controller.LoadClaims().isEmpty());//precondition
			
		  ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimSummaryActivity.class.getName(), null, false);
			
		  ApproverClaimSummaryActivity approverCSA = new ApproverClaimSummaryActivity(); 
		  final ListView expenseListLV = (ListView) approverCSA.findViewById(R.id.LVExpenseList);
		  approverCSA.runOnUiThread(new Runnable(){
				
				public void run(){
					
					expenseListLV.performClick();//onClick would be overrided
				}
				
			});
		
			
			Approver lastActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
			assertNotNull(lastActivity);
			
		 
		  final TextView expenseSummary = lastActivity.findViewById(R.id.expenseSummaryTV);
		  ViewAsserts.assertOnScreen(lastActivity.getWindow().getDecorView(),view); 
			
		  String expense_check = expenseSummary.getText();
		  
		  AssertTrue("details,cost, and description displayed?", expense_check.toLowerCase().contains("details") 
				  && expense_check.toLowerCase().contains("desc") && expense_check.toLowerCase().contains("cost"));
	
		
	}
	
	
	
	//US08.06.01
	/*
	*Tests if an approver comment
	*was successfully added to 
	*a claim
	*/
	public void testCommentAddable(){
			String testComment = "Test comment."; // pre condition. comment must exist
			CommentFragment commentBox = new CommentFragment();
			
			commentBox.setArguments(DummyClaim().addExpense(DummyExpense()));
			commentBox.addComment(testComment);
			Claim claim = commentBox.getArguments();
			
			AssertEquals("Comment Written successfully?", testComment, claim.getComment());
		
	}
	
	
	//US08.07.01
	/*
	* tests if returned claims
	* show up for claimants
	*
	*/
	public void testReturnedVisible(){
		Claimlist list = new ClaimList();
		list.addClaim(DummyClaim());
		
		
		ActivityMonitor activityMonitor = getInstrumentation().addMonitor(ApproverClaimsActivity.class.getName(), null, false);
		ApproverClaimSummaryActivity  ActivityCSA = new ApproverClaimSummaryActivity();
		
		final Button returnBT = (Button) userSelect.findViewById(R.id.returnBT);
		userSelect.runOnUiThread(new Runnable(){
			
			public void run(){
				
				returnBT.performClick();// approver user type is selected
				//User type selected : precondition
			}
			
		});
		
		ApproverClaimListActivity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
		assertNotNull(nextActivity);
		
		claimsListController listControl = new claimsListController();
		
		ClaimList claimList = new ClaimList();
		
		claimList = listControl.loadClaims();
		
		int statuscount;
		
		for(int i =0; i<claimlist.length; i++){
			
			if(claimlist.getClaimAtIndex(i).getStatus()=="returned"){
				statuscount++;
				
			}
			
		}
		
		assertTrue("Claim was returned?", statuscount > 0);
		
	}
	
	
	//US08.08.01
	/*
	*Tests if the Appover's name
	*is attached to 
	* an approved claim
	*/
	public void testApproverNameAttached(){
		
		Claimlist list = new ClaimList();
		Claim claim = new DummyClaim("claimant","Jonh")
		list.addClaim(claim);
		
		
		ApproverClaimActivity activity = startWithClaims(list);
		
		User checkuser = New User("appover","John");
	
		//get activity and assert user has logged in
		ClaimActivity Activity = getActivity();
		AssertTrue("not logged in",User.loggedin());
		// get list view
		ListView view = (ListView) Activity.findViewById(ca.ualberta.cs.R.id.claimlistview);
		// longclick the claim
		Activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
			// click button and open the add claim activity.
			view.getAdapter().getView(0, null, null).performLongClick();
			// I create getLastDialog method in claimactivity class. Its return last created AlertDialog
			AlertDialog dialog = Activity.getLastDialog();
			performClick(dialog.getButton(DialogInterface.APPROVE));
			}
		});
		// assert that on the screen
		ClaimListController list = new ClaimListController();
		assertTrue("claim is visible",list.get(0).getVisibility() == View.GONE);
		//claim cannot be approved when claimant is the appover 
		assertFalse(claim.setApproverName(checkuser));//In this the name of the approver should be set only if not same name
		checkuser = New User("appover","Kim");
		assertTrue(claim.setApproverName(checkuser));//In this the name of the approver should be set only if not same name
		
		ClaimantClaimsListActivity ClaimantActivity = new ClaimantClaimsListActivity();
		
		
		
		ClaimList claimlist = ClaimantActivity.LoadClaims();
		
		AssertEquals("Approver name comes through?","Kim", claimlist.getClaim(0).getApproverName());
		
		
	}
	
	
}
