package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cs.team1travelexpenseapp.Destination;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

/**
 * 
 * @author volivare
 * interface for methods a Claim must have
 *
 */
public interface ClaimInfo {
	
	public UUID getUniqueId();

	public boolean isSynced();

	public void setSynced(boolean synced);
	
	/** 	 
	 * returns a exenepseList object that contains 
	 * list of expenses for the claim 
	 * @param 
	 * @return ExpenseList object **/
	public ExpenseList getExpenseList();

	
	/** 
	 * sets the claim's expense list object to a given expenseList
	 * @param expenseList
	 */
	public void setExpenseList(ExpenseList expenseList);
	
	/**
	 * adds a destination, with a reason to the claim 
	 * if destination already exist, new reason will write over old reason 
	else new destination will reason will be added to the Map 
	 * @param destination - a string
	 * @param reason - a string 
	 */
	public void addDestination(Destination destination);
	
	/**
	 * Returns a HashMap with destinations as keys and reasons as values.
	 * @return HashMap of destinations (String) mapped to reasons (String).
	 */
	public ArrayList<Destination> getDestinationList();
	
	
	/**
	 * Set the claimantName for the Claim.
	 * @param name The name of the claimant for the Claim.
	 */
	public void setClaimantName(String name);

	/**
	 * Get the claimantName for the Claim.
	 * @return name The name of the claimant for the Claim.
	 */
	public String getClaimantName();

	/**
	 * Get the ArrayList of Tags claimTagList for the claim.
	 * @return ArrayList of Tags containing the tags set for the claim.
	 */
	public ArrayList<Tag> getClaimTagList();
	
	/**
	 * Get a list of the names of all the tags attached to the claim
	 * @return
	 */
	public ArrayList<String> getClaimTagNameList();

	/**
	 * Set the TagList for the claim
	 * @param claimTagList The TagList containing the new tags to be set for the claim.
	 */
	public void setClaimTagList(ArrayList<Tag> claimTagList);

	/**
	 * Return a boolean indicating whether the claim is "complete".
	 * @return boolean indicating whether claim is complete.
	 */
	public boolean isComplete();

	/**
	 * Set the completeness of the claim.
	 * @param isComplete true or false, is the claim complete?
	 */
	public void setComplete(boolean isComplete);

	/**
	 * Get the list of approvers for the current Claim
	 * @return ArrayList of Users corresponding to the approvers who have returned or approved the claim
	 */
	public ArrayList<User> getApproverList();

	/**
	 * Set the list of approvers for the current Claim
	 * @param approverList ArrayList of Users corresponding to the approvers who have returned or approved the claim
	 */
	public void setApproverList(ArrayList<User> approverList);

	/**
	 * Get a Map mapping approvers of the claim to any comments they may have left.
	 * @return Map from approver name (String) to that approver's comments (String).
	 */
	public Map<String, String> getCommentList();
	
	/**
	 * Add a comment to the claim from the current User.
	 * @param comment String to be added as comment.
	 */
	public void addComment(String comment);

	/**
	 * Return the startDate for the Claim.
	 * @return startDate (Date) of the Claim.
	 */
	public Date getStartDate();
	
	/**
	 * Set the startDate of the Claim.
	 * @param date startDate to be set for the current Claim.
	 */
	public void setStartDate(Date date);
	
	/**
	 * Return the endDate for the Claim.
	 * @return endDate (Date) of the Claim.
	 */
	public Date getEndDate();
	
	/**
	 * Set the endDate of the Claim.
	 * @param date endDate to be set for the current Claim.
	 */
	public void setEndDate(Date date);
	
	/**
	 * Return a map from currency types (String) to amounts (BigDecimal) of the currency spent in the expenses of the Claim.
	 * @return Map from currency types (String) to amount of that currency spent (BigDecimal).
	 */
	public Map<String, BigDecimal> getCurrencyTotals();
	
	public String getCurrencyTotal(String currency);
	
	/**
	 * Return a String representation of the information contained in the claim to be printed to a list item or similar.
	 */
	public String toString();

	//sorting
	//https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html 03/24/15 
	/**
	 * override comapreTo method so that claims will be sorted by largest to smallest date 
	 * for a Claimant user and oldest to newest for an Approver user. Note only two types of users exists.
	 * use Collections.sort(ArrayList<Claim> Object); to sort Object. 
	 */
	public int compareTo( Claim claim );

	/**
	 * Get the status (inProgress, submitted, approved, returned) for the claim.
	 * @return Status for the claim.
	 */
	public Class<?> getStatus();
	
	public String getStatusString();

	public Claim changeStatus(Class<?> claimStatusType);

	public boolean isSubmittable();
	
	

}
