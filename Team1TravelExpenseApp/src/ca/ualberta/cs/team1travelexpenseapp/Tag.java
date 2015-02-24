package ca.ualberta.cs.team1travelexpenseapp;

public class Tag {
	private String tagName;
	
	public Tag(String name){
		tagName=name;
	}
	
	@Override
	public String toString() {
		return tagName;
	}
	
	public void rename(String newName){
		this.tagName=newName;
	}
}
