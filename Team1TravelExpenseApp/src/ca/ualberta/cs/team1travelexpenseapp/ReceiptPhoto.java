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
	private boolean photoSavedToWeb;
	
	//private ReceiptPhotoManager photoManager = new ReceiptPhotoManager();
	
	private final int MAX_IMAGE_SIZE = 65536;
	
	/**
	 * The constructor for a ReceiptPhoto. Initializes
	 * values. 
	 * 
	 */
	public ReceiptPhoto(){
		this.receiptFile = null;
		this.uniquePhotoId = UUID.randomUUID();
		this.photoSavedToWeb = false;
	}
	
	/**
	 * Returns the File object for the receipt File.
	 * Attempts to load from the web if it does not exist
	 * on disk.
	 * 
	 */
	public File loadReceiptFile() {
		if (this.receiptFile != null){ 
			if (!this.receiptFile.exists()){
				//Try to pull photo from the web if the file does not exits
				ReceiptPhotoManager photoManager = new ReceiptPhotoManager();
				photoManager.loadPhotoFromWeb(this);					
			}
		}
		return this.receiptFile;
	}
	
	/**
	 * Sets the file of the receipt photo. Attempts to compress it 
	 * to be less than 65536 and attempts to save it to the web.
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
		
		ReceiptPhotoManager photoManager = new ReceiptPhotoManager();
		//photoManager.setContext(context);
		photoManager.savePhotoToWeb(this);
		//photoManager.savePhotoToWeb(SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentExpense());
		return true;
	}
	
	
	/**
	 * Just returns the File object for the receipt File.
	 * 
	 */
	public File getReceiptFile() {
		return this.receiptFile;
	}
	
	/**
	 * Just sets the File object for the receipt File to the given file.
	 * 
	 */
	public void setReceiptFile(File receiptFile){
		this.receiptFile = receiptFile;
		if(receiptFile == null){
			this.setPhotoSavedToWeb(false);
		}
	}
	
	/**
	 * Returns an initialized file to save a photo to.
	 * 
	 */
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
	 * Retrieves the unique identifier for the ReceiptPhoto
	 * 
	 */
	public UUID getUniquePhotoId() {
		return uniquePhotoId;
	}

	/**
	 * Should Return true if this photo has been successfully
	 * saved to the web
	 */
	public boolean isPhotoSavedToWeb(){
		return photoSavedToWeb;
	}
	
	/**
	 * Sets boolean state of PhotoSavedToWeb  
	 *
	 */
	public void setPhotoSavedToWeb(boolean state){
		photoSavedToWeb = state;
	}
	
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
