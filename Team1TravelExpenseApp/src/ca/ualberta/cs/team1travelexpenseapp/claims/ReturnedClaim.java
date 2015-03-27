package ca.ualberta.cs.team1travelexpenseapp.claims;

import ca.ualberta.cs.team1travelexpenseapp.adapter.ClaimAdapter;

public class ReturnedClaim extends ProgressClaim {

	@Override
	public BasicClaim changeStatus(Class<?> claimStatusType) {
		return new ClaimAdapter<ReturnedClaim>(this, claimStatusType);
	}
}
