package ca.ualberta.cs.team1travelexpenseapp.users;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.TagList;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import dataManagers.ClaimantClaimListManager;
import dataManagers.TagListManager;

public class Claimant extends User {
	public TagList tagList;



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
		//this is failing badly at the moment, not sure why
		//syncTags();
	}
	
	public void initManagers(Context context){
		TagListManager tagListManager=getTagList().getManager();
		tagListManager.setContext(context);
		tagListManager.setClaimantName(this.name);
		
		
		ClaimantClaimListManager claimantClaimListManager= (ClaimantClaimListManager) getClaimList().getManager();
		claimantClaimListManager.setContext(context);
		claimantClaimListManager.setClaimantName(this.name);
	}
	
	/**
	 * Match tags of the same name in claims to tags in the passed tagList.
	 * This is needed to fix saved tags of the same name which do not correspond to the same object once saved and loaded
	 * @param tagList to be matched with
	 */
	/*private void syncTags() {
		ArrayList<Tag> personalTags = tagList.getTags();
		for(Claim claim: claimList.getClaims()){
			ArrayList<Tag> oldTags = new ArrayList<Tag>();
			ArrayList<Tag> newTags = new ArrayList<Tag>();
			ArrayList<Tag> claimTagList =  claim.getClaimTagList();
			for(int claimIndex=0; claimIndex<claimTagList.size(); claimIndex++){
				boolean exists=false;
				for(int personalIndex=0; personalIndex< personalTags.size(); personalIndex++){
					if(claimTagList.get(claimIndex).getName().equals(personalTags.get(personalIndex).getName())){
						oldTags.add(claimTagList.get(claimIndex));
						newTags.add(personalTags.get(personalIndex));
						exists=true;
					}
				}
				if(!exists){
					personalTags.add(claimTagList.get(claimIndex));
				}
			}
			claimTagList.addAll(newTags);
			claimTagList.removeAll(oldTags);
			claim.setClaimTagList(claimTagList);
		}
		
	}*/
	
}
