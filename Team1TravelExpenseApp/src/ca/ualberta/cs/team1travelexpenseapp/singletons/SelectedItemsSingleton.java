package ca.ualberta.cs.team1travelexpenseapp.singletons;

import ca.ualberta.cs.team1travelexpenseapp.Expense;
import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;
/**
 * 
 *
 */
public class SelectedItemsSingleton {
	private static SelectedItemsSingleton selectedItemsSingleton = null;
	private Claim currentClaim;
	private Expense currentExpense;

	private SelectedItemsSingleton() {

	}
	 /**
     * Returns the item runtime object associated with the current Android application.
     * Most of the methods of class <code>Runtime</code> are instance 
     * methods and must be invoked with respect to the current runtime object. 
     * 
     * @return  the <code>selectedItemsSingleton</code> object associated user's claims and expenses.
     */
	public static SelectedItemsSingleton getSelectedItemsSingleton() {
		if (selectedItemsSingleton == null) {
			selectedItemsSingleton = new SelectedItemsSingleton();
		}
		return selectedItemsSingleton;
	}

	/**
	 * Gets the current claim that the user is accessing
	 * @return currentClaim claim object 
	 */
	public Claim getCurrentClaim() {
		return currentClaim;
	}

	/**
	 * Sets the current claim 
	 * @param currentClaim the claim object to be set
	 * 
	 */
	public void setCurrentClaim(Claim currentClaim) {
		this.currentClaim = currentClaim;
	}

	/**
	 * Gets the current expense that the user is accessing 
	 * @return currentExpense Expense object
	 */
	public Expense getCurrentExpense() {
		return currentExpense;
	}
	/**
	 * Sets the current expense 
	 * @param currentExpense the expense object to be set
	 * 
	 */
	public void setCurrentExpense(Expense currentExpense) {
		this.currentExpense = currentExpense;
	}

}
