package ca.ualberta.cs.team1travelexpenseapp;

import ca.ualberta.cs.team1travelexpenseapp.claims.Claim;

public class SelectedItemsSingleton {
	private static SelectedItemsSingleton selectedItemsSingleton=null;
	private Claim currentClaim;
	private Expense currentExpense;
	
	private SelectedItemsSingleton(){
		
	}
	
	public static SelectedItemsSingleton getSelectedItemsSingleton(){
		if(selectedItemsSingleton==null){
			selectedItemsSingleton=new SelectedItemsSingleton();
		}
		return selectedItemsSingleton;
	}
	
	public Claim getCurrentClaim() {
		return currentClaim;
	}

	public void setCurrentClaim(Claim currentClaim) {
		this.currentClaim = currentClaim;
	}
	
	public Expense getCurrentExpense() {
		return currentExpense;
	}

	public void setCurrentExpense(Expense currentExpense) {
		this.currentExpense = currentExpense;
	}

	
}
