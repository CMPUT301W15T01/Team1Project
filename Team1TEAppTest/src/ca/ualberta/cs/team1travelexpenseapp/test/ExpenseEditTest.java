package ca.ualberta.cs.team1travelexpenseapp.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import ca.ualberta.cs.team1travelexpenseapp.ClaimListController;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantClaimsListActivity;
import ca.ualberta.cs.team1travelexpenseapp.ClaimantExpenseListActivity;
import ca.ualberta.cs.team1travelexpenseapp.EditExpenseActivity;
import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.ExpenseListController;
import ca.ualberta.cs.team1travelexpenseapp.LoginActivity;
import ca.ualberta.cs.team1travelexpenseapp.R;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import ca.ualberta.cs.team1travelexpenseapp.users.User;


//Tests adding, editing, and deleting expenseitems.
//When trying to run all the tests in a row one might stop or hang and cause the rest to not run
//but running each individually should give results. 

public class ExpenseEditTest extends ActivityInstrumentationTestCase2<ClaimantClaimsListActivity> {
	
	protected Instrumentation instrumentation;
	protected EditExpenseActivity activity = null;
	protected ClaimantExpenseListActivity listActivity = null;
	protected ClaimantClaimsListActivity claimlistActivity = null;
	//protected ActivityMonitor activityMonitor;
	
	protected EditText descText;
	protected DatePicker datePicker;
	protected EditText costText;
	protected Spinner currencySpinner;
	protected Spinner categorySpinner;
	protected ImageButton imageButton;
	protected Button saveButton; 
	protected File photoFile;
	
	protected Expense expense;
	protected Claimant user;
	protected Claim claim;
	
	protected ExpenseListController ExpenseListController;
	protected ClaimListController ClaimListController;
	
	
	public ExpenseEditTest() throws Exception {
		super(ClaimantClaimsListActivity.class);
	}
	protected void tearDown() throws Exception{
		cleanUp();
		super.tearDown();
		
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		Log.d("EditExpenseTest", "Setup Started");
		Claimant user= new Claimant("CoolGuy");
		Log.d("EditExpenseTest", "User Created");
		UserSingleton.getUserSingleton().setUser(user);
		//user.getClaimList().getClaims().clear();
		Log.d("EditExpenseTest", "User Singleton set");
		ClaimListController = new ClaimListController(user.getClaimList());
		
		user.getClaimList().getManager().setContext(getActivity().getApplicationContext());
		
		Log.d("EditExpenseTest", "ClaimList has a manager? " + String.valueOf(user.getClaimList().getManager() != null));
		instrumentation = getInstrumentation();
		claimlistActivity = getActivity();
		getExpenseListactivity();
	}
	
	protected void cleanUp(){
		instrumentation.waitForIdleSync();
		if(activity != null){
			activity.finish();
		}
		if(listActivity != null){
			listActivity.finish();
		}

		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run() {
				if (ExpenseListController.getExpenseList().getExpenses().contains(expense)){
					ExpenseListController.removeExpense(expense);
			    }
				if(ClaimListController.getClaimList().getClaims().contains(claim)){
					ClaimListController.deleteClaim(claim);
			    }
			}
		});
		instrumentation.waitForIdleSync();

	}
	
	// US04.01.01
	// As a claimant, I want to make one or more expense items for an expense claim,
	// each of which has a date the expense was incurred, a category, a textual description,
	// amount spent, and unit of currency.
	public void testAddExpenseItem() {
		// precondition
		activity = getAddExpenseActivity();	
		//User user = new User("Claimant", "EditExpenseTest");
		//assertEquals("User is claimant", "Claimant", user.type());
		
		instrumentation.runOnMainSync(new Runnable(){
		    @Override
		    public void run() {
		       categorySpinner.setSelection(5);
			   datePicker.updateDate(2005, 1, 8);
			   descText.setText("Description");
			   costText.setText(BigDecimal.valueOf(10.55).toString());
			   currencySpinner.setSelection(5);
			   saveButton.performClick();
		    }
		});
		instrumentation.waitForIdleSync();
		
		expense = ExpenseListController.getCurrentExpense();
		
		// Ensure that the expense contains all of the set data
		Calendar   calendar   = Calendar.getInstance();
		calendar.set(2005, 1, 8);
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		assertEquals("Date?", dateformat.format(calendar.getTime()), dateformat.format(expense.getDate()));
		assertEquals("Category?", categorySpinner.getItemAtPosition(5), expense.getCategory());
		assertEquals("Description?", "Description", expense.getDescription());
		assertEquals("Amount?", BigDecimal.valueOf(10.55), expense.getAmount());
				assertEquals("Currency?", currencySpinner.getItemAtPosition(5), expense.getCurrency());

		// Ensure that the expense can be added to a claim
		assertTrue("Expense in claim?", claim.getExpenseList().getExpenses().contains(expense));
	}
	
	
	//US04.02.01
	// As a claimant, I want the category for an expense item to be one of air fare, ground transport,
	// vehicle rental, private automobile, fuel, parking, registration, accommodation, meal, or supplies.
	public void testExpenseCategory() {

		activity = getAddExpenseActivity();	
		
		// Code for initializing a list adapted on Feb 2015 from Tom's post on 
		// https://stackoverflow.com/questions/1005073/initialization-of-an-arraylist-in-one-line 
		List<String> categories = Arrays.asList(activity.getResources().getStringArray(R.array.expense_categories));

		Spinner categorySpinner = (Spinner) activity.findViewById(R.id.categorySelector);
    	// Ensure that there are the right number of categories in the spinner
    	assertEquals("Amount of categories?", categories.size(), categorySpinner.getAdapter().getCount());
    	
    	// Ensure that the choices in the spinner are the desired categories
    	for (int i = 0; i < categorySpinner.getAdapter().getCount();++i){
    		String category = String.valueOf(categorySpinner.getItemAtPosition(i));
    		assertTrue("Improper category: " + category, categories.contains(category));
			}
    	
    	// Ensure that when user does not select a category the displayed one is used 
    	expense.setAmount(BigDecimal.valueOf(1));
    	expense.setCategory(String.valueOf(categorySpinner.getSelectedItem()));
    	assertEquals("Expense category set to displayed?", expense.getCategory(), categorySpinner.getSelectedItem());
    	
		}

	// US04.03.01
	// As a claimant, I want the currency for an expense amount to be one of 
	// CAD, USD, EUR, GBP, CHF, JPY, or CNY.
	public void testAmountType() {
		// go into add/edit expense
		activity = getAddExpenseActivity();	
		
		// Code for initializing a list adapted on Feb 2015 from Tom's post on 
		// https://stackoverflow.com/questions/1005073/initialization-of-an-arraylist-in-one-line 
		List<String> currencies = Arrays.asList(activity.getResources().getStringArray(R.array.currencies));
		
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
		
		activity = getAddExpenseActivity();	
		
		// Ensure clicking the box toggles whether or not the expense is flagged
		final CheckBox checkBox = (CheckBox) activity.findViewById(R.id.incompleteCheck);
		instrumentation.runOnMainSync(new Runnable(){
		    @Override
		    public void run() {
		      // click button and open next activity.
		    	checkBox.performClick();
		    }
		});
		instrumentation.waitForIdleSync();	
		assertTrue("Clicking check box does not flag expense", expense.isComplete());		
		instrumentation.runOnMainSync(new Runnable(){
		    @Override
		    public void run() {
		      // click button and open next activity.
		    	checkBox.performClick();
		    }
		});
		instrumentation.waitForIdleSync();	
		assertFalse("Clicking check box does not unflag expense", expense.isComplete());	
	}
	
	// US04.05.01
	// As a claimant, I want to view an expense item and its details.
	
	public void testViewExpenseDetails() {  	
		
		activity = getEditExpenseActivity();
		
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
		
		activity = getEditExpenseActivity();	
		
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
		instrumentation.waitForIdleSync();	
		//claim.setStatus(Claim.Status.submitted); -------------------------------------------------------------------
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				saveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();	
		
		assertNotSame("Desc edited while submitted", descText.getText().toString(), "Test Edit");
	
		//claim.setStatus(Claim.Status.inProgress);		---------------------------------------------------------------
		//claim.setStatus(Claim.Status.submitted); --------------------------------------------------------------------
		
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				saveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();	
		
		assertEquals("Desc not saved after edit", descText.getText().toString(), "Test Edit");
	}

	// US04.07.01
	// As a claimant, I want to delete an expense item while changes are allowed.	

	public void testDeleteExpense() throws InterruptedException{
		//preconditions - there's an expense item to delete
		final Expense expense1 = new Expense();
		final Expense expense2 = new Expense();
		final Expense expense3 = new Expense();
		
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run() {
				ExpenseListController.addExpense(expense1);
				ExpenseListController.addExpense(expense2);
				ExpenseListController.addExpense(expense3);
			}
		});
		instrumentation.waitForIdleSync();
		assertEquals("New expenses not added", 3, claim.getExpenseList().getExpenses().size());
		
		final ListView listOfExpenses = (ListView) listActivity.findViewById(R.id.claimantExpensesList);

		// click on an expense and hit delete button
		
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run() {
				View item = listOfExpenses.getChildAt(1);
				// click button, should produce dialog to choose edit or delete claim
				item.performLongClick();
				AlertDialog dialog=listActivity.editExpenseDialog;
				
				//this should click the delete button in the dialog
				Button deleteButton=(Button)dialog.getButton(android.content.DialogInterface.BUTTON_NEGATIVE);
			    deleteButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();
		
		//assertEquals("New expense not deleted", claim.getExpenseList().getExpenses().size(), 0);
		
		//listActivity.finish();

		ArrayList <Expense> expectedExpenses=new ArrayList<Expense>();
		expectedExpenses.add(expense1);
		expectedExpenses.add(expense3);
		
		assertTrue("Wrong expense removed after deletion", 
				claim.getExpenseList().getExpenses().containsAll(expectedExpenses));
		assertFalse("Deleted expense remains after deletion",
				claim.getExpenseList().getExpenses().contains(expense2));
		assertEquals("Size of expense list after deletion is not as expected",
				claim.getExpenseList().getExpenses().size(), 2);
		
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run() {
				View item = listOfExpenses.getChildAt(0);
				// click button, should produce dialog to choose edit or delete claim
				item.performLongClick();
				AlertDialog dialog=listActivity.editExpenseDialog;
				
				//this should click the delete button in the dialog
				Button deleteButton=(Button)dialog.getButton(android.content.DialogInterface.BUTTON_NEGATIVE);
			    deleteButton.performClick();
				//ExpenseListController.removeExpense(expense1);
				//ExpenseListController.removeExpense(expense3);
			}
		});
		instrumentation.waitForIdleSync();
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run() {
				View item = listOfExpenses.getChildAt(0);
				// click button, should produce dialog to choose edit or delete claim
				item.performLongClick();
				AlertDialog dialog=listActivity.editExpenseDialog;
				
				//this should click the delete button in the dialog
				Button deleteButton=(Button)dialog.getButton(android.content.DialogInterface.BUTTON_NEGATIVE);
			    deleteButton.performClick();
				//ExpenseListController.removeExpense(expense1);
				//ExpenseListController.removeExpense(expense3);
			}
		});
		instrumentation.waitForIdleSync();
		assertEquals("Size of expense list after deleting all expenses is not as expected",
				claim.getExpenseList().getExpenses().size(), 0);
	}
	
	// US06.01.01
	// As a claimant, I want to optionally take a single photograph of a receipt and attach the
	// photograph to an editable expense item, so that there is supporting documentation for the 
	// expense item in case the physical receipt is lost.
	
	public void testAddPhoto(){	
		activity = getEditExpenseActivity();	
		photoFile = createTestPhotoFile();
		
		// An editable expense is currently being edited or added
		
		// A test photo is added to simulate an actual photo being taken
		expense.setReceiptFile(photoFile);
		assertNotNull("Photofile not added to expence", expense.getReceiptFile());
		
		//claim.setStatus(Claim.Status.submitted); -----------------------------------------------------------------------------
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				saveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();	
		// restart the activity
		//activity.finish();
		ActivityMonitor activityMonitor = instrumentation.addMonitor(EditExpenseActivity.class.getName(), null, false);
		final ListView listOfExpenses = (ListView) listActivity.findViewById(R.id.claimantExpensesList);
		 listActivity.runOnUiThread(new Runnable() {
			    @Override
			    public void run() {
			      // click an existing expense to open next activity.
			    	listOfExpenses.performItemClick(listOfExpenses.getAdapter().getView(0, null, null),0, 0);
			    }
		});
		instrumentation.waitForIdleSync();	
		activity = (EditExpenseActivity) instrumentation.waitForMonitorWithTimeout(activityMonitor, 10000); 	
		
		assertNotNull(activity);
		setUiElements();
		//activity = getAddExpenseActivity();
		assertTrue("Submitted, Image should not be set in button", imageButton.getDrawable() == null);	
		//claim.setStatus(Claim.Status.inProgress); ------------------------------------------------------------------------------
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				saveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();	
		
		// A photo is attached to the expense
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);
		assertTrue("Image File should be set", expense.getReceiptFile() != null);
		
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				imageButton.setImageDrawable(null);	
			}
		});
		instrumentation.waitForIdleSync();	
		
		assertTrue("Image should not be set in button", imageButton.getDrawable() == null);	
		final Bitmap bm = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				imageButton.setImageBitmap(bm);
			}
		});
		instrumentation.waitForIdleSync();	
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);	
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				imageButton.setImageDrawable(null);	
			}
		});
		instrumentation.waitForIdleSync();	
		expense.setReceiptFile(null);
		
	}
	
	
	
	// US06.02.01
	// As a claimant, I want to view any attached photographic receipt for an expense 
	
	public void testViewPhoto(){
		
		
		activity = getAddExpenseActivity();	
		photoFile = createTestPhotoFile();
		
		assertTrue("Image not visable?", imageButton.getVisibility() == View.VISIBLE);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), imageButton);
		
		expense.setReceiptFile(photoFile);
		assertNotNull("Photofile not added to expence", expense.getReceiptFile());
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				saveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();	
		activity.finish();
		activity = getAddExpenseActivity();
		
		// An expense is currently being edited or added and a photo is already attached to it
		
		assertTrue("Image not visable?", imageButton.getVisibility() == View.VISIBLE);
		ViewAsserts.assertOnScreen(activity.getWindow().getDecorView(), imageButton);
		assertTrue("Image should be set in button", imageButton.getDrawable() != null);
		assertTrue("Image File should be set", expense.getReceiptFile() != null);
		
		// A user can see the thumbnail of the photo 
	}
	
	// US06.03.01
	// As a claimant, I want to delete any attached photographic receipt on an editable expense item, so that unclear images can be re-taken.
	
	public void testDeletePhoto(){
		
		activity = getEditExpenseActivity();
		photoFile = createTestPhotoFile();
		
		expense.setReceiptFile(photoFile);
		assertNotNull("Photofile not added to expence", expense.getReceiptFile());
		instrumentation.runOnMainSync(new Runnable(){
			@Override
			public void run(){
				saveButton.performClick();
			}
		});
		instrumentation.waitForIdleSync();	
		// restart the activity
		activity.finish();
		activity = getAddExpenseActivity();
		//An editable expense is currently being edited or added and a photo is already attached to it 

		
		assertTrue("Image set so should be set in button", imageButton.getDrawable() != null);
		assertTrue("Image set so file should be set", expense.getReceiptFile() != null);
		
		//click delete photo 
		activity.findViewById(R.id.deletePhotoButton).performClick();	
		//click yes
		//activity.findViewById(R.id.yesdeletePhotoButton).performClick();
		//assertSame(ClaimListController.getCurrentClaim().getStatus(),Claim.Status.submitted); -------------------------------------------------------
		
		//The editable expense no longer has a photo attached to it
		assertTrue("Image deleted so should be not be set in button", imageButton.getDrawable() == null);
		assertTrue("Image deleted so file should not be set", expense.getReceiptFile() == null);
		
	}
	
	// US06.04.01
	// As a sysadmin, I want each receipt image file to be under 65536 bytes in size. (less than 65.536 KB)
	public void testMaxPhotoSize(){	
		
		
		activity = getEditExpenseActivity();	
		photoFile = createTestPhotoFile();
		// An editable expense is currently being edited or added and the claimant has taken a photo that is greater than 65536 bytes 
		
		expense.setReceiptFile(photoFile);
		assertNotNull("Photofile not added to expence", expense.getReceiptFile());
		
		// The program attempts to reduce the image to make it less than 65536 bytes in size.
		Expense.compressPhoto(activity, photoFile);
		assertTrue("Compressed photo file too large (" + photoFile.length() + ")", expense.getReceiptFile().length() < 65536);	
		
		// A photo that is less than 65536 bytes in size is attached to the expense
		expense.setReceiptFile(photoFile);
		assertTrue("Compressed photo file too large (" + expense.getReceiptFile().length() + ")", expense.getReceiptFile().length() < 65536);
		
	}
	private ClaimantExpenseListActivity getExpenseListactivity(){
		
		ActivityMonitor activityMonitor = instrumentation.addMonitor(ClaimantExpenseListActivity.class.getName(), null, false);
		final ListView listOfClaims = (ListView) claimlistActivity.findViewById(R.id.claimsList);
		//claimlistActivity.runOnUiThread(new Runnable() {
		instrumentation.runOnMainSync(new Runnable(){
			    @Override
			    public void run() {
					claim = new Claim();
					ClaimListController.addClaim(claim); 
					ClaimListController.updateCurrentClaim(claim); 
					
					ExpenseListController = new ExpenseListController(claim.getExpenseList());
			    }
		});
		instrumentation.waitForIdleSync();
		
		
		//instrumentation.runOnMainSync(new Runnable(){
		instrumentation.runOnMainSync(new Runnable(){
		    @Override
		    public void run() {
		    	listOfClaims.performItemClick(listOfClaims.getAdapter().getView(0, null, null),0,listOfClaims.getAdapter().getItemId(0));
		    	//listOfClaims.performItemClick(listOfClaims.getChildAt(0),0,listOfClaims.getAdapter().getItemId(0));
		    }
		});
		instrumentation.waitForIdleSync();	
		listActivity = (ClaimantExpenseListActivity) instrumentation.waitForMonitorWithTimeout(activityMonitor, 10000); 
		assertNotNull(listActivity);
		return listActivity;
	}
	
	//transitions from the ExpenseList to EditExpense by clicking add expense
	private EditExpenseActivity getAddExpenseActivity(){
		
		ActivityMonitor activityMonitor = instrumentation.addMonitor(EditExpenseActivity.class.getName(), null, false);
		
		final Button addButton = (Button) listActivity.findViewById(R.id.addExpenseButton);
		
		instrumentation.runOnMainSync(new Runnable(){
			    @Override
			    public void run() {
			      // click button and open next activity.
			    	addButton.performClick();
			    }
		});
		instrumentation.waitForIdleSync();	
		activity = (EditExpenseActivity) instrumentation.waitForMonitorWithTimeout(activityMonitor, 10000); 	
		assertNotNull(activity);
		setUiElements();
		
		expense = ExpenseListController.getCurrentExpense();
		return activity;
	}
	
	//transitions from the ExpenseList to EditExpense by clicking an existing expense
	private EditExpenseActivity getEditExpenseActivity(){
		
		ActivityMonitor activityMonitor = instrumentation.addMonitor(EditExpenseActivity.class.getName(), null, false);

		final ListView listOfExpenses = (ListView) listActivity.findViewById(R.id.claimantExpensesList);
		instrumentation.runOnMainSync(new Runnable(){
		    @Override
		    public void run() {
				expense = new Expense();
				ExpenseListController.addExpense(expense);
				//ExpenseListController.updateExpense(expense, expense);
		    }
		});
		instrumentation.waitForIdleSync();
		
		instrumentation.runOnMainSync(new Runnable(){
			    @Override
			    public void run() {
			      // click an existing expense to open next activity.
			    	listOfExpenses.performItemClick(listOfExpenses.getAdapter().getView(0, null, null),0, 0);
			    }
		});
		instrumentation.waitForIdleSync();	
		activity = (EditExpenseActivity) instrumentation.waitForMonitorWithTimeout(activityMonitor, 10000); 	
		
		assertNotNull(activity);
		setUiElements();
		

		
		return activity;
	}
	
	private void setUiElements(){
		descText = (EditText) activity.findViewById(R.id.descriptionBody);
    	datePicker = (DatePicker) activity.findViewById( R.id.expenseDate);  
    	costText = (EditText) activity.findViewById( R.id.currencyBody);  
    	categorySpinner = (Spinner) activity.findViewById(R.id.categorySelector); 
    	currencySpinner = (Spinner) activity.findViewById(R.id.currencySelector);  
    	imageButton = (ImageButton) activity.findViewById(R.id.viewPhotoButton);
		saveButton = (Button) activity.findViewById(R.id.saveExpenseButton);	
	}
	
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
		File photoFile = new  File(activity.getFilesDir().getAbsolutePath() + "/testPhoto.jpg");
		return photoFile;
	}
}


