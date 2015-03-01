package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;



public class ClaimsListController {
	
	protected static ClaimList claimsList = null;
	protected static ClaimList displayedClaimList = null;
	protected static Claim currentClaim = null;
	protected static User user = null;
	
	public static ClaimList getClaims() { 
		if (claimsList == null) {
			claimsList = new ClaimList();
		}
		
		return claimsList;
	}
	
	public static ClaimList getDisplayedClaims() {
		if (displayedClaimList == null) {
			displayedClaimList = getClaims();
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
	
	public static void setCurrentClaim(Claim claim) {
		currentClaim = claim;
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
		// TODO Auto-generated method stub
		return 0;
	}

	public static void addClaim(Claim claim5) {
		// TODO Auto-generated method stub
		
	}

	public static Claim getSubmittedClaim(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void clearClaims() {
		claimsList = null;
		getClaims();
		
	}

	public static Claim getReturnedClaim(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Claim getApprovedClaim(int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
