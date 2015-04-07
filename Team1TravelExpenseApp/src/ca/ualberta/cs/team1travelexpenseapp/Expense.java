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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;

import dataManagers.ReceiptPhotoManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

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
	protected File receiptFile;
	protected Uri receiptUri;
	
	private final int MAX_IMAGE_SIZE = 65536;
	
	public Uri getReceiptUri() {
		return receiptUri;
	}
	public void setReceiptUri(Uri receiptUri) {
		this.receiptUri = receiptUri;
	}

	protected Location location;


	/**
	 * Create a new Expense with a given description, date, and category. If all these values are
	 * set as something the Expense will be designated as complete by setting Expense.isComplete
	 * to true. 
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
		receiptFile = null;
		
		if(description.equals("") || currency.equals("") || amount.floatValue() == 0 || category.equals("none") 
				|| date.equals(null) ){
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
		receiptFile = null;
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
	 * Get the Location of the destination.
	 * @return
	 * The Location
	 */
	public Location getLocation() {
		if (location == null) {
			location = new Location("");
		}
		return location;
	}
	
	/**
	 * Set the Location of the destination.
	 * @param currency
	 * The Location is from android.location
	 */	
	public void setLocation(Location location) {
		this.location = location;
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
		if ( receiptFile != null) {
			str += "\nPhoto: Yes";
		} else {
			str += "\nPhoto: No";
		}
		if ( !isComplete()) {
			str += "\nMISSING FIELDS";
		}
		if (isFlagged()){
			str += "\nFlagged as incomplete";
		}


		return str;
	}
	

	/**
	 * Get the file of the receipt photo.
	 * @return
	 * A File
	 */
	public File getReceiptFile() {
		//Stub
		return this.receiptFile;
	}
	
	/**
	 * Set the file of the receipt photo.
	 * @param receipt
	 * The file object for the stored image.
	 * @param context 
	 */
	public boolean setReceiptFile(File receipt, Context context) {
		// Check if the file needs to be compressed first		
		if(receipt != null && receipt.exists()){
			Log.d("Expense Setting ReceiptFile", "File has size: " + String.valueOf(receipt.length()));
			if(receipt.length() >= MAX_IMAGE_SIZE){
				if (!this.compressPhoto(receipt, 750, 50)){
					if (!this.compressPhoto(receipt,500, 35)){
						if (!this.compressPhoto(receipt, 350, 25)){
							// Photo failed to be compressed
							receipt.delete();
							return false;
						}
					}
				}
			}	
			if(receipt.exists() && receipt.length() >= 65536){
				receipt.delete();
				return false;
			}
		}
		// Photo successfully compressed or is null
		this.receiptFile = receipt;
		
		uniquePhotoId = UUID.randomUUID();
		
		ReceiptPhotoManager photoManager = new ReceiptPhotoManager();
		photoManager.setContext(context);
		photoManager.savePhotoToWeb(this);
		//photoManager.savePhotoToWeb(SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentExpense());
		return true;
	}
	
	public UUID getUniquePhotoId() {
		return uniquePhotoId;
	}
	
	private UUID uniquePhotoId;
	
	private boolean photoSaved;
	
	public boolean isPhotoSaved(){
		return photoSaved;
	}
	
	public void setPhotoSaved(boolean state){
		photoSaved = state;
	}
	
	/**
	 * Compress the given image file to be less than 65536 bytes (65.536KB) in size.
	 * @param activity
	 * The currently running activity.
	 * @param photoFile
	 * The image file to be compressed
	 */
	private boolean compressPhoto(File photoFile, int maxLength, int quality) {
		Log.d("Expense CompressingPhoto", "Attempting to compress photo with size: " + String.valueOf(photoFile.length()
				+ " to " + maxLength + "x" + maxLength));
		
		Bitmap photo = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
		
		Log.d("Expense CompressingPhoto", "photo is " + photo.getWidth() + "x" + photo.getHeight());
		
		//int maxLength = 750;
		
		//If the image is too large resize it to be smaller
		if(photo.getHeight() > maxLength || photo.getWidth() > maxLength){
			Double curWidth = Double.valueOf(photo.getWidth());
			Double curHeight = Double.valueOf(photo.getHeight());
			
			Double newWidth = curWidth > curHeight ? maxLength : curWidth*(maxLength/curHeight);  
			Double newHeight = curHeight > curWidth ? maxLength : curHeight*(maxLength/curWidth);
			
			Log.d("Expense CompressingPhoto", "Rescaling photo from: " + curWidth + "x" + curHeight + " to " + newWidth + "x" + newHeight);
			photo = Bitmap.createScaledBitmap(photo, newWidth.intValue(), newHeight.intValue(), false);
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(photoFile);
			photo.compress(CompressFormat.JPEG, quality, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("Expense CompressingPhoto", "Photo File not found");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Expense CompressingPhoto", "IO Exception");
			return false;
		}
		
		if (photoFile.length() < MAX_IMAGE_SIZE){
			Log.d("Expense CompressingPhoto", "Successfully compressed photo to size: " + String.valueOf(photoFile.length()));
			Log.d("Expense CompressingPhoto", "New photo is " + photo.getWidth() + "x" + photo.getHeight());
			return true;
		}
		
		return false;
	}
}
