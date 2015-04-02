package ca.ualberta.cs.team1travelexpenseapp.claims;

import ca.ualberta.cs.team1travelexpenseapp.adapter.ClaimAdapter;

public class ApprovedClaim extends Claim {
	
	ApprovedClaim(){
		super();
	}

	@Override
	public Claim changeStatus(Class<?> claimStatusType) {
		return new ClaimAdapter<ApprovedClaim>(this, claimStatusType);
	}

}
