package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import ca.ualberta.cs.team1travelexpenseapp.Claim.Status;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;



public class ClaimListController {
	
	protected static ClaimList claimsList = null;
	protected static ClaimList displayedClaimList = null;
	protected static Claim currentClaim = null;
	protected static User user = null;
	
	public static ClaimList getClaimList() { 
		if (claimsList == null) {
			claimsList = new ClaimList();
		}
		
		return claimsList;
	}
	
	public static ClaimList getDisplayedClaims() {
		if (displayedClaimList == null) {
			displayedClaimList = getClaimList();
		}
		
		return displayedClaimList;
	}
	
	public void returnClaim() {
		//As an approver, I want to return a submitted expense claim that was not approved, 
		//denoting the claim status as returned and setting my name as the approver for the expense claim.
		currentClaim.setStatus(Status.returned);
		currentClaim.getApproverList().add(user);
		currentClaim.setApproverList(currentClaim.getApproverList());
	}
	
	public static void setDisplayedClaims(ClaimList claims) {
		displayedClaimList = claims;
	}
	
	public static void updateCurrentClaim(Claim newClaim) {
		if (currentClaim == null) {
			throw new RuntimeException("no current claim");
		}
		claimsList.updateClaim(currentClaim, newClaim);
		
		
	}
	
	public static void setCurrentClaim(Claim claim){
		currentClaim=claim;
	}
	
	public static void deleteClaim(Claim claim){
		ArrayList<Claim> claims=claimsList.getClaims();
		claims.remove(claim);
		claimsList.setClaimList(claims);
	}
	
	
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
		
		
		activity.finish();
		
	}
	
	public static void onAddClaimClick(ClaimantClaimsListActivity activity) {
		ClaimListController.addClaim(new Claim());
		Intent intent = new Intent(activity, EditClaimActivity.class);
		activity.startActivity(intent);
		
	}
	
	public static void onAddDestinationClick(EditClaimActivity activity) {
		EditText destination = (EditText) activity.findViewById(R.id.claimDestinationBody);
		EditText reason      = (EditText) activity.findViewById(R.id.claimReasonBody);
		Claim claim = ClaimListController.getCurrentClaim();
		Map<String, String> drlist = claim.getDestinationReasonList();
		drlist.put(destination.getText().toString(), reason.getText().toString());
		destination.setText("");
		reason.setText("");
	}
	
	public static Claim getCurrentClaim() {
		return currentClaim;
	}
	
	public static void setUser(User theUser) {
		user = theUser; 
	}
	
	public static User getUser() {
		return user; 
	}

	public static int getClaimCount() {
		return claimsList.getClaims().size();
	}

	public static void addClaim(Claim claim) {
		ArrayList<Claim> claimArray=getClaimList().getClaims();
		setCurrentClaim(claim);
		claimArray.add(claim);
		//displays an empty claim in claim list 
		claimsList.setClaimList(claimArray);
		
	}
	
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
	
	public static Claim getSubmittedClaim(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void clearClaims() {
		claimsList = null;
		getClaimList();
		
	}

	public static Claim getReturnedClaim(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static ArrayList<Claim> getApprovedClaimsList() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void onRemoveClaimClick() {
		
		if(ClaimListController.getCurrentClaim().getStatus()!= Status.submitted && ClaimListController.getCurrentClaim().getStatus() != Status.approved){
		
			ArrayList<Claim> claims = getClaimList().getClaims();
			claims.remove(currentClaim);
			claimsList.setClaimList(claims);
		}
		
	}

	public static void onApproveClick() {
		// denote the claim status as approved and set approver
		//name as the approver for the expense claim.
		
		currentClaim.setStatus(Status.approved);
		ArrayList<User> approverList = currentClaim.getApproverList();
		approverList.add(user);
		currentClaim.setApproverList(approverList);
	}
	
	public static void onReturnClick() {
		currentClaim.setStatus(Status.returned);
		currentClaim.getApproverList().add(user);
		currentClaim.setApproverList(currentClaim.getApproverList());
	}

	public static void onCommentClick(String comment) {
		currentClaim.getCommentList().put(user.getName(), comment);
	}

	public static void SaveToOnline() {
		// TODO Auto-generated method stub
		
	}

	public static ClaimList LoadFromOnline() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
}
