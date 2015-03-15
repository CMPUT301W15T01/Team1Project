package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;

/**
 * Stores a list of Expenses along with a set of listeners to be updated whenever the list is modified.
 *
 */
public class ExpenseList {

	private ArrayList<Expense> expenseList;
	private ArrayList<Listener> listeners;
	
	/**
	 * Create a new empty ExpenseList
	 */
	public ExpenseList(){
		expenseList=new ArrayList<Expense>();
		listeners=new ArrayList<Listener>();
	}
	
	/**
	 * Add a new listener to be updated whenever this ExpenseList is changed.
	 * @param listener
	 * a listener object
	 */
	public void addListener(Listener listener){
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener so that it will no longer be updated when this ExpenseList is changed.
	 * @param listener
	 * a listener object
	 */
	public void removeListener(Listener listener){
		listeners.remove(listener);
	}
	
	/**
	 * Call update method on all listeners (called on ExpenseList changes).
	 */
	private void notifyListeners(){
		for(int i=0; i<listeners.size();i++){
			listeners.get(i).update();
		}
	}

	/**
	 * Returns the stored expenseList Array
	 * @return
	 * An ArrayList of Expenses.
	 */
	public ArrayList<Expense> getExpenses() {
		return expenseList;
	}

	/**
	 * Set the stored expenseList, will automatically update listeners and save the list 
	 * once saving is implemented.
	 * @param expenseList
	 * An ArrayList of Expenses.
	 */
	public void setExpenseList(ArrayList<Expense> expenseList) {
		this.expenseList = expenseList;
		notifyListeners();
	}
}