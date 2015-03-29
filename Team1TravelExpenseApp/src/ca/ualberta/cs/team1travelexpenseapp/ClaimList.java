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


package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.users.Approver;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import dataManagers.ApproverClaimListManager;
import dataManagers.ClaimListManager;
import dataManagers.ClaimantClaimListManager;
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
	private ClaimListManager manager;
	private transient ArrayList<Listener> listeners;
	
	/**
	 * Create the ClaimList with new ArrayLists of Claims, selectedTags, and listeners.
	 */
	public ClaimList(Claimant claimant){
		claimList = new ArrayList<Claim>();
		selectedTags = new ArrayList<Tag>();
		manager=new ClaimantClaimListManager(this);
	}
	
	public ClaimList(Approver approver){
		claimList = new ArrayList<Claim>();
		selectedTags = new ArrayList<Tag>();
		manager = new ApproverClaimListManager(this);
	}
	
	/**
	 * Return the underlying ArrayList of Claims.
	 * @return The underlying ArrayList of Claims.
	 */
	public ArrayList<Claim> getClaims() {
		Collections.sort(claimList);
		return claimList;
	}
	
	/**
	 * Add the passed claim to the underlying ArrayList of Claims.
	 * @param claim The Claim to be added
	 */
	public void addClaim(Claim claim) {
		claimList.add(claim);
		Collections.sort(claimList);
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
		currentClaim.setStatus(newClaim.getStatus());
		//claim is modified and therefore no longer synced online
		currentClaim.setSynced(false);
		saveClaims();
		notifyListeners();
	}
	
	/**
	 * Return the claim located at the passed index.
	 * @param index The index of the desired claim.
	 * @return The claim at the given index.
	 */
	public Claim getClaim(int index) {
		return claimList.get(index);
	}
	
	/**
	 * Save the claim list to disk (and to the web server if possible) (not currently implemented)
	 */
	public void saveClaims() {
		manager.saveClaims();
	}
	
	/**
	 * Load the claim list from disk (and to the web server if possible) (not currently implemented)
	 */
	public void loadClaims() {
		manager.loadClaims();
	}
	
	/**
	 * Set the selectedTags list to the passed ArrayList of tags. To be used in filtering claims by Tag.
	 * @param tags ArrayList of tags to filter by.
	 */
	public void filterByTags(ArrayList<Tag> tags) {
		//to do
	}
	
	/**
	 * Add a new listener to be updated whenever the tagList is changed.
	 * @param listener The listener to be added.
	 */
	public void addListener(Listener listener) {
		if(listeners==null){
			listeners=new ArrayList<Listener>();
		}
		this.listeners.add(listener);
	}
	
	/**
	 * Call update method on all listeners (called on tagList changes).
	 */
	private void notifyListeners() {
		if(listeners==null){
			listeners=new ArrayList<Listener>();
		}
		for(int i=0; i<listeners.size();i++){
			listeners.get(i).update();
		}
	}
	
	/**
	 * Set the underlying ArrayList of Claims to the passed value.
	 * @param claims The ArrayList of claims to be set.
	 */
	public void setClaimList(ArrayList<Claim> claims) {
		this.claimList = claims;
		Collections.sort(claimList);
		saveClaims();
		notifyListeners();
	}
	
	/**
	 * Set the selectedTags.
	 * @param tags Tags to be set as selected
	 */
	public void setSelectedTags(ArrayList<Tag> tags) {
		this.selectedTags = tags;
		
	}

	/**
	 * Remove a listener so that it will no longer be updated when the tagList is changed.
	 * @param listener The listener to be removed
	 */
	public void removeListener(Listener listener) {
		if(listeners==null){
			listeners=new ArrayList<Listener>();
		}
		listeners.remove(listener);
	}
	
	/**
	 * Returns the full list of listeners listening to the ClaimList
	 * @return ArrayList of Listeners listening to the ClaimList
	 */
	public ArrayList<Listener> getListeners() {
		return listeners;
	}
	/**
	 * Returns the length of the claimlist
	 * @return interger length of the claim list
	 */
	public int length() {
		return claimList.size();
		
	}
	/**
	 * Gets the claim at an index in the list
	 * @param i The index
	 * @return The claim at the index
	 */
	public Claim get(int i) {
		// TODO Auto-generated method stub
		return claimList.get(i);
	}

	public ClaimListManager getManager() {
		return this.manager;
	}
	
}
