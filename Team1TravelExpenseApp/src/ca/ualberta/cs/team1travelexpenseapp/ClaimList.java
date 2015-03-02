package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;

public class ClaimList {
	private ArrayList<Claim> claimList;
	private ArrayList<Tag> selectedTags;
	private ArrayList<Listener> listeners;
	
	public ArrayList<Claim> getClaims() {
		return claimList;
	}
	
	public void deleteClaim(int index) {
		claimList.remove(index);
	}
	
	public void addClaim(Claim claim) {
		claimList.add(claim);
	}
	
	//pointer to currentClaim in list and newClaim 
	public void updateClaim(Claim currentClaim, Claim newClaim){
		currentClaim.setClaimantName(newClaim.getClaimantName());
		currentClaim.setApproverList(newClaim.getApproverList());
		currentClaim.setClaimTagList(newClaim.getClaimTagList());
		currentClaim.setComplete(newClaim.isComplete());
		currentClaim.setEndDate(newClaim.getEndDate());
		currentClaim.setExpenses(newClaim.getExpenses());
		currentClaim.setStartDate(newClaim.getStartDate());
		currentClaim.setStatus(newClaim.getStatus());
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
		//to do
	}
	
	private void notifyListeners() {
		//to do
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

	public void setClaimList() {
		claimList = new ArrayList<Claim>();
	}
	
	public void setSelectedTags() {
		selectedTags = new ArrayList<Tag>();
		
	}

	public void setListeners() {
		listeners = new ArrayList<Listener>();
		
	}
	
}
