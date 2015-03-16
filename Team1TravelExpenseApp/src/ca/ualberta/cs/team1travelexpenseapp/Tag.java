package ca.ualberta.cs.team1travelexpenseapp;
/**
 * Implements the Tags which will be added to Claims in order to provide a way of filtering specific types of Claim.
 * Very simple class which contains only a name that can be changed.
 *
 */
public class Tag {
	private String name;
	
	/**
	 * Create a new Tag with a specific name.
	 * @param name A String to be used as the name of the new tag.
	 */
	public Tag(String name){
		this.name=name;
	}
	
	/**
	 * Get the current name of the Tag.
	 * @return The name of the Tag.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * return a string representation of the Tag (currently just the name).
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	/**
	 * Change the name of the tag to newName.
	 * @param newName A String to be used as the new name for the Tag
	 */
	public void setName(String newName){
		this.name=newName;
	}
}
