package ca.ualberta.cs.team1travelexpenseapp.claims;

public class ReturnedClaim extends ProgressClaim {
	
	ReturnedClaim(){
		super();
	}

	private Claim claim = null;
	
	public ReturnedClaim(Claim claim) {
		this.claim = claim;
	}
	
}
