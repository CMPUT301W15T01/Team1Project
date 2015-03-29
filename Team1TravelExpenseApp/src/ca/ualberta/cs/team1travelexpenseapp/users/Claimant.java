package ca.ualberta.cs.team1travelexpenseapp.users;

import ca.ualberta.cs.team1travelexpenseapp.TagList;

public class Claimant extends User {
	private TagList tagList;
	
	
	public Claimant(String name){
		super(name);
		this.tagList= new TagList();
	}


	public TagList getTagList() {
		return tagList;
	}


	public void setTagList(TagList tagList) {
		this.tagList = tagList;
	}
	
}
