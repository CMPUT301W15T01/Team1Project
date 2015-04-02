package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class ProgressClaim extends Claim {
	
	private Claim claim = null;
	
	/** Initializes attributes to new instances **/
	public ProgressClaim() { 
		claim = new Claim();
	}
	
	public ProgressClaim(Claim claim){
		this.claim = claim;
	}

	/** set claimant name, start and end date, all other attributes are initializes to new instances 
	 * @param cName - a string
	 * @param sDate - a Date
	 * @param eDate - a Date **/
	public ProgressClaim(String cName, Date sDate, Date eDate) {
		claim = new Claim(cName, sDate, eDate);
	}

	@Override
	public UUID getUniqueId() {
		return claim.getUniqueId();
	}

	@Override
	public boolean isSynced() {
		return claim.isSynced();
	}

	@Override
	public void setSynced(boolean synced) {
		claim.setSynced(synced);
	}

	@Override
	public ExpenseList getExpenseList() {
		return claim.getExpenseList();
	}

	@Override
	public void setExpenseList(ExpenseList expenseList) {
		claim.setExpenseList(expenseList);
	}

	@Override
	public void addDestination(String destination, String reason) {
		claim.addDestination(destination, reason);
	}

	@Override
	public String getReason(String destination) {
		return claim.getReason(destination);
	}

	@Override
	public HashMap<String, String> getDestinationReasonList() {
		return claim.getDestinationReasonList();
	}

	@Override
	public Set<String> getDestinations() {
		return claim.getDestinations();
	}

	@Override
	public void setClaimantName(String name) {
		claim.setClaimantName(name);
	}

	@Override
	public String getClaimantName() {
		return claim.getClaimantName();
	}

	@Override
	public ArrayList<Tag> getClaimTagList() {
		return claim.getClaimTagList();
	}

	@Override
	public ArrayList<String> getClaimTagNameList() {
		return claim.getClaimTagNameList();
	}

	@Override
	public void setClaimTagList(ArrayList<Tag> claimTagList) {
		claim.setClaimTagList(claimTagList);
	}

	@Override
	public boolean isComplete() {
		return claim.isComplete();
	}

	@Override
	public void setComplete(boolean isComplete) {
		claim.setComplete(isComplete);
	}

	@Override
	public ArrayList<User> getApproverList() {
		return claim.getApproverList();
	}
	
	@Override
	public Date getStartDate() {
		return claim.getStartDate();
	}

	@Override
	public void setStartDate(Date date) {
		claim.setStartDate(date);
	}

	@Override
	public Date getEndDate() {
		return claim.getEndDate();
	}

	@Override
	public void setEndDate(Date date) {
		claim.setEndDate(date);
	}

	@Override
	public void setApproverList(ArrayList<User> approverList) {
		throw new RuntimeException("Unable to set approver list for inprogress claims!!!");
	}

	@Override
	public Map<String, String> getCommentList() {
		return claim.getCommentList();
	}

	@Override
	public void addComment(String comment) {
		throw new RuntimeException("Unable to add comments for inprogress claims!!!");
	}

	@Override
	public int compareTo( Claim claim ) {
		if (UserSingleton.getUserSingleton().getUserType().isInstance(Claimant.class)) {
			return claim.getStartDate().compareTo(this.claim.getStartDate());
		}
		return this.claim.getStartDate().compareTo(claim.getStartDate());
	}
	
	@Override
	public Class<?> getStatus() {
		return ProgressClaim.class;
	}
	
	@Override
	public String getStatusString(){
		return "inProgress";
	}

	@Override
	public Claim changeStatus(Class<?> claimStatusType) {
		if (claimStatusType == SubmittedClaim.class) {
			return new SubmittedClaim(claim);
		} else { 
			throw new RuntimeException("In progress claims can only change status to submitted!!!");
		}
	}

	@Override
	public boolean isSubmittable() {
		return true;
	}

}
