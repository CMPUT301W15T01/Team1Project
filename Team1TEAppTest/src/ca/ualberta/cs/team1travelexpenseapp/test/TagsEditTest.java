package ca.ualberta.cs.team1travelexpenseapp.test;

import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;
import junit.framework.TestCase;

public class TagsEditTest extends TestCase {
	Activity activity;
	ListView tagListView;
	
	public ClaimantClaimsListTest() {
		super(MainActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		setActivityIntent(intent);
		activity = getActivity();
		tagListView = (ListView) (activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.tagsList));
	}
	
	
	
	//US03.02.01: As a claimant, I want to manage my personal use of tags by listing 
	//the available tags, adding a tag, renaming a tag, and deleting a tag.
	
	//US03.03.01: As a claimant, I want to filter the list of expense claims by tags, 
	//to show only those claims that have at least one tag matching any of a given set 
	//of one or more filter tags.

}
