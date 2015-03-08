package ca.ualberta.cs.team1travelexpenseapp;

public class User {
	
	protected String type = null;
	private String name = null;

	public User(String type, String name) {
		// TODO Auto-generated constructor stub
		
		this.type = type;
		this.name = name;
	}

	public String type() {
		// TODO Auto-generated method stub
		return type;
	}

	public static Claim getClaim(int i) {
		// TODO Auto-generated method stub
		return new Claim();
	}

	public static User getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean loggedin() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public String getName(){
		return this.name;
	}

}
