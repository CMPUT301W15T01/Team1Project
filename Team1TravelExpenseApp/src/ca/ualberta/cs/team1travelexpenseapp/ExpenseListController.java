package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

public class ExpenseListController {
	private static ExpenseList expenseList=null;

	public static ExpenseList getExpenseList() { 
		if (expenseList == null) {
			expenseList = new ExpenseList();
		}
		
		return expenseList;
	}
	
	public static void addExpense(Expense expense){
		ArrayList<Expense> expenseArray=ExpenseListController.getExpenseList().getExpenses();
		expenseArray.add(expense);
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
}
