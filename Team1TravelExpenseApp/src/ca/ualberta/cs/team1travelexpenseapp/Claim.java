package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;

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

	//if destination already exist, new reason will write over old reason 
	//else new destination will reason will be added to the dictionary 
	public void addDestination(String destination, String reason) {
			destinationReasonList.put(destination, reason);
	}
	
	public String getReason(String destination) {
		return destinationReasonList.get(destination);
	}
	
	public Enumeration<String>  getDestinations() {
		return destinationReasonList.keys();
	}
	
	public void setName(String name) {
		claimantName = name;
	}


	public ArrayList<Tag> getClaimTagList() {
		return claimTagList;
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

	public Dictionary<User, String> getCommentList() {
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
	
}
