package ca.ualberta.cs.team1travelexpenseapp;

import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
/**
 * The view for viewing comments attached to claims as a claimant
 * 
 *
 */
public class ClaimantCommentActivity extends Activity {
	
	Claim claim;
	TextView comments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claimant_comment);
		claim = ClaimListController.getCurrentClaim();
	}
	
	public void onStart() {
		super.onStart();
		comments = (TextView) findViewById(R.id.claimantCommentString);
		//String commentString = claim.getCommentList().toString();
		String commentString = "";
		Map<String, String> commentList = claim.getCommentList();
		// how to iterate through keys found at http://stackoverflow.com/questions/46898/iterate-over-each-entry-in-a-map (March 15, 2015)
		for(Map.Entry<String, String> comment : commentList.entrySet()) {
			commentString += comment.getKey() + "\n" + comment.getValue();
		}
		comments.setText(commentString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claimant_comment, menu);
		return true;
	}

}
