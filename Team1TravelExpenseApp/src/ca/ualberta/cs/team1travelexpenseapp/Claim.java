package ca.ualberta.cs.team1travelexpenseapp;

import java.math.BigDecimal;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
=======
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
>>>>>>> 1c8a34522c690f73d0529ee6aa51bf2181a7acc6
import java.util.Map;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
<<<<<<< HEAD
=======
import java.util.concurrent.CyclicBarrier;
>>>>>>> 1c8a34522c690f73d0529ee6aa51bf2181a7acc6

import android.nfc.Tag;
import android.widget.Toast;

/**
 * warning 
 * skeletal code
 */

public class Claim { 
	
	public enum Status {
		inProgress, submitted, approved, returned
	}
	protected ExpenseList expenseList;
	protected String claimantName;
	protected Date startDate;
	protected Date endDate;
	protected Map<String, String> destinationReasonList;
	protected ArrayList<Tag> claimTagList;
	protected boolean isComplete;
	protected ArrayList<User> approverList;
	protected Map<String, String> commentList;
	protected ArrayList<Listener> listeners;
	protected Status status;
	
	
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
	
	public Map<String, String> getDestinationReasonList() {
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
		ArrayList<Expense> cexpenses = this.getExpenseList().getExpenses();
		Map<String, BigDecimal> totals = new HashMap<String, BigDecimal>();
		while ( !cexpenses.isEmpty()) {
			String currency = cexpenses.get(0).getCurrency();
			if (totals.get(currency) != null) {
				BigDecimal tempAmount = totals.get(currency);
				tempAmount.add(cexpenses.get(0).getAmount());
				totals.put(currency, tempAmount );
			} else {
				totals.put(currency, cexpenses.get(0).getAmount());
			}
			cexpenses.remove(0);
		}
		return totals;
	}
	
	public String getCurrencyTotal(String currency) {
		return getCurrencyTotals().get(currency).toString();
	}
	

	public String toString(){
		
		String str = claimantName + "\n";
		
		//date format, has year month day 
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		str += "Starting Date of travel: " + dateformat.format(getStartDate()) + "\n";
		//str += "End Date: " + dateformat.format(getEndDate()) + "\n";
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
		
		//get total currency amounts
		Map<String, BigDecimal> currencyMap = getCurrencyTotals();
		Iterator<String> currencies = currencyMap.keySet().iterator();
		str += "\nCurrency Totals:";
		while (currencies.hasNext()) {
			String tempCurrency = currencies.next().toString();
			//if has next iterator or only has one currency
			if (tags.hasNext() || (currencyMap.keySet().size() == 1) ) {
				str += " " + tempCurrency + ": ";
				str += currencyMap.get(tempCurrency).toString();
				if (getTagCount() != 1) {
					str += ",";
				}
			} else {
				str += " and " + tempCurrency + ": ";
				str += currencyMap.get(tempCurrency).toString();
			}
		}
		
		
		return str;
		
	}

	
}
