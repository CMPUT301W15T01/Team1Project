package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ca.ualberta.cs.team1travelexpenseapp.Destination;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.users.User;
/**
 * The approved claim is a claim with status of approved.
 * See claim for function descriptions
 *
 */
public class ApprovedClaim extends Claim {
	

	private Claim claim = null;

	public ApprovedClaim(Claim claim) {
		this.claim = claim;
		setSynced(false);
	}
	
	@Override
	public UUID getUniqueId(){
		return claim.getUniqueId();
	}
	
	@Override
	public ExpenseList getExpenseList() {
		return claim.getExpenseList();
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
	protected int getTagCount() {
		return claim.getTagCount();
	}
	
	@Override
	public Map<String, String> getCommentList() {
		return claim.getCommentList();
	}
	
	@Override
	public ArrayList<String> getClaimTagNameList() {
		return claim.getClaimTagNameList();
	}
	
	@Override
	public Date getStartDate() {
		return claim.getStartDate();
	}
	
	@Override
	public Date getEndDate() {
		return claim.getEndDate();
	}
	
	@Override
	public ArrayList<String> getApproverList() {
		return claim.getApproverList();
	}
	
	@Override
	public void setApproverList(ArrayList<String> approverList) {
		claim.setApproverList(approverList);
		setSynced(false);
	}
	
	@Override
	public ArrayList<Destination> getDestinationList() {
		return claim.getDestinationList();
	}
	
	@Override
	public Class<?> getStatus() {
		return SubmittedClaim.class;
	}
	
	public void setClaim(Claim newClaim) {
		this.claim = newClaim;
		setSynced(false);
	}
	
	public Claim getClaim() {
		return this.claim;
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
	public boolean isSubmittable() {
		return false;
	}
	
	@Override
	public String getStatusString(){
		return "approved";
	}
}
