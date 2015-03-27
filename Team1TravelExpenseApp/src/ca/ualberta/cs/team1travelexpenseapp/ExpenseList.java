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

import java.util.ArrayList;

/**
 * Stores a list of Expenses along with a set of listeners to be updated whenever the list is modified.
 *
 */
public class ExpenseList {

	private ArrayList<Expense> expenseList;
	private transient ArrayList<Listener> listeners;
	
	/**
	 * Create a new empty ExpenseList
	 */
	public ExpenseList(){
		expenseList=new ArrayList<Expense>();
	}
	
	/**
	 * Add a new listener to be updated whenever this ExpenseList is changed.
	 * @param listener
	 * a listener object
	 */
	public void addListener(Listener listener){
		if(listeners==null){
			listeners=new ArrayList<Listener>();
		}
		listeners.add(listener);
	}
	
	/**
	 * Remove a listener so that it will no longer be updated when this ExpenseList is changed.
	 * @param listener
	 * a listener object
	 */
	public void removeListener(Listener listener){
		if(listeners==null){
			listeners=new ArrayList<Listener>();
		}
		listeners.remove(listener);
	}
	
	/**
	 * Call update method on all listeners (called on ExpenseList changes).
	 */
	private void notifyListeners(){
		for(int i=0; i<listeners.size();i++){
			listeners.get(i).update();
		}
		//added for now so claimslist is saved whenever expenselist is modified, probably suboptimal
		UserSingleton.getUserSingleton().getUser().getClaimList().saveClaims();
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