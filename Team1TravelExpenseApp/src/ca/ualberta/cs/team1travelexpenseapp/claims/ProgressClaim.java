package ca.ualberta.cs.team1travelexpenseapp.claims;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseList;
import ca.ualberta.cs.team1travelexpenseapp.Listener;
import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.User;
import ca.ualberta.cs.team1travelexpenseapp.adapter.ClaimAdapter;

public class ProgressClaim extends BasicClaim {
	/** Initializes attributes to new instances **/
	public ProgressClaim() { 
		super();
		setStatus(ProgressClaim.class);
	}

	/** set claimant name, start and end date, all other attributes are initializes to new instances 
	 * @param cName - a string
	 * @param sDate - a Date
	 * @param eDate - a Date **/
	public ProgressClaim(String cName, Date sDate, Date eDate) {
		super();
		setStatus(ProgressClaim.class);
	}
	
	/** 
	 * sets the claim's expense list object to a given expenseList
	 * @param expenseList
	 */
	public void setExpenseList(ExpenseList expenseList) {
		this.expenseList = expenseList;
	}
	
	/**
	 * adds a destination, with a reason to the claim 
	 * if destination already exist, new reason will write over old reason 
	else new destination will reason will be added to the Map 
	 * @param destination - a string
	 * @param reason - a string 
	 */
	public void addDestination(String destination, String reason) {
			destinationReasonList.put(destination, reason);
	}
	
	/**
	 * Set the claimantName for the Claim.
	 * @param name The name of the claimant for the Claim.
	 */
	public void setClaimantName(String name) {
		claimantName = name;
	}

	/**
	 * Set the TagList for the claim
	 * @param claimTagList The TagList containing the new tags to be set for the claim.
	 */
	public void setClaimTagList(ArrayList<Tag> claimTagList) {
		this.claimTagList = claimTagList;
	}

	/**
	 * Set the startDate of the Claim.
	 * @param date startDate to be set for the current Claim.
	 */
	public void setStartDate(Date date) {
		startDate = date;
	}
	
	/**
	 * Set the endDate of the Claim.
	 * @param date endDate to be set for the current Claim.
	 */
	public void setEndDate(Date date) {
		endDate = date;
	}


	@Override
	public BasicClaim changeStatus(Class<?> claimStatusType) {
		return new ClaimAdapter<ProgressClaim>(this, claimStatusType);
	}

}
