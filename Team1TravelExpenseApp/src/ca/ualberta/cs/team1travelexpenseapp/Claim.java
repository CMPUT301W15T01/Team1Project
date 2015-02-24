package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;

import android.nfc.Tag;

/**
 * warning 
 * skeletal code
 */

public class Claim { 
	
	protected String claimantName;
	protected Date startDate;
	protected Date endDate;
	protected Dictionary<String, String> destinationReasonList;
	protected ArrayList<Tag> claimTagList;
	protected int status;
	protected boolean isComplete;
	protected ArrayList<User> approverList;
	protected Dictionary<User, String> commentList;
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

	public int getDestinationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getDestination(String destination) {
		return destinationReasonList.get(destination);
	}

	public String getStatus() {
		// TODO Auto-generated method stub
		return new String();
	}

	public int getTagCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTotal(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean submit() {
		return false;
		// TODO Auto-generated method stub
	}

	public void setName(String name) {
		claimantName = name;
	}

	public Date getStartDate() {
		// TODO Auto-generated method stub
		return startDate;
	}
	
	public void setStartDate(Date date) {
		startDate = date;
	}
	
	public Date getEndDate() {
		// TODO Auto-generated method stub
		return endDate;
	}
	
	public void setEndDate(Date date) {
		endDate = date;
	}
	
	//if destination already exist, new reason will write over old reason 
	//else new destination will reason will be added to the dictionary 
	public void addDestination(String destination, String reason) {
			destinationReasonList.put(destination, reason);
	}

	public void addApprover(String string) {
		// TODO Auto-generated method stub
		
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
			for(int j=0;j<destCount-1; j++){
				str += this.getDestination(j)+", ";
			}
			str+=this.getDestination(destCount-1)+"\n";
		}
		str+="Status: "+this.getStatus()+"\n";
		str+="Tag(s): ";
		int tagCount=this.getTagCount();
		if(tagCount==0){
			str+="None";
		}
		else{
			for(int j=0;j<tagCount-1; j++){
				str+=this.getTag(j).toString()+", ";
			}
			str+=this.getTag(tagCount-1).toString()+"\n";
		}
		str+="Totals: ";
		int totalCount=this.getTagCount();
		if(totalCount==0){
			str += "None";
		}
		else{
			for(int j=0;j<totalCount-1; j++){
				str+=Integer.toString(this.getTotal(j))+", ";
			}
			str+=this.getTotal(totalCount-1)+"\n";
		}
		
		return str;
	}

	private Object getTag(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public Expense getExpense(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addExpense(Expense expense1) {
		// TODO Auto-generated method stub
	}
		
	public void addComment(String string) {
		// TODO Auto-generated method stub
	}

	public String getApprover() {
		// TODO Auto-generated method stub
		return new String();
	}

	public String getComments() {
		// TODO Auto-generated method stub
		return new String();
	}

	public boolean addTag(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public ArrayList<Tag> getTags() {
		return null;
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReason() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Claim> getExpenses() {
		// TODO Auto-generated method stub
		return null;
	}


}
