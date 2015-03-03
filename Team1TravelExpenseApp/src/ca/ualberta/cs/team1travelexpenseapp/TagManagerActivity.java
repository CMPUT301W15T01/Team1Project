package ca.ualberta.cs.team1travelexpenseapp;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TagManagerActivity extends Activity {
	private TagList tagList;

	@Override
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
		
		tagList.addListener(new Listener() {			
			@Override
			public void update() {
				tagsList.clear();
				Collection<Tag> tags = TagListController.getTagList().getTags();
				tagsList.addAll(tags);
				tagsAdapter.notifyDataSetChanged();
			}
		});
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
	
	public void onAddTagClick(View v){
		
		//taken and modified from http://developer.android.com/guide/topics/ui/dialogs.html
		AlertDialog.Builder newTagDialog = new AlertDialog.Builder(this);
		
		final EditText nameField = new EditText(this);
		newTagDialog.setView(nameField);
		
		newTagDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               String name=nameField.getText().toString();
	               TagListController.addTag(new Tag(name));
	           }
	       });
		newTagDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               //Do nothing
	           }
	       });
		newTagDialog.setTitle("New Tag Name:");
		newTagDialog.show();
	}
}
