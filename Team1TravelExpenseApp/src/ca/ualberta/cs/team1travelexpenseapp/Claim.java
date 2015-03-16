package ca.ualberta.cs.team1travelexpenseapp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;

import android.util.Log;
import android.widget.Toast;

/** 
 * Model of a Claim used to edit a list of expenses, claimant name, start and end date, destination & reason list, Tag list for the claim,
 * completeness, list of approvers for the claim, a list of comments, listeners for the views that will use this model, and the status
 * **/
public class Claim { 
	
	/** enum class Status public to access values of enum **/
	public enum Status {
		inProgress, submitted, approved, returned
	}
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
	protected Status status;
	
	/** Initializes attributes to new instances **/
	public Claim() { 
		claimantName          = "";
		startDate             = new Date();
		endDate               = new Date();
		destinationReasonList = new HashMap<String, String>();
		claimTagList          = new ArrayList<Tag>();
		status                = Status.inProgress;
		isComplete            = false;
		approverList          = new ArrayList<User>();
		commentList           = new HashMap<String, String>();
		listeners             = new ArrayList<Listener>();
		expenseList           = new ExpenseList();
	}

	/** set claimant name, start and end date, all other attributes are initializes to new instances **/
	public Claim(String cName, Date sDate, Date eDate) {
		claimantName = cName;
		startDate = sDate;
		endDate = eDate;
		
		destinationReasonList = new HashMap<String, String>();
		claimTagList          = new ArrayList<Tag>();
		status                = Status.inProgress;
		isComplete            = false;
		approverList          = new ArrayList<User>();
		commentList           = new HashMap<String, String>();
		listeners             = new ArrayList<Listener>();
		expenseList           = new ExpenseList();
	}


	public ExpenseList getExpenseList() {
		return expenseList;
	}

	public void setExpenseList(ExpenseList expenseList) {
		this.expenseList = expenseList;
	}
	
	//if destination already exist, new reason will write over old reason 
	//else new destination will reason will be added to the Map 
	public void addDestination(String destination, String reason) {
			destinationReasonList.put(destination, reason);
	}
	
	public String getReason(String destination) {
		return destinationReasonList.get(destination);
	}
	
	public HashMap<String, String> getDestinationReasonList() {
		return destinationReasonList;
	}
	
	private int getDestinationCount() {
		return destinationReasonList.size();
	}
	
	public Set<String>  getDestinations() {
		return destinationReasonList.keySet();
	}
	
	public void setClaimantName(String name) {
		claimantName = name;
	}

	public String getClaimantName() {
		return claimantName;
	}

	public ArrayList<Tag> getClaimTagList() {
		return claimTagList;
	}
	
	private int getTagCount() {
		return claimTagList.size();
	}

	public void setClaimTagList(ArrayList<Tag> claimTagList) {
		this.claimTagList = claimTagList;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public ArrayList<User> getApproverList() {
		return approverList;
	}

	public void setApproverList(ArrayList<User> approverList) {
		this.approverList = approverList;
	}

	public Map<String, String> getCommentList() {
		return commentList;
	}
	
	public void addComment(String comment) {
		commentList.put(ClaimListController.getUser().getName(), comment);
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date date) {
		startDate = date;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date date) {
		endDate = date;
	}
	
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
		str += "\nStatus: " + getStatus().toString();
		
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

	
}
