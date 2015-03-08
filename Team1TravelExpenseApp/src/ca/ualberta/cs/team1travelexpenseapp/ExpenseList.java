package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

public class ExpenseList {

	private ArrayList<Expense> expenseList;
	private ArrayList<Listener> listeners;
	
	ExpenseList(){
		expenseList=new ArrayList<Expense>();
		listeners=new ArrayList<Listener>();
	}
	
	public void addListener(Listener listener){
		listeners.add(listener);
	}
	
	public void removeListener(Listener listener){
		listeners.remove(listener);
	}
	
	private void notifyListeners(){
		for(int i=0; i<listeners.size();i++){
			listeners.get(i).update();
		}
	}

	public ArrayList<Expense> getExpenses() {
		return expenseList;
	}

	public void setExpenseList(ArrayList<Expense> expenseList) {
		this.expenseList = expenseList;
		notifyListeners();
	}
}