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
		this.date = date;
		this.category = category;
		this.amount = amount;
		this.currency = currency;
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
		this.amount = amount;

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
		return receipt;
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
		str += "Date: " + dateformat.format(getDate()) + "\n";
		str += getCategory() + "\n";
		str += getDescription() +"\n";
		str += getAmount() + getCurrency() + "\n";
		str += "Photographic Receipt: " + receipt.exists();
		
		return str;
	}

}
