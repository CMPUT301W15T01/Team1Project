package ca.ualberta.cs.team1travelexpenseapp;

import java.util.Date;

/**
 * warning 
 * skeletal code
 */
// TODO create code 

public class Claim { 
	
	public Claim() { 
		// TODO Auto-generated constructor stub
	}

	public Claim(String string, Date date, Date date2) {
		// TODO Auto-generated constructor stub 
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Date getStartDate() {
		// TODO Auto-generated method stub
		return new Date();
	}

	public int getDestinationCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getDestination(int j) {
		// TODO Auto-generated method stub
		return new String();
	}

	public String getStatus() {
		// TODO Auto-generated method stub
		return new String();
	}

	public int getTagCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Tag getTag(int j) {
		// TODO Auto-generated method stub
		return new Tag();
	}

	public int getTotal(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void submit() {
		// TODO Auto-generated method stub
		
	}

	public void setStatus(String string) {
		// TODO Auto-generated method stub
		
	}

	public boolean setName() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addDestination() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addReason() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean setFromDate() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean setToDate() {
		// TODO Auto-generated method stub
		return false;
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
