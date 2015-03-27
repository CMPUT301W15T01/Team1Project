package ca.ualberta.cs.team1travelexpenseapp;

public class Claimant extends User {
	private TagList tagList;
	
	
	Claimant(String name){
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
