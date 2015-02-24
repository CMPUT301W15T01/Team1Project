package ca.ualberta.cs.team1travelexpenseapp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.nfc.Tag;

/**
 * warning 
 * skeletal code
 */

public class Claim { 
	
	protected ArrayList<Expense> Expenses;
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
		claimantName = null;
		startDate    = null;
		endDate      = null;
		destinationReasonList = null;
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
		return Expenses;
	}

	public void setExpenses(ArrayList<Expense> expenses) {
		Expenses = expenses;
	}

	//if destination already exist, new reason will write over old reason 
	//else new destination will reason will be added to the Map 
	public void addDestination(String destination, String reason) {
			destinationReasonList.put(destination, reason);
	}
	
	public String getReason(String destination) {
		return destinationReasonList.get(destination);
	}
	
	private int getDestinationCount() {
		return destinationReasonList.size();
	}
	
	public Set<String>  getDestinations() {
		return destinationReasonList.keySet();
	}
	
	public void setName(String name) {
		claimantName = name;
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
	

	public String toString(){
		String str ="Start Date: "+this.getStartDate().toString()+"\n";
		str += "Destination(s): ";
		//write out comma separated destinations 
		int destCount = this.getDestinationCount();
		if ( destCount == 0 ) {
			str += "None";
		}
		else{
			//destCount-1 else extra , 
			Iterator<String> destinations = this.getDestinations().iterator();
			for(int j=0;j<destCount-1; j++) {
				str += destinations.next().toString() + ", ";
			}
			str += destinations.next().toString() + "\n";
		}
		str+="Status: "+this.getStatus()+"\n";
		str+="Tag(s): ";
		int tagCount=this.getTagCount();
		if(tagCount==0){
			str+="None";
		}
		else{
			ArrayList<Tag> tags = this.getClaimTagList();
			for(int j=0;j<tagCount-1; j++){
				str+= tags.get(j).toString() + ", ";
			}
			str+= tags.get(tagCount-1).toString() + "\n";
		}
		str+="Totals: ";
		int totalCount=this.getTagCount();
		if(totalCount==0){
			str += "None";
		}
		else{
			str += getCurrencyTotals().get("CAD") + "CAD";
			str += getCurrencyTotals().get("USD") + "USD";
			str += getCurrencyTotals().get("EUR") + "EUR";
			str += getCurrencyTotals().get("GBP") + "GBP";
			str += getCurrencyTotals().get("CHF") + "CHF";
			str += getCurrencyTotals().get("JPY") + "JPY";
			str += getCurrencyTotals().get("CNY") + "CNY";
		}
		
		return str;
	}
	
}
