package ca.ualberta.cs.team1travelexpenseapp;

import java.math.BigDecimal;
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

import android.nfc.Tag;
import android.widget.Toast;

/**
 * warning 
 * skeletal code
 */

public class Claim { 
	
	protected ArrayList<Expense> expenses;
	protected String claimantName;
	protected Date startDate;
	protected Date endDate;
	protected Map<String, String> destinationReasonList;
	protected ArrayList<Tag> claimTagList;
	protected int status;
	protected boolean isComplete;
	protected ArrayList<User> approverList;
	protected Map<User, String> commentList;
	protected ArrayList<Listener> listeners;
	
	public Claim() { 
		claimantName          = "empty claim";
		startDate             = new Date();
		endDate               = new Date();
		destinationReasonList = new HashMap<String, String>();
		claimTagList          = null;
		status                = 0;
		isComplete            = false;
		approverList          = null;
		commentList           = null;
		listeners             = null;
	}

	public Claim(String cName, Date sDate, Date eDate) {
		claimantName = cName;
		startDate = sDate;
		endDate = eDate;
	}


	public ArrayList<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(ArrayList<Expense> expenses) {
		this.expenses = expenses;
	}
	
	public void addExpense(Expense expense) {
		this.expenses.add(expense);
	}

	public void removeExpense(int index) {
		this.expenses.remove(index);
	}
	
	public void updateExpense(int index, Expense expense ) {
		// As a claimant, I want to edit an expense claim while changes are allowed.
		if (status == 1) {
			return;
		}
		this.expenses.set(index, expense);
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public Map<User, String> getCommentList() {
		return commentList;
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
		ArrayList<Expense> cexpenses = this.getExpenses();
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
		str += "From Date: " + dateformat.format(getStartDate()) + "\n";
		str += "End Date: " + dateformat.format(getEndDate()) + "\n";
		
		return str;
		
	}
	
}
