package ca.ualberta.cs.team1travelexpenseapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ClaimantCommentActivity extends Activity {
	
	Claim claim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_claimant_comment);
		claim = ClaimListController.getCurrentClaim();
	}
	
	public void onStart() {
		super.onStart();
		TextView comments = (TextView) findViewById(R.id.claimantCommentString);
		String commentString = claim.getCommentList().toString();
		comments.setText(commentString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.claimant_comment, menu);
		return true;
	}

}
