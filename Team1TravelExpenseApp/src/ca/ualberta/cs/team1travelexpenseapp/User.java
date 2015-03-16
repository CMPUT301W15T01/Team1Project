package ca.ualberta.cs.team1travelexpenseapp;
/**
 * Class representing a user that can add and approve claims, has a name and a type which should be either claimant or approver
 */
public class User {
	
	protected String type = null;
	private String name = null;

	/**
	 * Create the user with a passed name and type
	 * @param type A String representing type of the user (claimant or approver)
	 * @param name A String representing the name of the user
	 */
	public User(String type, String name) {
		// TODO Auto-generated constructor stub
		
		this.type = type;
		this.name = name;
	}

	/**
	 * Return the current type of the user
	 * @return The current type of the user (claimant or approver)
	 */
	public String type() {
		// TODO Auto-generated method stub
		return type;
	}

	/**
	 * Will eventually return whether the user in question has logged in to the app (not yet implemented)
	 * @return boolean indicating whether the user is logged into the app
	 */
	public static boolean loggedin() {
		// TODO Auto-generated method stub
		return true;
	}
	/**
	 * Return the name of the User
	 * @return name of the current User
	 */
	public String getName(){
		return this.name;
	}

}
