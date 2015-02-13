
package ca.ualberta.cs.team1travelexpenseapp.test;

import junit.framework.TestCase;

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

import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import ca.ualberta.cs.team1travelexpenseapp.EditExpenseActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.R;

public class ExpenseEditTest extends ActivityInstrumentationTestCase2<EditExpenseActivity> {
	
	Instrumentation instrumentation;
	EditExpenseActivity activity;
	
	EditText descText;
	DatePicker datePicker;
	EditText costText;
	Spinner categorySpinner;
	Spinner currencySpinner;
	ImageButton imageButton;
	
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
		ClaimsListController.getClaims().add(0, claim);
		claim.addExpense(expense);
		activity.setExpensePos(0);
		activity.setClaimPos(0);
		activity.setClaim(claim);
		
		descText = (EditText) activity.findViewById(R.id.descriptionBody);
    	datePicker = (DatePicker) activity.findViewById( R.id.expenseDate);  
    	costText = (EditText) activity.findViewById( R.id.currencyBody);  
    	categorySpinner = (Spinner) activity.findViewById(R.id.categorySelector); 
    	currencySpinner = (Spinner) activity.findViewById(R.id.currencySelector);  
    	imageButton = (ImageButton) activity.findViewById(R.id.imageButton1);
	}
	
	// US04.01.01
	// As a claimant, I want to make one or more expense items for an expense claim,
	// each of which has a date the expense was incurred, a category, a textual description,
	// amount spent, and unit of currency.
	public void testAddExpenseItem() {
		//Expense expense = new Expense();
		expense.setDate(new Date(2005,1,8));
		expense.setCategory("fuel");
		expense.setDesc("Description");
		expense.setCost(10.55);
		expense.setCurrency("USD");
		
		// Ensure that the expense contains all of the set data
		assertEquals("Date?", new Date(2005,1,8), expense.getDate());
		assertEquals("Category?", "fuel", expense.getCategory());
		assertEquals("Description?", "Description", expense.getDesc());
		assertEquals("Amount?", 10.55, expense.getCost());
		assertEquals("Currency?", "USD", expense.getCurrency());
		
		// Ensure that the expense can be added to a claim
		assertTrue("Expense in claim?", claim.getExpenses().get(0) ==  expense);
	}
	
	//US04.02.01
	// As a claimant, I want the category for an expense item to be one of air fare, ground transport,
	// vehicle rental, private automobile, fuel, parking, registration, accommodation, meal, or supplies.
	public void testExpenseCategory() {

		// Code for initializing a list adapted on Feb 2015 from Tom's post on 
		// https://stackoverflow.com/questions/1005073/initialization-of-an-arraylist-in-one-line 
		List<String> categories = Arrays.asList("air fare", "ground transport", "vehicle rental", 
				"private automobile", "fuel", "parking", "registration", "accommodation", "meal", "supplies");

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

		// Code for initializing a list adapted on Feb 2015 from Tom's post on 
		// https://stackoverflow.com/questions/1005073/initialization-of-an-arraylist-in-one-line 
		List<String> currencies = Arrays.asList("CAD", "USD", "EUR", "GBP", "CHF", "JPY", "CNY");
				
    	//Spinner currencySpinner = (Spinner) activity.findViewById(R.id.testCurrencyspinner);  
    	
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
		// Ensure that a new expense is not flagged by default
		assertFalse("Expense flagged by default?", expense.getFlagged());
		
		expense.setFlagged(true);
		assertTrue("Expense not flagged?", expense.getFlagged());
		expense.setFlagged(false);
		
		// Ensure clicking the box toggles whether or not the expense is flagged
		CheckBox checkBox = (CheckBox) activity.findViewById(R.id.incompleteCheck);
		checkBox.performClick();
		assertTrue("Clicking check box does not flag expense", expense.getFlagged());		
		checkBox.performClick();
		assertFalse("Clicking check box does not unflag expense", expense.getFlagged());	
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
		claim.setStatus("Submitted");
		
		Button saveButton = (Button) activity.findViewById(R.id.saveExpenseButton);
		saveButton.performClick();
		assertNotSame("Desc edited while submitted", descText.getText().toString(), "Test Edit");
	
		claim.setStatus("in progress");		
		saveButton.performClick();		
		assertEquals("Desc not saved after edit", descText.getText().toString(), "Test Edit");
	}
	
	// US04.07.01
	// As a claimant, I want to delete an expense item while changes are allowed.	
	public void testDeleteExpense(){
		Expense expense2 = new Expense();
		claim.getExpenses().add(1, expense2);;
		claim.removeExpense(1);
		assertEquals("New expense not deleted", claim.getExpenses().size(), 1);
	}
	
	// US06.01.01
	// As a claimant, I want to optionally take a single photograph of a receipt and attach the
	// photograph to an editable expense item, so that there is supporting documentation for the 
	// expense item in case the physical receipt is lost.
	public void testAddPhoto(){	
		imageButton.setImageDrawable(null);	
		assertTrue("Image should not be set in button", imageButton.getDrawable() == null);	
		Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		imageButton.setImageBitmap(bm);
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);	
		
		imageButton.setImageDrawable(null);	
		expense.setPhotoUri = null;
		
		claim.setStatus("Submitted");
		expense.setPhoto(bm);
		assertTrue("Image should not be set in button", imageButton.getDrawable() == null);	
		assertTrue("Image File should not be set", expense.getPhotoFile() != null);
		
		claim.setStatus("In progress");
		expense.setPhoto(bm);
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);
		assertTrue("Image File should be set", expense.getPhotoFile() != null);
	}
	
	// US06.02.01
	// As a claimant, I want to view any attached photographic receipt for an expense 
	public void testViewPhoto(){
		assertTrue("Image not visable?", imageButton.getVisibility() == View.VISIBLE);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), imageButton);
	}
	
	// US06.03.01
	// As a claimant, I want to delete any attached photographic receipt on an editable expense item, so that unclear images can be re-taken.
	public void testDeletePhoto(){
		imageButton.setImageDrawable(null);	
		expense.setPhotoUri = null;
		
		Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		expense.setPhoto(bm);
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);
		
		Button deletePhotoButton = (Button) activity.findViewById(R.id.button2);
		claim.setStatus("Submitted");
		deletePhotoButton.performClick();
		
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);
		assertTrue("Image File should be set", expense.getPhotoFile() != null);
		
		
		claim.setStatus("In progress");
		deletePhotoButton.performClick();
		
		assertTrue("Image should not be set in button", imageButton.getDrawable() == null);
		assertTrue("Image File should be null", expense.getPhotoFile() == null);
	}
	
	// US06.04.01
	// As a sysadmin, I want each receipt image file to be under 65536 bytes in size.
	public void testMaxPhotoSize(){
		Bitmap bm = Bitmap.createBitmap(130, 130, Bitmap.Config.ARGB_8888); //should be 67600 bytes in size
		Bitmap cbm = activity.compressPhoto(bm);
		Bitmap cbm = bm;
		assertTrue("Compressed photo too large (" + cbm.getByteCount() + ")", cbm.getByteCount() < 65536);
	}
	
		
	
	
}


