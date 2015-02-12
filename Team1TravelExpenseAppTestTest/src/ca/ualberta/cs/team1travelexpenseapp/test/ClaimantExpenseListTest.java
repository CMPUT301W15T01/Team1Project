package ca.ualberta.cs.team1travelexpenseapp.test;

import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.TestCase;

public class ClaimantExpenseListTest extends TestCase {
	Activity activity;
	ListView expenseList;
	
	public ClaimantExpenseListTest() {
		super(ClaimantExpenseListActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		expenseList= (activity.findViewById(ca.ualberta.cs.team1expenseapp.R.id.body));
	}

	
	//US02.01.01: As a claimant, I want to list all my expense claims, showing for each claim:
	//the starting date of travel, the destination(s) of travel, the claim status, tags, and 
	//total currency amounts.
	public void testListClaims(){
		int itemCount=expenseList.getCount();
		for(int i=0; i<claimCount; i++){
			TextView item=expenseList.getItemAtPosition(i);
			String text=item.getText().toString();
			Claim claim= ClaimsListController.getClaim(i);
			
			//this is what the text in the listview at position i should look like to match
			//the corresponding claim in the ClaimsListController
			String expectedText="Start Date: "+claim.getStartDate().toString()+"\n";
			expectedText+="Destination(s): ";
			int destCount=claim.getDestinationCount();
			if(destCount==0){
				expectedText+="None";
			}
			else{
				for(int j=0;j<destCount-1; j++){
					expectedText+=claim.getDestination(j)+", ";
				}
				expectedText+=claim.getDestination(destCount-1)+"\n";
			}
			expectedText+="Status: "+claim.getStatus()+"\n";
			expectedText+="Tag(s): ";
			int tagCount=claim.getTagCount();
			if(tagCount==0){
				expectedText+="None";
			}
			else{
				for(int j=0;j<tagCount-1; j++){
					expectedText+=claim.getTag(j)+", ";
				}
				expectedText+=claim.getTag(tagCount-1)+"\n";
			}
			expectedText+="Totals: ";
			int totalCount=claim.getTagCount();
			if(totalCount==0){
				expectedText+="None";
			}
			else{
				for(int j=0;j<totalCount-1; j++){
					expectedText+=claim.getTotal(j)+", ";
				}
				expectedText+=claim.getTotal(totalCount-1)+"\n";
			}
			assertEquals("Claim summary at list item"+i+"does not match expected value",expectedText, text);	
		}
		
		
	}

}
