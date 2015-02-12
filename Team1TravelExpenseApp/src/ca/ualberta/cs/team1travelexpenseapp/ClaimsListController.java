package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

/**
 * warning 
 * skeletal code
 */

public class ClaimsListController {
	
	protected static ArrayList<Claim> claimsList = null;
	
	public static ArrayList<Claim> getClaims() {
		if (claimsList == null) {
			claimsList = new ArrayList<Claim>();
		}
		
		return claimsList;
	}
	
	public static Claim getClaim(int i) {
		return ClaimsListController.getClaims().get(i);
	}

}
