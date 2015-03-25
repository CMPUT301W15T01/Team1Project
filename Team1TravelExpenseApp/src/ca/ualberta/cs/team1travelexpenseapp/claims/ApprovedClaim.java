package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.util.Date;

public class ApprovedClaim extends SubmittedClaim {

	public ApprovedClaim() {
		super();
		status = "approved";
	}

	public ApprovedClaim(String cName, Date sDate, Date eDate) {
		super(cName, sDate, eDate);
		status = "approved";
	}
	
	

}
