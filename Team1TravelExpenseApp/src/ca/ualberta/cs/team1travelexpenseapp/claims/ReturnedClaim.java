package ca.ualberta.cs.team1travelexpenseapp.claims;
/**
 * A claim with the status of returned.
 * See claim for function descriptions
 *
 */
public class ReturnedClaim extends ProgressClaim {
	
	public ReturnedClaim(Claim claim) {
		super.setClaim(claim);
		setSynced(false);
	}
	
	public void setClaim(Claim newClaim) {
		super.setClaim(newClaim);
		setSynced(false);
	}
	
	public Claim getClaim() {
		return super.getClaim();
	}
	
	@Override
	public boolean isSubmittable() {
		return true;
	}
	
	@Override
	public String getStatusString(){
		return "returned";
	}
	
}
