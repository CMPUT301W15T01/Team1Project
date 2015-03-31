package ca.ualberta.cs.team1travelexpenseapp.claims;


import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.adapter.ClaimAdapter;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class SubmittedClaim extends Claim {

	public SubmittedClaim() {
		super();
		setStatus(SubmittedClaim.class);;
	}

	public SubmittedClaim(String cName, Date sDate, Date eDate) {
		super(cName, sDate, eDate);
		setStatus(SubmittedClaim.class);
		//if we make it this way then a submitted claim never gets the destination list
	}
	
	/**
	 * Set the list of approvers for the current Claim
	 * @param approverList ArrayList of Users corresponding to the approvers who have returned or approved the claim
	 */
	public void setApproverList(ArrayList<User> approverList) {
		approverList = approverList;
	}
	
	/**
	 * Add a comment to the claim from the current User.
	 * @param comment String to be added as comment.
	 */
	public void addComment(String comment) {
		commentList.put(UserSingleton.getUserSingleton().getUser().getName(), comment);
	}
	
	@Override
	public Claim changeStatus(Class<?> claimStatusType) {
		return new ClaimAdapter<SubmittedClaim>(this, claimStatusType);
	}

}
