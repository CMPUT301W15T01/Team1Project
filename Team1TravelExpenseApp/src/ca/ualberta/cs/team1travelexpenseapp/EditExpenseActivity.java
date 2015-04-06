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
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.team1travelexpenseapp.claims.ApprovedClaim;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.claims.SubmittedClaim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;

/**
 * View for adding/editing an Expense item.
 * Provides a user interface from which the user can fill in the info for an Expense item.
 *
 */
public class EditExpenseActivity extends Activity {
	public AlertDialog deletePhotoDialog;
	private ExpenseList expenseList;
	private ExpenseListController expenseListController;
	private Expense expense;
	private Listener listener;
	private Claim claim;
	private ImageButton receiptButton;
	File photoFile = null;
	Uri photoUri = null;
	private static final int PICK_GEOLOCATION_REQUEST = 1;
	private static final int CAMERA_CAPTURE_REQUEST = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		claim=SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim();
		expenseList = claim.getExpenseList();
		expense=SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentExpense();
		expenseListController=new ExpenseListController(expenseList);
		expenseListController.setCurrentExpense(expense);
		receiptButton = (ImageButton) findViewById(R.id.viewPhotoButton);
		
		listener=new Listener() {			
			@Override
			public void update() {
				updateValues();
			}
		};
		
		expenseList.addListener(listener);
		
	}
	
	public void GeolocationSelect(View v) {
		Intent intent = new Intent(this,OSMDroidMapActivity.class);
		startActivityForResult(intent, PICK_GEOLOCATION_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == PICK_GEOLOCATION_REQUEST ) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // The user picked a contact.
	            // The Intent's data Uri identifies which contact was selected.
	        	Double lon = data.getExtras().getDouble("longitude");
	        	Double lat = data.getExtras().getDouble("latitude");
	        	Location location = new Location("");
	        	location.setLongitude(lon);
	        	location.setLatitude(lat);
	        	expense.setLocation(location);
	        	Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();
	        
	            // Do something with the contact here (bigger example below)
	        }
	    }
		// Code for capturing the photo influenced/copied from https://github.com/dfserrano/BogoPicLab Arptil 2015
	    // Handle the results from CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
	    else if (requestCode == CAMERA_CAPTURE_REQUEST) {
	    	Log.d("Testing Add Photo", "onActivityResult Started");
			if (resultCode == RESULT_OK) {
				Log.d("Testing Add Photo", "Creating File: " + photoFile.getName());	

				//Should be moved to a controller
				attachReceipt(photoUri, photoFile);
			}
	
	    } else if (resultCode == RESULT_CANCELED) {
	    	Log.d("Testing Add Photo", "result is canceled");
	    	Toast.makeText(getApplicationContext(), "Photo not taken", Toast.LENGTH_SHORT).show();
	    } else {
	    	Log.d("Testing Add Photo", "Result is neither canceled or okay");
	    	Toast.makeText(getApplicationContext(), "An unexpected error has occured. Photo not taken", Toast.LENGTH_SHORT).show();
				
	    }
	}

	private void updateValues(){
		Spinner categorySpinner = (Spinner) this.findViewById(R.id.categorySelector);	
		for (int i = 0; i < categorySpinner.getAdapter().getCount();++i){
			if (String.valueOf(categorySpinner.getItemAtPosition(i)).equals(expense.getCategory())){
				categorySpinner.setSelection(i);
				break;
			}
		}
		EditText descriptionView = (EditText) this.findViewById(R.id.descriptionBody);
		descriptionView.setText(expense.getDescription());
		

		DatePicker dateView = (DatePicker) this.findViewById( R.id.expenseDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expense.getDate());
		dateView.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

		EditText amountView = (EditText) this.findViewById(R.id.currencyBody);
		amountView.setText(expense.getAmount().toString());
		
		Spinner currencySpinner = (Spinner) this.findViewById(R.id.currencySelector);
		for (int i = 0; i < currencySpinner.getAdapter().getCount();++i){
			if (String.valueOf(currencySpinner.getItemAtPosition(i)).equals(expense.getCurrency())){
				currencySpinner.setSelection(i);
				break;
			}
		}
		
		CheckBox flaggedCheckBox = (CheckBox) this.findViewById(R.id.incompleteCheck);
		flaggedCheckBox.setChecked(expense.isFlagged);

		
		if ( claim.getStatus() == SubmittedClaim.class || 
				claim.getStatus() == ApprovedClaim.class ){
			//Disable UI if the claim is not editable
			descriptionView.setEnabled(false);
			dateView.setEnabled(false);
			amountView.setEnabled(false);
			
			Button saveButton = (Button) this.findViewById(R.id.saveExpenseButton);
			saveButton.setEnabled(false);
			
			Button deletePhotoButton = (Button) this.findViewById(R.id.deletePhotoButton);
			deletePhotoButton.setEnabled(false);
			
			Button addPhotoButton = (Button) this.findViewById(R.id.addPhotoButton);
			addPhotoButton.setEnabled(false);
		}

		Log.d("Testing Add Photo", "File for updating? " + (expense.getReceiptFile() != null));
		if (expense.getReceiptFile() != null){			
			thumbnailReceipt(BitmapFactory.decodeFile(expense.getReceiptFile().getAbsolutePath()));
			Log.d("Testing Add Photo", "Update thumbed." + " has size: " + String.valueOf(expense.getReceiptFile().length()));
		}
	} 
	
	protected void onStart(){
		super.onStart();
		updateValues();
	}
	
	/**
	 * When the user clicks the save expense button (or back button) call the onExpenseSaveClick button in ExpenseListController
	 * @param v
	 */
	public void onExpenseSaveClick(View v) {
		//editing model happens in controller 
		expenseListController.onExpenseSaveClick(this);
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_expense, menu);
		return true;
	}
		
	protected void onDestroy(){
		super.onDestroy();
		expenseList.removeListener(listener);
		//ExpenseListController.onExpenseSaveClick(this);
	}


	/**
	 * Starts the take photo process to take a photo of a receipt
	 * @param v
	 * 
	 */
	// Code for taking a photo Heavily influenced/copied from https://developer.android.com/training/camera/photobasics.html April 2015
	// and https://github.com/dfserrano/BogoPicLab
	public void takePhoto(View v){
	Log.d("Testing Add Photo", "TakePhoto Started");
	
	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	// Create a folder to store pictures
	String folder = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/Receipts";
	File folderF = new File(folder);
	if (!folderF.exists()) {
		folderF.mkdir();
	}
	
	// Create an URI for the picture file
	String imageFilePath = folder + "/"
			+ String.valueOf(System.currentTimeMillis()) + ".jpg";
	photoFile = new File(imageFilePath);
	photoUri = Uri.fromFile(photoFile);
	
	
	Log.d("Testing Add Photo", "File created at:" + photoFile.toString() + " has size: " + String.valueOf(photoFile.length()));
	
	if (photoFile != null) {
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(photoFile));
		startActivityForResult(takePictureIntent, CAMERA_CAPTURE_REQUEST);
	}
	}
		
	public boolean onDeletePhotoClick(View v){
		if(expense.getReceiptFile() == null){
			Toast.makeText(getApplicationContext(), "There is no receipt photo to delete", Toast.LENGTH_SHORT).show();
			return false;
		}

		//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html (April, 2015)
		AlertDialog.Builder deletePhotoDialogBuilder = new AlertDialog.Builder(EditExpenseActivity.this);

		deletePhotoDialogBuilder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				DeleteReceipt();
			}
		});
		deletePhotoDialogBuilder.setPositiveButton("cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//Do nothing
			}
		});
		deletePhotoDialogBuilder.setTitle("Are you sure you want to delete the attached photo receipt?");
		
		deletePhotoDialog=deletePhotoDialogBuilder.create();

		deletePhotoDialog.show();
		return true;
	}	
    
	// Should Be moved to a controller
	public void DeleteReceipt(){
		
		if(expense.getReceiptFile() != null){
			if(expense.getReceiptFile().exists()){
				expense.getReceiptFile().delete();
			}
			thumbnailReceipt(null);
			expense.setReceiptFile(null);
			expense.setReceiptUri(null);
		}
	}
	
	/**
	 * Places a bitmap to be used as the image butotn picture
	 * @param bm
	 * The bitmap to be placed
	 * 
	 */
	//Places a photo into the button, currently places the entire photo, should maybe use a thumbnail
	protected void thumbnailReceipt(Bitmap bm){
		ImageView viewReciept = (ImageView) this.findViewById( R.id.viewPhotoButton);

		TextView receiptText = (TextView) this.findViewById(R.id.recieptHeader);
		if(bm != null && expense.getReceiptFile() != null){		
			viewReciept.setImageBitmap(bm); 
			receiptText.setText("Receipt Image " + " File Size: " + String.valueOf(expense.getReceiptFile().length()) + "Bytes");
		} else{
			receiptText.setText("Receipt Image");
			viewReciept.setImageDrawable(getResources().getDrawable(R.drawable.default_receipt));
		}
		
	}
	
	// Should Be moved to a controller
	protected void attachReceipt(Uri photoUri, File photoFile){
		if (expense.setReceiptFile(photoFile)){
			Log.d("Testing Add Photo", "File Added to Expense? " + (expense.getReceiptFile() != null) + "has size: " + String.valueOf(photoFile.length()));
			thumbnailReceipt(BitmapFactory.decodeFile(expense.getReceiptFile().getAbsolutePath()));		
		}
		else{
			Toast.makeText(getApplicationContext(), "An error occured while attempting to compress the photo", Toast.LENGTH_SHORT).show();
			Log.d("Testing Add Photo", "File failed to be compressed the Expense");
		}
		expense.setReceiptUri(photoUri);
		
	}
	
}
