package ca.ualberta.cs.team1travelexpenseapp.users;

import ca.ualberta.cs.team1travelexpenseapp.ClaimList;

public class Approver extends User {
	public Approver(String name){
		super(name);
		this.claimList=new ClaimList(this);
	}
	
	
}
