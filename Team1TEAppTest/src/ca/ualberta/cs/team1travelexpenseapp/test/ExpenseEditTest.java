package ca.ualberta.cs.team1travelexpenseapp.test;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.EditExpenseActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.User;

public class ExpenseEditTest extends ActivityInstrumentationTestCase2<EditExpenseActivity> {
	
	Instrumentation instrumentation;
	EditExpenseActivity activity;
	
	EditText descText;
	DatePicker datePicker;
	EditText costText;
	Spinner currencySpinner;
	Spinner categorySpinner;
	ImageButton imageButton;
	Button saveButton; 
	File photoFile;
	
	Expense expense;
	Claim claim;
	public ExpenseEditTest() {
		super(EditExpenseActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		instrumentation = getInstrumentation();
		activity = getActivity();	
		
		expense = new Expense();
		claim = new Claim();
		ClaimListController.setCurrentClaim(claim); 
		claim.addExpense(expense);
		//activity.setExpensePos(0);
		//activity.setClaimPos(0);
		//activity.setClaim(claim);
		
		
		
		descText = (EditText) activity.findViewById(R.id.descriptionBody);
    	datePicker = (DatePicker) activity.findViewById( R.id.expenseDate);  
    	costText = (EditText) activity.findViewById( R.id.currencyBody);  
    	categorySpinner = (Spinner) activity.findViewById(R.id.categorySelector); 
    	currencySpinner = (Spinner) activity.findViewById(R.id.currencySelector);  
    	imageButton = (ImageButton) activity.findViewById(R.id.imageButton1);
		saveButton = (Button) activity.findViewById(R.id.saveExpenseButton);
    	photoFile = createTestPhotoFile();
	}
	
	// US04.01.01
	// As a claimant, I want to make one or more expense items for an expense claim,
	// each of which has a date the expense was incurred, a category, a textual description,
	// amount spent, and unit of currency.
	@SuppressWarnings("deprecation")
	public void testAddExpenseItem() {
		// precondition
		User user = User.getUser();
		assertEquals("User is claimant", "Claimant", user.type());
		//trigger
		final Button button1 = (Button) activity.findViewById(R.id.addExpenseButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button1.performClick();
		    }
		});
		
		Spinner category = (Spinner) activity.findViewById(R.id.spinner1);
		category.setSelection(0);
		
		DatePicker date = (DatePicker) activity.findViewById(R.id.expenseDate);
		date.updateDate(2005, 1, 8);
		
		EditText editDescription = (EditText) activity.findViewById(R.id.descriptionBody);
		editDescription.setText("Description");
		
		EditText editCost = (EditText) activity.findViewById(R.id.currencyBody);
		expense.setAmount(BigDecimal.valueOf(10.55));
		editCost.setText(expense.getAmount().toString());
		Spinner currency = (Spinner) activity.findViewById(R.id.currencySelector);
		currency.setSelection(0);
		
		//save claim
		final Button button2 = (Button) activity.findViewById(R.id.saveExpenseButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button2.performClick();
		    }
		});
		
		// Ensure that the expense contains all of the set data
		assertEquals("Date?", new Date(2005/1/8), expense.getDate());
		assertEquals("Category?", "fuel", expense.getCategory());
		assertEquals("Description?", "Description", expense.getDescription());
		assertEquals("Amount?", 10.55, expense.getAmount());
		assertEquals("Currency?", "USD", expense.getCurrency());
		
		// Ensure that the expense can be added to a claim
		// ------------- assertTrue("Expense in claim?", claim.getExpenseList().getExpense(expense) ==  expense);
	}
	
	
	//US04.02.01
	// As a claimant, I want the category for an expense item to be one of air fare, ground transport,
	// vehicle rental, private automobile, fuel, parking, registration, accommodation, meal, or supplies.
	public void testExpenseCategory() {

		
		final Button button = (Button) activity.findViewById(R.id.addExpenseButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button.performClick();
		    }
		});

		ArrayList<String> categories = new ArrayList<String>();
		categories.add("air fare");
		categories.add("ground transport");
		categories.add("vehicle rental");
		categories.add("private automobile");
		categories.add("fuel");
		categories.add("parking");
		categories.add("registration");
		categories.add("accommodation");
		categories.add("meal");
		categories.add("supplies");
		Spinner categorySpinner = (Spinner) activity.findViewById(R.id.categorySelector);
    	// Ensure that there are the right number of categories in the spinner
    	assertEquals("Amount of categories?", categories.size(), categorySpinner.getAdapter().getCount());
    	
    	// Ensure that the choices in the spinner are the desired categories
    	for (int i = 0; i < categorySpinner.getAdapter().getCount();++i){
    		String category = String.valueOf(categorySpinner.getItemAtPosition(i));
    		assertTrue("Improper category: " + category, 
    				categories.contains(category));
			}
    	
    	// Ensure that when user does not select a category the displayed one is used 
    	expense.setCategory(String.valueOf(categorySpinner.getSelectedItem()));
    	assertEquals("Expense category set to displayed?", expense.getCategory(), categorySpinner.getSelectedItem());
		}

	// US04.03.01
	// As a claimant, I want the currency for an expense amount to be one of 
	// CAD, USD, EUR, GBP, CHF, JPY, or CNY.
	public void testAmountType() {
		// go into add/edit expense
		final Button button = (Button) activity.findViewById(R.id.addExpenseButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button.performClick();
		    }
		});
		
		// Code for initializing a list adapted on Feb 2015 from Tom's post on 
		// https://stackoverflow.com/questions/1005073/initialization-of-an-arraylist-in-one-line 
		List<String> currencies = Arrays.asList("CAD", "USD", "EUR", "GBP", "CHF", "JPY", "CNY");
				
    	Spinner currencySpinner = (Spinner) activity.findViewById(R.id.currencySelector);  
    	
    	// Ensure that there are the right number of currencies in the spinner
    	assertEquals("Amount of categories?", currencies.size(), currencySpinner.getAdapter().getCount());
    	
    	// Ensure that the choices in the spinner are the desired currencies
    	for (int i = 0; i < currencySpinner.getAdapter().getCount();++i){
    		String currency = String.valueOf(currencySpinner.getItemAtPosition(i));
    		assertTrue("Improper category: " + currency, 
    				currencies.contains(currency));
			}
    	
    	// Ensure that when user does not select a currency the displayed one is used 
    	//Expense expense = new Expense();
    	expense.setCurrency(String.valueOf(currencySpinner.getSelectedItem()));
    	assertEquals("Expense category set to displayed?", expense.getCurrency(), currencySpinner.getSelectedItem());
		}
	
	// US04.04.01
	// As a claimant, I want to manually flag an expense item with an incompleteness indicator, 
	// even if all item fields have values, so that I am reminded that the item needs further editing.
	public void testFlagExpense() {
		//Claim claim = ClaimListController.getClaim(0);
		//Expense expense = claim.getExpense(0);
		
		final Button button = (Button) activity.findViewById(R.id.addExpenseButton);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button.performClick();
		    }
		});
		
		// Ensure clicking the box toggles whether or not the expense is flagged
		final CheckBox checkBox = (CheckBox) activity.findViewById(R.id.incompleteCheck);
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		    	checkBox.performClick();
		    }
		});
		
		assertTrue("Clicking check box does not flag expense", expense.isComplete());		
		activity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		    	checkBox.performClick();
		    }
		});
		assertFalse("Clicking check box does not unflag expense", expense.isComplete());	
	}
	
	// US04.05.01
	// As a claimant, I want to view an expense item and its details.
	public void testViewExpenseDetails() {  	
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				descText.setText("Test View");
				datePicker.updateDate(2005, 5, 5);
				costText.setText((Double.toString(5.55)));
				categorySpinner.setSelection(5);
				currencySpinner.setSelection(5);	
			}
		});
		instrumentation.waitForIdleSync();
		assertEquals("Edit test not set right", descText.getText().toString(), "Test View");
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), descText);
		
		assertEquals("Date not set right", datePicker.getYear(), 2005);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), datePicker);
		
		assertEquals("Amount not set right", costText.getText().toString(), "10.55");
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), costText);
		
		assertEquals("category not set right", String.valueOf(categorySpinner.getSelectedItem()), String.valueOf(categorySpinner.getItemAtPosition(5)));
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), categorySpinner);
		
		assertEquals("currency not set right", String.valueOf(currencySpinner.getSelectedItem()), String.valueOf(currencySpinner.getItemAtPosition(5)));
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), currencySpinner);
	}
	//Button saveButton = (Button) activity.findViewById(R.id.expSaveButton);
	
	// US04.06.01
	// As a claimant, I want to edit an expense item while changes are allowed.
	
	/*
	public void testEditExpense(){
		
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				descText.setText("Test Edit");
				datePicker.updateDate(2000, 1, 4);
				costText.setText((Double.toString(4.44)));
				categorySpinner.setSelection(4);
				currencySpinner.setSelection(4);	
			}
		});
		claim.setStatus(status.SUBMITTED);

		saveButton.performClick();
		assertNotSame("Desc edited while submitted", descText.getText().toString(), "Test Edit");
	
		claim.setStatus(status.IN_PROGRESS);		
		saveButton.performClick();		
		assertEquals("Desc not saved after edit", descText.getText().toString(), "Test Edit");
	}
	
	// US04.07.01
	// As a claimant, I want to delete an expense item while changes are allowed.	
	public void testDeleteExpense(){
		//preconditions - there's an expense item to delete
		Claim claim = new Claim();
		Expense expense = new Expense();
		claim.addExpense(expense);
		
		// click on an expense and hit delete button
		final View item = expenseListView.getAdapter().getView(0, null, null);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// click button, should produce dialog to choose edit or delete claim
				item.performLongClick();
				AlertDialog dialog=activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.expenseDialog);
				
				//this should click the delete button in the dialog
				Button deleteButton=(Button)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.deleteExpenseButton);
			    deleteButton.performClick();
			}
		});
		
		assertEquals("New expense not deleted", claim.getExpenses().size(), 1);
	}
	
	// US06.01.01
	// As a claimant, I want to optionally take a single photograph of a receipt and attach the
	// photograph to an editable expense item, so that there is supporting documentation for the 
	// expense item in case the physical receipt is lost.
	public void testAddPhoto(){	
		
		// An editable expense is currently being edited or added
		imageButton.setImageDrawable(null);	
		assertTrue("Image should not be set in button", imageButton.getDrawable() == null);	
		Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		imageButton.setImageBitmap(bm);
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);	
		imageButton.setImageDrawable(null);	
		expense.setReceipt(null);
		
		// A test photo is added to simulate an actual photo being taken
		expense.setReceipt(photoFile);
		
		claim.setStatus(status.SUBMITTED);
		saveButton.performClick();
		// restart the activity
		activity.finish();
		activity = getActivity();
		assertTrue("Submitted, Image should not be set in button", imageButton.getDrawable() == null);	
		claim.setStatus(status.IN_PROGRESS);
		saveButton.performClick();
		// restart the activity
		activity.finish();
		activity = getActivity();
		
		// A photo is attached to the expense
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);
		assertTrue("Image File should be set", expense.getReceipt() != null);
	}
	
	*/
	
	// US06.02.01
	// As a claimant, I want to view any attached photographic receipt for an expense 
	public void testViewPhoto(){
		assertTrue("Image not visable?", imageButton.getVisibility() == View.VISIBLE);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), imageButton);
		
		expense.setReceipt(photoFile);
		saveButton.performClick();
		// restart the activity
		activity.finish();
		activity = getActivity();
		
		// An expense is currently being edited or added and a photo is already attached to it
		
		assertTrue("Image not visable?", imageButton.getVisibility() == View.VISIBLE);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), imageButton);
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);
		assertTrue("Image File should be set", expense.getReceipt() != null);
		
		// A user can see the thumbnail of the photo 
	}
	
	// US06.03.01
	// As a claimant, I want to delete any attached photographic receipt on an editable expense item, so that unclear images can be re-taken.
	/*
	public void testDeletePhoto(){
		expense.setReceipt(photoFile);
		saveButton.performClick();
		// restart the activity
		activity.finish();
		activity = getActivity();
		//An editable expense is currently being edited or added and a photo is already attached to it 

		
		assertTrue("Image set so should be set in button", imageButton.getDrawable() != null);
		assertTrue("Image set so file should be set", expense.getReceipt() != null);
		
		//click delete photo 
		activity.findViewById(R.id.deletePhotoButton).performClick();	
		//click yes
		activity.findViewById(R.id.yesdeletePhotoButton).performClick();
		assertTrue(claim.getStatus().equals(status,SUBMITTED));
		
		//The editable expense no longer has a photo attached to it
		assertTrue("Image deleted so should be not be set in button", imageButton.getDrawable() == null);
		assertTrue("Image deleted so file should not be set", expense.getReceipt() == null);
	}
	
	// US06.04.01
	// As a sysadmin, I want each receipt image file to be under 65536 bytes in size. (less than 65.536 KB)
	public void testMaxPhotoSize(){	
		// An editable expense is currently being edited or added and the claimant has taken a photo that is greater than 65536 bytes 
		
		// The program attempts to reduce the image to make it less than 65536 bytes in size.
		photoFile = activity.compressPhoto(photoFile);
		assertTrue("Compressed photo file too large (" + photoFile.length() + ")", expense.getReceipt().length() < 65536);	
		
		// A photo that is less than 65536 bytes in size is attached to the expense
		expense.setReceipt(photoFile);
		assertTrue("Compressed photo file too large (" + expense.getReceipt().length() + ")", expense.getReceipt().length() < 65536);	
	}
	*/
	//Create a Photo file for testing to avoid creating a unique file each time these tests are run
	private File createTestPhotoFile(){	
		try {		
			Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.test_photo);
			FileOutputStream fos = activity.openFileOutput("testPhoto.jpg", Context.MODE_PRIVATE);
			bm.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return new File(activity.getFilesDir().getAbsolutePath() + "/testPhoto.jpg");
	}
}


