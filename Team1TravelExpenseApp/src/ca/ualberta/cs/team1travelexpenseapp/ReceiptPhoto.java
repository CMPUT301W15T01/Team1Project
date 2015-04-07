package ca.ualberta.cs.team1travelexpenseapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import dataManagers.ReceiptPhotoManager;

public class ReceiptPhoto {

	protected File receiptFile;
	private UUID uniquePhotoId;
	private boolean photoSaved;
	
	//private ReceiptPhotoManager photoManager = new ReceiptPhotoManager();
	
	private final int MAX_IMAGE_SIZE = 65536;
	
	public File loadReceiptFile() {
		if (this.receiptFile != null){ 
			if (!this.receiptFile.exists()){
				//Try to pull photo from the web if the file does not exits
				ReceiptPhotoManager photoManager = new ReceiptPhotoManager();
				//photoManager.setContext();
				photoManager.loadPhotoFromWeb(this);					
			}
		}
		return this.receiptFile;
	}
	
	public File getReceiptFile() {
		return this.receiptFile;
	}
	
	public void setReceiptFile(File receiptFile){
		this.receiptFile = receiptFile;
	}
	
	public File initNewPhoto(){
		ReceiptPhotoManager photoManager = new ReceiptPhotoManager();
		return photoManager.initNewPhoto();
	}
	
	/**
	 * Removes the receipt photo from the expense,
	 * deletes the local file, and attempts to delete the photo 
	 * from the web as well.  
	 * 
	 */
	public void removeReceiptFile(){
		if(this.getReceiptFile() != null){
			ReceiptPhotoManager photoManager = new ReceiptPhotoManager();
			photoManager.removePhoto(this);
			if(this.getReceiptFile().exists()){
				this.getReceiptFile().delete();
			}
			this.setReceiptFile(null);
		}
	}
	
	/**
	 * Set the file of the receipt photo.
	 * @param receipt
	 * The file object for the stored image.
	 * @param context 
	 */
	public boolean createReceiptFile(File receipt) {
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
		//photoManager.setContext(context);
		photoManager.savePhotoToWeb(this);
		//photoManager.savePhotoToWeb(SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentExpense());
		return true;
	}
	
	public UUID getUniquePhotoId() {
		return uniquePhotoId;
	}

	public boolean isPhotoSavedToWeb(){
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
