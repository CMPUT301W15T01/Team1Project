package testObjects;

import android.location.Location;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.TagList;
import ca.ualberta.cs.team1travelexpenseapp.users.Approver;

public class MockApprover extends Approver {

	public MockApprover(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}


	@Override
		public void loadData(){
			//do nothing
		}
	 
	 public void clearData(){
		 claimList = new ClaimList(this);
	 }
	 
}
