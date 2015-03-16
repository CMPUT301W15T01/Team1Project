package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The controller allowing modifications from the ui to change the claims and claims lists
 * Allows general functionality like adding/deleting claims
 *
 */

public class ClaimListController {
	/**
	 * The apps main list of claims
	 */
	protected static ClaimList claimsList = null;
	/**
	 * The list of claims that are to be displayed in a view
	 */
	protected static ClaimList displayedClaimList = null;
	/**
	 * The claim that is currently in use when the user selects a claim for viewing/modification
	 */
	protected static Claim currentClaim = null;
	/**
	 * The current user of the app
	 */
	protected static User user = null;
	/**
	 * Gets the current claims list
	 * @return returns the current claims list
	 */
	public static ClaimList getClaimList() { 
		if (claimsList == null) {
			claimsList = new ClaimList();
		}
		
		return claimsList;
	}
	/**
	 * Gets the displayed claims list
	 * @return returns the displayed claims list
	 */
	public static ClaimList getDisplayedClaims() {
		if (displayedClaimList == null) {
			displayedClaimList = getClaimList();
		}
		
		return displayedClaimList;
	}
	/**
	 * Sets the current claim's status to returned and add the users name to the list of approvers
	 */
	public void returnClaim() {
		//As an approver, I want to return a submitted expense claim that was not approved, 
		//denoting the claim status as returned and setting my name as the approver for the expense claim.
		currentClaim.setStatus(Status.returned);
		currentClaim.getApproverList().add(user);
		currentClaim.setApproverList(currentClaim.getApproverList());
	}
	/**
	 * Sets the displayed claims list
	 * @param claims The claims list to be displayed
	 */
	public static void setDisplayedClaims(ClaimList claims) {
		displayedClaimList = claims;
	}
	/**
	 * Updates the current claim to be updated
	 * @param newClaim the claim to be updated
	 */
	public static void updateCurrentClaim(Claim newClaim) {
		if (currentClaim == null) {
			throw new RuntimeException("no current claim");
		}
		claimsList.updateClaim(currentClaim, newClaim);
	}
	/**
	 * Sets the current claim that is selected by user
	 * @param claim The claim that is selected
	 */
	public static void setCurrentClaim(Claim claim){
		currentClaim=claim;
	}
	/**
	 * Deletes a claim
	 * @param claim The claim to be deleted
	 */
	public static void deleteClaim(Claim claim){
		ArrayList<Claim> claims=claimsList.getClaims();
		claims.remove(claim);
		claimsList.setClaimList(claims);
	}
	
	/**
	 * The onClick for the submit button
	 * Shows a warning dialog if the claim or expense is incomplete
	 * Redirects to the function within the ClaimListController
	 * @param activity 
	 */
	public static void onSubmitClick (final ClaimantExpenseListActivity activity) {
		AlertDialog submitWarningDialog;
	
		
		boolean expensesFlag = false;
		boolean expensesComplete = true;
		for(Expense expense: ClaimListController.getCurrentClaim().getExpenseList().getExpenses()){
			//flag check 
			if(expense.isFlagged() == true){
				expensesFlag = true;
			}
			//empty field check for expense 
			if (expense.isComplete() == false ) {
				expensesComplete = false;
			}
		}
		
		if(ClaimListController.getCurrentClaim().isComplete() == false 
				|| expensesFlag == true || expensesComplete == false){
			
			AlertDialog.Builder submitBuilder = new AlertDialog.Builder(activity);
			submitBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               //Do nothing
		        	   Claim submittedClaim = ClaimListController.getCurrentClaim();
						submittedClaim.setStatus(Status.submitted);
						ClaimListController.updateCurrentClaim(submittedClaim);
		        	   
		        	  // ClaimListController.getCurrentClaim().setStatus(Status.submitted);
		        	   Toast.makeText(activity.getApplicationContext(),"Claim submitted", Toast.LENGTH_LONG).show();
		        	   //push online here
		           }
		    });
			submitBuilder.setNegativeButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(activity.getApplicationContext(), "Claim was not Submitted", Toast.LENGTH_SHORT).show();
				}
			});
			submitBuilder.setTitle("Claim may be incomplete");
			submitWarningDialog=submitBuilder.create();
			submitWarningDialog.show();

		}else{
			
			if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted && ClaimListController.getCurrentClaim().getStatus() != Status.approved){
				
				//ClaimListController.getCurrentClaim().setStatus(Status.submitted);
				Claim submittedClaim = ClaimListController.getCurrentClaim();
				submittedClaim.setStatus(Status.submitted);
				ClaimListController.updateCurrentClaim(submittedClaim);
				
				Toast.makeText(activity.getApplicationContext(),"Claim submitted", Toast.LENGTH_LONG).show();
				//push online here
				Intent intent = new Intent(activity, ClaimantClaimsListActivity.class);
				activity.startActivity(intent);
			}
			else{
				Toast.makeText(activity.getApplicationContext(),"Claim can not be submitted", Toast.LENGTH_SHORT).show();

			}
			
		}
		
		
		
		
		
		
	}
	
	/**
	 * The onClick method for the save button when editing/adding claims
	 * @param activity The edit claim activity containing the views
	 */
	public static void onSaveClick(EditClaimActivity activity) {
		TextView   nameView   = (TextView) activity.findViewById(R.id.claimNameBody);
		String     nameText   = nameView.getText().toString();
		
		DatePicker fDateView  = (DatePicker) activity.findViewById(R.id.claimFromDate);
		Calendar   calendar   = Calendar.getInstance();
		calendar.set(fDateView.getYear(), fDateView.getMonth(), fDateView.getDayOfMonth());
		Date fromDate = calendar.getTime();
		
		DatePicker eDateView  = (DatePicker) activity.findViewById(R.id.claimEndDate);
		calendar.set(eDateView.getYear(), eDateView.getMonth(), eDateView.getDayOfMonth());
		Date endDate = calendar.getTime();
		

	
		MultiSelectionSpinner tagSpinner= (MultiSelectionSpinner) activity.findViewById(R.id.claimTagSpinner);
		ArrayList<Tag> claimTags = (ArrayList<Tag>) tagSpinner.getSelectedItems();
		//Claim newClaim=new Claim(nameText, fromDate, endDate);
		
		//newClaim.setClaimTagList(claimTags);
		
		if(getCurrentClaim().getStatus()!=Status.submitted && getCurrentClaim().getStatus()!=Status.approved ){
		
				Claim newClaim=new Claim(nameText, fromDate, endDate);
				newClaim.setClaimTagList(claimTags);
				ClaimListController.updateCurrentClaim(newClaim);
				
		
		}else{
			Claim newClaim = getCurrentClaim();
			newClaim.setClaimTagList(claimTags);
			ClaimListController.updateCurrentClaim(newClaim);
			
		}
		
		HashMap<String,String> daMAP = new HashMap<String, String>();
		
		
		if(!getCurrentClaim().getClaimantName().equals("")  && !getCurrentClaim().getDestinationReasonList().equals(daMAP)
				&& !getCurrentClaim().getCurrencyTotals().equals("")){
			
			getCurrentClaim().setComplete(true);
			
		}
		
		activity.finish();
		
	}
	/**
	 * The onClick method for adding a claim
	 * @param activity The activity which holds the add claim button
	 */
	public static void onAddClaimClick(ClaimantClaimsListActivity activity) {
		ClaimListController.addClaim(new Claim());
		Intent intent = new Intent(activity, EditClaimActivity.class);
		activity.startActivity(intent);
		
	}
	/**
	 * The onClick method for adding a destination/reason pair to a claim
	 * @param activity The activity containing the add destination/reason button
	 */
	public static void onAddDestinationClick(EditClaimActivity activity) {
		EditText destination = (EditText) activity.findViewById(R.id.claimDestinationBody);
		EditText reason      = (EditText) activity.findViewById(R.id.claimReasonBody);
		Claim claim = ClaimListController.getCurrentClaim();
		Map<String, String> drlist = claim.getDestinationReasonList();
		drlist.put(destination.getText().toString(), reason.getText().toString());
		destination.setText("");
		reason.setText("");
	}
	/**
	 * The getter for the currently selected claim
	 * @return Returns the current claim selected
	 */
	public static Claim getCurrentClaim() {
		return currentClaim;
	}
	/**
	 * Sets the current user 
	 * @param theUser The user that is to be set as the current user
	 */
	public static void setUser(User theUser) {
		user = theUser; 
	}
	/**
	 * Get the current user
	 * @return The current user
	 */
	public static User getUser() {
		return user; 
	}
	/**
	 * Get the number of claims
	 * @return The number of claims
	 */
	public static int getClaimCount() {
		return claimsList.getClaims().size();
	}
	/**
	 * Allows a claim to be added to the claims list
	 * @param claim The claim to be added
	 */
	public static void addClaim(Claim claim) {
		ArrayList<Claim> claimArray=getClaimList().getClaims();
		setCurrentClaim(claim);
		claimArray.add(claim);
		//displays an empty claim in claim list 
		claimsList.setClaimList(claimArray);
		
	}
	/**
	 * Get the claims that are submitted
	 * @return returns a list of the currently submitted claims
	 */
	public static ClaimList getSubmittedClaims() {
		// TODO Auto-generated method stub
		ClaimList submittedclaims = new ClaimList();
		
		for (Claim item: getClaimList().getClaims()) {
			
			if ((item.status.equals(Claim.Status.submitted))) {
				submittedclaims.addClaim(item);
			}
		}
		return submittedclaims;
	}
	/**
	 * clears the claims list
	 */
	public static void clearClaims() {
		claimsList = null;
		getClaimList();
		
	}
	/**
	 * The onClick method for deleting a claim
	 */
	public static void onRemoveClaimClick() {
		
		if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted && ClaimListController.getCurrentClaim().getStatus() != Status.approved){
		
			ArrayList<Claim> claims = getClaimList().getClaims();
			claims.remove(currentClaim);
			claimsList.setClaimList(claims);
		}
		
	}
	/**
	 * The onClick method for the approve button
	 * Sets the claims status to approved, and adds the user to the list of approvers
	 */
	public static void onApproveClick() {
		// denote the claim status as approved and set approver
		//name as the approver for the expense claim.
		Claim approvedClaim =getCurrentClaim();		
		approvedClaim.setStatus(Status.approved);
		
		ArrayList<User> approverList = approvedClaim.getApproverList();
		approverList.add(user);
		approvedClaim.setApproverList(approverList);
		ClaimListController.updateCurrentClaim(approvedClaim);
		
		
	}
	/**
	 * The onClick method for the return button
	 * Sets the claim status as returned and adds user to approver list
	 */
	public static void onReturnClick() {
		currentClaim.setStatus(Status.returned);
		currentClaim.getApproverList().add(user);
		currentClaim.setApproverList(currentClaim.getApproverList());
	}
	/**
	 * The onClick method for the comment button
	 * Adds a comment to a claim
	 * @param comment The comment to be added
	 */
	public static void onCommentClick(String comment) {
		currentClaim.getCommentList().put(user.getName(), comment);
	}
	/**
	 * Save the data to the elastic server
	 */
	public static void SaveToOnline() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * loads the data from the elastic server
	 * @return
	 */
	public static ClaimList LoadFromOnline() {
		// TODO Auto-generated method stub
		return null;
	}
	
    /**
     * Reset the claimList to a new claimList removing all it's old contents.
     */
    public static void clearClaimList(){
    	claimsList=new ClaimList();
    }
	
}
