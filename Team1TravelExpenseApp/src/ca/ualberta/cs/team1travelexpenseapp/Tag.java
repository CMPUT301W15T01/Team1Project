package ca.ualberta.cs.team1travelexpenseapp;

public class Tag {
	private String name;
	
	public Tag(String name){
		name=name;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	public void setName(String newName){
		this.name=newName;
	}
}
