package ca.ualberta.cs.team1travelexpenseapp.claims;

public class ReturnedClaim extends ProgressClaim {

	private Claim claim = null;
	
	public ReturnedClaim(Claim claim) {
		this.claim = claim;
	}
	
	public void setClaim(Claim newClaim) {
		this.claim = newClaim;
	}
	
	public Claim getClaim() {
		return this.claim;
	}
	
}
