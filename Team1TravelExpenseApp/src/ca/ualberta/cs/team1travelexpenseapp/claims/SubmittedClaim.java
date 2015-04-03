package ca.ualberta.cs.team1travelexpenseapp.claims;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cs.team1travelexpenseapp.Destination;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class SubmittedClaim extends Claim {
	
	Claim claim = null;

	public SubmittedClaim() {
		claim = new Claim();
	}

	public SubmittedClaim(String cName, Date sDate, Date eDate) {
		claim = new Claim();
		//if we make it this way then a submitted claim never gets the destination list
	}
	
	public SubmittedClaim(Claim claim) {
		this.claim = claim;
	}
	
	public void setClaim(Claim newClaim) {
		this.claim = newClaim;
	}
	
	public Claim getClaim() {
		return this.claim;
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
		throw new RuntimeException("Submitted Claims can not set expense list!!!");
	}

	@Override
	public void addDestination(Destination destination) {
		throw new RuntimeException("Submitted Claims can not add destinations!!!");
	}

	@Override
	public ArrayList<Destination> getDestinationList() {
		return claim.getDestinationList();
	}

	@Override
	public void setClaimantName(String name) {
		throw new RuntimeException("Submitted Claims can not set claimant name!!!");
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
	public void setApproverList(ArrayList<User> approverList) {
		claim.setApproverList(approverList);
	}
	
	@Override
	public Date getStartDate() {
		return claim.getStartDate();
	}

	@Override
	public void setStartDate(Date date) {
		throw new RuntimeException("submitted claims can not set start date!!!");
	}

	@Override
	public Date getEndDate() {
		return claim.getEndDate();
	}

	@Override
	public void setEndDate(Date date) {
		throw new RuntimeException("submitted claims can not set end date!!!");
	}

	@Override
	public Map<String, String> getCommentList() {
		return claim.getCommentList();
	}

	@Override
	public void addComment(String comment) {
		claim.addComment(comment);
	}

	@Override
	public int compareTo( Claim claim ) {
		if (UserSingleton.getUserSingleton().getUserType().equals("Claimant")) {
			return claim.getStartDate().compareTo(this.claim.getStartDate());
		}
		return this.claim.getStartDate().compareTo(claim.getStartDate());
	}
	
	@Override
	public Class<?> getStatus() {
		return SubmittedClaim.class;
	}
	
	@Override
	public String getStatusString(){
		return "submitted";
	}

	@Override
	public Claim changeStatus(Class<?> claimStatusType) {
		if (claimStatusType == ApprovedClaim.class) {
			return new ApprovedClaim(claim);
		} else {
			if (claimStatusType == ReturnedClaim.class)  {
				return new ReturnedClaim(claim);
			} else { 
				throw new RuntimeException("In progress claims can only change status to approved or returned!!!");
			}
		}
	}

	@Override
	public boolean isSubmittable() {
		return true;
	}

}
