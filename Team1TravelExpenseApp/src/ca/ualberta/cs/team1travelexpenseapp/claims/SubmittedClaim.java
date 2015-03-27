package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.User;
import ca.ualberta.cs.team1travelexpenseapp.UserSingleton;

public class SubmittedClaim extends AbstractClaim {

	public SubmittedClaim() {
		super();
		status = "submitted";
	}

	public SubmittedClaim(String cName, Date sDate, Date eDate) {
		super(cName, sDate, eDate);
		status = "submitted";
	}
	
	/**
	 * Set the list of approvers for the current Claim
	 * @param approverList ArrayList of Users corresponding to the approvers who have returned or approved the claim
	 */
	public void setApproverList(ArrayList<User> approverList) {
		this.approverList = approverList;
	}
	
	/**
	 * Add a comment to the claim from the current User.
	 * @param comment String to be added as comment.
	 */
	public void addComment(String comment) {
		commentList.put(UserSingleton.getUserSingleton().getUser().getName(), comment);
	}

}
