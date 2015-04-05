package testObjects;

import android.location.Location;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.TagList;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;

public class MockClaimant extends Claimant {
	 public MockClaimant(String name) {
		super(name);
		this.claimList=new MockClaimList(this);
		this.tagList=new MockTagList();
	}

	@Override
		public void loadData(){
			//do nothing
		}
		
	 
	 public void clearData(){
		 tagList = new TagList();
		 claimList = new ClaimList(this);
		 location = new Location("");
	 }
	 
}
