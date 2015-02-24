package ca.ualberta.cs.team1travelexpenseapp;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

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

	public void setCost(double d) {
		// TODO Auto-generated method stub
		
	}

	public void setCurrency(String string) {
		// TODO Auto-generated method stub
		
	}

	public Object getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getCost() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

}
