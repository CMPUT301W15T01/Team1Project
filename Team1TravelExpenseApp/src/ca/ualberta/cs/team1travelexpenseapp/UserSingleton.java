package ca.ualberta.cs.team1travelexpenseapp;

import ca.ualberta.cs.team1travelexpenseapp.users.User;

public class UserSingleton {
	private static UserSingleton userSingleton=null;
	private User user;
	private Class<?> userType;
	
	private UserSingleton(){
		
	}
	
	public static UserSingleton getUserSingleton(){
		if(userSingleton==null){
			userSingleton=new UserSingleton();
		}
		return userSingleton;
	}
	
	public User getUser(){
		return this.user;
	}
	
	public void setUser(User user){
		this.user=user;
		this.userType = user.getClass();
	}
	
	public Class<?> getUserType() {
		return userType;
	}
}
