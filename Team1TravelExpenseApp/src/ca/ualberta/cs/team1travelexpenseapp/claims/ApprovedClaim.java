package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.util.UUID;

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
	
	public void setClaim(Claim newClaim) {
		this.claim = newClaim;
		setSynced(false);
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
	public boolean isSubmittable() {
		return false;
	}
	
	@Override
	public String getStatusString(){
		return "approved";
	}
}
