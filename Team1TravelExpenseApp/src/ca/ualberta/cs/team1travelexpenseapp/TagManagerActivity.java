package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collection;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * View for managing the application's list of tags.
 * Provides a user interface from which the user can add tags, change tag names and delete tags.
 * Listens to the underlying tagList.
 *
 */
public class TagManagerActivity extends Activity {
	private TagList tagList;
	public AlertDialog newTagDialog;
	public AlertDialog editTagDialog;
	private Listener listener;

	@Override
	/**
	 * On create we initialize our listener and set it to listen to the TagList.
	 * We set up the functions to be called to spawn interactive dialogs when list items are long clicked, and when
	 * the add tag button is clicked.
	 * 
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_tags);
		
		//taken from https://github.com/abramhindle/student-picker and modified
		final ListView tagsListView = (ListView) findViewById(R.id.tagsList);
		tagList=TagListController.getTagList();
		Collection<Tag> tags = tagList.getTags();
		final ArrayList<Tag> tagsList = new ArrayList<Tag>(tags);
		final ArrayAdapter<Tag> tagsAdapter = new ArrayAdapter<Tag>(this, android.R.layout.simple_list_item_1, tagsList);
		tagsListView.setAdapter(tagsAdapter);
		
		listener=new Listener() {			
			@Override
			public void update() {
				tagsList.clear();
				Collection<Tag> tags = TagListController.getTagList().getTags();
				tagsList.addAll(tags);
				tagsAdapter.notifyDataSetChanged();
			}
		};
		
		tagList.addListener(listener);
		
		tagsListView.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,int index,
					long id){
						 final Tag tag= (Tag) tagsAdapter.getItem(index);
						 
						//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html
						 AlertDialog.Builder editTagDialogBuilder = new AlertDialog.Builder(TagManagerActivity.this);
						
						 editTagDialogBuilder.setView(getLayoutInflater().inflate(R.layout.simple_edit_text, null));
						 editTagDialogBuilder.setPositiveButton("save", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   TagListController.onSetTagClick(dialog, tag);
					           }
					       });
						editTagDialogBuilder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   TagListController.onRemoveTagClick(dialog, tag);
					           }
					       });
						editTagDialogBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					               //Do nothing
					           }
					       });
						editTagDialogBuilder.setTitle("New Tag Name:");
						editTagDialog=editTagDialogBuilder.create();
						editTagDialog.show();
						EditText nameField=((EditText) editTagDialog.findViewById(R.id.simpleEditText));
						nameField.setText(tag.toString());
						return true;//not too sure on return value look into this
					}
		});
		
		//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html
		AlertDialog.Builder newTagDialogBuilder = new AlertDialog.Builder(this);
		
		newTagDialogBuilder.setView(getLayoutInflater().inflate(R.layout.simple_edit_text, null));
		
		newTagDialogBuilder.setPositiveButton("save", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               TagListController.onAddTagClick(dialog);
	           }
	       });
		newTagDialogBuilder.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               //Do nothing
	           }
	       });
		newTagDialogBuilder.setTitle("New Tag Name:");
		newTagDialog=newTagDialogBuilder.create();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tag_manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * When add tag button is clicked we display a dialog allowing the user to enter the name of the newly added Tag.
	 * @param view The view that was clicked
	 */
	public void onAddTagClick(View view){
		newTagDialog.show();
		EditText nameField=(EditText) newTagDialog.findViewById(R.id.simpleEditText);
		nameField.setText("");
	}
	
	/**
	 * On destroy simply remove the listener so the TagList does not continue to update it.
	 */
	public void onDestroy(){
		super.onDestroy();
		tagList.removeListener(listener);
	}
}
