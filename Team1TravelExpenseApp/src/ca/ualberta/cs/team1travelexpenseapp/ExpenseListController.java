package ca.ualberta.cs.team1travelexpenseapp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class ExpenseListController {
	protected static Expense currentExpense = null;
	private static ExpenseList expenseList=null;

	public static ExpenseList getExpenseList() { 
		if (expenseList == null) {
			expenseList = new ExpenseList();
		}
		
		return expenseList;
	}
	
	public static void setCurrentExpense(Expense expense){
		currentExpense= expense;
	}
	
	public static Expense getCurrentExpense(){
		return currentExpense;
	}

	
	public static void addExpense(Expense expense){
		ArrayList<Expense> expenseArray=ExpenseListController.getExpenseList().getExpenses();
		expenseArray.add(expense);
		setCurrentExpense(expense);
		expenseList.setExpenseList(expenseArray);
	}
	
	public static void removeExpense(Expense expense){
		ArrayList<Expense> expenseArray=ExpenseListController.getExpenseList().getExpenses();
		expenseArray.remove(expense);
		expenseList.setExpenseList(expenseArray);
	}
	
	public static void updateExpense(Expense expense, Expense newExpense){
		ArrayList<Expense> expenseArray=ExpenseListController.getExpenseList().getExpenses();
		expenseArray.set(expenseArray.indexOf(expense), newExpense);
		expenseList.setExpenseList(expenseArray);
	}
	
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
		
		updateExpense(currentExpense, new Expense(descriptionText, date, categoryText, amountValue, currencyText));
		
		
		activity.finish();	
	}
	
	public static void onAddExpenseClick(ClaimantExpenseListActivity activity) {
		ExpenseListController.addExpense(new Expense());
		Intent intent = new Intent(activity, EditExpenseActivity.class);
		activity.startActivity(intent);
		
	}
}
