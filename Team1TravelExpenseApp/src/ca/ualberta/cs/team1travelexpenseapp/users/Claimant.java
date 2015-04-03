package ca.ualberta.cs.team1travelexpenseapp.users;

import android.content.Context;
import android.location.Location;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.TagList;
import dataManagers.ClaimantClaimListManager;
import dataManagers.TagListManager;

public class Claimant extends User {
	private TagList tagList;
	private Location location;
	
	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public Claimant(String name){
		super(name);
		this.claimList=new ClaimList(this);
		this.tagList= new TagList();
	}


	public TagList getTagList() {
		return tagList;
	}


	public void setTagList(TagList tagList) {
		this.tagList = tagList;
	}
	
	public void loadData(){
		claimList.loadClaims();
		tagList.loadTags();
		claimList.syncTags(tagList);
	}
	
	public void initManagers(Context context){
		TagListManager tagListManager=getTagList().getManager();
		tagListManager.setContext(context);
		tagListManager.setClaimantName(this.name);
		
		
		ClaimantClaimListManager claimantClaimListManager= (ClaimantClaimListManager) getClaimList().getManager();
		claimantClaimListManager.setContext(context);
		claimantClaimListManager.setClaimantName(this.name);
	}
	
}
