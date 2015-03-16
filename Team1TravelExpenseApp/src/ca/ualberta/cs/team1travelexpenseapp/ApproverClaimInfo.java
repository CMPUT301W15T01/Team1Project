package ca.ualberta.cs.team1travelexpenseapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

/**
 * Activity displaying a claim for an approver, approvers get this view instead of EditClaimActivity since they can't edit a claim
 * but may need to view all the info available in the EditClaimActivity.
 */
public class ApproverClaimInfo extends Activity {
	TextView info;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_approver_claim_info);
		info = (TextView) findViewById(R.id.ApproverClaimInfoTextView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.approver_claim_info, menu);
		return true;
	}

	public void onStart() {
		super.onStart();
		info.setText(ClaimListController.getCurrentClaim().toString());
	}
}
