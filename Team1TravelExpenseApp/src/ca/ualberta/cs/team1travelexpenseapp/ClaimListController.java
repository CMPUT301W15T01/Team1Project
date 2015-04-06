/*
Copyright 2015 Jeffrey Oduro, Cody Ingram, Boyan Peychoff, Kenny Young, Dennis Truong, Victor Olivares 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import views.MultiSelectionSpinner;
import ca.ualberta.cs.team1travelexpenseapp.claims.ApprovedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ProgressClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.ReturnedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

/**
 * The controller allowing modifications from the ui to change the claims and claims lists
 * Allows general functionality like adding/deleting claims
 */

public class ClaimListController {
	/**
	 * The apps main list of claims
	 */
	protected ClaimList claimsList;
	
	/**
	 * The list of claims that are to be displayed in a view
	 */
	protected ClaimList displayedClaimList;
	/**
	 * The claim that is currently in use when the user selects a claim for viewing/modification
	 */
	protected Claim currentClaim;
	/**
	 * The current user of the app
	 */
	protected User user;
	/**
	 * Gets the current claims list
	 * @return returns the current claims list
	 */
	
	public ClaimListController(ClaimList claimList){
		this.claimsList=claimList;
	}
	
	public ClaimList getClaimList(){ 
		return this.claimsList;
	}
	
	/**
	 * Updates the current claim to be updated
	 * @param newClaim the claim to be updated
	 */
	public void updateCurrentClaim(Claim newClaim){
		if (currentClaim == null) {
			throw new RuntimeException("no current claim");
		}
		claimsList.updateClaim(currentClaim, newClaim);
	}
	
	public void changeClaim(Claim newClaim){
		deleteClaim(currentClaim);
		setCurrentClaim(newClaim);
		addClaim(newClaim);
	}
	
	/**
	 * Sets the current claim's status to returned and add the users name to the list of approvers
	 */
	public void returnClaim() {
		//As an approver, I want to return a submitted expense claim that was not approved, 
		//denoting the claim status as returned and setting my name as the approver for the expense claim.
		//currentClaim.setStatus(ReturnedClaim.class);
		currentClaim.getApproverList().add(user);
		currentClaim.setApproverList(currentClaim.getApproverList());
	}
	
	/**
	 * Deletes a claim
	 * @param claim The claim to be deleted
	 */
	public void deleteClaim(Claim claim){
		ArrayList<Claim> claims=claimsList.getClaims();
		claims.remove(claim);
		claimsList.getManager().removeClaim(claim);
		claimsList.setClaimList(claims);
	}
	
	/**
	 * The onClick for the submit button
	 * Shows a warning dialog if the claim or expense is incomplete
	 * Redirects to the function within the ClaimListController
	 * @param activity 
	 */

	public void onSubmitClick (final ClaimantExpenseListActivity activity) {
	

		
		boolean expensesFlag = false;
		boolean expensesComplete = true;
		for(Expense expense: getCurrentClaim().getExpenseList().getExpenses()){
			//flag check 
			if(expense.isFlagged() == true){
				expensesFlag = true;
			}
			//empty field check for expense 
			if (expense.isComplete() == false ) {
				expensesComplete = false;
			}
		}
		
		if(getCurrentClaim().isComplete() == false 
				|| expensesFlag == true || expensesComplete == false){
			
			AlertDialog.Builder submitBuilder = new AlertDialog.Builder(activity);
			submitBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {

		        	   SubmittedClaim submittedClaim = (SubmittedClaim) getCurrentClaim().changeStatus(SubmittedClaim.class);
		        	   changeClaim(submittedClaim);
		        	   
		        	   Toast.makeText(activity.getApplicationContext(),"Claim submitted", Toast.LENGTH_LONG).show();
		        	   activity.finish();
		           }
		    });
			submitBuilder.setNegativeButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(activity.getApplicationContext(), "Claim was not Submitted", Toast.LENGTH_SHORT).show();
				}
			});
			submitBuilder.setTitle("Claim may be incomplete");
			activity.submitWarningDialog=submitBuilder.create();
			activity.submitWarningDialog.show();

		}else{
			
			try{
				
				SubmittedClaim submittedClaim = (SubmittedClaim) getCurrentClaim().changeStatus(SubmittedClaim.class);
	        	changeClaim(submittedClaim);
				
				Toast.makeText(activity.getApplicationContext(),"Claim submitted", Toast.LENGTH_LONG).show();
				claimsList.setClaimList(claimsList.getClaims());
				activity.finish();
			}
			catch (Throwable RuntimeException){
				Toast.makeText(activity.getApplicationContext(),"Claim can not be submitted", Toast.LENGTH_SHORT).show();
			}
			
		}
		
			
	}
	
	
	/**
	 * The onClick method for the save button when editing/adding claims
	 * @param activity The edit claim activity containing the views
	 */
	public void onSaveClick(EditClaimActivity activity) {
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
		
		if(getCurrentClaim().isSubmittable() ){
		
				Claim newClaim=new Claim(UserSingleton.getUserSingleton().getUser().getName(), fromDate, endDate);
				newClaim.setClaimTagList(claimTags);
				updateCurrentClaim(newClaim);
				
		
		}else{
			//shouldn't this be a SubmittedClaim object?
			Claim newClaim = getCurrentClaim();
			newClaim.setClaimTagList(claimTags);
			updateCurrentClaim(newClaim);
			
		}
		
		
		
		if(!getCurrentClaim().getClaimantName().equals("")  && !getCurrentClaim().getDestinationList().isEmpty()
				&& !getCurrentClaim().getCurrencyTotals().equals("")){
			
			getCurrentClaim().setComplete(true);
			
		}
		
		activity.finish();
		
	}
	/**
	 * The onClick method for adding a claim
	 * @param activity The activity which holds the add claim button
	 */
	public void onAddClaimClick(ClaimantClaimsListActivity activity) {
		ProgressClaim claim = new ProgressClaim();
		addClaim(claim);
		SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentClaim(claim);
		Intent intent = new Intent(activity, EditClaimActivity.class);
		activity.startActivity(intent);
		
	}
	/**
	 * The onClick method for adding a destination/reason pair to a claim
	 * @param activity The activity containing the add destination/reason button
	 */
	public void onAddDestinationClick(EditClaimActivity activity, DialogInterface dialog, Location location) {
        EditText nameText = ((EditText) ((AlertDialog) dialog).findViewById(R.id.destinationNameBody));
        EditText reasonText = ((EditText) ((AlertDialog) dialog).findViewById(R.id.destinationReasonBody));
        String name=nameText.getText().toString();
        String reason = reasonText.getText().toString();
        currentClaim.addDestination(new Destination(name, reason, location));
        claimsList.setClaimList(claimsList.getClaims());
    }
	
	public void onSetDestinationClick(DialogInterface dialog, Destination dest, Location location){
		EditText nameText = ((EditText) ((AlertDialog) dialog).findViewById(R.id.destinationNameBody));
        EditText reasonText = ((EditText) ((AlertDialog) dialog).findViewById(R.id.destinationReasonBody));
        String name=nameText.getText().toString();
        String reason = reasonText.getText().toString();
        dest.setName(name);
        dest.setReason(reason);
        dest.setLocation(location);
        claimsList.setClaimList(claimsList.getClaims());
	}
	
    public void onRemoveDestinationClick(DialogInterface dialog, Destination dest) {
    	currentClaim.getDestinationList().remove(dest);
    	claimsList.setClaimList(claimsList.getClaims());
    }

	/**
	 * The getter for the currently selected claim
	 * @return Returns the current claim selected
	 */
	public Claim getCurrentClaim() {
		return currentClaim;
	}
	/**
	 * Sets the current user 
	 * @param theUser The user that is to be set as the current user
	 */
	public void setUser(User theUser) {
		user = theUser; 
	}
	/**
	 * Get the current user
	 * @return The current user
	 */
	public User getUser() {
		return user; 
	}
	/**
	 * Get the number of claims
	 * @return The number of claims
	 */
	public int getClaimCount() {
		return claimsList.getClaims().size();
	}
	/**
	 * Allows a claim to be added to the claims list
	 * @param claim The claim to be added
	 */
	public void addClaim(Claim claim) {
		ArrayList<Claim> claimArray=getClaimList().getClaims();
		setCurrentClaim(claim);
		claimArray.add(claim);
		//displays an empty claim in claim list 
		claimsList.setClaimList(claimArray);
	}
	
	public void setCurrentClaim(Claim claim) {
		currentClaim=claim;	
	}

	
	/**
	 * The onClick method for deleting a claim
	 */
	public void onRemoveClaimClick() {
		

		if(getCurrentClaim().isSubmittable()){
			deleteClaim(currentClaim);
		}
		
	}
	/**
	 * The onClick method for the approve button
	 * Sets the claims status to approved, and adds the user to the list of approvers
	 */
	public void onApproveClick() {
		// denote the claim status as approved and set approver
		//name as the approver for the expense claim.
		User currentUser = UserSingleton.getUserSingleton().getUser();
		ApprovedClaim approvedClaim = (ApprovedClaim) getCurrentClaim().changeStatus(ApprovedClaim.class);		
		//approvedClaim.setStatus(ApprovedClaim.class);
		
		ArrayList<User> approverList = approvedClaim.getApproverList();
		approverList.add(currentUser);
		approvedClaim.setApproverList(approverList);
		changeClaim(approvedClaim);
		//sync with server then update the local list to reflect the returned claim
		claimsList.saveClaims();
		claimsList.loadClaims();
	}
	/**
	 * The onClick method for the return button
	 * Sets the claim status as returned and adds user to approver list
	 */
	public void onReturnClick() {
		User currentUser = UserSingleton.getUserSingleton().getUser();
		currentClaim.getApproverList().add(currentUser);
		currentClaim.setApproverList(currentClaim.getApproverList());
		changeClaim((ReturnedClaim)currentClaim.changeStatus(ReturnedClaim.class));
		//sync with server then update the local list to reflect the returned claim
		claimsList.saveClaims();
		claimsList.loadClaims();	
	}
	/**
	 * The onClick method for the comment button
	 * Adds a comment to a claim
	 * @param comment The comment to be added
	 */
	public void onCommentClick(User approver ,String comment) {
		currentClaim.getCommentList().put(approver.getName(), comment);
	}

	
    /**
     * Reset the claimList to a new claimList removing all it's old contents.
     */
    public void clearClaimList(){
    	claimsList.getClaims().clear();
    }
	
}
