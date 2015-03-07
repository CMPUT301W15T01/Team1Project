package ca.ualberta.cs.team1travelexpenseapp;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class Expense {
	protected Date date;
	protected String category;
	protected String description;
	protected BigDecimal amount;
	protected String currency;
	protected boolean isFlagged;
	protected File receipt;
	protected boolean isComplete;

	public Expense(String string, Date date, String string2,
			BigDecimal bigDecimal, String string3) {
		// TODO Auto-generated constructor stub
	}
	
	public Expense() {
		date = new Date();
		category = "";
		description = "";
		amount = new BigDecimal(0.0); 
		currency = "";
		isFlagged = false;
		receipt = new File("");
		isComplete = false;
	}

	public void setIncomplete(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public boolean checkIncomplete() {
		// Checks indicator checkbox AND input fields
		return false;
	}

	public void setDate(Date date) {
		// TODO Auto-generated method stub
		
	}

	public void setCategory(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setDesc(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setCost(BigDecimal d) {
		// TODO Auto-generated method stub
		this.amount = d;
	}

	public void setCurrency(String string) {
		// TODO Auto-generated method stub
		
	}

	public Object getDate() {
		// TODO Auto-generated method stub
		return date;
	}

	public String getCategory() {
		// TODO Auto-generated method stub
		return category;
	}

	public String getDesc() {
		// TODO Auto-generated method stub
		return description;
	}

	public Object getCost() {
		// TODO Auto-generated method stub
		return amount;
	}

	public String getCurrency() {
		// TODO Auto-generated method stub
		return currency;
	}
	
	public String toString() {
		String str = "";
		
		//date format, has year month day 
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		str += "Date: " + dateformat.format(getDate()) + "\n";
		str += getCategory() + "\n";
		str += getDesc() +"\n";
		str += getAmount() + getCurrency() + "\n";
		str += "Photographic Receipt: " + receipt.exists();
		
		return str;
	}

}
