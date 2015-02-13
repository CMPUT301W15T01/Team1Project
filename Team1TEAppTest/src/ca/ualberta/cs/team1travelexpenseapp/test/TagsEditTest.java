package ca.ualberta.cs.team1travelexpenseapp.test;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import android.widget.TextView;
import junit.framework.TestCase;

public class TagsEditTest extends ActivityInstrumentationTestCase2<EditTagActivity> {
	Activity activity;
	ListView tagListView;
	
	public ClaimantClaimsListTest() {
		super(EditTagActivity.class);
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
	public testTagList{
		String[] strings={"good","great","excellent"};
		Tag tag1=new Tag(strings[0]);
		Tag tag2=new Tag(strings[1]);
		Tag tag3=new Tag(strings[2]);
		
		TagsListController.addTag(tag1);
		TagsListController.addTag(tag2);
		TagsListController.addTag(tag3);
		
		assertTrue("tags list on screen does not reflect added tags",checkTags(strings));
		
		strings[1]="fantastic";
		tag2.setText(strings[1]);
		assertTrue("tags list on screen does not reflect renamed tag",checkTags(strings));
		TagsListController.removeTag(1);
		String[] newStrings={"good","excellent"};
		assertTrue("tags list on screen does not reflect deleted tag",checkTags(newStrings));
	}
	
	//this function checks if the info in the tagListView match the given string array
	private boolean checkTags(String[] strings){
		int tagCount=TagsListController.tagCount();
		if(tagCount!=strings.length) return false;
		
		for(int i=0; i < tagCount; i++){
			//get text from a tag at position of i of tagListView 
			TextView tagInfo = (TextView) tagListView.getItemAtPosition(i);
			String viewText = tagInfo.getText().toString();
			
			String expectedText =strings[i];
			if(viewText!=expectedText){
				return false;
			}
		}
		return true;
	}

}
