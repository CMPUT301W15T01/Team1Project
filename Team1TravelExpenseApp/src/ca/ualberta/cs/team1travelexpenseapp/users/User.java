/*
Copyright 2015 Jeffrey Oduro, Cody Ingram, Boyan Peychoff, Kenny Young, Dennis Truong, Victor Olivares 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package ca.ualberta.cs.team1travelexpenseapp.users;

import android.location.Location;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;

/**
 * An abstract Class representing a user that can add and approve claims, has a
 * name and a type which should be either claimant or approver
 */
public abstract class User {

	protected String name;
	transient protected ClaimList claimList;
	protected Location location;

	/**
	 * Gets the location of the user
	 * 
	 * @return location android.location of the user
	 */
	public Location getLocation() {
		if (location == null) {
			location = new Location("");
		}
		return location;
	}

	/**
	 * Name of the user
	 * 
	 * @return name String of the user's name
	 */
	public String toString() {
		return name;
	}

	/**
	 * Sets the location of the user
	 * 
	 * @param location
	 *            object of the user
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/*
	 * Retrieves the claim list of the user
	 * 
	 * @return claimList object
	 */
	public ClaimList getClaimList() {
		return claimList;
	}

	/**
	 * Sets the claims list object of the user
	 * 
	 * @param claimList
	 *            object
	 */
	public void setClaimList(ClaimList claimList) {
		this.claimList = claimList;
	}

	/**
	 * Create the user with a passed name and type
	 * 
	 * @param type
	 *            A String representing type of the user (claimant or approver)
	 * @param name
	 *            A String representing the name of the user
	 */
	public User(String name) {
		// TODO Auto-generated constructor stub
		this.name = name;
	}

	/**
	 * Return the name of the User
	 * 
	 * @return name of the current User
	 */
	public String getName() {
		return this.name;
	}

	abstract public void loadData();

}
