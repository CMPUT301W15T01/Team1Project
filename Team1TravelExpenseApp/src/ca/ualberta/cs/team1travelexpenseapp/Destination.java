package ca.ualberta.cs.team1travelexpenseapp;

import android.location.Location;

public class Destination {
	private String name;
	private String reason;
	private Location location;
	
	public Destination(String name, String reason, Location location){
		this.name = name;
		this.reason = reason;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String toString(){
		String string = new String(name);
		if(!reason.equals("")){
			string+="\nReason: "+reason;
		}
		if(location!=null){
			string+="\nLat: "+location.getLatitude();
			string+="\nLon: "+location.getLongitude();
		}
		return string;
	}
	
}
