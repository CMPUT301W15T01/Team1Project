package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

/**
 * warning 
 * skeletal code
 */

//TODO create code 

public class ClaimsListController {
	
	protected static ArrayList<Claim> claimsList = null;
	
	public static ArrayList<Claim> getClaims() { 
		if (claimsList == null) {
			claimsList = new ArrayList<Claim>();
		}
		
		return claimsList;
	}
	
	public static Claim getClaim(int i) {
		// TODO Auto-generated method stub
		return getClaims().get(i);
	}

	public static int getClaimCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public static void addClaim(Claim claim5) {
		// TODO Auto-generated method stub
		
	}

	public static void clearClaims() {
		claimsList = null;
		getClaims();
	}

}
