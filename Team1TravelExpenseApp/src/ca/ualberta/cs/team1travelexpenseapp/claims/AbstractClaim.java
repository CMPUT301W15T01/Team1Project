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

public abstract class AbstractClaim implements Comparable<AbstractClaim> {
	protected ExpenseList expenseList;
	protected String claimantName;
	protected Date startDate;
	protected Date endDate;
	protected HashMap<String, String> destinationReasonList;
	protected ArrayList<Tag> claimTagList;
	protected boolean isComplete;
	protected ArrayList<User> approverList;
	protected Map<String, String> commentList;
	protected ArrayList<Listener> listeners;
	protected String status = null;
	
	
	/** Initializes attributes to new instances **/
	public AbstractClaim(){ 
		claimantName          = "";
		startDate             = new Date();
		endDate               = new Date();
		destinationReasonList = new HashMap<String, String>();
		claimTagList          = new ArrayList<Tag>();
		isComplete            = false;
		approverList          = new ArrayList<User>();
		commentList           = new HashMap<String, String>();
		listeners             = new ArrayList<Listener>();
		expenseList           = new ExpenseList();
	}

	/** set claimant name, start and end date, all other attributes are initializes to new instances 
	 * @param cName - a string
	 * @param sDate - a Date
	 * @param eDate - a Date **/
	public AbstractClaim(String cName, Date sDate, Date eDate) {
		claimantName = cName;
		startDate = sDate;
		endDate = eDate;
		
		destinationReasonList = new HashMap<String, String>();
		claimTagList          = new ArrayList<Tag>();
		isComplete            = false;
		approverList          = new ArrayList<User>();
		commentList           = new HashMap<String, String>();
		listeners             = new ArrayList<Listener>();
		expenseList           = new ExpenseList();
	}
	
	
	/** 	 
	 * returns a exenepseList object that contains 
	 * list of expenses for the claim 
	 * @param 
	 * @return ExpenseList object **/
	public ExpenseList getExpenseList() {
		return expenseList;
	}

	

	
	/**
	 * 
	 * @param destination - a string 
	 * @return
	 */
	public String getReason(String destination) {
		return destinationReasonList.get(destination);
	}
	
	/**
	 * Returns a HashMap with destinations as keys and reasons as values.
	 * @return HashMap of destinations (String) mapped to reasons (String).
	 */
	public HashMap<String, String> getDestinationReasonList() {
		return destinationReasonList;
	}
	
	/**
	 * Return the number of destinations in the Claim.
	 * @return int corresponding to number of destinations in Claim.
	 */
	private int getDestinationCount() {
		return destinationReasonList.size();
	}
	
	/**
	 * Return the set of destinations for the Claim.
	 * @return Set of destinations (Strings) in claim
	 */
	public Set<String>  getDestinations() {
		return destinationReasonList.keySet();
	}
	

	/**
	 * Get the claimantName for the Claim.
	 * @return name The name of the claimant for the Claim.
	 */
	public String getClaimantName() {
		return claimantName;
	}

	/**
	 * Get the ArrayList of Tags claimTagList for the claim.
	 * @return ArrayList of Tags containing the tags set for the claim.
	 */
	public ArrayList<Tag> getClaimTagList() {
		return claimTagList;
	}
	
	/**
	 * Get the number of tags in the claim.
	 * @return int corresponding the number of tags set for the claim.
	 */
	private int getTagCount() {
		return claimTagList.size();
	}



	/**
	 * Return a boolean indicating whether the claim is "complete".
	 * @return boolean indicating whether claim is complete.
	 */
	public boolean isComplete() {
		return isComplete;
	}



	/**
	 * Get the list of approvers for the current Claim
	 * @return ArrayList of Users corresponding to the approvers who have returned or approved the claim
	 */
	public ArrayList<User> getApproverList() {
		return approverList;
	}



	/**
	 * Get a Map mapping approvers of the claim to any comments they may have left.
	 * @return Map from approver name (String) to that approver's comments (String).
	 */
	public Map<String, String> getCommentList() {
		return commentList;
	}

	

	/**
	 * Return the startDate for the Claim.
	 * @return startDate (Date) of the Claim.
	 */
	public Date getStartDate() {
		return startDate;
	}
	

	/**
	 * Return the endDate for the Claim.
	 * @return endDate (Date) of the Claim.
	 */
	public Date getEndDate() {
		return endDate;
	}
	

	
	/**
	 * Return a map from currency types (String) to amounts (BigDecimal) of the currency spent in the expenses of the Claim.
	 * @return Map from currency types (String) to amount of that currency spent (BigDecimal).
	 */
	public Map<String, BigDecimal> getCurrencyTotals() {
		HashMap<String, BigDecimal> counts = new HashMap<String, BigDecimal>();	
		for (Expense expense : this.getExpenseList().getExpenses()){
			if(counts.containsKey(expense.getCurrency())){
				Log.d("String test", expense.getAmount().toString());
				counts.put(expense.getCurrency(), expense.getAmount().add(counts.get(expense.getCurrency())));
			}
			else {
				counts.put(expense.getCurrency(), expense.getAmount());
			}
		}
		return counts;
	}
	
	public String getCurrencyTotal(String currency) {
		return getCurrencyTotals().get(currency).toString();
	}
	
	/**
	 * Return a String representation of the information contained in the claim to be printed to a list item or similar.
	 */
	public String toString(){
		
		String str = claimantName + "\n";
		
		//date format, has year month day 
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		str += "Starting Date of travel: " + dateformat.format(getStartDate()) + "\n";
		str += "End Date: " + dateformat.format(getEndDate()) + "\n";
		Iterator<String> destinations = getDestinations().iterator();
		str += "Destinations:";
		while (destinations.hasNext()) {
			String tempDest = destinations.next();
			//if has next iterator or only has one destination
			if (destinations.hasNext() || (getDestinationCount() == 1) ) {
				str += " " + tempDest;
				if (getDestinationCount() != 1) {
					str += ",";
				}
			} else {
				str += " and " + tempDest;
			}
		}
		
		//get status
		str += "\nStatus: " + status.toString();
		
		//get tag list 
		str += "\nTags:";
		Iterator<Tag> tags = getClaimTagList().iterator();
		while (tags.hasNext()) {
			String tempTag = tags.next().toString();
			//if has next iterator or only has one tag
			if (tags.hasNext() || (getTagCount() == 1) ) {
				str += " " + tempTag;
				if (getTagCount() != 1) {
					str += ",";
				}
			} else {
				str += " and " + tempTag;
			}
		}
		str+="\nTotals: ";
		//get total currency amounts
		Map<String,BigDecimal> totals = getCurrencyTotals();
	    for(Map.Entry<String, BigDecimal> currency: totals.entrySet()) {
	         // add each currency to string
	    	//Log.d("String test", currency.getValue().toString());
	    	if(currency.getValue().floatValue()==0 || currency.getKey().equals("") ) {
	    		continue;
	    	}
	    	str += currency.getValue() + "-" + currency.getKey() + " ";
	    }
		
		
		return str;
		
	}

	//sorting
	//https://docs.oracle.com/javase/tutorial/collections/interfaces/order.html 03/24/15 
	/**
	 * override comapreTo method so that claims will be sorted by largest to smallest date 
	 * for a Claimant user and oldest to newest for an Approver user. Note only two types of users exists.
	 * use Collections.sort(ArrayList<Claim> Object); to sort Object. 
	 */
	@Override
	public int compareTo( AbstractClaim claim ) {
		if (ClaimListController.getUserType().equals("Claimant")) {
			return claim.startDate.compareTo(this.startDate);
		}
		return this.startDate.compareTo(claim.startDate);
	}
}
