package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;

/**
 * Implements a list of claims along along with a set of listeners to be updated whenever the list is modified.
 * Will eventually provide an interface to a ClaimListManager which will save the list to disk and the web when connected.
 * Also contains a list of selected Tags which will eventually allow for filter by tag functionality (not yet implemented).
 *
 */
public class ClaimList {
	private ArrayList<Claim> claimList;
	private ArrayList<Tag> selectedTags;
	private ArrayList<Listener> listeners;
	
	/**
	 * Create the ClaimList with new ArrayLists of Claims, selectedTags, and listeners.
	 */
	public ClaimList(){
		claimList = new ArrayList<Claim>();
		selectedTags = new ArrayList<Tag>();
		listeners = new ArrayList<Listener>();
	}
	
	/**
	 * Return the underlying ArrayList of Claims.
	 * @return The underlying ArrayList of Claims.
	 */
	public ArrayList<Claim> getClaims() {
		return claimList;
	}
	
	/**
	 * Add the passed claim to the underlying ArrayList of Claims.
	 * @param claim The Claim to be added
	 */
	public void addClaim(Claim claim) {
		claimList.add(claim);
	}
	
	/**
	 * Changes the basic claim information in one claim to match the information in another, does not modify the
	 * claims expense list, status or approver list.
	 * @param currentClaim The Claim to be updated
	 * @param newClaim The Claim to fetch the new info from
	 */
	public void updateClaim(Claim currentClaim, Claim newClaim){
		currentClaim.setClaimantName(newClaim.getClaimantName());
		//currentClaim.setApproverList(newClaim.getApproverList());
		currentClaim.setClaimTagList(newClaim.getClaimTagList());
		//currentClaim.setComplete(newClaim.isComplete());
		currentClaim.setEndDate(newClaim.getEndDate());
		//currentClaim.setExpenses(newClaim.getExpenses());
		currentClaim.setStartDate(newClaim.getStartDate());
		//currentClaim.setStatus(newClaim.getStatus());
		notifyListeners();
	}
	
	public Claim getClaim(int index) {
		return claimList.get(index);
	}
	
	public void saveClaimList() {
		//to do 
	}
	
	public void filterByTags(ArrayList<Tag> tags) {
		//to-do
	}
	
	public void addListener(Listener listener) {
		this.listeners.add(listener);
	}
	
	private void notifyListeners() {
		for(int i=0; i<listeners.size();i++){
			listeners.get(i).update();
		}
	}
	
	public void onAddDRClick() {
		//to do
	}

	public void onAddClaim() {
		//to do
	}
	
	public void onManageTagsClick() {
		//to do
	}

	public void setClaimList(ArrayList<Claim> claims) {
		this.claimList = claims;
		notifyListeners();
	}
	
	public void setSelectedTags(ArrayList<Tag> tags) {
		this.selectedTags = tags;
		
	}

	public void setListeners(ArrayList<Listener> listeners) {
		this.listeners = listeners;
		
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	public ArrayList<Listener> getListeners() {
		return listeners;
	}
	
}
