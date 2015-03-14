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
	protected boolean isComplete;
	protected File receipt;

	public Expense(String description, Date date, String category,
			BigDecimal amount, String currency) {
		this.description = description;
		this.date = date;
		this.category = category;
		this.amount = amount.setScale(2,BigDecimal.ROUND_HALF_EVEN); 
		this.currency = currency;
		
		isFlagged = false;
		receipt = null;
		isComplete = false;
	}
	
	public Expense() {
		date = new Date();
		category = "";
		description = "";
		amount = new BigDecimal(0.0); 
		currency = "";
		isFlagged = false;
		receipt = null;
		isComplete = false;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount.setScale(2,BigDecimal.ROUND_HALF_EVEN); 

	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

	public File getReceipt() {
		//TODO 
		return null;
	}

	public void setReceipt(File receipt) {
		this.receipt = receipt;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}



	public String toString() {
		String str = "";
		
		//date format, has year month day 
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		str += "Date: " + dateformat.format(getDate()) + "\nCategory: " + getCategory();
		if(getDescription().length() > 0){
			str += "\nDescription:" + getDescription();
		}
		if (getAmount().intValue() != 0){
			str += getAmount() + getCurrency();
		}
		if ( receipt != null) {
			str += "\nHas Photo";
		}
		if ( !isComplete()) {
			str += "\nincomplete";
		}
		if (isFlagged()){
			str += "\nflagged";
		}


		return str;
	}

}
