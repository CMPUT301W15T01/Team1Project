package ca.ualberta.cs.team1travelexpenseapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * 
 * Activity providing login screen where user can enter a user name for them self and then choose to enter either the claimant or
 * approver mode of the application.
 *
 */
public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
	 * Start the ApproverClaimsListActivity if the user chooses to log in as an approver.
	 * @param v the button clicked by the user.
	 */
	public void approverLogin(View v) {
		Intent intent = new Intent(LoginActivity.this,ApproverClaimsListActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Start the ClaimantClaimsListActivity if the user chooses to log in as an claimant.
	 * @param v the button clicked by the user.
	 */
	public void userLogin(View v){
		Intent intent = new Intent(LoginActivity.this,ClaimantClaimsListActivity.class);
		startActivity(intent);
	}
}
