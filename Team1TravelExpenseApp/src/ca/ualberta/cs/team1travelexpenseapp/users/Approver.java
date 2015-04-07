package ca.ualberta.cs.team1travelexpenseapp.users;

import android.content.Context;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import dataManagers.ApproverClaimListManager;
import dataManagers.ClaimListManager;

/**
 * Approver is the base class for initializing managers and loading the claims
 * from the web server.
 * 
 * @since 1.0
 */
public class Approver extends User {
	/**
	 * Initializes an Approver object
	 * 
	 * @param name
	 *            the name of the approver
	 */
	public Approver(String name) {
		super(name);
		this.claimList = new ClaimList(this);
	}

	/**
	 * Loads claim data from the server
	 */
	public void loadData() {
		claimList.loadClaims();
	}

	/**
	 * Initializes the approver's claim list manager
	 * 
	 * @param context
	 *            context of the application
	 */
	public void initManagers(Context context) {
		ApproverClaimListManager approverClaimListManager = (ApproverClaimListManager) getClaimList()
				.getManager();
		approverClaimListManager.setContext(context);
	}

}
