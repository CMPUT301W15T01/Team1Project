package ca.ualberta.cs.team1travelexpenseapp.claims;

import ca.ualberta.cs.team1travelexpenseapp.adapter.ClaimAdapter;

public class ApprovedClaim extends BasicClaim {

	@Override
	public BasicClaim changeStatus(Class<?> claimStatusType) {
		return new ClaimAdapter<ApprovedClaim>(this, claimStatusType);
	}

}
