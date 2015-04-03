package ca.ualberta.cs.team1travelexpenseapp.claims;

public class ApprovedClaim extends Claim {
	

	private Claim claim = null;

	public ApprovedClaim(Claim claim) {
		this.claim = claim;
	}
	
	public void setClaim(Claim newClaim) {
		this.claim = newClaim;
	}
	
	public Claim getClaim() {
		return this.claim;
	}
	
	@Override
	public boolean isSubmittable() {
		return false;
	}
	
}
