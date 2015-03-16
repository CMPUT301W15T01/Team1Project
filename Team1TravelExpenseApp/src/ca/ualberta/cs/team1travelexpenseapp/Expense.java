/*
Copyright 2015 Jeffrey Oduro, Cody Ingram, Boyan Peychoff, Kenny Young, Dennis Truong, Victor Olivares 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ca.ualberta.cs.team1travelexpenseapp;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Implements the model of an Expense item which is an individual part of a specific claim 
 * and contains a date, category, description, cost, currency, receipt photo and whether 
 * the expense is flagged or incomplete. 
 */
public class Expense {
	protected Date date;
	protected String category;
	protected String description;
	protected BigDecimal amount;
	protected String currency;
	protected boolean isFlagged;
	protected boolean isComplete;
	protected File receipt;

	/**
	 * Create a new Expense with a given description, date, and category.
	 * @param description
	 * The Description for the Expense as a String.
	 * @param date
	 * The date that the charge occurred on.
	 * @param category
	 * The category type that the expense was used for (fuel, meal, supplies, ect...).
	 * @param amount
	 * The number amount that the charge costed.
	 * @param currency
	 * The type of currency the amount is in as a 3 letter string (CAD, USD< EUR, ect...).
	 */
	public Expense(String description, Date date, String category,
			BigDecimal amount, String currency) {
		this.description = description;
		this.date = date;
		this.category = category;
		this.amount = amount.setScale(2,BigDecimal.ROUND_HALF_EVEN); 
		this.currency = currency;
		
		isFlagged = false;
		receipt = null;
		
		if(description.equals("") || currency.equals("") || amount.floatValue() == 0 || category.equals("none")){
			isComplete = false;
		}
		else {
			isComplete = true;
		}
	}
	/**
	 * Create a new Expense with its values uninitialized.
	 */
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
	
	/**
	 * Get the date for the Expense .
	 * @return
	 * The date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set the date for the expense.
	 * @param date
	 * The date that the charge occurred on.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Get the category of the Expense.
	 * @return
	 * The category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Set the category of the Expense.
	 * @param category
	 * The category type that the expense was used for (fuel, meal, supplies, ect...).
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Get the description of the Expense.
	 * @return
	 * The description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the Expense.
	 * @param description
	 * The Description for the Expense as a String.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the numerical cost of the Expense to 2 decimal places.
	 * @return
	 * The amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Set the numerical cost of the Expense (will be rounded to 2 decimal places).
	 * @param amount
	 * The number amount that the charge costed.
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount.setScale(2,BigDecimal.ROUND_HALF_EVEN); 

	}

	/**
	 * Get the currency type of the cost.
	 * @return
	 * The currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Set the currency type of the cost.
	 * @param currency
	 * The type of currency the amount is in as a 3 letter string (CAD, USD< EUR, ect...).
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * Get whether or not the expense has been flagged.
	 * @return
	 * true of false
	 */
	public boolean isFlagged() {
		return isFlagged;
	}

	/**
	 * Set whether or not the expense has been flagged.
	 * @param isFlagged
	 * True if the expense is currently flagged.
	 */
	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

	/**
	 * Get the file of the receipt photo.
	 * @return
	 * A File
	 */
	public File getReceipt() {
		//Stub
		return null;
	}
	
	/**
	 * Set the file of the receipt photo.
	 * @param receipt
	 * The file object for the stored image.
	 */
	public void setReceipt(File receipt) {
		this.receipt = receipt;
	}
	
	/**
	 * Get whether the Expense is complete or not.
	 * @return
	 * true of false
	 */
	public boolean isComplete() {
		return isComplete;
	}
	
	/**
	 * Set whether the Expense is complete or not.
	 * @param isComplete
	 * Should be true if the expense
	 */
	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	/**
	 * Returns a string containing all the information about an Expense item to
	 * be displayed.
	 * @return
	 * a printable representation of this object.
	 */
	public String toString() {
		String str = "";
		
		//date format, has year month day 
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		str += "Date: " + dateformat.format(getDate()) + "\nCategory: " + getCategory();
		//if(getDescription().length() > 0){
			str += "\nDescription:" + getDescription();
		//}
		if (getAmount().floatValue() != 0){
			str += "\n" + getAmount() + getCurrency();
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

	/**
	 * Compress the given image file to be less than 65536 bytes in size.
	 * @param activity
	 * The currently running activity.
	 * @param photoFile
	 * The image file to be compressed
	 */
	public static void compressPhoto(EditExpenseActivity activity,
			File photoFile) {
		// TODO Compress photofile
	}
}
