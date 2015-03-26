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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

/**
 * View for adding/editing an Expense item.
 * Provides a user interface from which the user can fill in the info for an Expense item.
 *
 */
public class EditExpenseActivity extends Activity {
	private ExpenseList expenseList;
	private ExpenseListController expenseListController;
	private Expense expense;
	private Listener listener;
	private Claim claim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		claim=SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim();
		expenseList = claim.getExpenseList();
		expense=SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentExpense();
		expenseListController=new ExpenseListController(expenseList);
		expenseListController.setCurrentExpense(expense);
		
		listener=new Listener() {			
			@Override
			public void update() {
				updateValues();
			}
		};
		
		expenseList.addListener(listener);
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
		
		if (claim.status == Claim.Status.submitted || 
				claim.status == Claim.Status.approved){
			//Disable UI if the claim is not editable
			descriptionView.setEnabled(false);
			dateView.setEnabled(false);
			amountView.setEnabled(false);
			
			Button saveButton = (Button) this.findViewById(R.id.saveExpenseButton);
			saveButton.setEnabled(false);
		}
		
		
		//TEMPORARY code to show the photo in the image button
		Log.d("Testing Add Photo", "File for updating? " + (expense.getReceipt() != null));
		if (expense.receipt != null){			
			thumbnailReceipt(BitmapFactory.decodeFile(expense.getReceipt().getAbsolutePath()));
			Log.d("Testing Add Photo", "Update thumbed");
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
	//Currently adds a test photo to the receipt and code that adds the photo should be moved to ExpenseListController
	public void takePhoto(View v){
	//WARNING: TEMPORARY code to add a test photo as the receipt, needs to be replaced with actually taking a photo
	Log.d("Testing Add Photo", "TakePhoto Stated");
	String filePath = String.valueOf(new Date().getTime()) + ".jpg";//ClaimListController.getUser().getName() + "/Receipts/" + (new Date().getTime()) + ".jpg";
	try{
		Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.drawable.test_photo);
		FileOutputStream fos = this.openFileOutput(filePath, Context.MODE_PRIVATE); //ClaimListController.getUser().getName() + "/Receipts/" + /*new Date().getTime() +*/ ".jpg", Context.MODE_PRIVATE);
		bm.compress(CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
		//adding the tumbnail, should be moved away from this temporary code
		thumbnailReceipt(bm);
		
		
	} catch (FileNotFoundException e) {
		Log.d("Testing Add Photo", "FileNotFound");
		e.printStackTrace();
		
	} catch (IOException e) {
		Log.d("Testing Add Photo", "IO Exception");
		e.printStackTrace();
		
	}
	
	Log.d("Testing Add Photo", "Creating File");
	File photoFile = new  File(this.getFilesDir().getAbsolutePath() + 
			"/" + filePath);
	expense.setReceipt(photoFile);
	Log.d("Testing Add Photo", "File Added to Expense? " + (expense.getReceipt() != null));
	}
	
	public void onDeletePhotoClick(View v){
		if(expense.getReceipt() != null){
			expense.getReceipt().delete();
			thumbnailReceipt(null);
			expense.setReceipt(null);
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
		viewReciept.setImageBitmap(bm);
	}
}
