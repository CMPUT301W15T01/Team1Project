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
import java.io.ObjectInputStream.GetField;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ca.ualberta.cs.team1travelexpenseapp.singletons.SelectedItemsSingleton;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Controller for all ExpenseLists, The contained ExpenseList will be automatically linked when expenses are being accessed.
 */
public class ExpenseListController {
	protected ExpenseList expenseList;
	protected Expense currentExpense;
	
	
	public ExpenseListController(ExpenseList expenseList) {
		this.expenseList=expenseList;
	}
	
	
	/**
	 * Sets the current Expense item that is to be controlled.
	 * @param expense
	 * An Expense.
	 */
	public void setCurrentExpense(Expense expense){
		currentExpense = expense; 
	}
	
	
	/**
	 * Returns the current Expense item that is being controlled.
	 * @return
	 * An Expense.
	 */
	public Expense getCurrentExpense(){
		return currentExpense;
	}
	
	
	/**
	 * Returns the ExpenseList for the claim that is currently being accessed.
	 * @return
	 * The ExpenseList for the active claim.
	 */
	public ExpenseList getExpenseList() { 
		return expenseList;
	}

	/**
	 * Add the given Expense to the ExpenseList.
	 * @param expense
	 * An Expense.
	 */
	public void addExpense(Expense expense){
		ArrayList<Expense> expenseArray=expenseList.getExpenses();
		expenseArray.add(expense);
		setCurrentExpense(expense);
		///setCurrentExpenseList(ClaimListController.getCurrentClaim().getExpenseList());
		//display empty expense then show activity to edit empty claim otherwise an empty expense will not show after hitting back
		expenseList.setExpenseList(expenseArray);
	}
	
	/**
	 * Remove the given Expense from the ExpenseList.
	 * @param expense
	 * An Expense.
	 */
	public void removeExpense(Expense expense){
		ArrayList<Expense> expenseArray=expenseList.getExpenses();
		expenseArray.remove(expense);
		expenseList.setExpenseList(expenseArray);
	}
	
	/**
	 * Update an Expense in the current ExpenseList with a new expense.
	 * @param expense
	 * The Expense to be overwritten. 
	 * @param newExpense
	 * The new Expense.
	 */
	public void updateExpense(Expense expense, Expense newExpense){
		if (SelectedItemsSingleton.getSelectedItemsSingleton().getCurrentClaim().isSubmittable()){
			
			if(newExpense.getAmount().floatValue() == 0){
				newExpense.setCurrency("");
			}
			ArrayList<Expense> expenseArray=expenseList.getExpenses();
			expenseArray.set(expenseArray.indexOf(expense), newExpense);
			setCurrentExpense(newExpense);
			expenseList.setExpenseList(expenseArray);
		}
	}
	
	/**
	 * Saves an Expense by transferring the values set on the EditExpenseActivity screen into
	 * and Expense Item.  
	 * @param activity
	 * The EditExpenseActivity which contains the needed layouts.
	 */
	public void onExpenseSaveClick(EditExpenseActivity activity) {
			
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
		if ( completeBox.isChecked() ) {
			expense.setFlagged(true);
		}
		expense.setReceipt(getCurrentExpense().getReceipt());
		
		updateExpense(getCurrentExpense(), expense);
		
		
		activity.finish();	
	}
	
	/**
	 * Called by ClaimantExpenseListActivity when the claimant clicks on the add button.
	 * Adds a new expense and opens the EditExpenseActivity. 
	 * @param activity
	 * The ClaimantExpenseListActivity which contains the needed layout.
	 */
	public void onAddExpenseClick(ClaimantExpenseListActivity activity) {
		addExpense(new Expense());
		SelectedItemsSingleton.getSelectedItemsSingleton().setCurrentExpense(currentExpense);
		Intent intent = new Intent(activity, EditExpenseActivity.class);
		activity.startActivity(intent);
		
	}

	/**
	 * Called from the dialog in the ClaimantExpenseListActivity.
	 * Simply removes the expense that was selected. 
	 */
	public void onRemoveExpenseClick() {
		removeExpense(getCurrentExpense());
	}
	
}
