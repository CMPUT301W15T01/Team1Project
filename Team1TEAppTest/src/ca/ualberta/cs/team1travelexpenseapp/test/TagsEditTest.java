package ca.ualberta.cs.team1travelexpenseapp.test;

import ca.ualberta.cs.team1travelexpenseapp.Claim;
import ca.ualberta.cs.team1travelexpenseapp.ClaimsListController;
import ca.ualberta.cs.team1travelexpenseapp.TagListController;
import ca.ualberta.cs.team1travelexpenseapp.TagsListController;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
		
		TagListController.addTag(tag1);
		TagListController.addTag(tag2);
		TagListController.addTag(tag3);
		
		assertTrue("tags list on screen does not reflect added tags",checkTags(strings));
		//get tag list item at position 1
		final View item=claimListView.getAdapter().getView(1, null, null);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// click button, should produce dialog to choose edit or delete claim
				item.performClick();
				AlertDialog dialog=activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.tagDialog);
				
				//enter new name for the tag into tag name box in dialog
				EditText tagName=(EditText)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.tagText);
				tagName.setText("fantastic");
				
				//press the setName button in the dialog
				Button setTagButton=(Button)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.setTagButton);
				setTagButton.performClick();
				
			}
		});
		
		//the tags in the TagsListController should now match this update to the string array:
		strings[1]="fantastic";
		assertTrue("tags list on screen does not reflect renamed tag",checkTags(strings));
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// click button, should produce dialog to choose edit or delete claim
				item.performClick();
				AlertDialog dialog=activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.tagDialog);
				
				//enter new name for the tag into tag name box in dialog
				EditText tagName=(EditText)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.tagText);
				tagName.setText("fantastic");
				
				//press the deleteTag button in the dialog
				Button setTagButton=(Button)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.setTagButton);
				setTagButton.performClick();
			}
		});
		
		//the tags in the TagsListController should now match this update to the string array:
		String[] strings2={"good","excellent"};
		assertTrue("tags list on screen does not reflect deleted tag",checkTags(newStrings));
		
		Button addTagButton=activity.findViewById(ca.ualberta.ca.team1travelexpenseapp.R.id.addTagButton);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// click button, should produce dialog to enter name of new tag
				addTagButton.performClick();
				AlertDialog dialog=activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.addTagDialog);
				
				//enter new name for the tag into tag name box in dialog
				EditText tagName=(EditText)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.tagText);
				tagName.setText("fantastic");
				
				//press the setTag button in the dialog
				Button addTagButton=(Button)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.setTagButton);
				setTagButton.performClick();
			}
		});
		
		//the tags in the TagsListController should now match this update to the string array:
		String[] strings3={"good","excellent","fantastic"};
		assertTrue("tags list on screen does not reflect deleted tag",checkTags(strings2));
	}
	
	//this function checks if the info in the tagListView match the given string array
	private boolean checkTags(String[] strings){
		int tagCount=TagListController.tagCount();
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
