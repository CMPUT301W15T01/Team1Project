package ca.ualberta.cs.team1travelexpenseapp.claims;

public class ReturnedClaim extends Claim {

	private Claim claim = null;
	
	public ReturnedClaim(Claim claim) {
		super();
		this.claim = claim;
	}
	
	public void setClaim(Claim newClaim) {
		this.claim = newClaim;
	}
	
	public Claim getClaim() {
		return this.claim;
	}
	
}
