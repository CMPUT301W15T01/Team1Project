package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.util.ArrayList;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.Tag;

public class ProgressClaim extends AbstractClaim {

	public ProgressClaim() {
		super();
		status = "inProgress";
	}

	public ProgressClaim(String cName, Date sDate, Date eDate) {
		super(cName, sDate, eDate);
		status = "inProgress";
	}
	
	/** 
	 * sets the claim's expense list object to a given expenseList
	 * @param expenseList
	 */
	public void setExpenseList(ExpenseList expenseList) {
		this.expenseList = expenseList;
	}
	
	/**
	 * adds a destination, with a reason to the claim 
	 * if destination already exist, new reason will write over old reason 
	else new destination will reason will be added to the Map 
	 * @param destination - a string
	 * @param reason - a string 
	 */
	public void addDestination(String destination, String reason) {
			destinationReasonList.put(destination, reason);
	}
	
	/**
	 * Set the claimantName for the Claim.
	 * @param name The name of the claimant for the Claim.
	 */
	public void setClaimantName(String name) {
		claimantName = name;
	}
	
	/**
	 * Set the TagList for the claim
	 * @param claimTagList The TagList containing the new tags to be set for the claim.
	 */
	public void setClaimTagList(ArrayList<Tag> claimTagList) {
		this.claimTagList = claimTagList;
	}
	
	/**
	 * Set the completeness of the claim.
	 * @param isComplete true or false, is the claim complete?
	 */
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}
	
	/**
	 * Set the startDate of the Claim.
	 * @param date startDate to be set for the current Claim.
	 */
	public void setStartDate(Date date) {
		startDate = date;
	}
	
	/**
	 * Set the endDate of the Claim.
	 * @param date endDate to be set for the current Claim.
	 */
	public void setEndDate(Date date) {
		endDate = date;
	}
	

}
