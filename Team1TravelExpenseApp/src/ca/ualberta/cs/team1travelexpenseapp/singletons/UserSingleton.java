package ca.ualberta.cs.team1travelexpenseapp.singletons;

import ca.ualberta.cs.team1travelexpenseapp.users.Approver;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class UserSingleton {
	private static UserSingleton userSingleton = null;
	private User user;
	private String userType;

	private UserSingleton() {

	}

	/**
	 * Initializes singleton.
	 *
	 * UserSingleton is loaded on the first execution of
	 * UserSingleton.getUserSingleton() or the first access to
	 * UserSingleton.INSTANCE, not before.
	 */
	public static UserSingleton getUserSingleton() {
		if (userSingleton == null) {
			userSingleton = new UserSingleton();
		}
		return userSingleton;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
		this.userType = "user";
		if (user.getClass() == Claimant.class) {
			this.userType = "Claimant";
		}
		if (user.getClass() == Approver.class) {
			this.userType = "Approver";
		}
	}

	public String getUserType() {
		return userType;
	}
}
