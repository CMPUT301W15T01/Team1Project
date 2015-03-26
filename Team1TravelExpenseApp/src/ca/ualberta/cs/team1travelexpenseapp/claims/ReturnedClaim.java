package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.util.Date;

public class ReturnedClaim extends AbstractClaim {

	public ReturnedClaim() {
		super();
		status = "returned";
	}

	public ReturnedClaim(String cName, Date sDate, Date eDate) {
		super(cName, sDate, eDate);
		status = "returned";
		
	}
	
}
