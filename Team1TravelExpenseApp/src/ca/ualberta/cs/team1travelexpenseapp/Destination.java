package ca.ualberta.cs.team1travelexpenseapp;

import android.location.Location;

/**
 * Destination is the base class model object for storing the name, reason and
 * location of the travel destination.
 * 
 */
public class Destination {
	private String name;
	private String reason;
	private Location location;

	/**
	 * @param name
	 * @param reason
	 * @param location
	 */
	public Destination(String name, String reason, Location location) {
		this.name = name;
		this.reason = reason;
		this.location = location;
	}

	/** Gets the name of destination
	 * @return name of destination
	 */
	public String getName() {
		return name;
	}

	/** Sets the name of the destination
	 * @param name of the destination
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** Gets the reason of the destination
	 * @return reason String of the destination
	 */
	public String getReason() {
		return reason;
	}

	/** Sets the reason of the destination
	 * @param reason string of the destination
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**Gets the location of the destination
	 * @return location object of the destination
	 */
	public Location getLocation() {
		return location;
	}

	/**Sets the location of the destination
	 * @param location object of the destination
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String string = new String(name);
		if (!reason.equals("")) {
			string += "\nReason: " + reason;
		}
		if (location != null) {
			string += "\nLat: " + location.getLatitude();
			string += "\nLon: " + location.getLongitude();
		}
		return string;
	}

}
