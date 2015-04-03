
package ca.ualberta.cs.team1travelexpenseapp.test;

import java.util.ArrayList;

import ca.ualberta.cs.team1travelexpenseapp.Tag;
import ca.ualberta.cs.team1travelexpenseapp.TagListController;
import ca.ualberta.cs.team1travelexpenseapp.TagManagerActivity;
import ca.ualberta.cs.team1travelexpenseapp.singletons.UserSingleton;
import ca.ualberta.cs.team1travelexpenseapp.users.Claimant;
import android.app.AlertDialog;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TagsEditTest extends ActivityInstrumentationTestCase2<TagManagerActivity> {
	TagManagerActivity activity;
	ListView tagListView;
	Claimant user;
	
	public TagsEditTest() {
		super(TagManagerActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		user = new Claimant("CoolGuy");
		UserSingleton.getUserSingleton().setUser(user);
		Intent intent = new Intent();
		setActivityIntent(intent);
		activity = getActivity();
		tagListView = (ListView) (activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.tagsList));
	}
	
	
	
	//US03.02.01: As a claimant, I want to manage my personal use of tags by listing 
	//the available tags, adding a tag, renaming a tag, and deleting a tag.
	public void testTagList() throws InterruptedException{
		String[] strings={"good","great","excellent"};
		final Tag tag1=new Tag(strings[0]);
		final Tag tag2=new Tag(strings[1]);
		final Tag tag3=new Tag(strings[2]);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ArrayList<Tag> tags = user.getTagList().getTags();
				tags.add(tag1);
				tags.add(tag2);
				tags.add(tag3);
			}
		});
		getInstrumentation().waitForIdleSync();
		
		assertTrue("Tags list on screen does not reflect tags added via controller",checkTags(strings));
		
		//get tag list item at position 1
		final View item=tagListView.getChildAt(1);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// click button, should produce dialog to choose edit or delete claim
				item.performLongClick();
				AlertDialog dialog=activity.editTagDialog;
				
				//enter new name for the tag into tag name box in dialog
				EditText tagName=(EditText)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.simpleEditText);
				tagName.setText("fantastic");
				
				//press the setName button in the dialog
				Button setTagButton=(Button)dialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE);
				setTagButton.performClick();
				
			}
		});
		getInstrumentation().waitForIdleSync();
		//the tags in the TagsListController should now match this update to the string array:
		strings[1]="fantastic";
		assertTrue("tags list on screen does not reflect renamed tag",checkTags(strings));
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// click button, should produce dialog to choose edit or delete claim
				item.performLongClick();
				AlertDialog dialog=activity.editTagDialog;
				
				//press the deleteTag button in the dialog
				Button deleteTagButton=(Button) dialog.getButton(android.content.DialogInterface.BUTTON_NEGATIVE);
				deleteTagButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		//the tags in the TagsListController should now match this update to the string array:
		String[] strings2={"good","excellent"};
		assertTrue("tags list on screen does not reflect deleted tag",checkTags(strings2));
		
		final Button addTagButton=(Button) activity.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.addTagButton);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// click button, should produce dialog to enter name of new tag
				addTagButton.performClick();
				AlertDialog dialog=activity.newTagDialog;
				
				//enter new name for the tag into tag name box in dialog
				EditText tagName=(EditText)dialog.findViewById(ca.ualberta.cs.team1travelexpenseapp.R.id.simpleEditText);
				tagName.setText("fantastic");
				
				//press the setTag button in the dialog
				Button setTagButton=(Button)dialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE);
				setTagButton.performClick();
			}
		});
		getInstrumentation().waitForIdleSync();
		//the tags in the TagsListController should now match this update to the string array:
		String[] strings3={"good","excellent","fantastic"};
		assertTrue("tags list on screen does not reflect added tag",checkTags(strings3));
	}
	
	//this function checks if the info in the tagListView match the given string array
	private boolean checkTags(String[] strings){
		int tagCount=user.getTagList().getTags().size();
		//assertTrue(Integer.toString(tagCount),tagCount==strings.length);
		if(tagCount!=strings.length) return false;
		
		for(int i=0; i < tagCount; i++){
			//get text from a tag at position of i of tagListView 
			TextView tagInfo = (TextView) tagListView.getChildAt(i);
			String viewText =tagInfo.getText().toString();
			
			String expectedText =strings[i];
			if(!viewText.equals(expectedText)){
				//assertTrue(viewText+"!="+expectedText, false);
				return false;
			}
		}
		return true;
	}
	

}
