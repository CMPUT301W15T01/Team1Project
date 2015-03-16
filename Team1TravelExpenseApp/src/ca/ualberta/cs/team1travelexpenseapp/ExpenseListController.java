package ca.ualberta.cs.team1travelexpenseapp;


import java.io.File;
import java.io.ObjectInputStream.GetField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Controller for all ExpenseLists, The contained ExpenseList will be automatically linked when expenses are being accessed.
 */
public class ExpenseListController {
	protected static Expense currentExpense = null;
	
	/**
	 * Returns the ExpenseList for the claim that is currently being accessed.
	 * @return
	 * The ExpenseList for the active claim.
	 */
	public static ExpenseList getCurrentExpenseList() { 
		return ClaimListController.getCurrentClaim().getExpenseList();
	}
	
	/**
	 * Sets the current Expense item that is to be controlled.
	 * @param expense
	 * An Expense.
	 */
	public static void setCurrentExpense(Expense expense){
		currentExpense = expense; 
	}
	
	/**
	 * Returns the current Expense item that is being controlled.
	 * @return
	 * An Expense.
	 */
	public static Expense getCurrentExpense(){
		return currentExpense;
	}

	/**
	 * Add the given Expense to the ExpenseList.
	 * @param expense
	 * An Expense.
	 */
	public static void addExpense(Expense expense){
		ArrayList<Expense> expenseArray=ExpenseListController.getCurrentExpenseList().getExpenses();
		expenseArray.add(expense);
		setCurrentExpense(expense);
		///setCurrentExpenseList(ClaimListController.getCurrentClaim().getExpenseList());
		//display empty expense then show activity to edit empty claim otherwise an empty expense will not show after hitting back
		getCurrentExpenseList().setExpenseList(expenseArray);
	}
	
	/**
	 * Remove the given Expense from the ExpenseList.
	 * @param expense
	 * An Expense.
	 */
	public static void removeExpense(Expense expense){
		ArrayList<Expense> expenseArray=ExpenseListController.getCurrentExpenseList().getExpenses();
		expenseArray.remove(expense);
		getCurrentExpenseList().setExpenseList(expenseArray);
	}
	
	/**
	 * Update an Expense in the current ExpenseList with a new expense.
	 * @param expense
	 * The Expense to be overwritten. 
	 * @param newExpense
	 * The new Expense.
	 */
	public static void updateExpense(Expense expense, Expense newExpense){
		if (ClaimListController.getCurrentClaim().status != Claim.Status.submitted && 
				ClaimListController.getCurrentClaim().status != Claim.Status.approved){
			
			if(newExpense.getAmount().floatValue() == 0){
				newExpense.setCurrency("");
			}
			ArrayList<Expense> expenseArray=ExpenseListController.getCurrentExpenseList().getExpenses();
			expenseArray.set(expenseArray.indexOf(expense), newExpense);
			setCurrentExpense(newExpense);
			getCurrentExpenseList().setExpenseList(expenseArray);
		}
	}
	
	/**
	 * Saves an Expense by transferring the values set on the EditExpenseActivity screen into
	 * and Expense Item.  
	 * @param activity
	 * The EditExpenseActivity which contains the needed layouts.
	 */
	public static void onExpenseSaveClick(EditExpenseActivity activity) {
			
		Spinner categorySpinner = (Spinner) activity.findViewById(R.id.categorySelector);
		String categoryText = String.valueOf(categorySpinner.getSelectedItem());
		
		EditText descriptionView = (EditText) activity.findViewById(R.id.descriptionBody);
		String descriptionText = descriptionView.getText().toString();
		
		DatePicker dateView = (DatePicker) activity.findViewById( R.id.expenseDate);
		Calendar   calendar   = Calendar.getInstance();
		calendar.set(dateView.getYear(), dateView.getMonth(), dateView.getDayOfMonth());
		Date date = calendar.getTime();
		
		EditText amountView = (EditText) activity.findViewById(R.id.currencyBody);
		BigDecimal amountValue = new BigDecimal(amountView.getText().toString());
		
		Spinner currencySpinner = (Spinner) activity.findViewById(R.id.currencySelector);
		String currencyText = String.valueOf(currencySpinner.getSelectedItem());
		

		Expense expense = new Expense(descriptionText, date, categoryText, amountValue, currencyText);
		CheckBox completeBox = (CheckBox) activity.findViewById(R.id.incompleteCheck);
		//if not check, set complete 
		if ( !completeBox.isChecked() ) {
			expense.setComplete(true);
		}
		
		updateExpense(getCurrentExpense(), expense);
		
		
		activity.finish();	
	}
	
	/**
	 * Called by ClaimantExpenseListActivity when the claimant clicks on the add button.
	 * Adds a new expense and opens the EditExpenseActivity. 
	 * @param activity
	 * The ClaimantExpenseListActivity which contains the needed layout.
	 */
	public static void onAddExpenseClick(ClaimantExpenseListActivity activity) {
		ExpenseListController.addExpense(new Expense());
		Intent intent = new Intent(activity, EditExpenseActivity.class);
		activity.startActivity(intent);
		
	}

	/**
	 * Called from the dialog in the ClaimantExpenseListActivity.
	 * Simply removes the expense that was selected. 
	 */
	public static void onRemoveExpenseClick() {
		removeExpense(currentExpense);
	}
}
