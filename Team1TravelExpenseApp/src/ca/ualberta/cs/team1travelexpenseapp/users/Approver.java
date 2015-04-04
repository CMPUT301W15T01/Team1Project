package ca.ualberta.cs.team1travelexpenseapp.users;

import android.content.Context;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import dataManagers.ApproverClaimListManager;

public class Approver extends User {
	public Approver(String name){
		super(name);
		this.claimList=new ClaimList(this);
	}
	
	public void loadData(){
		claimList.loadClaims();
	}
	
	public void initManagers(Context context){
		ApproverClaimListManager approverClaimListManager = (ApproverClaimListManager) getClaimList().getManager();
		approverClaimListManager.setContext(context);
	}
	
	
}
