package ca.ualberta.cs.team1travelexpenseapp.claims;

public interface ClaimStatus {
	
	public BasicClaim changeStatus(Class<?> claimStatusType);
	public Class<?> getStatus();
	public void setStatus(Class<?> claimStatusType);
}
