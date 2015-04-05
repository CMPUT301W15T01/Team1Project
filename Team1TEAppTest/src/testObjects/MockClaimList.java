package testObjects;

import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.users.Approver;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;

public class MockClaimList extends ClaimList {
	public MockClaimList(Approver approver) {
		super(approver);
		// TODO Auto-generated constructor stub
	}
	
	public MockClaimList(Claimant claimant) {
		super(claimant);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveClaims(){
		//do nothing
	}
	
	@Override
	public void loadClaims(){
		//do nothing
	}
}
