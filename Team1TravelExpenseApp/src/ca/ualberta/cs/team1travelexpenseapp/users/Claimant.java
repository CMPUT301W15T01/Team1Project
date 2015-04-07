package ca.ualberta.cs.team1travelexpenseapp.users;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import ca.ualberta.cs.team1travelexpenseapp.ClaimList;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.TagList;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import dataManagers.ClaimListManager;
import dataManagers.ClaimantClaimListManager;
import dataManagers.ReceiptPhotoManager;
import dataManagers.HomeLocationManager;
import dataManagers.TagListManager;

/**
 * Claimant is the base class for loading the tags, managers and the the claims
 * from the web server. A claimant object encapsulates the taglist object
 * information, which is needed for the sorting the claims by tags.
 * 
 * @since 1.0
 */
public class Claimant extends User {
	protected TagList tagList;
	protected HomeLocationManager locationManager;

	/**
	 * Initializes a claimant object
	 * 
	 * @param name
	 *            Name of the claimant
	 */
	public Claimant(String name) {
		super(name);
		this.claimList=new ClaimList(this);
		this.tagList= new TagList();
		this.locationManager = new HomeLocationManager(this.name);
	}

	/**
	 * Returns the tag list of the claimant
	 * 
	 * @return tagList the taglist of the claimant
	 */
	public TagList getTagList() {
		return tagList;
	}

	/**
	 * Set the tag list of the claimant
	 * 
	 * @param tagList
	 *            The taglist of the claimant
	 */
	public void setTagList(TagList tagList) {
		this.tagList = tagList;
	}
	
	@Override
	public void setLocation(Location location) {
		this.location = location;
		locationManager.saveLocation(location);
	}
	
	/**
	 * Loads the users claims, location and tags from the server
	 */
	public void loadData(){
		claimList.loadClaims();
		tagList.loadTags();
		location = locationManager.loadLocation();
		syncTags();
	}

	/**
	 * Initializes the managers for taglist and claimslist for the current
	 * claimant
	 * 
	 * @param context
	 *            The application context
	 */
	public void initManagers(Context context) {
		TagListManager tagListManager = getTagList().getManager();
		tagListManager.setContext(context);
		tagListManager.setClaimantName(this.name);
		ClaimantClaimListManager claimantClaimListManager = (ClaimantClaimListManager) getClaimList()
				.getManager();

		claimantClaimListManager.setContext(context);
		claimantClaimListManager.setClaimantName(this.name);
		locationManager.setContext(context);
		UserSingleton.getUserSingleton().setContext(context);
	}

	/**
	 * Match tags of the same name in claims to tags in the passed tagList. This
	 * is needed to fix saved tags of the same name which do not correspond to
	 * the same object once saved and loaded
	 * 
	 * @param tagList
	 *            to be matched with
	 */
	private void syncTags() {
		ArrayList<Tag> personalTags = tagList.getTags();
		for (Claim claim : claimList.getClaims()) {
			ArrayList<Tag> oldTags = new ArrayList<Tag>();
			ArrayList<Tag> newTags = new ArrayList<Tag>();
			ArrayList<Tag> claimTagList = claim.getClaimTagList();
			for (int claimIndex = 0; claimIndex < claimTagList.size(); claimIndex++) {
				boolean exists = false;
				for (int personalIndex = 0; personalIndex < personalTags.size(); personalIndex++) {
					if (claimTagList.get(claimIndex).getName()
							.equals(personalTags.get(personalIndex).getName())) {
						oldTags.add(claimTagList.get(claimIndex));
						newTags.add(personalTags.get(personalIndex));
						exists = true;
					}
				}
				if (!exists) {
					personalTags.add(claimTagList.get(claimIndex));
				}
			}
			claimTagList.addAll(newTags);
			claimTagList.removeAll(oldTags);
			claim.setClaimTagList(claimTagList);
		}

	}

}
