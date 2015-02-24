package ca.ualberta.cs.team1travelexpenseapp;

public class User {
	
	protected String type = null;

	public User(String string) {
		// TODO Auto-generated constructor stub
		type = string;
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

}
