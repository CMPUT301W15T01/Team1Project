package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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
		currentClaim.setStatus(2);
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
		
		
		ClaimListController.updateCurrentClaim(new Claim(nameText, fromDate, endDate));
		
		
		Intent intent = new Intent(activity, ClaimantClaimsListActivity.class);
		activity.startActivity(intent);
		
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
		destination.setText(" ");
		reason.setText(" ");
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
		getClaimList();
		currentClaim = claim;
		claimsList.addClaim(currentClaim);
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

	public static Claim getApprovedClaim(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void onRemoveClaimClick() {
		ArrayList<Claim> claims = getClaimList().getClaims();
		claims.remove(currentClaim);
		claimsList.setClaimList(claims);
		
	}
	
}
